package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import li.cil.oc.common.block.Item;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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
        return stack.getItem().getRegistryName().toString() + stack.getMetadata();
    }

    public int getActivityScore() {
        int score = 0;
        for (String s : productionHistory.keySet()) score += productionHistory.get(s).getLength();
        for (String s : consumptionHistory.keySet()) score += consumptionHistory.get(s).getLength();
        return score;
    }

}
