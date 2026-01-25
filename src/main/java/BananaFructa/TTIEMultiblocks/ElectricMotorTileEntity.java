package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import crafttweaker.api.block.IBlock;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import li.cil.oc.common.block.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class ElectricMotorTileEntity extends TileEntityIEBase implements IEnergyStorage, ITickable {

    public int energy = 0;
    public boolean good = false;
    public int maxEnergy = 16000;
    public EnumFacing facing;
    public float targetSpeed = 0;
    public float targetTorque = 0;

    public static int minRPMLV = 0;
    public static int maxRPMLV = 250;
    public static int minRPMMV = 250;
    public static int maxRPMMV = 500;
    public static int torqueRange = 50;

    public PowerConnectionType connectionType = PowerConnectionType.NONE;
    public static ItemStack lvConnector = Utils.itemStackFromCTId("<immersiveengineering:connector>");
    public static ItemStack mvConnector = Utils.itemStackFromCTId("<immersiveengineering:connector:2>");

    public RotaryStorage rotaryStorage = new RotaryStorage() {

        @Override
        public float getTorque() {
            return good ? targetTorque : 0;
        }

        @Override
        public float getRotationSpeed() {
            return good ? targetSpeed : 0;
        }

        @Override
        public float getOutputTorque() {
            return good ? targetTorque : 0;
        }

        @Override
        public float getOutputRotationSpeed() {
            return good ? targetSpeed : 0;
        }

        @Override
        public RotationSide getSide(@Nullable EnumFacing facing) {
            return RotationSide.OUTPUT;
        }
    };

    public ElectricMotorTileEntity() {
        super();
    }

    public ElectricMotorTileEntity(EnumFacing facing) {
        super();
        this.facing = facing;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        IEUtils.notifyClientUpdate(world,pos);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP) return true;
        if (capability == CapabilityRotaryEnergy.ROTARY_ENERGY&& facing == this.facing) return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && facing == EnumFacing.UP) return (T)this;
        if (capability == CapabilityRotaryEnergy.ROTARY_ENERGY&& facing == this.facing) return (T)rotaryStorage;
        return super.getCapability(capability, facing);
    }


    @Override
    public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        energy = nbtTagCompound.getInteger("energy");
        facing = EnumFacing.values()[nbtTagCompound.getInteger("facing")];
        targetSpeed = nbtTagCompound.getFloat("targetSpeed");
        targetTorque = nbtTagCompound.getFloat("targetTorque");
        connectionType = PowerConnectionType.values()[nbtTagCompound.getInteger("conType")];
        good = nbtTagCompound.getBoolean("good");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        nbtTagCompound.setInteger("energy",energy);
        nbtTagCompound.setInteger("facing",facing.ordinal());
        nbtTagCompound.setFloat("targetSpeed",targetSpeed);
        nbtTagCompound.setFloat("targetTorque",targetTorque);
        nbtTagCompound.setInteger("conType",connectionType.ordinal());
        nbtTagCompound.setBoolean("good",good);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(maxEnergy - energy, maxReceive);
        if (!simulate) {
            IEUtils.notifyClientUpdate(world,pos);
            energy += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) {
            IEUtils.notifyClientUpdate(world,pos);
            energy -= energyExtracted;
        }
        return energyExtracted;
    }

    public static float getEfficiency(float torque) {
        return -(float)Math.pow((torque/(torqueRange+20)),2) + 0.9f;
    }

    public static float getPowerConsumption(float speed, float torque) {
        float pOut = (float)(speed/60 * 2 * Math.PI * torque);
        float eff = getEfficiency(torque);
        return pOut/eff;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public void update() {
        IBlockState state = world.getBlockState(pos.up());
        if (state != null) {
            ItemStack stack = state.getBlock().getItem(world, pos.up(), state);
            if (Utils.ItemStacksEqual(stack,lvConnector)) {
                connectionType = PowerConnectionType.LV;
            } else if (Utils.ItemStacksEqual(stack,mvConnector)) {
                connectionType = PowerConnectionType.MV;
            } else {
                connectionType = PowerConnectionType.NONE;
            }
        } else {
            connectionType = PowerConnectionType.NONE;
        }
        int energyRequired = (int)getPowerConsumption(targetSpeed,targetTorque);
        if (connectionType == PowerConnectionType.LV && energyRequired > 255) {
            good = false;
            return;
        }
        if (connectionType == PowerConnectionType.MV && energyRequired > 1024) {
            good = false;
            return;
        }
        if (connectionType == PowerConnectionType.NONE) {
            good = false;
            return;
        }
        if (energy > energyRequired) {
            energy -= energyRequired;
            good = true;
            markDirty();
            IEUtils.notifyClientUpdate(world, pos);
        } else {
            good = false;
        }

        /*BlockPos connectedTo = pos.offset(this.facing);
        TileEntity te = world.getTileEntity(connectedTo);
        if (te instanceof TileEntityMetalPress) {
            TileEntityMetalPress metalPress = (TileEntityMetalPress) te;
            if (metalPress.isEnergyPos()) {
                if (this.rotaryStorage.getOutputTorque() > 10 && this.rotaryStorage.getOutputRotationSpeed() > 30 && this.rotaryStorage.getOutputRotationSpeed() < 60) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1000 - wrapper.getEnergyStored(),false);
                }
            }
        }
        if (te instanceof TileEntityPumpjack) {
            TileEntityPumpjack metalPress = (TileEntityPumpjack) te;
            if (metalPress.isEnergyPos()) {
                if (this.rotaryStorage.getOutputTorque() > 50 && this.rotaryStorage.getOutputRotationSpeed() > 60 && this.rotaryStorage.getOutputRotationSpeed() < 80) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }

        if (te instanceof TileEntityCrusher) {
            TileEntityCrusher metalPress = (TileEntityCrusher) te;
            if (metalPress.isEnergyPos()) {
                if (this.rotaryStorage.getOutputTorque() > 20 && this.rotaryStorage.getOutputRotationSpeed() > 30 && this.rotaryStorage.getOutputRotationSpeed() < 60) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }

        if (te instanceof TileEntityPowerLoom) {
            TileEntityPowerLoom metalPress = (TileEntityPowerLoom) te;
            if (metalPress.isEnergyPos()) {
                if (this.rotaryStorage.getOutputTorque() > 10 && this.rotaryStorage.getOutputRotationSpeed() > 150 && this.rotaryStorage.getOutputRotationSpeed() < 200) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }*/
    }

    public void setTargetSpeed(float targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public enum PowerConnectionType {
        NONE("None"),
        LV("LV"),
        MV("MV");

        public String name;

        PowerConnectionType(String name) {
            this.name = name;
        }
    }
}
