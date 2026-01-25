package BananaFructa.TTIEMultiblocks.Gui;

public enum LatheAction {
    TURN("Turn"),
    GROOVE("Groove"),
    DRILL("Drill"),
    THREAD("Thread");

    public final String name;

    LatheAction(String name) {
        this.name = name;
    }
}
