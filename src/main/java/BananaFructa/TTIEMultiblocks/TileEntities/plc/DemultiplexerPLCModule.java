package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class DemultiplexerPLCModule extends PLCModule{
    public DemultiplexerPLCModule(String name) {
        super(name,Modules.DEMULTIPLEXER);
    }

    @Override
    public int[] process(int[] in) {
        int val = (toB(in[1])) |  (toB(in[2]) << 1) | (toB(in[3]) << 2) | (toB(in[4]) << 3);
        int[] out = new int[16];
        out[val] = in[0];
        return out;
    }

    private int toB(int a) {
        return a  >= 7 ? 1 : 0;
    }

    @Override
    public int inputCount() {
        return 1+4;
    }

    @Override
    public int colorForInput(int in) {
        if (in >= 1) {
            return 0xff57f01f;
        } else return super.colorForInput(in);
    }

    @Override
    public int outputCount() {
        return 16;
    }
}
