package BananaFructa.EmergingTechnologies;

import blusunrize.immersiveengineering.common.IEContent;
import io.moonman.emergingtechnology.machines.harvester.Harvester;
import io.moonman.emergingtechnology.machines.harvester.HarvesterTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class HarvesterModified extends Harvester {

    @Override
    public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote && playerIn.getHeldItem(EnumHand.MAIN_HAND).getItem() == IEContent.itemTool && playerIn.getHeldItem(EnumHand.MAIN_HAND).getMetadata() == 0) {
            HarvesterTileEntityModified te = (HarvesterTileEntityModified) worldIn.getTileEntity(pos);
            if (te != null) {
                te.togglePartial();
                playerIn.sendMessage(new TextComponentString("Mode changed to " + (te.harvestPartial ? "partial harvest." : "full harvest.")));
                return true;
            }
        }
        return super.func_180639_a(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public TileEntity func_149915_a(World worldIn, int meta) {
        return new HarvesterTileEntityModified();
    }
}
