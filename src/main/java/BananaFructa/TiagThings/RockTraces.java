package BananaFructa.TiagThings;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum RockTraces {
    NONE("None","","None"),
    COPPER("Copper","\u00a74","native_copper","surface_native_copper","surface_tetrahedrite","tetrahedrite","malachite"),
    GOLD("Gold","\u00a76","native_gold"),
    SILVER("Silver","\u00a77","native_silver"),
    PLATINUM("Platinum","\u00a77","native_platinum"),
    TIN("Tin","\u00a77","cassiterite","surface_cassiterite"),
    IRON("Iron Oxide","\u00a77","hematite","magnetite","limonite"),
    URANIUM("Pitchblende","\u00a72","pitchblende"),
    NICKEL("Nickel","\u00a7a","garnerite"),
    ALUMINIUM("Bauxite","\u00a7b","bauxite");

    String[] veinNames;
    String name;
    String color;


    RockTraces(String name, String color,String... veinName) {
        this.name = name;
        this.color = color;
        this.veinNames = veinName;
    }

    public String getName() {
        return color + name + "\u00a7r";
    }

    public ItemStack dustFor(int q) {
        ItemStack rock = Utils.itemStackFromCTId("<tiagthings:crushed_rock>",q);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("trace",ordinal());
        rock.setTagCompound(tag);
        return rock;
    }

}
