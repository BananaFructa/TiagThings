package BananaFructa.EmergingTechnologies;

import io.moonman.emergingtechnology.machines.hydroponic.Hydroponic;
import io.moonman.emergingtechnology.machines.hydroponic.HydroponicTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HydroponicModified extends Hydroponic {

    public TileEntity func_149915_a(World worldIn, int meta) {
        return new TEHydroponicBed();
    }

}
