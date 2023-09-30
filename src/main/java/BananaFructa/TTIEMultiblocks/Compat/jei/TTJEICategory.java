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
    TRESHER;

    public String getName() {
        return name().toLowerCase();
    }

}
