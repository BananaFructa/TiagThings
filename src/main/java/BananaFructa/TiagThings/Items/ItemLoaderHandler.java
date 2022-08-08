package BananaFructa.TiagThings.Items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemLoaderHandler {

    public static List<Item> BasicItems = new ArrayList<Item>();

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        /*BasicContainerItem mnsci = new BasicContainerItem("magnetized_nickel_sheet");
        mnsci.setContainerItem(mnsci);*/
        BasicItems.add(new BasicItem("magnetized_nickel_sheet",5));
        BasicItems.add(new BasicItem("bauxite_powder"));
        BasicItems.add(new BasicItem("rutile_powder"));

        BasicItem magnetite = new BasicItem("magnetite_powder");
        BasicItems.add(magnetite);

        BasicItems.add(new BasicItem("rough_iron_powder"));

        BasicItem jute_string = new BasicItem("jute_string");
        BasicItems.add(jute_string);

        BasicItem earthenware_pot_ash = new BasicItem("earthenware_pot_ash");
        BasicItems.add(earthenware_pot_ash);

        BasicItem earthenware_pot_potash = new BasicItem("earthenware_pot_potash");
        BasicItems.add(earthenware_pot_potash);


        BasicItem kaolinite_pot_ash = new BasicItem("kaolinite_pot_ash");
        BasicItems.add(kaolinite_pot_ash);

        BasicItem kaolinite_pot_potash = new BasicItem("kaolinite_pot_potash");
        BasicItems.add(kaolinite_pot_potash);

        BasicItem stoneware_pot_ash = new BasicItem("stoneware_pot_ash");
        BasicItems.add(stoneware_pot_ash);

        BasicItem stoneware_pot_potash = new BasicItem("stoneware_pot_potash");
        BasicItems.add(stoneware_pot_potash);

        BasicItems.add(new BasicItem("alumina_powder"));
        BasicItems.add(new BasicItem("clean_basalt"));
        BasicItems.add(new BasicItem("clean_catlinite"));
        BasicItems.add(new BasicItem("clean_gneiss"));
        BasicItems.add(new BasicItem("clean_granite"));
        BasicItems.add(new BasicItem("clean_sandstone"));
        BasicItems.add(new BasicItem("clean_schist"));

        loadMolds();

        for (Item i : BasicItems) {
            event.getRegistry().register(i);
        }
        OreDictionary.registerOre("dustMagnetite", magnetite);
        OreDictionary.registerOre("dyeBlack", magnetite);
        OreDictionary.registerOre("string",jute_string);
        OreDictionary.registerOre("stringJute",jute_string);

        OreDictionary.registerOre("potAsh",earthenware_pot_ash);
        OreDictionary.registerOre("potAsh",kaolinite_pot_ash);
        OreDictionary.registerOre("potAsh",stoneware_pot_ash);

        OreDictionary.registerOre("potPotash",earthenware_pot_potash);
        OreDictionary.registerOre("potPotash",kaolinite_pot_potash);
        OreDictionary.registerOre("potPotash",stoneware_pot_potash);
    }

    private static void loadMolds() {
        String[] molds = new String[] {
            "axe",
            "chisel",
            "hammer",
            "hoe",
            "javelin",
            "knife",
            "mace",
            "pick",
            "prospector",
            "saw",
            "scythe",
            "shovel",
            "sword"
        };

        for (String s : molds) {
            BasicItems.add(new BasicItem("mold/"+s));
        }
    }

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        for (Item i : BasicItems) {
            ModelLoader.setCustomModelResourceLocation(i,0,new ModelResourceLocation(i.getRegistryName(),"inventory"));
        }
    }
}
