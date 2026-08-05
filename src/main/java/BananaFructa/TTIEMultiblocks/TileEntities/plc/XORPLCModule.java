package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class XORPLCModule extends PLCModule{
    public XORPLCModule(String name) {
        super(name,Modules.XOR);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{(in[0] >= 7 != in[1] >= 7) ? 15 : 0};
    }

    @Override
    public int inputCount() {
        return 2;
    }

    @Override
    public int outputCount() {
        return 1;
    }
}
