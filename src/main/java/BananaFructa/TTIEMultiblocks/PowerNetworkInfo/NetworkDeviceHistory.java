package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkDeviceHistory {

    NBTTagCompound updatePacket = new NBTTagCompound();

    public Item deviceItem;
    public int deviceMetadata;
    public int deviceCount = 0;
    public int tempDeviceCount = 0;
    public boolean needsToClear = false;
    ModularList[] timeScales = new ModularList[] {
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
            new ModularList(100),
    };
    static int[] timeDivisions = new int[] {
            12,
            10,
            6,
            10,
            5,
            5,
            4
    };
    int[] counters = new int[]{0,0,0,0,0,0,0};
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
        tempDeviceCount++;
    }

    public void addEntry(int amount, int scale) {
        if (timeScales[scale].length() >= 1) {
            timeScales[scale].set(0, timeScales[scale].get(0) + amount);
        }
    }

    public void incrementCounter(int scale) {
        if (scale != timeScales.length - 1) {
            counters[scale]++;
            if (counters[scale] >= timeDivisions[scale]) {
                counters[scale] = 0;
                int avg = 0;
                for (int j = 0; j < timeDivisions[scale]; j++) avg += timeScales[scale].get(j);
                avg /= timeDivisions[scale];
                int sNext = scale+1;
                timeScales[sNext].add(avg);
                updatePacket.setInteger("add_" + sNext, timeScales[sNext].get(0));
                incrementCounter(sNext);
            }
        }
    }

    private HashMap<GraphScale,Float> averageCache = new HashMap<>();

    public float getAverage(GraphScale scale) {
        if (averageCache.containsKey(scale)) return averageCache.get(scale);
        if (scale.ordinal() < timeScales.length) {
            ModularList list = timeScales[scale.ordinal()];
            int sum = 0;
            for (int i = 0;i < list.length();i++) {
                sum += list.get(i);
            }
            float avg = (float)sum/list.length();
            averageCache.put(scale,avg);
            return avg;
        } else {
            return 0; // TODO: implement this?
        }
    }

    public boolean emptyFor(GraphScale scale) {
        ModularList list = timeScales[scale.ordinal()];
        for (int i = 0; i < list.length(); i++) {
            if (list.get(i) != 0) return false;
        }
        return true;
    }

    public int getSize(GraphScale scale) {
        return timeScales[scale.ordinal()].populatedLength;
    }

    public int getValue(int index,GraphScale scale) {
        return timeScales[scale.ordinal()].get(index);
    }

    public int getLength(GraphScale scale) {
        return timeScales[scale.ordinal()].length();
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
        // TODO: update connected device count
        averageCache.clear();
        for (int i = 0;i < timeScales.length;i++) {
            if (tag.hasKey("add_" + i)) {
                timeScales[i].add(tag.getInteger("add_"+i));
            }
        }
    }

    public void next() {
        deviceCount = tempDeviceCount;
        tempDeviceCount = 0;
        updatePacket = new NBTTagCompound();
        updatePacket.setInteger("add_" + 0, timeScales[0].get(0));
        incrementCounter(GraphScale.FIVE_SECONDS.ordinal());
        timeScales[GraphScale.FIVE_SECONDS.ordinal()].add(0);
        averageCache.clear();
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
