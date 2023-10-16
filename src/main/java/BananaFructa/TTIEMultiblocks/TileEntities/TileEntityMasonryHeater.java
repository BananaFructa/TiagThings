package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.dries007.tfc.objects.blocks.property.ILightableBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMasonryHeater extends SimplifiedTileEntityMultiblockMetal<TileEntityMasonryHeater, SimplifiedMultiblockRecipe> {

    public TileEntityMasonryHeater() {
        super(TTIEContent.masonryHeater, 0,true,new ArrayList<>());
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote) {
            if (isFireBurning() && world.rand.nextInt(45) == 0) {
                BlockPos pos = getBlockPosForPos(58);
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.1;
                double z = pos.getZ() + 0.5;
                switch (world.rand.nextInt(3))
                {
                    case 0:
                        TFCParticles.FIRE_PIT_SMOKE1.spawn(world, x, y, z, 0, 0.1D, 0, 120);
                        break;
                    case 1:
                        TFCParticles.FIRE_PIT_SMOKE2.spawn(world, x, y, z, 0, 0.1D, 0, 110);
                        break;
                    case 2:
                        TFCParticles.FIRE_PIT_SMOKE3.spawn(world, x, y, z, 0, 0.1D, 0, 100);
                        break;
                }
            }
        }
    }

    private boolean isFireBurning() {
        BlockPos pos = getBlockPosForPos(4).offset(EnumFacing.UP);
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFirePit) {
            return state.getValue(((ILightableBlock)state.getBlock()).LIT);
        }
        return false;
    }

    @Override
    public void initPorts() {}
}
