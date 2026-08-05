package BananaFructa.TTIEMultiblocks.TileEntities.plc;

import net.minecraft.util.text.TextFormatting;

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHH
public enum ChannelColors {
    WHITE("White", TextFormatting.WHITE),
    ORANGE("Orange", TextFormatting.GOLD),
    MAGENTA("Magenta", TextFormatting.AQUA),
    LIGHT_BLUE("Light Blue", TextFormatting.BLUE),
    YELLOW("Yellow", TextFormatting.YELLOW),
    LIME("Lime", TextFormatting.GREEN),
    PINK("Pink",TextFormatting.LIGHT_PURPLE),
    GRAY("Gray", TextFormatting.DARK_GRAY),
    SILVER("Silver", TextFormatting.GRAY),
    CYAN("Cyan", TextFormatting.DARK_AQUA),
    PURPLE("Purple", TextFormatting.DARK_PURPLE),
    BLUE("Blue", TextFormatting.DARK_BLUE),
    BROWN("Brown", TextFormatting.GOLD),
    GREEN("Green", TextFormatting.DARK_GREEN),
    RED("Red", TextFormatting.DARK_RED),
    BLACK("Black", TextFormatting.BLACK);

    public String name;
    public TextFormatting format;

    ChannelColors(String name, TextFormatting format) {
        this.name = name;
        this.format = format;
    }
}
