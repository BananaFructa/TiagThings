package BananaFructa.TTIEMultiblocks.TileEntities.plc;

public enum ColorScheme {

    SIGNAL_IN(0xff0066af),
    SIGNOAL_OUT(0xff0066af),
    BINARY_SELECT(0xff57f01f),
    CLOCK_IN(0xff8800ff);

    int color;

    public int get() {
        return color;
    }

    ColorScheme(int color) {
        this.color = color;
    }

}
