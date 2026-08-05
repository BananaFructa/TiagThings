package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class SwitchPLCModule extends PLCModule{

    boolean on = false;

    public SwitchPLCModule(String name) {
        super(name,Modules.SWITCH);
    }

    @Override
    public int[] process(int[] in) {
        if (on) return new int[]{in[0]};
        else return new int[]{0};
    }

    public void toggle() {
        on = !on;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        on = tag.getBoolean("on");
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setBoolean("on",on);
        return tag;
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
    public String[] getDisplay() {
        return new String[]{super.getDisplay()[0],"State: " + (on ? "On" : "Off")};
    }
}
