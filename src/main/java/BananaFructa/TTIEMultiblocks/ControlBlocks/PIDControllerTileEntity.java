package BananaFructa.TTIEMultiblocks.ControlBlocks;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.ModularList;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkData;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorHV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorLV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorMV;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Netowrk.CMessageUpdatePowerInfo;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import antibluequirk.alternatingflux.block.TileEntityRelayAF;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.immersiveflux.IFluxReceiver;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorLV;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PIDControllerTileEntity extends TileEntityIEBase implements IEnergyStorage, ITickable {

    public EnumFacing facing;

    public int reference = 7;
    public float p = 0;
    public float i = 0;
    public float d = 0;
    public int redstoneStrenght = 6;
    public int energy = 0;
    public int maxEnergy = 2;

    public int scalingP = 0;
    public int scalingI = 0;
    public int scalingD = 0;

    public ModularList inputHistory = new ModularList(200);
    public ModularList outputHistory = new ModularList(200);

    private float lastDerivativeIn = 0;
    private float integral = 0;

    public PIDControllerTileEntity() {
        super();
    }

    public PIDControllerTileEntity(EnumFacing facing) {
        super();
        this.facing = facing;
    }

    public static Map<BlockPos, UUID> playerSubscribers = new HashMap<>();

    public void subscribePlayer(UUID sub) {
        playerSubscribers.put(pos,sub);
    }

    public static void unsubPlayer(UUID sub) {
        for (BlockPos b : playerSubscribers.keySet()) {
            if (playerSubscribers.get(b).equals(sub)) {
                playerSubscribers.remove(b);
                break;
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        IEUtils.notifyClientUpdate(world,pos);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.DOWN || facing == null)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && (facing == EnumFacing.DOWN || facing == null)) {
            return (T)this;
        }
        return super.getCapability(capability, facing);
    }


    @Override
    public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        facing = EnumFacing.values()[nbtTagCompound.getInteger("facing")];
        reference = nbtTagCompound.getInteger("reference");
        p = nbtTagCompound.getFloat("p");
        i = nbtTagCompound.getFloat("i");
        d = nbtTagCompound.getFloat("d");
        redstoneStrenght = nbtTagCompound.getInteger("redstone_strength");
        energy = nbtTagCompound.getInteger("energy");
        integral = nbtTagCompound.getFloat("integral");
        lastDerivativeIn = nbtTagCompound.getFloat("last_der");
        scalingP = nbtTagCompound.getInteger("scaling_p");
        scalingI = nbtTagCompound.getInteger("scaling_i");
        scalingD = nbtTagCompound.getInteger("scaling_d");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean b) {
        nbtTagCompound.setInteger("facing", facing.ordinal());
        nbtTagCompound.setInteger("reference", reference);
        nbtTagCompound.setFloat("p", p);
        nbtTagCompound.setFloat("i", i);
        nbtTagCompound.setFloat("d", d);
        nbtTagCompound.setInteger("redstone_strength", redstoneStrenght);
        nbtTagCompound.setInteger("energy", energy);
        nbtTagCompound.setFloat("integral", integral);
        nbtTagCompound.setFloat("last_der", lastDerivativeIn);
        nbtTagCompound.setInteger("scaling_p",scalingP);
        nbtTagCompound.setInteger("scaling_i",scalingI);
        nbtTagCompound.setInteger("scalid_d",scalingD);

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
        if (world.isRemote) return;
        BlockPos posIn = pos.offset(IEUtils.shiftRelativeToNorth(facing,true,EnumFacing.EAST));
        IBlockState stateIn = world.getBlockState(posIn);
        int sens = stateIn.getBlock().getStrongPower(stateIn,world,posIn,IEUtils.shiftRelativeToNorth(facing,true,EnumFacing.EAST).getOpposite());
        int error = reference - sens;
        if (i != 0) {
            integral += (1.0f / 20) * error * i;
            integral = Math.max(Math.min(integral, 15), 0);
        } else integral = 0;
        float out = error * p + integral + (error-lastDerivativeIn) * d;
        out = Math.min(Math.max(out,0),15);
        lastDerivativeIn = error;
        inputHistory.add(sens);
        outputHistory.add((int)(out*1000));
        if (playerSubscribers.containsKey(pos)) {
            NBTTagCompound update = new NBTTagCompound();
            update.setInteger("in",sens);
            update.setInteger("out",(int)(out*1000));
            UUID uuid = playerSubscribers.get(pos);
            EntityPlayerMP playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
            if (playerMP == null) unsubPlayer(uuid);
            else {
                TTPacketHandler.wrapper.sendTo(new CMessageUpdatePowerInfo(update),playerMP);
            }
        }
        redstoneStrenght = (int)(out);
        world.notifyNeighborsOfStateChange(getPos(),world.getBlockState(getPos()).getBlock(),true);
        markDirty();
        IEUtils.notifyClientUpdate(world, pos);
    }
}
