package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class PulseExtenderPLCModule extends PLCModule{

    int delay = 10;
    int timeWithoutHigh = 0;

    public PulseExtenderPLCModule(String name) {
        super(name,Modules.PULSE_EXTENDER);
    }

    @Override
    public int[] process(int[] in) {
        if (timeWithoutHigh > 0) timeWithoutHigh -= 2; // runs every two ticks
        if (timeWithoutHigh < 0) timeWithoutHigh = 0;
        if (in[0] >= 7) timeWithoutHigh = delay;
        if (timeWithoutHigh <= 0) {
            return new int[]{0};
        } else {
            return new int[]{15};
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
    public int fieldCount() {
        return 1;
    }

    @Override
    public String[] getDisplay() {
        return new String[]{super.getDisplay()[0],"Length: " + String.format("%.1f",delay/20.0f) + " seconds"};
    }

    @Override
    public String getFieldName(int id) {
        if (id == 0) return "Length (seconds)";
        return super.getFieldName(id);
    }

    @Override
    public void setFieldValue(int id, String value) {
        if (id == 0) {
            if (value.matches("[+-]?([0-9]*[.])?[0-9]+")) {
                try {
                    float v = Float.parseFloat(value);
                    delay = (int)(Math.min(Math.max(0, v*20), 3600));
                } catch (Exception err) {

                }
            }
        }
        super.setFieldValue(id, value);
    }

    @Override
    public String getFieldValue(int id) {
        if (id == 0) return String.format("%.1f",delay/20.0f);
        return super.getFieldValue(id);
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setInteger("time", timeWithoutHigh);
        return tag;
    }

    @Override
    public void readState(NBTTagCompound tag) {
        super.readState(tag);
        timeWithoutHigh = tag.getInteger("time");
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setInteger("delay", delay);
        /*if (includeState) {
            tag.setInteger("time", timeWithoutHigh);
        }*/
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        delay = tag.getInteger("delay");
        /*if (tag.hasKey("time")) {
            timeWithoutHigh = tag.getInteger("time");
        }*/
    }
}
