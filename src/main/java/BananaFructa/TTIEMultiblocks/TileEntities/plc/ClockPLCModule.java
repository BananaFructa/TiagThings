package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

import javax.vecmath.Vector3f;

public class ClockPLCModule extends PLCModule{

    int delay = 10;
    int timeWithoutHigh = 0;
    boolean state = false;

    public ClockPLCModule(String name) {
        super(name,Modules.CLOCK);
    }

    @Override
    public int[] process(int[] in) {
        if (timeWithoutHigh > 0) timeWithoutHigh -= 2; // runs every two ticks
        if (timeWithoutHigh <= 0) {
            timeWithoutHigh = delay / 2;
            state = !state;
        }
        if (!state) {
            return new int[]{0};
        } else {
            return new int[]{15};
        }
    }

    @Override
    public int inputCount() {
        return 0;
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
        return new String[]{super.getDisplay()[0],"Period: " + String.format("%.1f",delay/20.0f) + " seconds"};
    }

    @Override
    public String getFieldName(int id) {
        if (id == 0) return "Period (seconds)";
        return super.getFieldName(id);
    }

    @Override
    public void setFieldValue(int id, String value) {
        if (id == 0) {
            if (value.matches("[+-]?([0-9]*[.])?[0-9]+")) {
                try {
                    float v = Float.parseFloat(value);
                    delay = (int)(Math.min(Math.max(2, v*20), 3600));
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
    public void readState(NBTTagCompound tag) {
        timeWithoutHigh = tag.getInteger("time");
        state = tag.getBoolean("state");
        super.readState(tag);
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setInteger("time", timeWithoutHigh);
        tag.setBoolean("state",state);
        return tag;
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setInteger("delay", delay);
        /*if (includeState) {
            tag.setInteger("time", timeWithoutHigh);
            tag.setBoolean("state",state);
        }*/
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        delay = tag.getInteger("delay");
        /*if (tag.hasKey("time")) {
            timeWithoutHigh = tag.getInteger("time");
            state = tag.getBoolean("state");
        }*/
    }
}
