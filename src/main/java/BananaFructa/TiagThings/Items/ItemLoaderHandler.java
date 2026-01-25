package BananaFructa.TiagThings.Items;

import BananaFructa.Galacticraft.ItemTritiumCanister;
import BananaFructa.TiagThings.TTMain;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
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
    public static BasicItem siliconWafer;
    public static BasicItem printedSiliconWafer1;
    public static BasicItem printedSiliconWafer2;
    public static BasicItem printedSiliconWafer3;
    public static BasicItem icChip1;
    public static BasicItem icChip2;
    public static BasicItem icChip3;
    public static BasicItem rockTest;

    public static void preInit() {
       tritiumCanister = new ItemTritiumCanister("tritium_canister_partial");
       GalacticraftCore.proxy.registerCanister(new PartialCanister(tritiumCanister, TTMain.modId,"tritium_canister_partial",7));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onItemRegister(RegistryEvent.Register<Item> event) {

        if(FluidRegistry.isUniversalBucketEnabled())
        {
            ForgeModContainer.getInstance().universalBucket = new TTUniversalBucket();
            ForgeModContainer.getInstance().universalBucket.setUnlocalizedName("forge.bucketFilled");
            event.getRegistry().register(ForgeModContainer.getInstance().universalBucket.setRegistryName(ForgeVersion.MOD_ID, "bucketFilled"));
            MinecraftForge.EVENT_BUS.register(ForgeModContainer.getInstance().universalBucket);
        }

        Item item = (new ItemBucket(Blocks.AIR)).setUnlocalizedName("bucket").setRegistryName("minecraft","bucket").setMaxStackSize(16);
        event.getRegistry().register(item);
        event.getRegistry().register((new TTItemBucket(Blocks.FLOWING_WATER)).setRegistryName("minecraft","water_bucket").setUnlocalizedName("bucketWater").setContainerItem(item));
        event.getRegistry().register((new TTItemBucket(Blocks.FLOWING_LAVA)).setRegistryName("minecraft","lava_bucket").setUnlocalizedName("bucketLava").setContainerItem(item));

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
        BasicItems.add(new BasicItem("pboule"));
        BasicItems.add(new BasicItem("bjtboule"));
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
        BasicItems.add(siliconWafer = new BasicItem("raw_silicon_wafer"));
        BasicItems.add(printedSiliconWafer1 = new BasicItem("printed_silicon_wafer_1"));
        BasicItems.add(printedSiliconWafer2 = new BasicItem("printed_silicon_wafer_2"));
        BasicItems.add(printedSiliconWafer3 = new BasicItem("printed_silicon_wafer_3"));
        BasicItems.add(icChip1 = new BasicItem("ic_chip_1"));
        BasicItems.add(icChip2 = new BasicItem("ic_chip_2"));
        BasicItems.add(icChip3 = new BasicItem("ic_chip_3"));
        BasicItems.add(new BasicItem("advanced_processor"));

        BasicItems.add(new BasicItem("mild_steel_rod"));

        BasicItems.add(new BasicItem("cryolite_powder"));

        BasicItems.add(new BasicItem("fesi"));

        BasicItems.add(new BasicItem("direct_reduced_iron"));

        BasicItems.add(new BasicItem("small_coke_pile"));
        BasicItems.add(new BasicItem("zircon_powder"));
        BasicItems.add(new BasicItem("zirconia_powder"));
        BasicItems.add(new BasicItem("coke_powder"));
        BasicItems.add(new BasicItem("material_component_iron_1"));
        BasicItems.add(new BasicItem("material_component_iron_2"));
        BasicItems.add(new BasicItem("material_component_iron_3"));
        BasicItems.add(new BasicItem("material_component_iron_4"));
        BasicItems.add(new BasicItem("material_component_steel_1"));
        BasicItems.add(new BasicItem("material_component_steel_2"));
        BasicItems.add(new BasicItem("material_component_steel_3"));
        BasicItems.add(new BasicItem("material_component_steel_4"));
        BasicItems.add(new BasicItem("material_component_tungsten_1"));
        BasicItems.add(new BasicItem("material_component_tungsten_2"));
        BasicItems.add(new BasicItem("material_component_tungsten_3"));
        BasicItems.add(new BasicItem("material_component_tungsten_4"));
        BasicItems.add(new BasicItem("relay"));
        BasicItems.add(new BasicItem("early_relay_circuit"));
        BasicItems.add(new BasicItem("basic_memory_chip"));
        BasicItems.add(new BasicItem("advanced_memory_chip"));
        BasicItems.add(new BasicItem("very_advanced_memory_chip"));
        BasicItems.add(new BasicItem("bjt_strip"));
        BasicItems.add(new BasicItem("mg_silicon"));
        BasicItems.add(new BasicItem("polysilicon"));
        BasicItems.add(new BasicItem("bjt_mix"));
        BasicItems.add(new BasicItem("crucible_steel_rod"));
        // ATFC

        BasicItems.add(new BasicItem("sisal_woven_pirn"));
        BasicItems.add(new BasicItem("cotton_woven_pirn"));
        BasicItems.add(new BasicItem("linen_woven_pirn"));
        BasicItems.add(new BasicItem("hemp_woven_pirn"));
        BasicItems.add(new BasicItem("yucca_woven_pirn"));
        BasicItems.add(new BasicItem("pineapple_woven_pirn"));
        BasicItems.add(new BasicItem("jet_powder"));
        BasicItems.add(new BasicItem("lignite_powder"));
        BasicItems.add(new BasicItem("bituminous_coal_powder"));
        BasicItems.add(new EmptyItemTool("steel_lathe_tool",1,200));
        BasicItems.add(new EmptyItemTool("tungsten_steel_lathe_tool",1,400));

        BasicItems.add(new BasicItem("s_magnetized"));
        BasicItems.add(new BasicItem("v_magnetized"));
        BasicItems.add(new BasicItem("petcoke"));
        BasicItems.add(new BasicItem("petcoke_powder"));
        BasicItems.add(new BasicItem("small_charcoal_pile"));
        BasicItems.add(new BasicItem("small_petcoke_pile"));
        BasicItems.add(new BasicItem("raw_electrode"));
        BasicItems.add(new BasicItem("crushed_rock"));
        BasicItems.add(new BasicItem("amon_hexcl"));
        BasicItems.add(new BasicItem("nick_hydr"));
        rockTest = new BasicItem("rock_test");
        rockTest.setMaxStackSize(1);
        BasicItems.add(rockTest);

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
