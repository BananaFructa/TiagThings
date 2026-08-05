package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.nbt.NBTTagCompound;

public class ConstantPLCModule extends PLCModule{

    int strength = 15;

    public ConstantPLCModule(String name) {
        super(name,Modules.CONSTANT);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{strength};
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
        return new String[]{super.getDisplay()[0],"Strength: " + strength};
    }

    @Override
    public String getFieldName(int id) {
        if (id == 0) return "Strength";
        return super.getFieldName(id);
    }

    @Override
    public void setFieldValue(int id, String value) {
        if (id == 0) {
            if (value.matches("-?\\d+")) {
                strength = Math.min(Math.max(0,Integer.parseInt(value)),15);
            }
        }
        super.setFieldValue(id, value);
    }

    @Override
    public String getFieldValue(int id) {
        if (id == 0) return Integer.toString(strength);
        return super.getFieldValue(id);
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setInteger("strength",strength);
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        strength = tag.getInteger("strength");
    }
}
