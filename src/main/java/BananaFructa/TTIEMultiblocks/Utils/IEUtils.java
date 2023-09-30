package BananaFructa.TTIEMultiblocks.Utils;

import BananaFructa.TTIEMultiblocks.TTBlockMetalMultiblocks;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@Mod.EventBusSubscriber
public class IEUtils {

    private static final Queue<Tuple<BlockPos,Integer>> updateQueue = new LinkedList<>();

    public static void populateMultiblock(World world, BlockPos sourcePos, EnumFacing sideHit, ItemStack[][][] structure, int structurePositionTriggerHeight, int structurePositionTriggerLength, int structurePositionTriggerWidth, IBlockState state, IBlockState stateChild, boolean mirror) {

        //if (world.isRemote) return;

        // Indexing shenanigans ahead
        // Still not fully understand some indexing rules, but that does not matter as this code is here to never be looked at again

        Class<? extends TileEntity> teClass = state.getBlock().createTileEntity(world, state).getClass();

        int structureHeight = structure.length;
        int structureLength = structure[0].length;
        int structureWidth = structure[0][0].length;

        for (int l = 0 - structurePositionTriggerLength; l < structureLength - structurePositionTriggerLength; l++) {
            for (int w = 0 - structurePositionTriggerWidth; w < structureWidth - structurePositionTriggerWidth; w++) {
                for (int h = 0 - structurePositionTriggerHeight; h < structureHeight - structurePositionTriggerHeight; h++) {

                    boolean dummy = !(l == 0 && w == 0 && h == 0);

                    if (structure[h + structurePositionTriggerHeight][l + structurePositionTriggerLength][w + structurePositionTriggerWidth].isEmpty())
                        continue;

                    int ww = mirror ? -w : w;

                    BlockPos pos = sourcePos.offset(sideHit, l).offset(sideHit.rotateY(), ww).add(0, h, 0);

                    if (dummy) world.setBlockState(pos, stateChild);
                    else world.setBlockState(pos,state);

                    TileEntity te = world.getTileEntity(pos);

                    if (teClass.isInstance(te)) {
                        TileEntityMultiblockPart<?> teMP = (TileEntityMultiblockPart<?>) te;
                        teMP.formed = true;
                        teMP.field_174879_c = (h + structurePositionTriggerHeight) * structureLength * structureWidth + (l + structurePositionTriggerLength) * structureWidth + (w + structurePositionTriggerWidth);
                        teMP.offset = new int[]{(sideHit == EnumFacing.WEST ? -l : sideHit == EnumFacing.EAST ? l : sideHit == EnumFacing.NORTH ? ww : -ww), h, (sideHit == EnumFacing.NORTH ? -l : sideHit == EnumFacing.SOUTH ? l : sideHit == EnumFacing.EAST ? ww : -ww)};
                        teMP.mirrored = mirror;
                        teMP.facing = sideHit;
                        if (teMP instanceof SimplifiedTileEntityMultiblockMetal) {
                            if (!dummy) {
                                ((SimplifiedTileEntityMultiblockMetal) teMP).initPorts();
                                ((SimplifiedTileEntityMultiblockMetal) teMP).setFace(sideHit);
                            }
                        }
                        if (!world.isRemote)teMP.markDirty();
                    } else if (te instanceof TileEntityAE2CompatMultiblock) { // a bit jank
                        TileEntityAE2CompatMultiblock<?> teMP = (TileEntityAE2CompatMultiblock<?>) te;
                        teMP.formed = true;
                        teMP.pos = (h + structurePositionTriggerHeight) * structureLength * structureWidth + (l + structurePositionTriggerLength) * structureWidth + (w + structurePositionTriggerWidth);
                        teMP.offset = new int[]{(sideHit == EnumFacing.WEST ? -l : sideHit == EnumFacing.EAST ? l : sideHit == EnumFacing.NORTH ? ww : -ww), h, (sideHit == EnumFacing.NORTH ? -l : sideHit == EnumFacing.SOUTH ? l : sideHit == EnumFacing.EAST ? ww : -ww)};
                        teMP.mirrored = mirror;
                        teMP.facing = sideHit;
                    }

                    world.addBlockEvent(pos, TTIEContent.ttBlockMetalMultiblock, 255, 0);
                    if (!dummy) updateQueue.add(new Tuple<>(pos,world.provider.getDimension()));

                }
            }
        }

    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        // this is jank
        for (Tuple<BlockPos,Integer> info;(info = updateQueue.poll()) != null;) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(info.getSecond()).addBlockEvent(info.getFirst(), TTIEContent.ttBlockMetalMultiblock, 255, 0);
        }
    }

    private static final BlockPos unitVectorPY = new BlockPos(0,1,0);
    private static final BlockPos unitVectorPX = new BlockPos(1,0,0);
    private static final BlockPos unitVectorPZ = new BlockPos(0,0,1);

    private static final BlockPos unitVectorNY = new BlockPos(0,-1,0);
    private static final BlockPos unitVectorNX = new BlockPos(-1,0,0);
    private static final BlockPos unitVectorNZ = new BlockPos(0,0,-1);

    private static BlockPos scalarMultiply(BlockPos v, int s) {
        return new BlockPos(v.getX() * s,v.getY() * s,v.getZ() * s);
    }

    private static final BlockPos unitVectors[][] = new BlockPos[][] {
//           NORTH        SOUTH        WEST         EAST
            {unitVectorPY,unitVectorPY,unitVectorPY,unitVectorPY}, // HEIGHT
            {unitVectorNZ,unitVectorPZ,unitVectorNX,unitVectorPX}, // LENGTH
            {unitVectorPX,unitVectorNX,unitVectorNZ,unitVectorPZ}  // WIDTH
    };

    public static BlockPos getUnitVectorForSide(EnumFacing enumFacing, MultiblockDimension dimension) {
        return unitVectors[dimension.ordinal()][enumFacing.ordinal() - 2];
    }

    //                  V valid V mirrored
    public static Tuple<Boolean,Boolean> checkStructure(World world, BlockPos sourcePos, EnumFacing sideHit, ItemStack[][][] structure,  int structurePositionTriggerHeight, int structurePositionTriggerLength, int structurePositionTriggerWidth) {
        if (sideHit == EnumFacing.UP || sideHit == EnumFacing.DOWN) return new Tuple<>(false,false);

        BlockPos unitH = getUnitVectorForSide(sideHit,MultiblockDimension.HEIGHT);
        BlockPos unitL = getUnitVectorForSide(sideHit,MultiblockDimension.LENGTH);
        BlockPos unitW = getUnitVectorForSide(sideHit,MultiblockDimension.WIDTH);

        int structureHeight = structure.length;
        int structureLength = structure[0].length;
        int structureWidth = structure[0][0].length;

        if (checkStructure(world, sourcePos, unitH,unitL,unitW, structure, structureHeight, structureLength, structureWidth, structurePositionTriggerHeight, structurePositionTriggerLength, structurePositionTriggerWidth)) {
            return new Tuple<>(true,false);
        }
        unitW = scalarMultiply(unitW,-1); // checks the mirrored version by inverting the width unit scalar vector
        if (checkStructure(world, sourcePos, unitH,unitL,unitW, structure, structureHeight, structureLength, structureWidth, structurePositionTriggerHeight, structurePositionTriggerLength, structurePositionTriggerWidth)) {
            return new Tuple<>(true,true);
        }

        return new Tuple<>(false,false);

    }

    public static boolean checkStructure(World world, BlockPos sourcePos, BlockPos unitH, BlockPos unitL, BlockPos unitW,ItemStack[][][] structure, int structureHeight, int structureLength, int structureWidth, int structurePositionTriggerHeight, int structurePositionTriggerLength, int structurePositionTriggerWidth ){

        for (int l = 0 - structurePositionTriggerLength;l < structureLength - structurePositionTriggerLength;l++) {
            for (int w = 0 - structurePositionTriggerWidth; w < structureWidth - structurePositionTriggerWidth; w++) {
                for (int h = 0 - structurePositionTriggerHeight; h < structureHeight - structurePositionTriggerHeight; h++) {
                    BlockPos current = sourcePos.add(scalarMultiply(unitH,h)).add(scalarMultiply(unitL,l)).add(scalarMultiply(unitW,w));
                    ItemStack expected = structure[h + structurePositionTriggerHeight][l + structurePositionTriggerLength][w + structurePositionTriggerWidth];
                    Block expectedBlock = Block.getBlockFromItem(expected.getItem());
                    int expectedMeta = expected.getMetadata();
                    if (!Utils.isBlockAt(world,current,expectedBlock,expectedMeta)) return false;
                }
            }
        }
        return true;
    }

    public static IngredientStack[] getMaterialsForStructure(ItemStack[][][] structure, int structureHeight, int structureLength, int structureWidth) {

        HashMap<BlockWithMeta,Integer> materials = new HashMap<>();
        for (int l = 0;l < structureLength;l++) {
            for (int w = 0; w < structureWidth; w++) {
                for (int h = 0 ; h < structureHeight; h++) {
                    ItemStack is = structure[h][l][w];
                    if (is == null) continue;
                    Block b = Block.getBlockFromItem(is.getItem());
                    int meta = is.getMetadata();
                    int quantity = is.getCount();
                    BlockWithMeta bm = new BlockWithMeta(b,meta);
                    if (!materials.containsKey(bm)) {
                        materials.put(bm,quantity);
                    } else {
                        materials.put(bm,quantity + materials.get(bm));
                    }
                }
            }
        }

        IngredientStack[] materialsArr = new IngredientStack[materials.size()];

        int index = 0;
        for (BlockWithMeta key : materials.keySet()) {
            Block b = key.b;
            int meta = key.meta;
            int quantity = materials.get(key);
            materialsArr[index++] = new IngredientStack(new ItemStack(b,quantity,meta));
        }

        return materialsArr;

    }

    public static <T extends SimplifiedMultiblockRecipe> T findRecipe(List<T> recipes, FluidStack[] inputFluids, ItemStack[] inputItems, List<T> blackList) {

        for (T r : recipes) {
            if (blackList.contains(r)) continue;

            int index = 0;

            boolean enough = true;
            for (FluidStack f : r.getFluidInputs()) {
                if (index >= inputFluids.length) {
                    enough = false;
                    break;
                }
                FluidStack input = inputFluids[index++];
                if (f == null) continue;
                if (input == null || input.getFluid() != f.getFluid() || input.amount < f.amount) {
                    enough = false;
                    break;
                }
            }

            index = 0;
            for (IngredientStack i : r.getItemInputs()) {
                if (index >= inputItems.length) {
                    enough = false;
                    break;
                }
                ItemStack input = inputItems[index++];
                if (i.stack == null || i.stack.isEmpty()) continue;
                if (input == null || input.getItem() != i.stack.getItem() || input.getCount() < i.stack.getCount()) {
                    enough = false;
                    break;
                }
            }

            if (enough) return r;
        }

        return null;
    }

    public static <T extends SimplifiedMultiblockRecipe> SimplifiedMultiblockRecipe findRecipeForItem(Item item, List<T> recipes) {
        for (SimplifiedMultiblockRecipe recipe : recipes) {
            if (recipe.getItemInputs().stream().map(i->i.stack.getItem()).anyMatch(i->i==item)) return recipe;
        }
        return null;
    }

    public static <T extends SimplifiedMultiblockRecipe> SimplifiedMultiblockRecipe findRecipeForFluid(Fluid fluid, List<T> recipes) {
        if (fluid == null) return null;
        for (SimplifiedMultiblockRecipe recipe : recipes) {
            if (recipe.getFluidInputs().stream().map(f->f == null ? null :f.getFluid()).anyMatch(f->f==fluid)) return recipe;
        }
        return null;
    }

    public static int[] booleanToInt(boolean[] arr) {
        int[] ints = new int[arr.length];
        for (int i = 0;i < arr.length;i++) ints[i] = arr[i] ? 1 : 0;
        return ints;
    }

    public static boolean[] intToBoolean(int[] arr) {
        boolean[] bools = new boolean[arr.length];
        for (int i = 0;i < arr.length;i++) bools[i] = arr[i] == 1;
        return bools;
    }


}
