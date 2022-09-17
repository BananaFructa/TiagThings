package BananaFructa.BCModifications;

import buildcraft.builders.block.BlockQuarry;
import buildcraft.lib.tile.TileBC_Neptune;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class RFBlockQuarry extends BlockQuarry {

    public RFBlockQuarry(Material material, String id) {
        super(material, id);
    }

    @Override
    public TileBC_Neptune createTileEntity(World world, IBlockState state) {
        return new RFTileQuarry();
    }
}
