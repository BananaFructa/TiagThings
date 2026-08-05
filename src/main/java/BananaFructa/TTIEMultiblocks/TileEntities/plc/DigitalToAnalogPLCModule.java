package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class DigitalToAnalogPLCModule extends PLCModule{
    public DigitalToAnalogPLCModule(String name) {
        super(name,Modules.DIGITAL_TO_ANALOG);
    }

    @Override
    public int[] process(int[] in) {
        int val = (toB(in[0])) |  (toB(in[1]) << 1) | (toB(in[2]) << 2) | (toB(in[3]) << 3);
        return new int[]{val};
    }

    private int toB(int a) {
        return a  >= 7 ? 1 : 0;
    }

    @Override
    public int inputCount() {
        return 4;
    }

    @Override
    public int outputCount() {
        return 1;
    }
}
