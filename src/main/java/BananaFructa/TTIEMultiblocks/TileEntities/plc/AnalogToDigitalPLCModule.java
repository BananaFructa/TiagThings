package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public class AnalogToDigitalPLCModule extends PLCModule{
    public AnalogToDigitalPLCModule(String name) {
        super(name,Modules.ANALOG_TO_DIGITAL);
    }

    @Override
    public int[] process(int[] in) {
        int val = in[0];
        return new int[]{toS(val & 0b0001),toS(val & 0b0010),toS(val & 0b0100),toS(val & 0b1000)};
    }

    private int toS(int a) {
        return a  != 0 ? 15 : 0;
    }

    private int toB(int a) {
        return a  >= 7 ? 1 : 0;
    }

    @Override
    public int inputCount() {
        return 1;
    }

    @Override
    public int outputCount() {
        return 4;
    }
}
