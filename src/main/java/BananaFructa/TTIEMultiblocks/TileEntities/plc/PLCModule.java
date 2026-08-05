package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.NetworkDeviceHistory;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityPLC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public abstract class PLCModule {

    public double x;
    public double y;
    public int[] outputs;
    public int[] bufferedOutputs;
    public int[] inputsFrom;
    public int[] inputsFromPosition;
    public String name;
    public int width;
    public int height;
    public int id;
    public Modules moduleType; // :(

    public PLCModule(String name,Modules moduleType) {
        this.name = name;
        this.outputs = new int[outputCount()];
        this.bufferedOutputs = new int[outputCount()];
        inputsFrom = new int[inputCount()];
        for (int i = 0;i < inputCount();i++) inputsFrom[i] = -1;
        inputsFromPosition = new int[inputCount()];
        this.moduleType = moduleType;
    }

    public void update(TileEntityPLC container) {
        int[] in = collectInputs(container);
        bufferedOutputs = process(in);
    }

    public String[] getDisplay() {
        return new String[]{name};
    }

    public void swapBuffers() {
        this.outputs = bufferedOutputs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(double x,double y) {
        this.x = x;
        this.y = y;
    }

    public void translate(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void setSize(int w,int h) {
        this.width = w;
        this.height = h;
    }

    public abstract int[] process(int[] in);

    protected int[] collectInputs(TileEntityPLC container) {
        int[] inputs = new int[inputCount()];
        for (int i = 0;i < inputCount();i++) {
            if (inputsFrom[i] == -1) inputs[i] = 0;
            else inputs[i] = container.getValue(inputsFrom[i],inputsFromPosition[i]);
        }
        return inputs;
    }

    public abstract int inputCount();
    public abstract int outputCount();

    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("x",x);
        tag.setDouble("y",y);
        if (includeState) tag.setTag("state",getState());
        tag.setIntArray("from",inputsFrom);
        tag.setIntArray("inputsFrom",inputsFromPosition);
        tag.setInteger("type",moduleType.ordinal());
        tag.setInteger("id",id);
        tag.setInteger("width",width);
        tag.setInteger("height",height);
        return tag;
    }

    public int colorForInput(int in) {
        return 0xff0066af;
    }

    public int colorForOutput(int out) {
        return 0xffe2632c;
    }

    public int fieldCount() {
        return 0;
    }

    public String getFieldValue(int id) {
        return null;
    }

    public void setFieldValue(int id, String value) {

    }

    public String getFieldName(int id) {
        return null;
    }

    public static PLCModule fromNBT(NBTTagCompound tag) {
        Modules mod = Modules.values()[tag.getInteger("type")];
        PLCModule module = mod.instance();
        module.read(tag);
        return module;
    }

    public void read(NBTTagCompound tag) {
        x = tag.getDouble("x");
        y = tag.getDouble("y");
        inputsFrom = tag.getIntArray("from");
        inputsFromPosition = tag.getIntArray("inputsFrom");
        moduleType = Modules.values()[tag.getInteger("type")];
        id = tag.getInteger("id");
        width = tag.getInteger("width");
        height = tag.getInteger("height");
        if (tag.hasKey("state")) readState(tag.getCompoundTag("state"));
    }

    public NBTTagCompound getState() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setIntArray("outputs",outputs);
        tag.setIntArray("buffered",bufferedOutputs);
        return tag;
    }

    public void readState(NBTTagCompound tag) {
        outputs = tag.getIntArray("outputs");
        bufferedOutputs = tag.getIntArray("buffered");
    }

    public int getComplexity() {
        return moduleType.complexity;
    }

    public void reset() {
        this.outputs = new int[outputCount()];
    }

}
