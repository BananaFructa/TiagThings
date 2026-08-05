package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class TimerPLCModule extends PLCModule{

    int delay = 20;
    int time = 0;
    boolean started = false;

    public TimerPLCModule(String name) {
        super(name,Modules.TIMER);
    }

    @Override
    public int[] process(int[] in) {
        if (in[0] >= 7 && !started) {
            started = true;
            time = delay;
        }
        if (time > 0) time -= 2; // runs every two ticks
        if (time <= 0 && started) {
            time = 0;
            started = false;
            return new int[]{15};
        }
        return new int[]{0};
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
        return new String[]{super.getDisplay()[0],"Delay: " + String.format("%.1f",delay/20.0f) + " seconds", "Time: " + String.format("%.1f",time/20.0f) + " seconds"};
    }

    @Override
    public String getFieldName(int id) {
        if (id == 0) return "Delay (seconds)";
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
    public void readState(NBTTagCompound tag) {
        super.readState(tag);
        time = tag.getInteger("time");
        started = tag.getBoolean("started");
    }

    @Override
    public NBTTagCompound getState() {
        NBTTagCompound tag = super.getState();
        tag.setInteger("time",time);
        tag.setBoolean("started",started);
        return tag;
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setInteger("delay", delay);
        /*if (includeState) {
            tag.setInteger("time", time);
        }*/
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        delay = tag.getInteger("delay");
        /*if (tag.hasKey("time")) {
            time = tag.getInteger("time");
        }*/
    }
}
