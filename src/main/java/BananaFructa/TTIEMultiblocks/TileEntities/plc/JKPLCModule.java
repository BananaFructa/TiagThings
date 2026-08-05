package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class JKPLCModule extends PLCModule{

    boolean last = false;
    boolean out = true;

    public JKPLCModule(String name) {
        super(name,Modules.JK_FLIP_FLOP);
    }

    @Override
    public int[] process(int[] in) {
        if (in[2] >= 7 && !last) {
            last = true;
            boolean A = in[0] >= 7;
            boolean B = in[1] >= 7;
            if (A && !B) {
                out = true;
            } else if (!A && B) {
                out = false;
            } else if (A && B) {
                out = !out;
            }
        } else {
            last = in[2] >= 7;
        }
        return new int[]{out ? 15 : 0,!out ? 15 : 0};
    }

    @Override
    public int inputCount() {
        return 3;
    }

    @Override
    public int colorForInput(int in) {
        if (in == 2) return ColorScheme.CLOCK_IN.get();
        return super.colorForInput(in);
    }

    @Override
    public int outputCount() {
        return 2;
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setBoolean("last",last);
        tag.setBoolean("out",out);
        return tag;
    }

    @Override
    public void readState(NBTTagCompound tag) {
        super.readState(tag);
        last = tag.getBoolean("last");
        out = tag.getBoolean("out");
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
