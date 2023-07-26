package BananaFructa.TTIEMultiblocks;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nonnull;

public class TileEntityElectricHeater extends TileEntityMultiblockPart<TileEntityElectricHeater> {

    private static final int[] size = new int[]{3,1,1};

    TileEntityElectricHeater() {
        super(size);
    }

    @Nonnull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing enumFacing) {
        return new IFluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack) {
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int i, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public ItemStack getOriginalBlock() {
        return new ItemStack(IEContent.blockSheetmetal,1, BlockTypes_MetalsAll.STEEL.getMeta()); // TODO: this is wrong
    }

    @Override
    public float[] getBlockBounds() {
        return new float[0];
    }

    @Override
    public void update() {

    }
}
