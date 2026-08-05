package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class LesserThanPLCModule extends PLCModule{
    public LesserThanPLCModule(String name) {
        super(name,Modules.LOWER_THAN);
    }

    @Override
    public int[] process(int[] in) {
        return new int[]{in[0] < in[1] ? 15 : 0};
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
