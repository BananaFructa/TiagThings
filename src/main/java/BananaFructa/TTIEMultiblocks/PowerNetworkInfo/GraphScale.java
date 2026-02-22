package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

public enum GraphScale {
    FIVE_SECONDS("5s"),
    ONE_MINUTE("1m"),
    TEN_MINUTES("10m"),
    ONE_HOUR("1h"),
    TEN_HOURS("10h"),
    FIFTY_HOURS("50h"),
    TWO_HUNDRED_FIFTY_HOURS("250h"),
    ONE_THOUSAND_HOURS("1000h");

    String name;

    GraphScale(String name) {
        this.name = name;
    }
}
