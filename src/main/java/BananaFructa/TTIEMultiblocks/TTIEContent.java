package BananaFructa.TTIEMultiblocks;

import BananaFructa.TTIEMultiblocks.IECopy.BlockTTBase;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.items.ItemIEBase;
import blusunrize.lib.manual.ManualPages;
import flaxbeard.immersivepetroleum.client.page.ManualPageBigMultiblock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod.EventBusSubscriber
public class TTIEContent {

    public static BlockTTBase<TTBlockTypes_MetalMultiblock> ttBlockMetalMultiblock;
    public static BlockTTBase<TTBlockTypes_MetalMultiblock_1> ttBlockMetalMultiblock_1;

    public static List<Block> registeredTTBlocks = new ArrayList<>();
    public static List<Item> registeredTTItems = new ArrayList<>();

    static {
        ttBlockMetalMultiblock = new TTBlockMetalMultiblocks();
        ttBlockMetalMultiblock_1 = new TTBlockMetalMultiblocks_1();
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
    public static SimplifiedMultiblockClass computerClusterUnit;

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
        ManualHelper.addEntry("Thresher","utility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Used to separate grains from their chaff and straw.",tresherMultiblock)
                );
        ManualHelper.addEntry("Electric Oven","utility",
                new ManualPageMultiblock(ManualHelper.getManual(),"Electric version of the normal Oven. Also does the job of the leaf drying mat.",electricOvenMultiblock));
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

    }

    public static void init() {
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
        GameRegistry.registerTileEntity(TileEntityComputerClusterUnit.class,new ResourceLocation(TTMain.modId,TileEntityComputerClusterUnit.class.getSimpleName()));

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

        computerClusterUnit = new SimplifiedMultiblockClass(
                "TT:ComputerClusterUnit",
                "computer_cluster_unit.tgz",
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.COMPUTER_CLUSTER_UNIT.getMeta()),
                TTIEContent.ttBlockMetalMultiblock.getStateFromMeta(TTBlockTypes_MetalMultiblock.COMPUTER_CLUSTER_UNIT_CHILD.getMeta())
        );

        MultiblockHandler.registerMultiblock(electricHeaterMultiblock);
        MultiblockHandler.registerMultiblock(flareStackMultiblock);
        MultiblockHandler.registerMultiblock(coalBoilerMultiblock);
        MultiblockHandler.registerMultiblock(clarifierMultiblock);
        MultiblockHandler.registerMultiblock(waterFilter);
        MultiblockHandler.registerMultiblock(oilBoilerMultiblock);
        MultiblockHandler.registerMultiblock(umPhotolithographyMachine);
        MultiblockHandler.registerMultiblock(nmPhotolitographyMachine);
        MultiblockHandler.registerMultiblock(euvPhotolitographyMachine);
        MultiblockHandler.registerMultiblock(gasCentrifugeMultiblock);
        MultiblockHandler.registerMultiblock(steamRadiatorMultiblock);
        MultiblockHandler.registerMultiblock(indoorAcUnitMultiblock);
        MultiblockHandler.registerMultiblock(outdoorAcUnitMultiblock);
        MultiblockHandler.registerMultiblock(tresherMultiblock);
        MultiblockHandler.registerMultiblock(electricOvenMultiblock);
        MultiblockHandler.registerMultiblock(computerClusterUnit);
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (Item i : registeredTTItems) {
            event.getRegistry().register(i);
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for (Block b : registeredTTBlocks) {
            event.getRegistry().register(b);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {

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
