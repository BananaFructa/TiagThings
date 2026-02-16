package BananaFructa.ImmersiveTech;

import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import mctmods.immersivetechnology.api.ITUtils;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityAlternatorMaster;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class AlternatorMultiblockMasterModified extends TileEntityAlternatorMaster {

    public void update() {
        if (this.formed) {
            if (!this.world.isRemote) {
                this.checkProvider();
                if (this.speed > 0) {
                    this.energyStorage.setCapacity(this.energyGenerated());
                }
            }
        }
        super.func_73660_a();
    }

}
