package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class CounterPLCModule extends PLCModule{

    boolean last = false;
    byte count = 0;

    public CounterPLCModule(String name) {
        super(name,Modules.COUNTER);
    }

    @Override
    public int[] process(int[] in) {
        if (in[0] >= 7 && !last) {
            last = true;
            count++;
        } else {
            last = in[0] >= 7;
        }
        return new int[]{toS(count & 0b0001),toS(count & 0b0010),toS(count & 0b0100),toS(count & 0b1000)};
    }

    private int toS(int a) {
        return a  != 0 ? 15 : 0;
    }

    @Override
    public int inputCount() {
        return 1;
    }

    @Override
    public int outputCount() {
        return 4;
    }

    @Override
    public int colorForInput(int in) {
        return 0xff8800ff;
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setBoolean("last",last);
        tag.setByte("count",count);
        return tag;
    }

    @Override
    public void readState(NBTTagCompound tag) {
        super.readState(tag);
        last = tag.getBoolean("last");
        count = tag.getByte("count");
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        /*if (includeState) {
            tag.setBoolean("last",last);
        }*/
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        //if (tag.hasKey("last")) last = tag.getBoolean("last");
    }
}
