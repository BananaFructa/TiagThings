package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class NOTPLCModule extends PLCModule{
    public NOTPLCModule(String name) {
        super(name,Modules.NOT);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{in[0] >= 7 ? 0 : 15};
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
