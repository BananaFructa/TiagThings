package BananaFructa.ImmersiveIntelligence;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import mctmods.immersivetechnology.api.client.MechanicalEnergyAnimation;
import mctmods.immersivetechnology.common.Config;
import mctmods.immersivetechnology.common.blocks.ITBlockInterfaces;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelIron;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelSteel;

public class ModifiedWheelTileEntitySteel extends TileEntityWheelSteel implements ITBlockInterfaces.IMechanicalEnergy {
    //boolean isFeeding = false;
    BlockPos feedingTo = new BlockPos(0,0,0);

    public ModifiedWheelTileEntitySteel() {
        super();
    }

    public ModifiedWheelTileEntitySteel(EnumFacing facing) {
        this.facing = facing;
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (world.isRemote) return;
        BlockPos connectedTo = pos.offset(this.facing);
        TileEntity te = world.getTileEntity(connectedTo);
        if (world.getWorldTime() % 20 == 0) IEUtils.notifyClientUpdate(world,getPos());

        /*isFeeding = false;
        if (te != null && te instanceof TileEntityMultiblockMetal<?,?> && TTIEContent.rotaryConvertedMultiblocks.containsKey(te.getClass())) {
            if (TTIEContent.rotaryConvertedMultiblocks.get(te.getClass()).fulfilsRequirement(this.energy.getOutputTorque(), this.energy.getOutputRotationSpeed())) {
                TileEntityMultiblockMetal<?,?> teMM = (TileEntityMultiblockMetal<?, ?>) te;
                EnergyHelper.IEForgeEnergyWrapper wrapper = teMM.getCapabilityWrapper(this.facing.getOpposite());
                if (wrapper != null) {
                    isFeeding = true;
                    feedingTo = te.getPos();
                    wrapper.receiveEnergy(1000 - wrapper.getEnergyStored(),false);
                }
            }
        }*/

        /*if (te instanceof TileEntityMetalPress) {
            TileEntityMetalPress metalPress = (TileEntityMetalPress) te;
            if (metalPress.isEnergyPos()) {
                if (this.energy.getOutputTorque() > 10 && this.energy.getOutputRotationSpeed() > 30 && this.energy.getOutputRotationSpeed() < 60) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1000 - wrapper.getEnergyStored(),false);
                }
            }
        }
        if (te instanceof TileEntityPumpjack) {
            TileEntityPumpjack metalPress = (TileEntityPumpjack) te;
            if (metalPress.isEnergyPos()) {
                if (this.energy.getOutputTorque() > 50 && this.energy.getOutputRotationSpeed() > 60 && this.energy.getOutputRotationSpeed() < 80) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }

        if (te instanceof TileEntityCrusher) {
            TileEntityCrusher metalPress = (TileEntityCrusher) te;
            if (metalPress.isEnergyPos()) {
                if (this.energy.getOutputTorque() > 20 && this.energy.getOutputRotationSpeed() > 30 && this.energy.getOutputRotationSpeed() < 60) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }

        if (te instanceof TileEntityPowerLoom) {
            TileEntityPowerLoom metalPress = (TileEntityPowerLoom) te;
            if (metalPress.isEnergyPos()) {
                if (this.energy.getOutputTorque() > 10 && this.energy.getOutputRotationSpeed() > 150 && this.energy.getOutputRotationSpeed() < 200) {
                    EnergyHelper.IEForgeEnergyWrapper wrapper = metalPress.getCapabilityWrapper(this.facing.getOpposite());
                    if (wrapper != null) wrapper.receiveEnergy(1500 - wrapper.getEnergyStored(),false);
                }
            }
        }*/
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isMechanicalEnergyTransmitter(EnumFacing enumFacing) {
        return true;
    }

    @Override
    public boolean isMechanicalEnergyReceiver(EnumFacing enumFacing) {
        return false;
    }

    @Override
    public int getSpeed() {
        return (int)(Math.pow((this.energy.getOutputRotationSpeed() * this.energy.getOutputTorque()/(60)*2*Math.PI)/ Config.ITConfig.Machines.Alternator.alternator_energy_perTick,1.0f/Config.ITConfig.Machines.Alternator.alternator_exponent)*Config.ITConfig.MechanicalEnergy.mechanicalEnergy_speed_max);
    }

    @Override
    public float getTorqueMultiplier() {
        return 1;
    }

    @Override
    public MechanicalEnergyAnimation getAnimation() {
        return null;
    }

}
