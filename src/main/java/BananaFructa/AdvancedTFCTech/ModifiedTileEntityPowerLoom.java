package BananaFructa.AdvancedTFCTech;

import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class ModifiedTileEntityPowerLoom extends TileEntityPowerLoom {

    @Override
    public int getEnergyStored(@Nullable EnumFacing fd) {
        return -1;
    }
}
