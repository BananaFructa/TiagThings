package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkDeviceHistory {

    NBTTagCompound updatePacket = new NBTTagCompound();

    public Item deviceItem;
    public int deviceMetadata;
    public int deviceCount = 0;
    public boolean needsToClear = false;
    ModularList[] timeScales = new ModularList[] {
            new ModularList(100),
            new ModularList(100),
            new ModularList(100)
    };
    static int[] timeDivisions = new int[] {
            12,
            10,
            6
    };
    int[] counters = new int[]{0,0,0};
    List<Integer> oneHour = new ArrayList<>();

    public NetworkDeviceHistory(ItemStack device) {
        this.deviceItem = device.getItem();
        this.deviceMetadata = device.getMetadata();
    }

    public NetworkDeviceHistory(Item item, int meta) {
        this.deviceItem = item;
        this.deviceMetadata = meta;
    }

    public void addEntry(int amount) {
        addEntry(amount,0);
    }

    public void addCount() {
        deviceCount++;
    }

    public void addEntry(int amount, int scale) {
        /*if (needsToClear) {
            deviceCount = 0;
            needsToClear = false;
        }
        deviceCount++;*/
        if (scale == 0) timeScales[scale].set(0,timeScales[scale].get(0) + amount);
        else timeScales[scale].set(0,amount);
        updatePacket.setInteger("add_"+scale,timeScales[scale].get(0));
        counters[scale]++;
        if (counters[scale] == timeDivisions[scale]) {
            counters[scale] = 0;
            int avg = 0;
            for (int j = 0; j < timeDivisions[scale]; j++) avg += timeScales[scale].get(j);
            avg /= timeDivisions[scale];
            if (scale != timeScales.length - 1) addEntry(avg,scale+1);
            else  {
                updatePacket.setInteger("add_hour",avg);
                oneHour.add(avg);
            }
        }
        if (scale != 0) timeScales[scale].nextFrame();
    }

    public int getSize(GraphScale scale) {
        switch (scale) {
            case FIVE_SECONDS:
                return timeScales[0].populatedLength;
            default:
                return 0;
        }
    }

    public int getValue(int index,GraphScale scale) {
        switch (scale) {
            case FIVE_SECONDS:
                return timeScales[0].get(index);
            default:
                return 0;
        }
    }

    public int getLength(GraphScale scale) {
        switch (scale) {
            case FIVE_SECONDS:
            case ONE_MINUTE:
            case TEN_MINUTES:
                return timeScales[scale.ordinal()].length();
            case ONE_HOUR:
                return oneHour.size();
            default:
                return 0;
        }
    }

    public int getTotalActivity() {
        int l = 0;
        for (ModularList list : timeScales) l += list.populatedLength;
        l += oneHour.size();
        return l;
    }

    public NBTTagCompound getUpdateDelta() {
        return updatePacket;
    }

    public void updateDelta(NBTTagCompound tag) {
        // TODO: update count
        for (int i = 0;i < timeScales.length;i++) {
            if (tag.hasKey("add_" + i)) {
                timeScales[i].nextFrame();
                timeScales[i].set(0,tag.getInteger("add_"+i));
            }
        }
        if (tag.hasKey("add_hour")) oneHour.add(tag.getInteger("one_hour"));
    }

    public void next() {
        //needsToClear = true;
        deviceCount = 0;
        updatePacket = new NBTTagCompound();
        timeScales[GraphScale.FIVE_SECONDS.ordinal()].nextFrame();
        timeScales[GraphScale.FIVE_SECONDS.ordinal()].set(0,0);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        ResourceLocation location = Item.REGISTRY.getNameForObject(deviceItem);
        tag.setString("item_device",location  == null ? "minecraft:air" : location.toString());
        tag.setInteger("meta_device",deviceMetadata);
        tag.setInteger("device_count",deviceCount);
        tag.setBoolean("needs_to_clear",needsToClear);
        for (int i = 0;i < timeScales.length;i++) tag.setTag("scale_"+i,timeScales[i].toNBT());
        tag.setIntArray("counters",counters);
        tag.setIntArray("one_hour", oneHour.stream().mapToInt(Integer::intValue).toArray());
        return tag;
    }

    public static NetworkDeviceHistory read(NBTTagCompound tag) {
        Item dItem = Item.getByNameOrId(tag.getString("item_device"));
        int meta = tag.getInteger("meta_device");
        int deviceCount = tag.getInteger("device_count");
        boolean needsToClear = tag.getBoolean("needs_to_clear");
        NetworkDeviceHistory deviceHistory = new NetworkDeviceHistory(dItem,meta);
        deviceHistory.deviceCount = deviceCount;
        deviceHistory.needsToClear = needsToClear;
        for (int i = 0;i < deviceHistory.timeScales.length;i++) {
            deviceHistory.timeScales[i] = ModularList.fromNBT(tag.getCompoundTag("scale_"+i));
        }
        deviceHistory.counters = tag.getIntArray("counters");
        deviceHistory.oneHour = Arrays.stream(tag.getIntArray("one_hour")).boxed().collect(Collectors.toList());
        return deviceHistory;
    }

    // TODO: save data

}
