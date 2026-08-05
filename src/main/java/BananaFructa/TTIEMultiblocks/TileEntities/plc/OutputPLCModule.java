package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityPLC;
import net.minecraft.nbt.NBTTagCompound;

public class OutputPLCModule extends PLCModule{

    public int port;
    public int channel;

    public OutputPLCModule(String name, Ports port) {
        super(name,Modules.values()[Modules.OUTPUT_A.ordinal()+port.ordinal()]);
        this.port = port.ordinal();
    }

    public void update(TileEntityPLC container) {
        bufferedOutputs = new int[0];
        int[] in = collectInputs(container);
        container.setStrength(port,channel,in[0]);
    }

    @Override
    public int[] process(int[] in) {
        return null;
    }

    @Override
    public int inputCount() {
        return 1;
    }

    @Override
    public int outputCount() {
        return 0;
    }

    @Override
    public String[] getDisplay() {
        return new String[]{super.getDisplay()[0],"Channel: " + ChannelColors.values()[channel].format.toString() + ChannelColors.values()[channel].name};
    }

    @Override
    public NBTTagCompound toNBT(boolean includeState) {
        NBTTagCompound tag = super.toNBT(includeState);
        tag.setInteger("port",port);
        tag.setInteger("channel",channel);
        return tag;
    }

    @Override
    public void read(NBTTagCompound tag) {
        super.read(tag);
        port = tag.getInteger("port");
        channel = tag.getInteger("channel");
    }
}
