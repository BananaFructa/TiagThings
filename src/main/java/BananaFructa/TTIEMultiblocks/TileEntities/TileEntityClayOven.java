package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import journeymap.client.render.map.Tile;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.dries007.tfc.objects.blocks.property.ILightableBlock;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.te.TEFirePit;
import net.dries007.tfc.objects.te.TEInventory;
import net.dries007.tfc.objects.te.TEPlacedItem;
import net.minecraft.block.BlockFire;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileEntityClayOven extends SimplifiedTileEntityMultiblockMetal<TileEntityClayOven, SimplifiedMultiblockRecipe> {

    static private String[] recipesOvenOnly = new String[]
            {
                    "<firmalife:dried_cocoa_beans>",
                    "<firmalife:roasted_cocoa_beans>",
                    "<firmalife:chestnuts>",
                    "<firmalife:roasted_chestnuts>",
                    "<firmalife:chestnut_dough>",
                    "<firmalife:chestnut_bread>",
                    "<firmalife:pizza_dough>",
                    "<firmalife:cooked_pizza>",
                    "<tfc:food/barley_dough>",
                    "<tfc:food/barley_bread>",
                    "<tfc:food/cornmeal_dough>",
                    "<tfc:food/cornbread>",
                    //"<tfc:food/oat_dough>",
                    //"<tfc:food/cornbread>",
                    "<tfc:food/oat_dough>",
                    "<tfc:food/oat_bread>",
                    "<tfc:food/rice_dough>",
                    "<tfc:food/rice_bread>",
                    "<tfc:food/rye_dough>",
                    "<tfc:food/rye_bread>",
                    "<tfc:food/wheat_dough>",
                    "<tfc:food/wheat_bread>",
                    "<firmalife:barley_flatbread_dough>",
                    "<firmalife:barley_flatbread>",
                    "<firmalife:corn_flatbread_dough>",
                    "<firmalife:corn_flatbread>",
                    "<firmalife:oat_flatbread_dough>",
                    "<firmalife:oat_flatbread>",
                    "<firmalife:rice_flatbread_dough>",
                    "<firmalife:rice_flatbread>",
                    "<firmalife:rye_flatbread_dough>",
                    "<firmalife:rye_flatbread>",
                    "<firmalife:wheat_flatbread_dough>",
                    "<firmalife:wheat_flatbread>",
                    "<firmalife:white_chocolate_blend>",
                    "<firmalife:white_chocolate>",
                    "<firmalife:dark_chocolate_blend>",
                    "<firmalife:dark_chocolate>",
                    "<firmalife:milk_chocolate_blend>",
                    "<firmalife:milk_chocolate>",
                    "<tfcflorae:food/hash_muffin_dough>",
                    "<tfcflorae:food/hash_muffin>",
                    "<tfcflorae:food/amaranth_dough>",
                    "<tfcflorae:food/amaranth_bread>",
                    "<tfcflorae:food/buckwheat_dough>",
                    "<tfcflorae:food/buckwheat_bread>",
                    "<tfcflorae:food/fonio_dough>",
                    "<tfcflorae:food/fonio_bread>",
                    "<tfcflorae:food/millet_dough>",
                    "<tfcflorae:food/millet_bread>",
                    "<tfcflorae:food/quinoa_dough>",
                    "<tfcflorae:food/quinoa_bread>",
                    "<tfcflorae:food/spelt_dough>",
                    "<tfcflorae:food/spelt_bread>",
                    "<tfcflorae:food/amaranth_flatbread_dough>",
                    "<tfcflorae:food/amaranth_flatbread>",
                    "<tfcflorae:food/buckwheat_flatbread_dough>",
                    "<tfcflorae:food/buckwheat_flatbread>",
                    "<tfcflorae:food/fonio_flatbread_dough>",
                    "<tfcflorae:food/fonio_flatbread>",
                    "<tfcflorae:food/millet_flatbread_dough>",
                    "<tfcflorae:food/millet_flatbread>",
                    "<tfcflorae:food/quinoa_flatbread_dough>",
                    "<tfcflorae:food/quinoa_flatbread>",
                    "<tfcflorae:food/spelt_flatbread_dough>",
                    "<tfcflorae:food/spelt_flatbread>",
                    "<tfcflorae:food/dried/coffea_cherries>",
                    "<tfcflorae:food/roasted/coffee_beans>"
            };

    private TileEntity teLast = null;
    private ItemStackHandler handler = null;
    private List<Integer> slotProcessOrder = new ArrayList<>();
    private HashMap<Integer,Integer> slotToProcess = new HashMap<>();

    boolean first = false; // can't use onLoad

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        for (int i = 0;i < recipesOvenOnly.length;i+=2) {
            add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId(recipesOvenOnly[i])},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId(recipesOvenOnly[i+1])},new FluidStack[0],0,20*60*2));
        }
    }};
    public TileEntityClayOven() {
        super(TTIEContent.clayOven, 0, false,recipes);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        BlockPos pos = getBlockPosForPos(16).offset(EnumFacing.DOWN);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TEPlacedItem) {
            handler = Utils.readDeclaredField(TEInventory.class, te, "inventory");
        }
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote || isDummy()) return;
        BlockPos pos = getBlockPosForPos(16).offset(EnumFacing.DOWN);
        TileEntity te = world.getTileEntity(pos);

        if (!first) {
            first = true;
            if (!(te instanceof TEPlacedItem)) {
                clearAll();
            }
        }

        if (teLast != te) {
            teLast = te;
            if (te instanceof TEPlacedItem) {
                handler = Utils.readDeclaredField(TEInventory.class,te,"inventory");
            } else {
                handler = null;
                clearAll();
            }
        }

        if (isFireBurning()) {
            if (handler != null) {
                // Checks if all recipes can be continues
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack is = handler.getStackInSlot(i);
                    if (slotToProcess.containsKey(i)) { // Means slot has a recipe associated
                        int p = slotToProcess.get(i);

                        // Check if the recipes associated with the slot is still matches
                        if (!ItemStack.areItemsEqual(this.processQueue.get(p).recipe.getItemInputs().get(0).stack, is)) {

                            // If not remove the slot binding info
                            slotToProcess.remove(i);
                            slotProcessOrder.remove((Integer) i);
                            this.processQueue.remove(p);

                            // Subtract 1 from existing positions of processQueue stored in the hashmap so that they still point towards the right recipes
                            for (Integer key : slotToProcess.keySet()) {
                                if (slotToProcess.get(key) > p) slotToProcess.put(key, slotToProcess.get(key) - 1);
                            }

                        }
                    } else {
                        // no black list as every recipe will consist of an item stack of size 1 since you can only place 1 item in each slot
                        SimplifiedMultiblockRecipe recipe = IEUtils.findRecipe(recipes, new FluidStack[0], new ItemStack[]{is}, new ArrayList<>());
                        if (recipe != null) {
                            this.processQueue.add(new MultiblockProcessInMachine<>(recipe));
                            slotToProcess.put(i, this.processQueue.size() - 1);
                            slotProcessOrder.add(i);
                        }
                    }
                }
            }
        } else {
            clearAll();
        }
    }

    private void clearAll() {
        slotProcessOrder.clear();
        slotToProcess.clear();
        this.processQueue.clear();
    }

    private boolean isFireBurning() {
        BlockPos pos = getBlockPosForPos(13).offset(EnumFacing.DOWN);
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFirePit) {
            return state.getValue(((ILightableBlock)state.getBlock()).LIT);
        }
        return false;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 4;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 4;
    }

    @Override
    public void doProcessOutput(ItemStack itemStack) {

        for (Integer i : slotProcessOrder) {
            if (ItemStack.areItemsEqual(this.processQueue.get(slotToProcess.get(i)).recipe.getItemOutputs().get(0),itemStack)) {

                handler.setStackInSlot(i,itemStack);
                IEUtils.notifyClientUpdate(world,getBlockPosForPos(16).offset(EnumFacing.DOWN));

                int p = slotToProcess.get(i);
                slotToProcess.remove(i);
                slotProcessOrder.remove((Integer) i);

                // Subtract 1 from existing positions of processQueue stored in the hashmap so that they still point towards the right recipes
                for (Integer key: slotToProcess.keySet()) {
                    if (slotToProcess.get(key) > p) slotToProcess.put(key,slotToProcess.get(key)-1);
                }

                break;
            }
        }
    }

    @Override
    public void onProcessFinish(MultiblockProcess<SimplifiedMultiblockRecipe> multiblockProcess) {}

    @Override
    public void initPorts() {}

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("slotProcessOrderSize",slotProcessOrder.size());
        for (int i = 0;i < slotProcessOrder.size();i++) {
            nbt.setInteger("slotProcessOrder-"+i,slotProcessOrder.get(i));
        }
        int index = 0;
        for (Integer k : slotToProcess.keySet()) {
            nbt.setInteger("slotToProcessKey-"+index,k);
            nbt.setInteger("slotToProcessValues-"+index,slotToProcess.get(k));
            index++;
        }
        nbt.setInteger("slotToProcessSize",index);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        int size = nbt.getInteger("slotProcessOrderSize");
        for(int i = 0;i < size;i++) {
            slotProcessOrder.add(nbt.getInteger("slotProcessOrder-"+i));
        }
        size = nbt.getInteger("slotToProcessSize");
        for (int i = 0;i < size;i++) {
            slotToProcess.put(nbt.getInteger("slotToProcessKey-"+i),nbt.getInteger("slotToProcessValues-"+i));
        }
    }
}
