package BananaFructa.BCModifications;

import BananaFructa.TiagThings.Utils;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.mj.MjBattery;
import buildcraft.builders.tile.TileQuarry;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.lwjgl.Sys;

public class RFTileQuarry extends TileQuarry implements IEnergyStorage {

    private final int conversionFactor = 2120; // 1 RF -> x MJ

    private MjBattery battery;

    int MJ2RF(long mj) {
        return (int)(mj / conversionFactor);
    }

    long RF2MJ(int rf) {
        return (long)rf * conversionFactor;
    }

    public RFTileQuarry() {
        super();
        MjBattery newBattery = new MjBattery(RF2MJ(24000)); // old capacity was too big, new one is 24k RF
        Utils.writeDeclaredField(TileQuarry.class,this,"battery",newBattery,true);
        battery = Utils.readDeclaredField(TileQuarry.class,this,"battery");
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T)this;
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        // thank god for obfed names
        super.func_73660_a();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        long mj = RF2MJ(maxReceive);

        long current = battery.getStored();

        System.out.println(current);

        if (battery.isFull()) return 0;
        else if (current + mj <= battery.getCapacity()) {
            if (!simulate) battery.addPower(mj,false);
            sendNetworkUpdate(NET_RENDER_DATA);
            return maxReceive;
        } else {
            if (!simulate) battery.addPower(battery.getCapacity() - current,false);
            sendNetworkUpdate(NET_RENDER_DATA);
            return MJ2RF(battery.getCapacity() - current);
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return MJ2RF(battery.getStored());
    }

    @Override
    public int getMaxEnergyStored() {
        return MJ2RF(battery.getCapacity());
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }
}
