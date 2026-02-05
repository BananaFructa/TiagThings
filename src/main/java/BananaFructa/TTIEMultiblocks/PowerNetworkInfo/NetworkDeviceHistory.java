package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

import static BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GraphScale.FIVE_SECONDS;

public class NetworkDeviceHistory {

    public Item deviceItem;
    public int deviceMetadata;
    public int deviceCount = 0;
    public boolean needsToClear = false;
    ModularList[] timeScales = new ModularList[] {
            new ModularList(100),
            new ModularList(100),
            new ModularList(100)
    };
    int[] timeDivisions = new int[] {
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

    public void addEntry(int amount) {
        addEntry(amount,0);
    }

    public void addEntry(int amount, int scale) {
        if (needsToClear) {
            deviceCount = 0;
            needsToClear = false;
        }
        deviceCount++;
        timeScales[scale].add(amount);
        counters[scale]++;
        if (counters[scale] == timeDivisions[scale]) {
            int avg = 0;
            for (int j = 0; j < timeDivisions[scale]; j++) avg += timeScales[scale].get(j);
            avg /= timeDivisions[scale];
            if (scale != timeScales.length - 1) addEntry(avg,scale+1);
            else oneHour.add(avg);
        }
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

    public int getLength() {
        int l = 0;
        for (ModularList list : timeScales) l += list.populatedLength;
        l += oneHour.size();
        return l;
    }

    public void tickClear() {
        needsToClear = true;
        for (ModularList list : timeScales) list.nextFrame();
    }

    // TODO: save data

}
