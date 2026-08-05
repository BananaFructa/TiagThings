package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class ANDPLCModule extends PLCModule{
    public ANDPLCModule(String name) {
        super(name,Modules.AND);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{in[0] >= 7 && in[1] >= 7 ? 15 : 0};
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
