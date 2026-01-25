package BananaFructa.TTIEMultiblocks.Gui;

public enum LatheActionFlag {
    LAST("Last"),
    SECOND("Second Last"),
    FIRST("Third Last"),
    NOT_LAST("Not Last"),
    ANY("Any");

    public final String name;

    LatheActionFlag(String name) {
        this.name = name;
    }
}
