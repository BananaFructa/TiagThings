package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class FallingDetectorPLCModule extends PLCModule{

    boolean last = false;

    public FallingDetectorPLCModule(String name) {
        super(name,Modules.FALLING_EDGE_DETECTOR);
    }

    @Override
    public int[] process(int[] in) {
        if (in[0] < 7 && last) {
            last = false;
            return new int[]{15};
        } else {
            last = in[0] >= 7;
            return new int[]{0};
        }
    }

    @Override
    public int inputCount() {
        return 1;
    }

    @Override
    public int outputCount() {
        return 1;
    }

    @Override
    public void readState(NBTTagCompound tag) {
        super.readState(tag);
        last = tag.getBoolean("last");
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setBoolean("last",last);
        return tag;
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
