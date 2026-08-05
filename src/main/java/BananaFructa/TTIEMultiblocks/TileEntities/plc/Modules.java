package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import BananaFructa.TiagThings.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public enum Modules {
    INPUT_A("Input Port A",()->{return new InputPLCModule("Input Port A",Ports.A);},"tiag.plc.input_port.desc",0),
    INPUT_B("Input Port B",()->{return new InputPLCModule("Input Port B",Ports.B);},"tiag.plc.input_port.desc",0),
    INPUT_C("Input Port C",()->{return new InputPLCModule("Input Port C",Ports.C);},"tiag.plc.input_port.desc",0),
    INPUT_D("Input Port D",()->{return new InputPLCModule("Input Port D",Ports.D);},"tiag.plc.input_port.desc",0),
    OUTPUT_A("Output Port A",()->{return new OutputPLCModule("Output Port A",Ports.A);},"tiag.plc.output_port.desc",0),
    OUTPUT_B("Output Port B",()->{return new OutputPLCModule("Output Port B",Ports.B);},"tiag.plc.output_port.desc",0),
    OUTPUT_C("Output Port C",()->{return new OutputPLCModule("Output Port C",Ports.C);},"tiag.plc.output_port.desc",0),
    OUTPUT_D("Output Port D",()->{return new OutputPLCModule("Output Port D",Ports.D);},"tiag.plc.output_port.desc",0),
    SWITCH("Switch",()->{return new SwitchPLCModule("Switch");},"tiag.plc.switch.desc",0),
    CONSTANT("Constant",()->{return new ConstantPLCModule("Constant");},"tiag.plc.constant.desc",0),

    AND("AND Gate",()->{return new ANDPLCModule("AND Gate");},"tiag.plc.and.desc",1),
    OR("OR Gate",()->{return new ORPLCModule("OR Gate");},"tiag.plc.or.desc",1),
    NOT("NOT Gate",()->{return new NOTPLCModule("NOT Gate");},"tiag.plc.not.desc",1),

    NAND("NAND Gate", ()->{return new NANDPLCModule("NAND Gate");},"tiag.plc.nand.desc",1), //
    NOR("NOR Gate", ()->{return new NORPLCModule("NOR Gate");},"tiag.plc.nor.desc",1), //
    XOR("XOR Gate",()->{return new XORPLCModule("XOR Gate");},"tiag.plc.xor.desc",1), //
    XNOR("XNOR Gate",()->{return new XNORPLCModule("XNOR Gate");},"tiag.plc.xnor.desc",1), //
    CLOCK("Clock",()->{return new ClockPLCModule("Clock");},"tiag.plc.clock.desc",2),
    RISING_EDGE_DETECTOR("Rising Edge Detector",()->{return new RisingDetectorPLCModule("Rising Edge Detector");},"tiag.plc.rising_edge.desc",2),
    FALLING_EDGE_DETECTOR("Falling Edge Detector",()->{return new FallingDetectorPLCModule("Falling Edge Detector");},"tiag.plc.falling_edge.desc",2),
    PULSE_EXTENDER("Pulse Extender",()->{return new PulseExtenderPLCModule("Pulse Extender");},"tiag.plc.pulse_extender.desc",2),
    TIMER("Timer",()->{return new TimerPLCModule("Timer");},"tiag.plc.timer.desc",2),

    SR_FLIP_FLOP("SR Flip-Flop", ()->{return new SRPLCModule("SR Flip-Flop");},"tiag.plc.sr.desc",2),
    D_FLIP_FLOP("D Flip-Flop", ()->{return new DPLCModule("D Flip-Flop");},"tiag.plc.d.desc",4),
    JK_FLIP_FLOP("JK Flip-Flop",()->{return new JKPLCModule("JK Flip-Flop");},"tiag.plc.jk.desc",6),

    COUNTER("Binary Counter",()->{return new CounterPLCModule("Binary Counter");},"tiag.plc.binary_counter.desc",10), //
    MULTIPLEXER("Multiplexer",()->{return new MultiplexerPLCModule("Multiplexer");},"tiag.plc.multiplexer.desc",30), //
    DEMULTIPLEXER("Demultiplexer",()->{return new DemultiplexerPLCModule("Demultiplexer");},"tiag.plc.demultiplexer.desc",30), //
    ADDER("Adder",()->{return new AdderPLCModule("Adder");},"tiag.plc.adder.desc",3),
    SUBTRACTOR("Subtractor",()->{return new SubtractorPLCModule("Subtractor");},"tiag.plc.subtractor.desc",3),
    GREATER_THAN("A > B",()->{return new GreaterThanPLCModule("A > B");},"tiag.plc.greater.desc",3),
    LOWER_THAN("A < B", ()->{return new LesserThanPLCModule("A < B");},"tiag.plc.lower.desc",3),
    EQUAL("A = B",()->{return new EqualPLCModule("A = B");},"tiag.plc.equal.desc",3),

    ANALOG_TO_DIGITAL("Analog to Digital",()->{return new AnalogToDigitalPLCModule("Analog to Digital");},"tiag.plc.adc.desc",20),
    DIGITAL_TO_ANALOG("Digital to Analog",()->{return new DigitalToAnalogPLCModule("Digital to Analog");},"tiag.plc.dac.desc",20),
    BUFFER("Buffer",()->{return new BufferPLCModule("Buffer");},"tiag.plc.buffer.desc",1);


    public String name;
    Supplier<PLCModule> supplier;
    public PLCModule instance() {
        return supplier.get();
    }
    public String desc;
    public int complexity;

    Modules(String name,Supplier<PLCModule> sup,String description,int complexity) {
        this.name = name;
        this.supplier = sup;
        this.desc = description;
        this.complexity = complexity;
    }

    public List<String> getDescription(Minecraft mc, int w) {
        String[] s = I18n.translateToLocalFormatted(desc).split("\\\\n");
        List<String> lines = new ArrayList<>();
        for (String l : s) {
            lines.addAll(Utils.wrappStringToWidth(mc,l,w));
        }
        lines.add("");
        lines.add(I18n.translateToLocalFormatted("tiag.complexity_cost") + ": " + complexity);
        return lines;
    }
}
