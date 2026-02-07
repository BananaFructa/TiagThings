package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.nbt.NBTTagCompound;

public class ModularList {

    int populatedLength = 0;
    int size = 0;
    int begin = 0;
    int[] arr;

    public ModularList(int size) {
        this.size = size;
        arr = new int[size];
    }

    public void set(int index, int val) {
        index = index + begin;
        index = index % size;
        arr[index] = val;
    }

    public void nextFrame() {
        if (populatedLength != size) populatedLength++;
        begin++;
        if (begin == size) begin = 0;
    }

    public int length() {
        return populatedLength;
    }

    public int get(int i) {
        int index = i+begin;
        index = index % size;
        return arr[index];
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("size",size);
        tag.setInteger("begin",begin);
        tag.setInteger("populated_length",populatedLength);
        tag.setIntArray("arr",arr);
        return tag;
    }

    public static ModularList fromNBT(NBTTagCompound tag) {
        int size = tag.getInteger("size");
        int begin = tag.getInteger("begin");
        int populatedLength = tag.getInteger("populated_length");
        int[] arr = tag.getIntArray("arr");
        ModularList list = new ModularList(size);
        list.begin = begin;
        list.populatedLength = populatedLength;
        list.arr = arr;
        return list;
    }

}
