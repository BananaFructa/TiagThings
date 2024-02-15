package BananaFructa.TTIEMultiblocks.Compat.jei;

public enum TTJEICategory {

    COAL_BOILER_BURN,
    OIL_BOILER_BURN,
    FLARE_STACK,
    COAL_BOILER,
    CLARIFIER,
    WATER_FILTER,
    OIL_BOILER,
    UM_PL_MACHINE,
    NM_PL_MACHINE,
    EUV_PL_MACHINE,
    GAS_CENTRIFUGE,
    STEAM_RADIATOR,
    INDOOR_AC,
    OUTDOOR_AC,
    ELECTRIC_OVEN,
    TRESHER,
    CCM,
    FBR,
    SHAFT_FURNACE,
    OPEN_HEARTH_FURNACE,
    MAGNETIC_SEPARATOR,
    MEMORY_FORMATTER,
    OPHF_SOLID,
    OPHF_LIQUID,
    OPHF_SIT,
    CLAY_OVEN,
    SMALL_COAL_FUEL,
    SMALL_COAL_BOILER;

    public String getName() {
        return name().toLowerCase();
    }

}
