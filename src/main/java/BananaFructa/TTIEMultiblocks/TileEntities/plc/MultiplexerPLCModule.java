package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class MultiplexerPLCModule extends PLCModule{
    public MultiplexerPLCModule(String name) {
        super(name,Modules.MULTIPLEXER);
    }

    @Override
    public int[] process(int[] in) {
        int val = (toB(in[16])) |  (toB(in[17]) << 1) | (toB(in[18]) << 2) | (toB(in[19]) << 3);
        return new int[]{in[val]};
    }

    private int toB(int a) {
        return a  >= 7 ? 1 : 0;
    }

    @Override
    public int inputCount() {
        return 16+4;
    }

    @Override
    public int colorForInput(int in) {
        if (in >= 16) {
            return 0xff57f01f;
        } else return super.colorForInput(in);
    }

    @Override
    public int outputCount() {
        return 1;
    }
}
