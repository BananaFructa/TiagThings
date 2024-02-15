package BananaFructa.TiagThings.Items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TTUniversalBucket extends UniversalBucket {

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        FluidStack fluidStack = getFluid(itemstack);
        // empty bucket shouldn't exist, do nothing since it should be handled by the bucket event
        if (fluidStack == null)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);
        }

        // clicked on a block?
        RayTraceResult mop = this.rayTrace(world, player, false);

        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, itemstack, mop);
        if (ret != null) return ret;

        if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return ActionResult.newResult(EnumActionResult.PASS, itemstack);
        }

        BlockPos clickPos = mop.getBlockPos();
        // can we place liquid there?
        if (world.isBlockModifiable(player, clickPos))
        {
            // the block adjacent to the side we clicked on
            BlockPos targetPos = clickPos.offset(mop.sideHit);

            // can the player place there?
            if (player.canPlayerEdit(targetPos, mop.sideHit, itemstack))
            {
                // try placing liquid
                FluidActionResult result = tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
                if (result.isSuccess() && !player.capabilities.isCreativeMode)
                {
                    // success!
                    player.addStat(StatList.getObjectUseStats(this));

                    itemstack.shrink(1);
                    ItemStack drained = result.getResult();
                    ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);

                    // check whether we replace the item or add the empty one to the inventory
                    if (itemstack.isEmpty())
                    {
                        return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
                    }
                    else
                    {
                        // add empty bucket to player inventory
                        ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
                    }
                }
            }
        }

        // couldn't place liquid there2
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }

    public static IFluidHandlerItem getFluidHandler(@Nonnull ItemStack itemStack)
    {
        if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
        {
            return itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        }
        else
        {
            return null;
        }
    }

    public static FluidActionResult tryPlaceFluid(@Nullable EntityPlayer player, World world, BlockPos pos, @Nonnull ItemStack container, FluidStack resource)
    {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(container, 1); // do not modify the input
        IFluidHandlerItem containerFluidHandler = getFluidHandler(containerCopy);
        if (containerFluidHandler != null && tryPlaceFluid(player, world, pos, containerFluidHandler, resource))
        {
            return new FluidActionResult(containerFluidHandler.getContainer());
        }
        return FluidActionResult.FAILURE;
    }

    public static boolean tryPlaceFluid(@Nullable EntityPlayer player, World world, BlockPos pos, IFluidHandler fluidSource, FluidStack resource)
    {
        if (world == null || resource == null || pos == null)
        {
            return false;
        }

        Fluid fluid = resource.getFluid();
        if (fluid == null || !fluid.canBePlacedInWorld())
        {
            return false;
        }

        if (fluidSource.drain(resource, false) == null)
        {
            return false;
        }

        // check that we can place the fluid at the destination
        IBlockState destBlockState = world.getBlockState(pos);
        Material destMaterial = destBlockState.getMaterial();
        boolean isDestNonSolid = !destMaterial.isSolid();
        boolean isDestReplaceable = destBlockState.getBlock().isReplaceable(world, pos);
        if (!world.isAirBlock(pos) && !isDestNonSolid && !isDestReplaceable)
        {
            return false; // Non-air, solid, unreplacable block. We can't put fluid here.
        }

        if (world.provider.doesWaterVaporize() && fluid.doesVaporize(resource))
        {
            FluidStack result = fluidSource.drain(resource, true);
            if (result != null)
            {
                result.getFluid().vaporize(player, world, pos, result);
                return true;
            }
        }
        else
        {
            // This fluid handler places the fluid block when filled
            IFluidHandler handler = getFluidBlockHandler(player, fluid, world, pos);
            FluidStack result = FluidUtil.tryFluidTransfer(handler, fluidSource, resource, true);
            if (result != null)
            {
                SoundEvent soundevent = resource.getFluid().getEmptySound(resource);
                world.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }

    private static IFluidHandler getFluidBlockHandler(EntityPlayer player, Fluid fluid, World world, BlockPos pos)
    {
        Block block = fluid.getBlock();
        if (block instanceof IFluidBlock)
        {
            return new FluidBlockWrapper((IFluidBlock) block, world, pos) {
                @Override
                public int fill(FluidStack resource, boolean doFill) {
                    if (fluidBlock instanceof BlockFluidClassic) {
                        if (resource == null)
                        {
                            return 0;
                        }
                        if (resource.amount < Fluid.BUCKET_VOLUME)
                        {
                            return 0;
                        }
                        if (doFill)
                        {
                            FluidUtil.destroyBlockOnFluidPlacement(world, pos);
                            world.setBlockState(pos, ((BlockFluidClassic)fluidBlock).getDefaultState().withProperty(BlockLiquid.LEVEL, (player.isCreative() ? 0 : 1)), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                        }
                        return Fluid.BUCKET_VOLUME;
                    } else {
                        return super.fill(resource, doFill);
                    }
                }
            };
        }
        else if (block instanceof BlockLiquid)
        {
            return new BlockLiquidWrapper((BlockLiquid) block, world, pos) {
                @Override
                public int fill(FluidStack resource, boolean doFill) {
                    // NOTE: "Filling" means placement in this context!
                    if (resource.amount < Fluid.BUCKET_VOLUME) {
                        return 0;
                    }

                    if (doFill) {
                        Material material = blockLiquid.getDefaultState().getMaterial();
                        BlockLiquid block = BlockLiquid.getFlowingBlock(material);
                        world.setBlockState(blockPos, block.getDefaultState().withProperty(BlockLiquid.LEVEL, (player.isCreative() ? 0 : 1)), 11);
                    }

                    return Fluid.BUCKET_VOLUME;
                }
            };
        }
        else
        {
            return new BlockWrapper(block, world, pos){
                @Override
                public int fill(FluidStack resource, boolean doFill)
                {
                    // NOTE: "Filling" means placement in this context!
                    if (resource.amount < Fluid.BUCKET_VOLUME)
                    {
                        return 0;
                    }
                    if (doFill)
                    {
                        FluidUtil.destroyBlockOnFluidPlacement(world, blockPos);
                        world.setBlockState(blockPos, block.getDefaultState().withProperty(BlockLiquid.LEVEL, (player.isCreative() ? 0 : 1)), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                    }
                    return Fluid.BUCKET_VOLUME;
                }
            };
        }
    }


}
