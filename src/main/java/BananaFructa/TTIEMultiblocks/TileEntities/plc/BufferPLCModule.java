package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class BufferPLCModule extends PLCModule{
    public BufferPLCModule(String name) {
        super(name,Modules.BUFFER);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{in[0]};
    }

    @Override
    public int inputCount() {
        return 1;
    }

    @Override
    public int outputCount() {
        return 1;
    }
}
