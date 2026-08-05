package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class AdderPLCModule extends PLCModule{
    public AdderPLCModule(String name) {
        super(name,Modules.ADDER);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{Math.max(Math.min(15,in[0] + in[1]),0)};
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
