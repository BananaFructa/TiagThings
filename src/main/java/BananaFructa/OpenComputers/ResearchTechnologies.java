package BananaFructa.OpenComputers;

import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Function;
import java.util.function.Supplier;

public enum ResearchTechnologies {

    ADVANCED_ELECTROMECHANICS(10,"tiag.research.advanced_electromechanics.name","tiag.research.desc.advanced_electronics",() -> ItemLoaderHandler.rpAdvMech,20),
    VLSI_DIGITAL_LOGIC(10,"tiag.research.vlsi_logic.name",null,() -> ItemLoaderHandler.rpVlsi,211),
    NETWORKING(10,"tiag.research.networking.name","tiag.research.desc.networking",() -> ItemLoaderHandler.rpNetworking,28),
    METEOROLOGY(10,"tiag.research.meteorology.name",null,() -> ItemLoaderHandler.rpMeteorology,27),
    HYDROPONICS(10,"tiag.research.hydroponics.name",null,()->ItemLoaderHandler.rpHydroponics,26),
    DYNAMIC_MEMORY(10,"tiag.research.dynamic_memory.name",null,()->ItemLoaderHandler.rpDynMem,24,VLSI_DIGITAL_LOGIC),
    EUV_LITHOGRAPHY(10,"tiag.research.euv_lithography.name",null,()->ItemLoaderHandler.rpEubLith,25,VLSI_DIGITAL_LOGIC),
    DIGITAL_RF_MODULATION(10,"tiag.research.digital_rf.name",null,()->ItemLoaderHandler.rpDigitalRf,22,VLSI_DIGITAL_LOGIC,NETWORKING),
    DISTRIBUTED_COMPUTING(10,"tiag.research.distributed_computing.name",null,()->ItemLoaderHandler.rpDistComp,23,NETWORKING),
    ADVANCED_SOLAR_PANELS(10,"tiag.research.advanced_panels.name",null,()->ItemLoaderHandler.rpAdvSolar,21,METEOROLOGY),
    NM4_NODE_SYSTEMS(10,"tiag.research.nm4.name",null,()->ItemLoaderHandler.rpNm4,29,EUV_LITHOGRAPHY),
    OFDM_TRANSCEIVERS(10,"tiag.research.ofdm_txrx.name",null,()->ItemLoaderHandler.rpOfdm,210,DIGITAL_RF_MODULATION);

    public long cost;
    public String desc;
    public String name;
    public ResearchTechnologies[] requirements;
    public Supplier<Item> rpProvider;
    public int stageCode;

    ResearchTechnologies(long cost, String name, String desc, Supplier<Item> rpProvider,int stageCode, ResearchTechnologies... requirements) {
        this.cost = cost;
        this.desc = desc;
        this.name = name;
        this.requirements = requirements;
        this.rpProvider = rpProvider;
        this.stageCode = stageCode;
    }

}
