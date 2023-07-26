package BananaFructa.TTIEMultiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.metal.*;
import blusunrize.immersiveengineering.common.util.Utils;
import mctmods.immersivetechnology.common.ITContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

public class MultiblockElectricHeater implements MultiblockHandler.IMultiblock {

    public static MultiblockElectricHeater instance = new MultiblockElectricHeater();
    static ItemStack[][][] structure = new ItemStack[1][3][1];

    static {
        structure[0][0][0] = new ItemStack(IEContent.blockSheetmetal,1, BlockTypes_MetalsAll.STEEL.getMeta());
        structure[0][1][0] = new ItemStack(IEContent.blockMetalDecoration0,1, BlockTypes_MetalDecoration0.RADIATOR.getMeta());
        structure[0][2][0] = new ItemStack(IEContent.blockSheetmetal,1, BlockTypes_MetalsAll.STEEL.getMeta());
    }
    static final IngredientStack[] materials = new IngredientStack[]{new IngredientStack(new ItemStack(IEContent.blockSheetmetal,2, BlockTypes_MetalsAll.STEEL.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0,1, BlockTypes_MetalDecoration0.RADIATOR.getMeta()))};

    @Override
    public String getUniqueName() {
        return "TT:ElectricHeater";
    }

    @Override
    public boolean isBlockTrigger(IBlockState iBlockState) {
        return iBlockState.getBlock() == IEContent.blockSheetmetal;
    }

    @Override
    public boolean createStructure(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) return false;

        EnumFacing f = EnumFacing.fromAngle((double)entityPlayer.rotationYaw);

        BlockPos start = blockPos;

        boolean correct = true;
        correct &= Utils.isBlockAt(world,start,IEContent.blockSheetmetal,BlockTypes_MetalsAll.STEEL.getMeta());
        start = start.add(enumFacing.getDirectionVec());
        correct &= Utils.isBlockAt(world,start,IEContent.blockMetalDecoration0,BlockTypes_MetalDecoration0.RADIATOR.getMeta());
        start = start.add(enumFacing.getDirectionVec());
        correct &= Utils.isBlockAt(world,start,IEContent.blockSheetmetal,BlockTypes_MetalsAll.STEEL.getMeta());
        if (!correct) return false;

        // TODO: fire event

        IBlockState state = TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.ELECTRIC_HEATER.getMeta());
        state = state.withProperty(IEProperties.FACING_HORIZONTAL,f);


        for (int i = 0;i < 3;i++) {
            world.setBlockState(blockPos,state);
            TileEntity te = world.getTileEntity(blockPos);
            if (te instanceof TileEntityElectricHeater) {
                ((TileEntityElectricHeater) te).formed = true;
            }
            blockPos = blockPos.add(enumFacing.getDirectionVec());
        }


        // TODO: set state and tilenetity

        System.out.println("FORMED");

        return false;
    }

    @Override
    public ItemStack[][][] getStructureManual() {
        return structure;
    }

    @Override
    public IngredientStack[] getTotalMaterials() {
        return materials;
    }

    @Override
    public boolean overwriteBlockRender(ItemStack itemStack, int i) {
        return false;
    }

    @Override
    public float getManualScale() {
        return 16;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderFormedStructure() {

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderFormedStructure() {

    }
}
