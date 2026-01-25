package BananaFructa.TFC;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCrucible extends net.dries007.tfc.objects.blocks.devices.BlockCrucible {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TECrucibleCAP();
    }
}
