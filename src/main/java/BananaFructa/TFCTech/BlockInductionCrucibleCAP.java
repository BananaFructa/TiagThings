package BananaFructa.TFCTech;

import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tfctech.objects.blocks.devices.BlockInductionCrucible;

import javax.annotation.Nullable;

public class BlockInductionCrucibleCAP extends BlockInductionCrucible {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TEInductionCrucibleCAP();
    }

}
