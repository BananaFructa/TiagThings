package BananaFructa.ImmersiveTech;

import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import mctmods.immersivetechnology.common.multiblocks.metal.tileentities.TileEntityAlternatorMaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AlternatorMultiblockMasterModified extends TileEntityAlternatorMaster {

    private static final Method checkProvide = Utils.getDeclaredMethod(TileEntityAlternatorMaster.class,"checkProvider");
    private static final Method energyGenerated = Utils.getDeclaredMethod(TileEntityAlternatorMaster.class,"energyGenerated");

    public void update() {
        if (this.formed) {
            if (!this.world.isRemote) {
                try {
                    checkProvide.invoke(this);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                if (this.speed > 0) {
                    try {
                        this.energyStorage.setCapacity((Integer) energyGenerated.invoke(this));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        super.func_73660_a();
    }

    public int getComparatorInputOverride() {
        return 0;
        //if (this.energyStorage.getMaxEnergyStored() == 0) return 0;
        //return !this.formed ? 0 : 15 * this.energyStorage.getEnergyStored() / this.energyStorage.getMaxEnergyStored();
    }

}
