package BananaFructa.TTIEMultiblocks;

import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.Sys;

import javax.annotation.Nullable;

public class SignalSourceBlock extends Block {

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public SignalSourceBlock() {
        super(Material.IRON);
        this.setDefaultState(blockState.getBaseState().withProperty(POWERED,false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new SignalSourceTileEntity();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWERED,meta == 1 ? true : false);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(POWERED , false);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos, state.withProperty(POWERED, state.getValue(POWERED)), 2);
    }

    public int getWeakPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing p_180656_4_) {
        return getActualState(state,access,pos).getValue(POWERED) ? ((SignalSourceTileEntity)access.getTileEntity(pos)).signalLevel : 0;
    }

    public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing p_176211_4_) {
        return getActualState(state,access,pos).getValue(POWERED) ? ((SignalSourceTileEntity)access.getTileEntity(pos)).signalLevel : 0;
    }

    public void breakBlock(World p_180663_1_, BlockPos p_180663_2_, IBlockState p_180663_3_) {

        p_180663_1_.notifyNeighborsOfStateChange(p_180663_2_, this, false);

        super.breakBlock(p_180663_1_, p_180663_2_, p_180663_3_);
    }

    public boolean canProvidePower(IBlockState p_149744_1_) {
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        SignalSourceTileEntity te = (SignalSourceTileEntity) worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(POWERED,te.good);
    }
}
