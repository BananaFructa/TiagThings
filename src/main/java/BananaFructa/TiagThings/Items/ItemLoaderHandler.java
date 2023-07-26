package BananaFructa.TiagThings.Items;

import BananaFructa.Galacticraft.ItemTritiumCanister;
import BananaFructa.TiagThings.TTMain;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemLoaderHandler {

    public static List<Item> BasicItems = new ArrayList<Item>();
    public static ItemTritiumCanister tritiumCanister;

    public static void preInit() {
       tritiumCanister = new ItemTritiumCanister("tritium_canister_partial");
       GalacticraftCore.proxy.registerCanister(new PartialCanister(tritiumCanister, TTMain.modId,"tritium_canister_partial",7));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
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
        BasicItems.add(new BasicItem("cooled_lava_amalgam"));
        BasicItems.add(new BasicItem("titania_powder"));
        BasicItems.add(new BasicItem("industrial_diamond"));
        BasicItems.add(new BasicItem("industrial_diamond_grit"));
        BasicItems.add(new BasicItem("industrial_diamond_mix"));
        BasicItems.add(new BasicItem("plastic"));
        BasicItems.add(new BasicItem("dust_plastic"));
        BasicItems.add(new BasicItem("fiberglass"));
        BasicItems.add(new BasicItem("vacuum_tube_body"));
        BasicItems.add(new BasicItem("ptype"));
        BasicItems.add(new BasicItem("ntype"));
        BasicItems.add(new BasicItem("arsenic_powder"));
        BasicItems.add(new BasicItem("fluorite_powder"));
        BasicItems.add(new BasicItem("wolframite_powder"));
        BasicItems.add(new BasicItem("zeolite"));
        BasicItems.add(new BasicItem("zeolite_powder"));
        BasicItems.add(new BasicItem("zeolite_catalyst"));
        BasicItems.add(new BasicItem("dust_dry_plastic"));

        BasicItems.add(new BasicItem("controller_circuit_board"));
        BasicItems.add(new BasicItem("controller_circuit_board_etched"));
        BasicItems.add(new BasicItem("controller_circuit_board_raw"));
        BasicItems.add(new BasicItem("microcontroller"));

        BasicItems.add(new BasicItem("beryl_powder"));
        BasicItems.add(new BasicItem("beryllium_hydroxide_powder"));
        BasicItems.add(new BasicItem("ammonium_sulfate_powder"));
        BasicItems.add(new BasicItem("diammonium_phosphate_powder"));

        // mudstone , chert , claystone , shale

        BasicItems.add(new BasicItem("clean_mudstone"));
        BasicItems.add(new BasicItem("clean_chert"));
        BasicItems.add(new BasicItem("clean_claystone"));
        BasicItems.add(new BasicItem("clean_shale"));
        BasicItems.add(new BasicItem("pitchblende_powder"));
        BasicItems.add(new BasicItem("carbon_filament_bulb"));
        BasicItems.add(new BasicItem("carbon_filament"));
        BasicItems.add(new BasicItem("lithium_6_canister"));
        BasicItems.add(new BasicItem("lithium_7_tritium_canister"));
        BasicItems.add(new BasicItem("processing_module"));

        BasicItems.add(new BasicItem("pyrolusite_powder"));
        BasicItems.add(new BasicItem("magnesite_powder"));
        BasicItems.add(new BasicItem("unf_controller_circuit_board"));
        BasicItems.add(new BasicItem("launch_software_memory_card"));

        BasicItems.add(new BasicItem("starch"));
        BasicItems.add(new BasicItem("wet_bioplastic_resin_sheet"));
        BasicItems.add(new BasicItem("bioplastic_sheet"));
        BasicItems.add(new BasicItem("bioplastic"));
        BasicItems.add(new BasicItem("dust_bioplastic"));

        BasicItems.add(new BasicItem("clean_peridotite"));
        BasicItems.add(new BasicItem("clean_porphyry"));
        BasicItems.add(new BasicItem("chalcopyrite_powder"));
        BasicItems.add(new BasicItem("pentlandite_powder"));

        loadMolds();

        for (Item i : BasicItems) {
            event.getRegistry().register(i);
        }

        event.getRegistry().register(tritiumCanister.setRegistryName(tritiumCanister.getUnlocalizedName().substring(5)));

        GCItems.canisterTypes.add(tritiumCanister);

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
            "sword",
            "mallet"
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
