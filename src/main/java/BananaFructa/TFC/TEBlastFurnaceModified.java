package BananaFructa.TFC;

import BananaFructa.TiagThings.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.dries007.tfc.api.capability.metal.CapabilityMetalItem;
import net.dries007.tfc.api.capability.metal.IMetalItem;
import net.dries007.tfc.api.recipes.BlastFurnaceRecipe;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.blocks.BlockFireBrick;
import net.dries007.tfc.objects.blocks.BlockMolten;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.devices.BlockBlastFurnace;
import net.dries007.tfc.objects.blocks.metal.BlockMetalSheet;
import net.dries007.tfc.objects.te.TEBlastFurnace;
import net.dries007.tfc.objects.te.TEMetalSheet;
import net.dries007.tfc.util.block.Multiblock;
import net.dries007.tfc.util.fuel.FuelManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfcmetallum.util.RegistryHandler;

import java.util.List;
import java.util.function.Predicate;

import static net.dries007.tfc.objects.blocks.property.ILightableBlock.LIT;

public class TEBlastFurnaceModified extends TEBlastFurnace {

    Utils.InstanceField<Integer> chimney = Utils.getAccessibleField(TEBlastFurnace.class,this,"chimney",false);
    Utils.InstanceField<Integer> delayTimer = Utils.getAccessibleField(TEBlastFurnace.class,this,"delayTimer",false);
    Utils.InstanceField<Integer> oreCount = Utils.getAccessibleField(TEBlastFurnace.class,this,"oreCount",false);
    Utils.InstanceField<Integer> fuelCount = Utils.getAccessibleField(TEBlastFurnace.class,this,"fuelCount",false);
    Utils.InstanceField<Integer> maxFuel = Utils.getAccessibleField(TEBlastFurnace.class,this,"maxFuel",false);
    Utils.InstanceField<Integer> maxOre = Utils.getAccessibleField(TEBlastFurnace.class,this,"maxOre",false);
    Utils.InstanceField<List<ItemStack>> oreStacks = Utils.getAccessibleField(TEBlastFurnace.class,this,"oreStacks",false);
    Utils.InstanceField<List<ItemStack>> fuelStacks = Utils.getAccessibleField(TEBlastFurnace.class,this,"fuelStacks",false);
    Utils.InstanceField<Integer> oreUnits = Utils.getAccessibleField(TEBlastFurnace.class,this,"oreUnits",false);

    //Method addItemsFromWorld = Utils.getDeclaredMethod(TEBlastFurnace.class,"addItemsFromWorld");
    //Method updateSlagBlock = Utils.getDeclaredMethod(TEBlastFurnace.class,"updateSlagBlock",boolean.class);

    @Override
    public void func_73660_a() {
        IBlockState state = world.getBlockState(pos);
        boolean update = false;
        if (delayTimer.get() - 1 <= 0) {
            delayTimer.set(20); // this is so that the normal logic does not run and have it be set to 20 because idk what if execution stops here if it were to be set to a large value it would brick a blast furnace
            update = true;
        }
        super.func_73660_a();
        if (update) {
            delayTimer.set(20);
            // Update multiblock status

            // Detect client changes
            int oldChimney = chimney.get();
            int oldOre = oreCount.get();
            int oldFuel = fuelCount.get();

            chimney.set(getChimneyLevels(world, pos));
            System.out.println(chimney.get());
            int newMaxItems = chimney.get() * 4;
            maxFuel.set(newMaxItems);
            maxOre.set(newMaxItems);
            while (maxOre.get() < oreStacks.get().size()) {
                //Structure lost one or more chimney levels
                InventoryHelper.spawnItemStack(world, pos.north().getX(), pos.getY(), pos.north().getZ(), oreStacks.get().get(0));
                oreStacks.get().remove(0);
            }
            while (maxFuel.get() < fuelStacks.get().size()) {
                InventoryHelper.spawnItemStack(world, pos.north().getX(), pos.north().getY(), pos.north().getZ(), fuelStacks.get().get(0));
                fuelStacks.get().remove(0);
            }
            addItemsFromWorld();
            updateSlagBlock(state.getValue(LIT));

            oreCount.set(oreStacks.get().size());
            oreUnits.set(oreStacks.get().stream().mapToInt(stack -> {
                IMetalItem metalObject = CapabilityMetalItem.getMetalItem(stack);
                if (metalObject != null) {
                    return metalObject.getSmeltAmount(stack);
                }
                return 1;
            }).sum());
            fuelCount.set(fuelStacks.get().size());

            if (oldChimney != chimney.get() || oldOre != oreCount.get() || oldFuel != fuelCount.get()) {
                markForSync();
            }
        }
    }

    private static final Multiblock BLAST_FURNACE_CHIMNEY;

    static {
        Predicate<IBlockState> stoneMatcher = (state) -> {
            return state.getBlock() instanceof BlockFireBrick;
        };
        Predicate<IBlockState> sheetMatcher = (state) -> {
            if (!(state.getBlock() instanceof BlockMetalSheet)) {
                return false;
            } else {
                BlockMetalSheet block = (BlockMetalSheet)state.getBlock();
                return block.getMetal() == Metal.WROUGHT_IRON || block.getMetal() == Metal.STEEL || (block.getMetal().getRegistryName() != null && block.getMetal().getRegistryName().getResourcePath().equals("mild_steel"));
            }
        };
        BLAST_FURNACE_CHIMNEY = (new Multiblock()).match(new BlockPos(0, 0, 0), (state) -> {
            return state.getBlock() == BlocksTFC.MOLTEN || state.getMaterial().isReplaceable();
        }).match(new BlockPos(0, 0, 1), stoneMatcher).match(new BlockPos(0, 0, -1), stoneMatcher).match(new BlockPos(1, 0, 0), stoneMatcher).match(new BlockPos(-1, 0, 0), stoneMatcher).match(new BlockPos(0, 0, -2), sheetMatcher).match(new BlockPos(0, 0, -2), (tile) -> {
            return tile.getFace(EnumFacing.NORTH);
        }, TEMetalSheet.class).match(new BlockPos(0, 0, 2), sheetMatcher).match(new BlockPos(0, 0, 2), (tile) -> {
            return tile.getFace(EnumFacing.SOUTH);
        }, TEMetalSheet.class).match(new BlockPos(2, 0, 0), sheetMatcher).match(new BlockPos(2, 0, 0), (tile) -> {
            return tile.getFace(EnumFacing.EAST);
        }, TEMetalSheet.class).match(new BlockPos(-2, 0, 0), sheetMatcher).match(new BlockPos(-2, 0, 0), (tile) -> {
            return tile.getFace(EnumFacing.WEST);
        }, TEMetalSheet.class).match(new BlockPos(-1, 0, -1), sheetMatcher).match(new BlockPos(-1, 0, -1), (tile) -> {
            return tile.getFace(EnumFacing.NORTH) && tile.getFace(EnumFacing.WEST);
        }, TEMetalSheet.class).match(new BlockPos(1, 0, -1), sheetMatcher).match(new BlockPos(1, 0, -1), (tile) -> {
            return tile.getFace(EnumFacing.NORTH) && tile.getFace(EnumFacing.EAST);
        }, TEMetalSheet.class).match(new BlockPos(-1, 0, 1), sheetMatcher).match(new BlockPos(-1, 0, 1), (tile) -> {
            return tile.getFace(EnumFacing.SOUTH) && tile.getFace(EnumFacing.WEST);
        }, TEMetalSheet.class).match(new BlockPos(1, 0, 1), sheetMatcher).match(new BlockPos(1, 0, 1), (tile) -> {
            return tile.getFace(EnumFacing.SOUTH) && tile.getFace(EnumFacing.EAST);
        }, TEMetalSheet.class);
    }

    public static int getChimneyLevels(World world, BlockPos pos)
    {
        for (int i = 1; i < 16; i++)
        {
            BlockPos center = pos.up(i);
            if (!BLAST_FURNACE_CHIMNEY.test(world, center))
            {
                return i - 1;
            }
        }
        // Maximum levels
        return 15;
    }

    @Override
    public void onBreakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        // Dump everything in world
        for (int i = 1; i < 16; i++)
        {
            if (world.getBlockState(pos.up(i)).getBlock() == BlocksTFC.MOLTEN)
            {
                world.setBlockToAir(pos.up(i));
            }
        }
        for (ItemStack stack : oreStacks.get())
        {
            InventoryHelper.spawnItemStack(world, pos.north().getX(), pos.getY(), pos.north().getZ(), stack);
        }
        for (ItemStack stack : fuelStacks.get())
        {
            InventoryHelper.spawnItemStack(world, pos.north().getX(), pos.getY(), pos.north().getZ(), stack);
        }
        super.onBreakBlock(world, pos, state);
    }

    private void updateSlagBlock(boolean cooking)
    {
        int slag = fuelStacks.get().size() + oreStacks.get().size();
        //If there's at least one item, show one layer so player knows that it is holding stacks
        int slagLayers = slag == 1 ? 1 : slag / 2;
        for (int i = 1; i < 16; i++)
        {
            if (slagLayers > 0)
            {
                if (slagLayers >= 4)
                {
                    slagLayers -= 4;
                    world.setBlockState(pos.up(i), BlocksTFC.MOLTEN.getDefaultState().withProperty(LIT, cooking).withProperty(BlockMolten.LAYERS, 4));
                }
                else
                {
                    world.setBlockState(pos.up(i), BlocksTFC.MOLTEN.getDefaultState().withProperty(LIT, cooking).withProperty(BlockMolten.LAYERS, slagLayers));
                    slagLayers = 0;
                }
            }
            else
            {
                //Remove any surplus slag(ie: after cooking/structure became compromised)
                if (world.getBlockState(pos.up(i)).getBlock() == BlocksTFC.MOLTEN)
                {
                    world.setBlockToAir(pos.up(i));
                }
            }
        }
    }

    private void addItemsFromWorld()
    {
        EntityItem fluxEntity = null, oreEntity = null;
        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up(), pos.up().add(1, 16, 1)), EntitySelectors.IS_ALIVE);
        for (EntityItem entityItem : items)
        {
            ItemStack stack = entityItem.getItem();
            BlastFurnaceRecipe recipe = BlastFurnaceRecipe.get(stack);
            if (recipe != null)
            {
                oreEntity = entityItem;
                // Try searching for the additive (flux for pig iron)
                for (EntityItem item : items)
                {
                    if (recipe.isValidAdditive(item.getItem()))
                    {
                        fluxEntity = item;
                        break;
                    }
                }
                if (fluxEntity != null)
                {
                    // We have both additives + ores for the found recipe
                    break;
                }
                else
                {
                    // Didn't found the correct additive, abort adding the ore to the input
                    oreEntity = null;
                }
            }
            if (FuelManager.isItemBloomeryFuel(stack))
            {
                // Add fuel
                while (maxFuel.get() > fuelStacks.get().size())
                {
                    markDirty();
                    fuelStacks.get().add(stack.splitStack(1));
                    if (stack.getCount() <= 0)
                    {
                        entityItem.setDead();
                        break;
                    }
                }
            }
        }

        // Add each ore consuming flux
        while (maxOre.get() > oreStacks.get().size())
        {
            if (fluxEntity == null || oreEntity == null)
            {
                break;
            }
            markDirty();

            ItemStack flux = fluxEntity.getItem();
            flux.shrink(1);

            ItemStack ore = oreEntity.getItem();
            oreStacks.get().add(ore.splitStack(1));

            if (flux.getCount() <= 0)
            {
                fluxEntity.setDead();
                fluxEntity = null;
            }

            if (ore.getCount() <= 0)
            {
                oreEntity.setDead();
                oreEntity = null;
            }
        }
    }
}
