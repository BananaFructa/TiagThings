package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import li.cil.oc.common.block.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;

public class NetworkData {

    public HashMap<String,NetworkDeviceHistory> productionHistory = new HashMap<>();
    public HashMap<String,NetworkDeviceHistory> consumptionHistory = new HashMap<>();

    public void registerTransfer(int delta, TileEntity interactor) {
        if (interactor instanceof TileEntityMultiblockMetal<?,?>) {
            interactor = ((TileEntityMultiblockMetal<?, ?>) interactor).master();
        }
        IBlockState state = interactor.getWorld().getBlockState(interactor.getPos());
        ItemStack deviceStack = new ItemStack(Item.getItemFromBlock(state.getBlock()),state.getBlock().getMetaFromState(state));
        String id = getId(deviceStack);
        if (delta > 0) {
            if (!productionHistory.containsKey(id)) {
                productionHistory.put(id,new NetworkDeviceHistory(deviceStack));
            }
            productionHistory.get(id).addEntry(delta);
        } else if (delta < 0){
            if (!consumptionHistory.containsKey(id)) {
                consumptionHistory.put(id,new NetworkDeviceHistory(deviceStack));
            }
            consumptionHistory.get(id).addEntry(-delta);
        }
    }

    public void tick() {
        for (String id : productionHistory.keySet()) productionHistory.get(id).tickClear();
        for (String id : consumptionHistory.keySet()) consumptionHistory.get(id).tickClear();
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
