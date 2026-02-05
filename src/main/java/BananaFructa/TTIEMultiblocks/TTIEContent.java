package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensor;
import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.ControlBlocks.VirtualConnector;
import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorHV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorLV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorMV;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.RocketModule_SMC;
import BananaFructa.TTIEMultiblocks.Utils.RotaryRequirement;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import blusunrize.lib.manual.ManualPages;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import flaxbeard.immersivepetroleum.client.page.ManualPageBigMultiblock;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.*;

import java.util.*;

@Mod.EventBusSubscriber
public class TTIEContent {

    public static Map<Class<? extends TileEntity>, RotaryRequirement> rotaryConvertedMultiblocks = new HashMap<Class<? extends TileEntity>, RotaryRequirement>() {{
        put(TileEntityMetalPress.class,new RotaryRequirement(10,30,60));
        put(TileEntityPumpjack.class,new RotaryRequirement(50,60,80));
        put(TileEntityCrusher.class,new RotaryRequirement(20,30,60));
        put(TileEntityPowerLoom.class,new RotaryRequirement(10,150,200));
    }};

    public static BlockTTBase<TTBlockTypes_MetalMultiblock> ttBlockMetalMultiblock;
    public static BlockTTBase<TTBlockTypes_MetalMultiblock_1> ttBlockMetalMultiblock_1;
    public static BlockTTBase<TTBlockTypes_MetalMultiblock_2> ttBlockMetalMultiblock_2;
    public static BlockTTBase<TTBlockTypes_MetalMultiblock_3> ttBlockMetalMultiblock_3;
    public static BlockTTBase<TTBlockTypes_MetalMultiblock_4> ttBlockMetalMultiblock_4;

    public static List<Block> registeredTTBlocks = new ArrayList<>();
    public static List<Item> registeredTTItems = new ArrayList<>();

    static {
        advancedComputerBlock = new Block(Material.IRON).setRegistryName("advanced_computer_block").setUnlocalizedName("advanced_computer_block").setCreativeTab(CreativeTabs.MISC);
        rocketControllerBlock = new Block(Material.IRON).setRegistryName("rocket_controller_block").setUnlocalizedName("rocket_controller_block").setCreativeTab(CreativeTabs.MISC);
        magnetizedNickelSheetMetal = new Block(Material.IRON).setRegistryName("magnetized_nickel_sm").setUnlocalizedName("magnetized_nickel_sm").setCreativeTab(CreativeTabs.MISC);
        electricMotorBlock = (ElectricMotorBlock) new ElectricMotorBlock().setRegistryName("electric_motor_tt").setUnlocalizedName("electric_motor_tt").setCreativeTab(CreativeTabs.MISC);
        signalSourceBlock = (SignalSourceBlock) new SignalSourceBlock().setRegistryName("signal_source_tt").setUnlocalizedName("signal_source_tt").setCreativeTab(CreativeTabs.MISC);
        magnetizedSteelSheetmetal = new Block(Material.IRON).setRegistryName("magnetized_steel_sm").setUnlocalizedName("magnetized_steel_sm").setCreativeTab(CreativeTabs.MISC);
        loadSensor = new LoadSensor().setRegistryName("load_sensor").setUnlocalizedName("load_sensor").setCreativeTab(CreativeTabs.MISC);

        ttBlockMetalMultiblock = new TTBlockMetalMultiblocks();
        ttBlockMetalMultiblock_1 = new TTBlockMetalMultiblocks_1();
        ttBlockMetalMultiblock_2 = new TTBlockMetalMultiblocks_2();
        ttBlockMetalMultiblock_3 = new TTBlockMetalMultiblocks_3();
        ttBlockMetalMultiblock_4 = new TTBlockMetalMultiblocks_4();
        OBJLoader.INSTANCE.addDomain(TTMain.modId);
    }

    public static SimplifiedMultiblockClass electricHeaterMultiblock;
    public static SimplifiedMultiblockClass flareStackMultiblock;
    public static SimplifiedMultiblockClass coalBoilerMultiblock;
    public static SimplifiedMultiblockClass clarifierMultiblock;
    public static SimplifiedMultiblockClass waterFilter;
    public static SimplifiedMultiblockClass oilBoilerMultiblock;
    public static SimplifiedMultiblockClass umPhotolithographyMachine;
    public static SimplifiedMultiblockClass nmPhotolitographyMachine;
    public static SimplifiedMultiblockClass euvPhotolitographyMachine;
    public static SimplifiedMultiblockClass gasCentrifugeMultiblock;
    public static SimplifiedMultiblockClass steamRadiatorMultiblock;
    public static SimplifiedMultiblockClass indoorAcUnitMultiblock;
    public static SimplifiedMultiblockClass outdoorAcUnitMultiblock;
    public static SimplifiedMultiblockClass tresherMultiblock;
    public static SimplifiedMultiblockClass electricOvenMultiblock;
    public static SimplifiedMultiblockClass clayOven;
    public static SimplifiedMultiblockClass masonryHeater;
    public static SimplifiedMultiblockClass rocketModuleScaffold;
    public static SimplifiedMultiblockClass rktModule1,rktModule2,rktModule3;
    public static SimplifiedMultiblockClass magneticSeparator;
    public static SimplifiedMultiblockClass openHearthFurnace;
    public static SimplifiedMultiblockClass shaftFurnace;
    public static SimplifiedMultiblockClass fbr;
    public static SimplifiedMultiblockClass ccm;
    public static SimplifiedMultiblockClass smallCoalBoiler;

    public static SimplifiedMultiblockClass rocketScaffoldBase;
    public static SimplifiedMultiblockClass rocketScaffoldBody;
    public static SimplifiedMultiblockClass rocket1Slice,rocket2Slice,rocket3Slice;
    public static SimplifiedMultiblockClass steamEngine;
    public static SimplifiedMultiblockClass lathe;
    public static SimplifiedMultiblockClass metalRoller;
    public static SimplifiedMultiblockClass siliconCrucible;
    public static SimplifiedMultiblockClass cokeOvenBattery;
    public static SimplifiedMultiblockClass electricFoodOven;
    public static SimplifiedMultiblockClass magnetizer;
    public static SimplifiedMultiblockClass cokerUnit;

    public static Block advancedComputerBlock;
    public static Block magnetizedNickelSheetMetal;
    public static ElectricMotorBlock electricMotorBlock;
    public static SignalSourceBlock signalSourceBlock;
    public static Item magnetizedNickelSheetMetalItem;
    public static Item electricMotorBlockItem;
    public static Item signalSourceBlockItem;
    public static Item advancedComputerBlockItem;
    public static Block rocketControllerBlock;
    public static Item rocketControllerBlockItem;

    public static Block magnetizedSteelSheetmetal;
    public static Item magnetizedSteelSheetmetalItem;

    public static Block loadSensor;
    public static Item loadSensorItem;

    @SideOnly(Side.CLIENT)
    public static void clientInit() {

        ManualHelper.addEntry("Electric Heater","temperatureControl",
                new ManualPageMultiblock(ManualHelper.getManual(),"The electric heater uses 2000 RF/t in order to raise the temperature around it by 21 \u00b0C / 69.8 F. It is worth to note that it becomes less efficient when multiple heaters are needed compared to the Steam Radiator.",electricHeaterMultiblock)
                );
        ManualHelper.addEntry("Steam Radiator","temperatureControl",
                new ManualPageMultiblock(ManualHelper.getManual(),"Compared to the Electric Heater, Steam Radiators could prove more efficient when dealing with centralised heating systems. This of course in cases where steam/oil is used to generate power.",steamRadiatorMultiblock));
        ManualHelper.addEntry("Air Conditioning","temperatureControl",
                new ManualPageMultiblock(ManualHelper.getManual(),"The indoor AC unit takes in cold fluoromethane and gives out hotfluoromethane generating a cooling effect of -10\u00b0C /-18 F.",indoorAcUnitMultiblock),
                new ManualPageMultiblock(ManualHelper.getManual(),"The outdoor AC unit Takes in (hot) fluoromethane and gives out cold fluoromethane. Uses 2000 RF/t.",outdoorAcUnitMultiblock));
        /*ManualHelper.addEntry("Thresher","utility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to separate grains from their chaff and straw.",tresherMultiblock)
                );*/
        ManualHelper.addEntry("Heat Treat Oven","utility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to perform heat treatments on various materials.",electricOvenMultiblock));
        ManualHelper.addEntry("Flare Stack","ip",
                new ManualPageBigMultiblock(ManualHelper.getManual(),flareStackMultiblock),
                new ManualPages.Text(ManualHelper.getManual(),"Used to dispose Butane.")
        );
        ManualHelper.addEntry("Coal Boiler","industrialBoilers",
                new ManualPageBigMultiblock(ManualHelper.getManual(),coalBoilerMultiblock),
                new ManualPages.Text(ManualHelper.getManual(),"Uses coal to generate steam. In order to operate 4096 RF/t must be fed through the to HV port. In order to boil Treated Water the internal temperature must reach 600 \u00b0C. Paired with a Steam Turbine it can generate 8192 RF/t.")
        );
        ManualHelper.addEntry("Oil Boiler", "industrialBoilers",
                new ManualPageBigMultiblock(ManualHelper.getManual(),oilBoilerMultiblock),
                new ManualPages.Text(ManualHelper.getManual(),"Uses diesel to generate steam. In order to operate 4096 RF/t must be fed through the to HV port. In order to boil Treated Water the internal temperature must reach 600 \u00b0C. Paired with a Steam Turbine it can generate 8192 RF/t.")
        );
        ManualHelper.addEntry("Clarifier","waterTreatment",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to clarify water as an initial step in the water treatment process. In order for it to execute recipes it must have more than 149990mb of the input fluid stored in it's internal tank.",clarifierMultiblock)
        );
        ManualHelper.addEntry("Water Filter", "waterTreatment",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to filter water as the second step in the water treatment process. Does not require power to operate.",waterFilter));
        ManualHelper.addEntry("um Lithography Machine","plms",
                new ManualPageMultiblock(ManualHelper.getManual(),"The most basic of lithography machines can only be used to create silicon wafers with the micro meter process.",umPhotolithographyMachine));
        ManualHelper.addEntry("nm Lithography Machine","plms",
                new ManualPageMultiblock(ManualHelper.getManual(),"Second tier of lithography machine can be used to create silicon wafers in either micro meter or nano meter processes. To change the process used right click on the machine while holding the Engineer's Hammer in the main hand.",nmPhotolitographyMachine));
        ManualHelper.addEntry("EUV Lithography Machine","plms",
                new ManualPageMultiblock(ManualHelper.getManual(),"Third tier of lithography machine. Can be used to create silicon wafers in either EUV, nm or um processes. To change the process used right click on the machine while holding the Engineer's Hammer in the main hand.",euvPhotolitographyMachine));
        ManualHelper.addEntry("Gas Centrifuge","uraniumProcessing",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to gradually concentrate Uranium Hexafluoride. Several stages are required to achieve a purity of either 99.9% of U-238 or 99.9% U-235 from an input gas of 99.3% U-238 and 0.7% U-235.",gasCentrifugeMultiblock));
        ManualHelper.addEntry("Clay Oven","primitiveDevices",
                new ManualPageMultiblock(ManualHelper.getManual(),"In order to form the Clay Oven a Fire Pit must be placed in the back of the front cavity and afterwards a player must shift-right click on the side pointing outwards of the Fire Pit (not the top). In order to cook inside the Clay Oven, the Fire Pit must be lit and the",clayOven),
                new ManualPages.Text(ManualHelper.getManual(),"items that need to be cooked be placed on the ground, vertically (default keybind is \"V\"). After the specified time (can be seen in JEI or in WAILA while looking at the oven) the placed items will turn into their cooked products."));
        ManualHelper.addEntry("Masonry Heater","primitiveDevices",
                new ManualPageMultiblock(ManualHelper.getManual(),"In order to form the Masonry Heater a Fire Pit must be placed in the back of the front cavity and afterwards a player must shift-right click on the side pointing outwards of the",masonryHeater),
                new ManualPages.Text(ManualHelper.getManual(),"Masonry Heater (not the top). While the Fire Pit inside is burning it gives of around 4 times more heat than a normal fire pit."));
        ManualHelper.addEntry("Fluidized Bed Reactor","chemicalProcessing",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Fluidized Bed Reactor is used to conduct reactions between a solid and a liquid. It does not require power to run. To form the device right click with the Engineer's Hammer on the bottom middle steel sheet metal block which has on its left side 2 mechanical engineering blocks.",fbr));
        ManualHelper.addEntry("Continuous Casting Machine","metallurgy",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Continous Casting Machine is used to turn molten metal into solid ingots automatically. It is formed by right clicking with the Engineer's Hammer on the front heavy mechanical engineering block.",ccm));
        ManualHelper.addEntry("Shaft Furnace","metallurgy",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Shaft Furnace is used to conduct metallurgic reduction processes. The item input hatch is",shaftFurnace),
                new ManualPages.Text(ManualHelper.getManual(),"located at the top of the structure and the output hatch at the bottom of the structure. The liquid input hatch is located on one of the side. The structure is formed by right clicking on one side of the bottom most mechanical engineering block."));
        ManualHelper.addEntry("Magnetic Separator", "metallurgy",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Magnetic Separator is used to purify iron ore powder into iron ore concentrate that can be more effectively worked with. It is formed by rick clicking with the Engineer's Hammer on bottom left steel sheet metal block, below the magnetized nickel sheet metal block and on",magneticSeparator),
                new ManualPages.Text(ManualHelper.getManual(),"the same side as the steel scaffolding slab."));
        ManualHelper.addEntry("Rocket Scaffold", "spaceTechnology",
            new ManualPages.Text(ManualHelper.getManual(),"The first step in space travel is the construction of the launch rocket. The rockets are by building the correct block structure inside the Rocket Assembly Scaffold. The Rocket Assembly scaffold is a tall multiblock device built out of steel scaffolding. Look at the next page to see how to build it."),
                new ManualPageMultiblock(ManualHelper.getManual(),"Since the multiblock itself is quite large it does not fit on a single page (the multiblock projector can also be used). Above there is the rocket scaffold base which is the first section of the structure,",rocketScaffoldBase),
                new ManualPages.Text(ManualHelper.getManual()," everything else going above it. At the end in order to form the whole structure the middle steel scaffolding present on the first layer must be right clicked with the Engineer's Hammer. See next page for the rocket scaffold body."),
                new ManualPageMultiblock(ManualHelper.getManual(),"Above the base the rocket scaffold body must be built 9 time. It is important to note that in order to build inside the scaffold it must be given 8192 RF/t and 100mb/sec of distilled water.",rocketScaffoldBody)
        );
        ManualHelper.addEntry("Lunar Rocket", "spaceTechnology",
                new ManualPageMultiblock(ManualHelper.getManual(),"This rocket type is able to travel to the Moon or establish a space station around the Earth. It can be made with two possible structure, one using simpler electronics and the",rocket1Slice),
                new ManualPages.Text(ManualHelper.getManual(),"other one more advanced ones. The one above is the first version. Only one slice of the whole structure is displayed, to fit in page, and it must be built inside the Rocket Scaffold, where the slice is repeated throughout its whole height (70 blocks high). After finishing the structure, right click with the Engineer's Hammer on the middle block from the upmost layer. If the structure is correctly built it will disappear and you will be given the rocket in your inventory. See the next page for the second variation of the rocket."),
                new ManualPageMultiblock(ManualHelper.getManual(),"This is the slice of the alternative version of the rocket. The only difference is in the tier of the electronic engineering blocks used.",rocket2Slice)
                );
        ManualHelper.addEntry("Solar System Rocket", "spaceTechnology",
                new ManualPageMultiblock(ManualHelper.getManual(),"The way it is built is the same as for the Lunar Rocket. It is able to travel everywhere the Lunar Rocket was able to plus the rest of the planets/places that exist in the game, namely:",rocket3Slice),
                new ManualPages.Text(ManualHelper.getManual(),"Mars, Venus and the Asteroid Belt."));
        ManualHelper.addEntry("Open Hearth Furnace","metallurgy",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Open Hearth is able to turn Pig Iron into Steel and Steel into Mild Steel. Coke or Diesel can be used as a fuel inputted",openHearthFurnace),
                new ManualPages.Text(ManualHelper.getManual(),"from the bottom left. In the front there are 2 input port one for solid metal and the other for molten metal, it does not matter which one is used. In order for it to operate it must be fed fuel and be kept at maximum temperature. To turn Pig Iron to Steel, the furnace must be fed any amount of Steel and then Pig Iron can be introduced. If the furnace is at maximum temperature then the Pig Iron will slowly start to convert to steel. Furthermore, if the steel is left idle at maximum temperature for 40 seconds it will turn into"),
                new ManualPages.Text(ManualHelper.getManual(),"Mild Steel. All molten outputs can be collected with a pipe from the liquid output port from the front of the machine."));
        ManualHelper.addEntry("Steam Engine","earlyIndustry",
                new ManualPageMultiblock(ManualHelper.getManual(),"The Steam Engine uses either Low Quality Steam or normal Steam to produce rotary power. The rotary power is outputed from the center of the flywheel. Formed by right clicking with the Engineer's Hammer on the further most treated wood plank from the flywheel",steamEngine));
        ManualHelper.addEntry("Lathe","earlyIndustry",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to manufacture finer parts. Requires rotary power in order to operate. Formed by right clicking with the Engineer's Hammer on the middle Mechanical Engineer's Block.",lathe));
        ManualHelper.addEntry("Roller", "earlyIndustry",
                new ManualPageMultiblock(ManualHelper.getManual(),"Allows for cold rolling replacing some of the functionallity of the anvil. Requires rotary power. Formed by right clicking with the Engineer's Hammer on the Block of Steel.",metalRoller));
        ManualHelper.addEntry("Czochralski Crucible","plms",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to manufacture monocritaline silicon boules, which are essential for integrated circuit production. Formed by right clicking with the Engineer's Hammer on the bottom block.",siliconCrucible));
        ManualHelper.addEntry("Coke Oven Battery","earlyIndustry",
                new ManualPageMultiblock(ManualHelper.getManual(),"The upgraded version of the Coke Oven. Unlike the Coke Oven it requires either solid or liquid fuel to perform its function. Formed by right",cokeOvenBattery),
                new ManualPages.Text(ManualHelper.getManual(),"clicking with the Engineer's Hammer on the bottom right concrete block while looking at the opposite side with the smoke stack."));
        ManualHelper.addEntry("Electric Food Oven","utility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to dry/bake various food items. Formed by right clicking on the bottom left Iron Sheetmetal block while looking at the side without the Light Engineering block",electricFoodOven));
        ManualHelper.addEntry("Magnetizer","untility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to magnetize metal using electricity. Formed by right clicking on the Copper Coil Block with the Engineer's Hammer.",magnetizer));
        ManualHelper.addEntry("Delayed Coker Unit","ip",
                new ManualPageBigMultiblock(ManualHelper.getManual(),cokerUnit),
                new ManualPages.Text(ManualHelper.getManual(),"The Delyed Coker Unit is used in the cracking process of Bitumen into Petcoke and Diesel. It is formed by right clicking with the Engineer's Hammer on the Light Engineering Block next to the Heavy Engineering Block."));
    }

    public static void init() {

        GameRegistry.registerTileEntity(ElectricMotorTileEntity.class,new ResourceLocation(TTMain.modId,ElectricMotorTileEntity.class.getSimpleName()));

        GameRegistry.registerTileEntity(TileEntityElectricHeater.class,new ResourceLocation(TTMain.modId,TileEntityElectricHeater.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityFlareStack.class,new ResourceLocation(TTMain.modId,TileEntityFlareStack.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityCoalBoiler.class,new ResourceLocation(TTMain.modId,TileEntityCoalBoiler.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityClarifier.class,new ResourceLocation(TTMain.modId,TileEntityClarifier.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityWaterFilter.class,new ResourceLocation(TTMain.modId,TileEntityWaterFilter.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityOilBoiler.class,new ResourceLocation(TTMain.modId,TileEntityOilBoiler.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityUMPLM.class,new ResourceLocation(TTMain.modId,TileEntityUMPLM.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityNMPLM.class,new ResourceLocation(TTMain.modId,TileEntityNMPLM.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityEUVPLM.class,new ResourceLocation(TTMain.modId,TileEntityEUVPLM.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityGasCentrifuge.class,new ResourceLocation(TTMain.modId,TileEntityGasCentrifuge.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntitySteamRadiator.class,new ResourceLocation(TTMain.modId, TileEntitySteamRadiator.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityIndoorACUnit.class,new ResourceLocation(TTMain.modId,TileEntityIndoorACUnit.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityOutdoorACUnit.class,new ResourceLocation(TTMain.modId,TileEntityOutdoorACUnit.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityTresher.class,new ResourceLocation(TTMain.modId,TileEntityTresher.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityElectricOven.class,new ResourceLocation(TTMain.modId,TileEntityElectricOven.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityClayOven.class,new ResourceLocation(TTMain.modId,TileEntityClayOven.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityMasonryHeater.class,new ResourceLocation(TTMain.modId,TileEntityMasonryHeater.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityRocketScaffold.class,new ResourceLocation(TTMain.modId,TileEntityRocketScaffold.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityMagneticSeparator.class,new ResourceLocation(TTMain.modId,TileEntityMagneticSeparator.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityOpenHearthFurnace.class,new ResourceLocation(TTMain.modId,TileEntityOpenHearthFurnace.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityShaftFurnace.class,new ResourceLocation(TTMain.modId,TileEntityShaftFurnace.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityFBR.class,new ResourceLocation(TTMain.modId,TileEntityFBR.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityCCM.class,new ResourceLocation(TTMain.modId,TileEntityCCM.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntitySmallCoalBoiler.class,new ResourceLocation(TTMain.modId,TileEntitySmallCoalBoiler.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntitySteamEngine.class,new ResourceLocation(TTMain.modId,TileEntitySteamEngine.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityLathe.class, new ResourceLocation(TTMain.modId,TileEntityLathe.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityMetalRoller.class, new ResourceLocation(TTMain.modId, TileEntityMetalRoller.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntitySiliconCrucible.class, new ResourceLocation(TTMain.modId, TileEntitySiliconCrucible.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityCokeOvenBattery.class,new ResourceLocation(TTMain.modId, TileEntityCokeOvenBattery.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityElectricFoodOven.class,new ResourceLocation(TTMain.modId, TileEntityElectricFoodOven.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityMagnetizer.class,new ResourceLocation(TTMain.modId, TileEntityMagnetizer.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityCokerUnit.class,new ResourceLocation(TTMain.modId, TileEntityCokerUnit.class.getSimpleName()));
        GameRegistry.registerTileEntity(SignalSourceTileEntity.class,new ResourceLocation(TTMain.modId, SignalSourceTileEntity.class.getSimpleName()));
        GameRegistry.registerTileEntity(LoadSensorTileEntity.class, new ResourceLocation(TTMain.modId, LoadSensorTileEntity.class.getSimpleName()));
        GameRegistry.registerTileEntity(VirtualConnector.class, new ResourceLocation(TTMain.modId,VirtualConnector.class.getSimpleName()));
        GameRegistry.registerTileEntity(TransactionalTEConnectorLV.class, new ResourceLocation(TTMain.modId,TransactionalTEConnectorLV.class.getSimpleName()));
        GameRegistry.registerTileEntity(TransactionalTEConnectorMV.class, new ResourceLocation(TTMain.modId,TransactionalTEConnectorMV.class.getSimpleName()));
        GameRegistry.registerTileEntity(TransactionalTEConnectorHV.class, new ResourceLocation(TTMain.modId,TransactionalTEConnectorHV.class.getSimpleName()));

        electricHeaterMultiblock = new SimplifiedMultiblockClass(
                "TT:ElectricHeater",
                "electric_heater.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.ELECTRIC_HEATER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.ELECTRIC_HEATER_CHILD.getMeta())
        );

        flareStackMultiblock = new SimplifiedMultiblockClass(
                "TT:FlareStack",
                "flare_stack.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.FLARE_STACK.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.FLARE_STACK_CHILD.getMeta())

        );

        coalBoilerMultiblock = new SimplifiedMultiblockClass(
                "TT:CoalBoiler",
                "coal_boiler.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.COAL_BOILER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.COAL_BOILER_CHILD.getMeta())
        );

        clarifierMultiblock = new SimplifiedMultiblockClass(
                "TT:Clarifier",
                "clarifier.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.CLARIFIER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.CLARIFIER_CHILD.getMeta())
        );

        waterFilter = new SimplifiedMultiblockClass(
                "TT:WaterFilter",
                "water_filter.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.WATER_FILTER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.WATER_FILTER_CHILD.getMeta())
        );

        oilBoilerMultiblock = new SimplifiedMultiblockClass(
                "TT:OilBoiler",
                "oil_boiler.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.OIL_BOILER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.OIL_BOILER_CHILD.getMeta())
        );

        umPhotolithographyMachine = new SimplifiedMultiblockClass(
                "TT:umPLM",
                "umplm.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.UM_PHOTOLITHOGRAPHY_MACHINE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.UM_PHOTOLITHOGRAPHY_MACHINE_CHILD.getMeta())
        );

        nmPhotolitographyMachine = new SimplifiedMultiblockClass(
                "TT:nmPLM",
                "nmplm.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.NMPLM.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.NMPLM_CHILD.getMeta())
        );

        euvPhotolitographyMachine = new SimplifiedMultiblockClass(
                "TT:euvPLM",
                "euvplm.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.EUVPLM.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.EUVPLM_CHILD.getMeta())
        );

        gasCentrifugeMultiblock = new SimplifiedMultiblockClass(
                "TT:GasCentrifuge",
                "gas_centrifuge.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.GAS_CENTRIFUGE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.GAS_CENTRIFUGE_CHILD.getMeta())
        );

        steamRadiatorMultiblock = new SimplifiedMultiblockClass(
                "TT:SteamRadiator",
                "steam_radiator.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.STEAM_RADIATOR.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.STEAM_RADIATOR_CHILD.getMeta())
        );

        indoorAcUnitMultiblock = new SimplifiedMultiblockClass(
                "TT:IndoorAcUnit",
                "indoor_ac_unit.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.INDOOR_AC_UNIT.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.INDOOR_AC_UNIT_CHILD.getMeta())
        );

        outdoorAcUnitMultiblock = new SimplifiedMultiblockClass(
                "TT:OutdoorAcUnit",
                "outdoor_ac_unit.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.OUTDOOR_AC_UNIT.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.OUTDOOR_AC_UNIT_CHILD.getMeta())
        );

        tresherMultiblock = new SimplifiedMultiblockClass(
                "TT:Tresher",
                "tresher.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.TRESHER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.TRESHER_CHILD.getMeta())
        );

        electricOvenMultiblock = new SimplifiedMultiblockClass(
                "TT:ElectricOven",
                "electric_oven.tgz",
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.ELECTRIC_OVEN.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_1.getStateFromMeta(TTBlockTypes_MetalMultiblock_1.ELECTRIC_OVEN_CHILD.getMeta())
        );

        clayOven = new SimplifiedMultiblockClass(
                "TT:ClayOven",
                "clay_oven.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.CLAY_OVEN.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.CLAY_OVEN_CHILD.getMeta())
        );

        masonryHeater = new SimplifiedMultiblockClass(
                "TT:MasonryHeater",
                "masonry_heater.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.MASONRY_HEATER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.MASONRY_HEATER_CHILD.getMeta())
        );

        rocketModuleScaffold = new SimplifiedMultiblockClass(
                "TT:RocketScaffold",
                "rocket_scaffold.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.ROCKET_SCAFFOLD_CHILD.getMeta())
        );

        magneticSeparator = new SimplifiedMultiblockClass(
                "TT:MagneticSeparator",
                "magnetic_separator.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.MAGNETIC_SEPARATOR.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.MAGNETIC_SEPARATOR_CHILD.getMeta())
        );

        openHearthFurnace = new SimplifiedMultiblockClass(
                "TT:OpenHearthFurnace",
                "open_hearth_furnace.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE_CHILD.getMeta())
        ){
            @Override
            public float getManualScale() {
                return 6;
            }
        };

        shaftFurnace = new SimplifiedMultiblockClass(
                "TT:ShaftFurnace",
                "shaft_furnace.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.SHAFT_FURNACE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.SHAFT_FURNACE_CHILD.getMeta())
        ){
            @Override
            public float getManualScale() {
                return 5;
            }
        };

        fbr = new SimplifiedMultiblockClass(
                "TT:FBR",
                "fbr.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.FBR.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.FBR_CHILD.getMeta())
        ){
            @Override
            public float getManualScale() {
                return 14;
            }
        };

        ccm = new SimplifiedMultiblockClass(
                "TT:CCM",
                "ccm.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.CCM.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.CCM_CHILD.getMeta())
        ) {
            @Override
            public float getManualScale() {
                return 14;
            }
        };

        rktModule1 = new RocketModule_SMC(
                "TT:RktModule1",
                "rkt_1.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            private ItemStack rkt = Utils.itemStackFromCTId("<galacticraftcore:rocket_t1>");

            @Override
            public ItemStack getItem() {
                return rkt.copy();
            }
        };

        rktModule2 = new RocketModule_SMC(
                "TT:RktModule2",
                "rkt_2.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            private ItemStack rkt = Utils.itemStackFromCTId("<galacticraftcore:rocket_t1>");

            @Override
            public ItemStack getItem() {
                return rkt.copy();
            }
        };

        rktModule3 = new RocketModule_SMC(
                "TT:RktModule3",
                "rkt_3.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            private ItemStack rkt = Utils.itemStackFromCTId("<galacticraftplanets:rocket_t3>");

            @Override
            public ItemStack getItem() {
                return rkt.copy();
            }
        };

        rocketScaffoldBase = new SimplifiedMultiblockClass(
                "TT:rocketScaffoldBase_NO_MULTIBLOCK",
                "rocket_scaffold_base.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            @Override
            public float getManualScale() {
                return 5;
            }
        };

        rocketScaffoldBody = new SimplifiedMultiblockClass(
                "TT:rocketScaffoldBody_NO_MULTIBLOCK",
                "rocket_scaffold_body.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            @Override
            public float getManualScale() {
                return 5;
            }
        };;

        rocket1Slice = new SimplifiedMultiblockClass(
                "TT:rocketSlice1_NO_MULTIBLOCK",
                "rocket_1_slice.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ) {
            @Override
            public float getManualScale() {
                return 10;
            }
        };

        rocket2Slice = new SimplifiedMultiblockClass(
                "TT:rocketSlice2_NO_MULTIBLOCK",
                "rocket_2_slice.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            @Override
            public float getManualScale() {
                return 10;
            }
        };

        rocket3Slice = new SimplifiedMultiblockClass(
                "TT:rocketSlice3_NO_MULTIBLOCK",
                "rocket_3_slice.tgz",
                Blocks.AIR.getDefaultState(),
                Blocks.AIR.getDefaultState()
        ){
            @Override
            public float getManualScale() {
                return 10;
            }
        };

        smallCoalBoiler = new SimplifiedMultiblockClass(
                "TT:SmallCoalBoiler",
                "small_coal_boiler.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.SMALL_COAL_BOILER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.SMALL_COAL_BOILER_CHILD.getMeta())
        );

        steamEngine = new SimplifiedMultiblockClass(
                "TT:SteamEngine",
                "steam_engine.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.STEAM_ENGINE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.STEAM_ENGINE_CHILD.getMeta())
        );

        lathe = new SimplifiedMultiblockClass(
                "TT:Lathe",
                "lathe.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.LATHE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.LATHE_CHILD.getMeta())
        );

        metalRoller = new SimplifiedMultiblockClass(
                "TT:MetalRoller",
                "metal_roller.tgz",
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.METAL_ROLLER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.METAL_ROLLER_CHILD.getMeta())
        );

        siliconCrucible = new SimplifiedMultiblockClass(
                "TT:SiliconCrucible",
                "silicon_crucible.tgz",
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.SILICON_CRUCIBLE.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.SILICON_CRUCIBLE_CHILD.getMeta())
        );

        cokeOvenBattery = new SimplifiedMultiblockClass(
                "TT:CokeOvenBattery",
                "coke_oven_battery.tgz",
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.COKE_OVEN_BATTERY.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.COKE_OVEN_BATTERY_CHILD.getMeta())
        ) {
            @Override
            public float getManualScale() {
                return 5;
            }
        };

        electricFoodOven = new SimplifiedMultiblockClass(
                "TT:ElectricFoodOven",
                "electric_food_oven.tgz",
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.ELECTRIC_FOOD_OVEN.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.ELECTRIC_FOOD_OVEN_CHILD.getMeta())
        ) {
            @Override
            public float getManualScale() {
                return 14;
            }
        };

        magnetizer = new SimplifiedMultiblockClass(
                "TT:Magnetizer",
                "magnetizer.tgz",
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.MAGNETIZER.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.MAGNETIZER_CHILD.getMeta())
        );

        cokerUnit = new SimplifiedMultiblockClass(
                "TT:CokerUnit",
                "coker_unit.tgz",
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.COKER_UNIT.getMeta()),
                TTIEContent.ttBlockMetalMultiblock_4.getStateFromMeta(TTBlockTypes_MetalMultiblock_4.COKER_UNIT_CHILD.getMeta())
        ) {
            @Override
            public float getManualScale() {
                return 4;
            }
        };

        MultiblockHandler.registerMultiblock(electricHeaterMultiblock); //
        MultiblockHandler.registerMultiblock(flareStackMultiblock);
        MultiblockHandler.registerMultiblock(coalBoilerMultiblock); //
        MultiblockHandler.registerMultiblock(clarifierMultiblock);
        MultiblockHandler.registerMultiblock(waterFilter);
        MultiblockHandler.registerMultiblock(oilBoilerMultiblock); //
        MultiblockHandler.registerMultiblock(umPhotolithographyMachine);
        MultiblockHandler.registerMultiblock(nmPhotolitographyMachine);
        MultiblockHandler.registerMultiblock(euvPhotolitographyMachine);
        MultiblockHandler.registerMultiblock(gasCentrifugeMultiblock);
        MultiblockHandler.registerMultiblock(steamRadiatorMultiblock);
        MultiblockHandler.registerMultiblock(indoorAcUnitMultiblock);
        MultiblockHandler.registerMultiblock(outdoorAcUnitMultiblock);//
        //MultiblockHandler.registerMultiblock(tresherMultiblock);
        MultiblockHandler.registerMultiblock(electricOvenMultiblock);
        MultiblockHandler.registerMultiblock(clayOven);
        MultiblockHandler.registerMultiblock(masonryHeater);
        MultiblockHandler.registerMultiblock(rocketModuleScaffold);
        MultiblockHandler.registerMultiblock(rktModule1); // -
        MultiblockHandler.registerMultiblock(rktModule2); // -
        MultiblockHandler.registerMultiblock(rktModule3); // -
        MultiblockHandler.registerMultiblock(magneticSeparator); // -
        MultiblockHandler.registerMultiblock(openHearthFurnace); // -
        MultiblockHandler.registerMultiblock(shaftFurnace);// -
        MultiblockHandler.registerMultiblock(fbr); // -
        MultiblockHandler.registerMultiblock(ccm); // -
        MultiblockHandler.registerMultiblock(smallCoalBoiler); // -


        MultiblockHandler.registerMultiblock(steamEngine);
        MultiblockHandler.registerMultiblock(lathe);       // done
        MultiblockHandler.registerMultiblock(metalRoller); // done
        MultiblockHandler.registerMultiblock(siliconCrucible);

        MultiblockHandler.registerMultiblock(cokeOvenBattery);
        MultiblockHandler.registerMultiblock(electricFoodOven);
        MultiblockHandler.registerMultiblock(magnetizer);
        MultiblockHandler.registerMultiblock(cokerUnit);
    }

    /**
     * CODE ADAPTED FROM THE IMMERSIVE ENGINEERING SOURCE CODE
     * REPOSITORY CAN BE FOUND HERE https://github.com/BluSunrize/ImmersiveEngineering/tree/1.13pre
     */

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (Item i : registeredTTItems) {
            event.getRegistry().register(i);
        }
        event.getRegistry().register(advancedComputerBlockItem = (new ItemBlock(advancedComputerBlock)).setRegistryName("advanced_computer_block"));
        event.getRegistry().register(rocketControllerBlockItem = (new ItemBlock(rocketControllerBlock)).setRegistryName("rocket_controller_block"));
        event.getRegistry().register(magnetizedNickelSheetMetalItem = (new ItemBlock(magnetizedNickelSheetMetal)).setRegistryName("magnetized_nickel_sm"));
        event.getRegistry().register(electricMotorBlockItem = (new ItemBlock(electricMotorBlock)).setRegistryName("electric_motor_tt"));
        event.getRegistry().register(loadSensorItem = (new ItemBlock(loadSensor)).setRegistryName("load_sensor"));
        event.getRegistry().register(signalSourceBlockItem = (new ItemBlock(signalSourceBlock)).setRegistryName("signal_source_tt"));
        event.getRegistry().register(magnetizedSteelSheetmetalItem = (new ItemBlock(magnetizedSteelSheetmetal)).setRegistryName("magnetized_steel_sm"));
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for (Block b : registeredTTBlocks) {
            event.getRegistry().register(b);
        }
        event.getRegistry().register(advancedComputerBlock);
        event.getRegistry().register(rocketControllerBlock);
        event.getRegistry().register(magnetizedNickelSheetMetal);
        event.getRegistry().register(electricMotorBlock);
        event.getRegistry().register(loadSensor);
        event.getRegistry().register(signalSourceBlock);
        event.getRegistry().register(magnetizedSteelSheetmetal);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {

        ModelLoader.setCustomModelResourceLocation(advancedComputerBlockItem, 0, new ModelResourceLocation(advancedComputerBlockItem.getRegistryName(), "normal"));
        ModelLoader.setCustomStateMapper(advancedComputerBlock, new DefaultStateMapper());
        ModelLoader.setCustomModelResourceLocation(rocketControllerBlockItem, 0, new ModelResourceLocation(rocketControllerBlockItem.getRegistryName(), "normal"));
        ModelLoader.setCustomStateMapper(rocketControllerBlock, new DefaultStateMapper());


        ModelLoader.setCustomModelResourceLocation(magnetizedNickelSheetMetalItem, 0, new ModelResourceLocation(magnetizedNickelSheetMetalItem.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(magnetizedSteelSheetmetalItem,0,new ModelResourceLocation(magnetizedSteelSheetmetalItem.getRegistryName(), "normal"));
        ModelLoader.setCustomStateMapper(magnetizedNickelSheetMetal, new DefaultStateMapper());
        ModelLoader.setCustomModelResourceLocation(electricMotorBlockItem, 0, new ModelResourceLocation(electricMotorBlockItem.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(loadSensorItem, 0, new ModelResourceLocation(loadSensorItem.getRegistryName(), "normal"));
        ModelLoader.setCustomModelResourceLocation(signalSourceBlockItem, 0, new ModelResourceLocation(signalSourceBlockItem.getRegistryName(), "normal"));
        ModelLoader.setCustomStateMapper(electricMotorBlock, new DefaultStateMapper());
        ModelLoader.setCustomStateMapper(loadSensor, new DefaultStateMapper());
        ModelLoader.setCustomStateMapper(signalSourceBlock, new DefaultStateMapper());
        ModelLoader.setCustomStateMapper(magnetizedSteelSheetmetal, new DefaultStateMapper());

        for(Block block : registeredTTBlocks) {
            final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
            Item blockItem = Item.getItemFromBlock(block);
            if(blockItem == null)	throw new RuntimeException("ITEMBLOCK for" + loc + " : " + block + " IS NULL");
            if(block instanceof IEBlockInterfaces.IIEMetaBlock) {
                IEBlockInterfaces.IIEMetaBlock ieMetaBlock = (IEBlockInterfaces.IIEMetaBlock)block;
                if(ieMetaBlock.useCustomStateMapper()) ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
                ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition() {
                    @Override
                    public ModelResourceLocation getModelLocation(ItemStack stack) {
                        return new ModelResourceLocation(loc, "inventory");
                    }
                });
                for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++) {
                    String location = loc.toString();
                    String prop = ieMetaBlock.appendPropertiesToState() ? ("inventory," + ieMetaBlock.getMetaProperty().getName() + "=" + ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
                    if(ieMetaBlock.useCustomStateMapper()) {
                        String custom = ieMetaBlock.getCustomStateMapping(meta, true);
                        if(custom != null) location += "_" + custom.replace("_child","");
                    } try {
                        ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
                    } catch (NullPointerException npe) {
                        throw new RuntimeException("WELP! apparently " + ieMetaBlock + " lacks an item!", npe);
                    }
                }
            }  else {
                ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
            }
        }


        for(Item item : registeredTTItems) {
            if(item instanceof ItemBlock) continue;
            if(item instanceof ItemIEBase) {
                ItemIEBase ipMetaItem = (ItemIEBase) item;
                if(ipMetaItem.registerSubModels && ipMetaItem.getSubNames() != null && ipMetaItem.getSubNames().length > 0) {
                    for(int meta = 0; meta < ipMetaItem.getSubNames().length; meta++) {
                        ResourceLocation loc = new ResourceLocation(TTMain.modId, ipMetaItem.itemName + "/" + ipMetaItem.getSubNames()[meta]);
                        ModelBakery.registerItemVariants(ipMetaItem, loc);
                        ModelLoader.setCustomModelResourceLocation(ipMetaItem, meta, new ModelResourceLocation(loc, "inventory"));
                    }
                } else {
                    final ResourceLocation loc = new ResourceLocation(TTMain.modId, ipMetaItem.itemName);
                    ModelBakery.registerItemVariants(ipMetaItem, loc);
                    ModelLoader.setCustomMeshDefinition(ipMetaItem, new ItemMeshDefinition() {
                        @Override
                        public ModelResourceLocation getModelLocation(ItemStack stack) {
                            return new ModelResourceLocation(loc, "inventory");
                        }
                    });
                }
            } else {
                final ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
                ModelBakery.registerItemVariants(item, loc);
                ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
                    @Override
                    public ModelResourceLocation getModelLocation(ItemStack stack) {
                        return new ModelResourceLocation(loc, "inventory");
                    }
                });
            }
        }
    }

}
