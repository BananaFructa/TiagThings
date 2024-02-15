package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_2;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import mctmods.immersivetechnology.common.ITContent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRocketScaffold extends SimplifiedTileEntityMultiblockMetal<TileEntityRocketScaffold, SimplifiedMultiblockRecipe> implements IEBlockInterfaces.IBlockBounds {

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(ITContent.fluidDistWater,100)},new ItemStack[0],new FluidStack[0],8192,20));
    }};
    public int rkt1 = 0;
    public int rkt2 = 0;
    private static final int[] poss = new int[]{
            1345,
            2528,
            3711,
            4894,
            6077,
            7260,
            8443,
            9626,
            10809
    };

    public TileEntityRocketScaffold() {
        super(TTIEContent.rocketModuleScaffold, 32000, false,recipes);
    }

    @Override
    public void initPorts() {
        int fluid = registerFluidTank(5000);
        registerFluidPort(0,fluid,PortType.INPUT,EnumFacing.SOUTH);
        addEnergyPort(12);
        addEnergyPort(181);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            IBlockState state = world.getBlockState(getPos());
            if (state.getBlock().getMetaFromState(state) == TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_CHILD.getMeta()) {
                if (Arrays.stream(poss).anyMatch((i) -> {
                    return field_174879_c == i;
                })) {
                    world.setBlockState(getPos(),
                            TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_BODY.getMeta()).withProperty(IEProperties.FACING_HORIZONTAL, this.facing)
                    );
                }
            }
        }
        super.update();
    }

    public void incrementRkt1() {
        rkt1++;
        markDirty();
        IEUtils.notifyClientUpdate(world,getPos());
    }

    public void incrementRkt2() {
        rkt2++;
        markDirty();
        IEUtils.notifyClientUpdate(world,getPos());
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("rkt1",rkt1);
        nbt.setInteger("rkt2",rkt2);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        rkt1 = nbt.getInteger("rkt1");
        rkt2 = nbt.getInteger("rkt2");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        if (oldState.getBlock() == TTIEContent.ttBlockMetalMultiblock_2 && newState.getBlock() == TTIEContent.ttBlockMetalMultiblock_2) return false;
        return super.shouldRefresh(world, pos, oldState, newState);
    }
}
