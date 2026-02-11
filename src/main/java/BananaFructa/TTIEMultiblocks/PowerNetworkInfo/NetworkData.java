package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import li.cil.oc.common.block.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NetworkData {

    public HashMap<String,NetworkDeviceHistory> productionHistory = new HashMap<>();
    public HashMap<String,NetworkDeviceHistory> consumptionHistory = new HashMap<>();

    private List<String> newProdEntries = new ArrayList<>();
    private List<String> newConEntries = new ArrayList<>();

    public void registerTransfer(int delta, boolean consumer, TileEntity interactor) {
        if (interactor instanceof TileEntityMultiblockMetal<?,?>) {
            interactor = ((TileEntityMultiblockMetal<?, ?>) interactor).master();
        }
        IBlockState state = interactor.getWorld().getBlockState(interactor.getPos());
        ItemStack deviceStack = new ItemStack(Item.getItemFromBlock(state.getBlock()));
        if (deviceStack.getItem().getHasSubtypes()) deviceStack.setItemDamage(state.getBlock().getMetaFromState(state));
        String id = getId(deviceStack);
        if (!consumer) {
            //boolean newe = false;
            //if (!productionHistory.containsKey(id)) {
            //    productionHistory.put(id,new NetworkDeviceHistory(deviceStack));
            //    newe = true;
            //}
            productionHistory.get(id).addEntry(delta);
            //if (newe) newProdEntries.add(id);
        } else {
            //boolean newe = false;
            //if (!consumptionHistory.containsKey(id)) {
            //    consumptionHistory.put(id,new NetworkDeviceHistory(deviceStack));
            //    newe = true;
            //}
            //System.out.println("DELTA: " + delta);
            //System.out.println(id + " " + consumer);

            consumptionHistory.get(id).addEntry(-delta);
            //if (newe) newConEntries.add(id);
        }
    }

    private List<TileEntity> uniqueProducers = new ArrayList<>();
    private List<TileEntity> uniqueConsumers = new ArrayList<>();

    public void notifyLoad(boolean consumer, TileEntity interactor) {
        if (interactor instanceof TileEntityMultiblockMetal<?,?>) {
            interactor = ((TileEntityMultiblockMetal<?, ?>) interactor).master();
        }
        IBlockState state = interactor.getWorld().getBlockState(interactor.getPos());
        ItemStack deviceStack = new ItemStack(Item.getItemFromBlock(state.getBlock()));
        if (deviceStack.getItem().getHasSubtypes()) deviceStack.setItemDamage(state.getBlock().getMetaFromState(state));
        String id = getId(deviceStack);
        if (!consumer) {
            boolean newe = false;
            if (!productionHistory.containsKey(id)) {
                productionHistory.put(id,new NetworkDeviceHistory(deviceStack));
                newe = true;
            }
            if (!uniqueProducers.contains(interactor)) {
                productionHistory.get(id).addCount();
                uniqueProducers.add(interactor);
            }
            if (newe) newProdEntries.add(id);
        } else {
            boolean newe = false;
            if (!consumptionHistory.containsKey(id)) {
                consumptionHistory.put(id,new NetworkDeviceHistory(deviceStack));
                newe = true;
            }
            if(!uniqueConsumers.contains(interactor)) {
                consumptionHistory.get(id).addCount();
                uniqueConsumers.add(interactor);
            }
            if (newe) newConEntries.add(id);
        }
    }

    public NBTTagCompound getUpdateDelta() {
        NBTTagCompound tag = new NBTTagCompound();
        int pCount = 0;
        for (String p : productionHistory.keySet()) {
            if (!newProdEntries.contains(p)) {
                tag.setString("prod_delta_key_"+pCount,p);
                tag.setTag("prod_delta_"+pCount,productionHistory.get(p).getUpdateDelta());
                pCount++;
            }
        }
        tag.setInteger("prod_delta_count",pCount);
        int cCount = 0;
        for (String c : consumptionHistory.keySet()) {
            if (!newConEntries.contains(c)) {
                tag.setString("cons_delta_key_"+cCount,c);
                tag.setTag("cons_delta_"+cCount,consumptionHistory.get(c).getUpdateDelta());
                cCount++;
            }
        }
        tag.setInteger("cons_delta_count",cCount);
        // TODO: Implement device additions or subtractions
        return tag;
    }

    public void updateFromDelta(NBTTagCompound tag) {
        int pCount = tag.getInteger("prod_delta_count");
        for (int i = 0;i < pCount;i++) {
            String key = tag.getString("prod_delta_key_"+i);
            NBTTagCompound deltaTag = tag.getCompoundTag("prod_delta_"+i);
            if (productionHistory.containsKey(key)) productionHistory.get(key).updateDelta(deltaTag);
        }
        int cCount = tag.getInteger("cons_delta_count");
        for (int i = 0;i < cCount;i++) {
            String key = tag.getString("cons_delta_key_"+i);
            NBTTagCompound deltaTag = tag.getCompoundTag("cons_delta_"+i);
            if (consumptionHistory.containsKey(key)) consumptionHistory.get(key).updateDelta(deltaTag);
        }
    }

    public void tick() {
        for (String id : productionHistory.keySet()) productionHistory.get(id).next();
        for (String id : consumptionHistory.keySet()) consumptionHistory.get(id).next();
        newProdEntries.clear();
        newConEntries.clear();
        uniqueProducers.clear();
        uniqueConsumers.clear();
    }

    private String getId(ItemStack stack) {
        return stack.getItem().getRegistryName().toString() + "@@" + stack.getMetadata();
    }

    public int getActivityScore() {
        int score = 0;
        for (String s : productionHistory.keySet()) score += productionHistory.get(s).getTotalActivity();
        for (String s : consumptionHistory.keySet()) score += consumptionHistory.get(s).getTotalActivity();
        return score;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        int sizeProd = productionHistory.size();
        tag.setInteger("prod_size",sizeProd);
        int i = 0;
        for (String prod : productionHistory.keySet()) {
            tag.setString("prod_key_" + i,prod);
            tag.setTag("prod_"+i,productionHistory.get(prod).toNBT());
            i++;
        }
        int sizeCons = consumptionHistory.size();
        tag.setInteger("cons_size",sizeCons);
        i = 0;
        for (String cons : consumptionHistory.keySet()) {
            tag.setString("cons_key_"+i,cons);
            tag.setTag("cons_"+i,consumptionHistory.get(cons).toNBT());
            i++;
        }
        return tag;
    }

    public static NetworkData fromNBT(NBTTagCompound tag) {
        NetworkData data = new NetworkData();
        int sizeProd = tag.getInteger("prod_size");
        for (int i = 0;i < sizeProd;i++) {
            String key = tag.getString("prod_key_"+i);
            NetworkDeviceHistory history = NetworkDeviceHistory.read(tag.getCompoundTag("prod_"+i));
            data.productionHistory.put(key,history);
        }
        int sizeCons = tag.getInteger("cons_size");
        for (int i = 0;i < sizeCons;i++) {
            String key = tag.getString("cons_key_"+i);
            NetworkDeviceHistory history = NetworkDeviceHistory.read(tag.getCompoundTag("cons_"+i));
            data.consumptionHistory.put(key,history);
        }
        return data;
    }
}
