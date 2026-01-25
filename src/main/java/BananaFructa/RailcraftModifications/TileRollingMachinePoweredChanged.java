package BananaFructa.RailcraftModifications;
import BananaFructa.TiagThings.Utils;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.IBatteryBlock;
import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.common.blocks.machine.equipment.TileRollingMachine;
import mods.railcraft.common.blocks.machine.equipment.TileRollingMachinePowered;
import mods.railcraft.common.util.inventory.InvTools;
import mods.railcraft.common.util.inventory.InventoryIterator;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// This may appear as it will throw an exception but, it will not

public class TileRollingMachinePoweredChanged extends TileRollingMachinePowered implements IEnergyStorage {

    EnergyStorage storage = new EnergyStorage(8000);


    @Override
    protected void progress() {
        if (storage.extractEnergy(40,false) == 40) {
            int progress = Utils.readDeclaredField(TileRollingMachine.class,this,"progress");
            progress++;
            Utils.writeDeclaredField(TileRollingMachine.class,this,"progress",progress,false);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T)this;
        return super.getCapability(capability, facing);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive,simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public boolean canMakeMore() {
        if (!Crafters.rollingMachine().getRecipe(this.craftMatrix, this.world).isPresent()) {
            return false;
        } else {
            return this.useLast ? true : !(InventoryIterator.get(this.craftMatrix).streamStacks().filter(InvTools::nonEmpty).anyMatch((s) -> {
                return InvTools.sizeOf(s) == 1;
            }));
        }
    }
}
