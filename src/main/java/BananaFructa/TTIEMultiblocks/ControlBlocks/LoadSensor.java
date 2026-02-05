package BananaFructa.TTIEMultiblocks.ControlBlocks;

import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.SignalSourceTileEntity;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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

import javax.annotation.Nullable;

public class LoadSensor extends Block {
    public LoadSensor() {
        super(Material.IRON);
        this.setDefaultState(blockState.getBaseState().withProperty(IEProperties.FACING_HORIZONTAL, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IEProperties.FACING_HORIZONTAL);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new LoadSensorTileEntity(state.getValue(IEProperties.FACING_HORIZONTAL));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IEProperties.FACING_HORIZONTAL).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IEProperties.FACING_HORIZONTAL,EnumFacing.getHorizontal(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(IEProperties.FACING_HORIZONTAL, placer.getHorizontalFacing().getOpposite());
    }

    public int getWeakPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing p_180656_4_) {
        return ((LoadSensorTileEntity)access.getTileEntity(pos)).redstoneStrenght;
    }

    public int getStrongPower(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing p_176211_4_) {
        return ((LoadSensorTileEntity)access.getTileEntity(pos)).redstoneStrenght;
    }

    @Deprecated
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(IEProperties.FACING_HORIZONTAL,rot.rotate((EnumFacing)state.getValue(IEProperties.FACING_HORIZONTAL)));
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        //if (worldIn.isRemote) return;
        worldIn.setBlockState(pos, state.withProperty(IEProperties.FACING_HORIZONTAL, state.getValue(IEProperties.FACING_HORIZONTAL)), 2);
    }

    public boolean canProvidePower(IBlockState p_149744_1_) {
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        LoadSensorTileEntity te = (LoadSensorTileEntity) worldIn.getTileEntity(pos);
        return super.getActualState(state, worldIn, pos).withProperty(IEProperties.FACING_HORIZONTAL,te.facing);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote)
        {
            player.openGui(TTMain.INSTANCE, 13, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

        return false;
    }
}
