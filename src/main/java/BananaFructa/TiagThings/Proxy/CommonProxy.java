package BananaFructa.TiagThings.Proxy;

//import BananaFructa.AppliedEnergistics.TileInscriberModified;
import BananaFructa.AdvancedTFCTech.ModifiedTileEntityPowerLoom;
import BananaFructa.AdvancedTFCTech.ModifiedTileEntityPowerLoomParent;
import BananaFructa.BCModifications.RFBlockQuarry;
import BananaFructa.BCModifications.RFTileQuarry;
import BananaFructa.EmergingTechnologies.HarvesterModified;
import BananaFructa.EmergingTechnologies.HarvesterTileEntityModified;
import BananaFructa.EmergingTechnologies.HydroponicModified;
import BananaFructa.EmergingTechnologies.TEHydroponicBed;
import BananaFructa.FLModifications.BlockClimateStationM;
import BananaFructa.Galacticraft.MapGenDungeonEmpty;
import BananaFructa.Galacticraft.MapGenDungeonEmptyVenus;
import BananaFructa.ImmersiveEngineering.*;
import BananaFructa.ImmersiveIntelligence.ModifiedCO2Filter;
import BananaFructa.ImmersiveIntelligence.ModifiedWheelTileEntityIron;
import BananaFructa.ImmersiveIntelligence.ModifiedWheelTileEntitySteel;
import BananaFructa.ImmersiveIntelligence.TileEntityModifiedMechanicalPump;
import BananaFructa.ImmersivePetroleum.ModifiedTileEntityPumpjack;
import BananaFructa.ImmersivePetroleum.ModifiedTileEntityPumpjackParent;
import BananaFructa.RailcraftModifications.RFBlockRockCrusher;
import BananaFructa.RailcraftModifications.TileRollingMachineManualChanged;
import BananaFructa.RailcraftModifications.TileRollingMachinePoweredChanged;
import BananaFructa.TFC.TEBlastFurnaceModified;
import BananaFructa.TFC.TECrucibleCAP;
import BananaFructa.TFC.TEInductionCrucibleCAP;
import BananaFructa.TTIEMultiblocks.ControlBlocks.LoadSensorTileEntity;
import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.Gui.*;
import BananaFructa.TTIEMultiblocks.Gui.CokeOvenBattery.ContainerCokeOvenBattery;
import BananaFructa.TTIEMultiblocks.Gui.CokerUnit.ContainerCokerUnit;
import BananaFructa.TTIEMultiblocks.Gui.ElectricfFoodOven.ContainerElectricFoodOven;
import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.PowerNetworkInfoGui;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorHV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorLV;
import BananaFructa.TTIEMultiblocks.PowerRework.TransactionalTEConnectorMV;
import BananaFructa.TTIEMultiblocks.SignalSourceTileEntity;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TerraFirmaCraft.TECropBaseHydroponic;
import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import BananaFructa.TiagThings.Netowrk.MessagePowerNetworkAskInfo;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import BananaFructa.TiagThings.RockTraces;
import BananaFructa.TiagThings.RockUtils;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import BananaFructa.Uem.DrainFluidPlacer;
import BananaFructa.Uem.DrainSpaceTE;
import BananaFructa.UnecologicalMethods.DrainBlock;
import BananaFructa.UnecologicalMethods.DrainTileEntity;
//import appeng.tile.AEBaseTile;
//import appeng.tile.misc.TileInscriber;
import betterquesting.client.BQ_Keybindings;
import betterquesting.core.BetterQuesting;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.*;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.builders.block.BlockQuarry;
import buildcraft.lib.registry.RegistrationHelper;
import com.eerussianguy.firmalife.FirmaLife;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import flaxbeard.immersivepetroleum.common.IPContent;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityDistillationTower;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import io.moonman.emergingtechnology.config.EmergingTechnologyConfig;
import io.moonman.emergingtechnology.machines.hydroponic.Hydroponic;
import io.moonman.emergingtechnology.providers.ModMediumProvider;
import io.moonman.emergingtechnology.providers.classes.ModMedium;
import journeymap.client.model.SplashPerson;
import li.cil.oc.common.recipe.Recipes;
import li.cil.oc.common.recipe.Recipes$;
import mctmods.immersivetechnology.common.blocks.metal.tileentities.TileEntityFluidPumpAlternative;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.ChunkProviderVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.MapGenDungeonVenus;
import mods.railcraft.common.blocks.ItemBlockEntityDelegate;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.machine.IEnumMachine;
import mods.railcraft.common.blocks.machine.equipment.EquipmentVariant;
import mods.railcraft.common.blocks.multi.MultiBlockPattern;
import mods.railcraft.common.blocks.multi.TileFluxTransformer;
import mods.railcraft.common.fluids.Fluids;
import mods.railcraft.common.util.misc.Game;
import nc.tile.IMultitoolLogic;
import nc.util.NBTHelper;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.te.TEBlastFurnace;
import net.dries007.tfc.objects.te.TECrucible;
import net.dries007.tfc.world.classic.worldgen.vein.VeinRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.Sys;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityCO2Filter;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalPump;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelIron;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelSteel;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt;
import tfctech.objects.tileentities.TEInductionCrucible;
import wile.engineersdecor.blocks.BlockDecorLabeledCrate;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommonProxy implements IGuiHandler {

    static {
        ItemIIMotorBelt.MotorBelt.CLOTH.dropItem.stack = ItemStack.EMPTY;
        ItemIIMotorBelt.MotorBelt.STEEL.dropItem.stack = ItemStack.EMPTY;
        ItemIIMotorBelt.MotorBelt.RUBBER.dropItem.stack = ItemStack.EMPTY;
    }

    public CommonProxy() {
        MinecraftForge.EVENT_BUS.register(this);

        try {
            Object def = Utils.readDeclaredField(RailcraftBlocks.ROCK_CRUSHER.getClass(),RailcraftBlocks.ROCK_CRUSHER,"def");
            Class<?> blockDef = Class.forName("mods.railcraft.common.blocks.RailcraftBlocks$BlockDef");
            Function f = new Function() {
                @Override
                public Object apply(Object o) {
                    return new ItemBlockEntityDelegate<>((RFBlockRockCrusher) o);
                }
            };
            Constructor constructor = blockDef.getConstructors()[0];
            constructor.setAccessible(true);
            Object o = constructor.newInstance(RailcraftBlocks.ROCK_CRUSHER,RailcraftBlocks.ROCK_CRUSHER,"rock_crusher", RFBlockRockCrusher.class,(Supplier)null,f,(Supplier)null);
            Utils.writeDeclaredField(RailcraftBlocks.ROCK_CRUSHER.getClass(),RailcraftBlocks.ROCK_CRUSHER, "def",o,true);
            IEnumMachine.Definition deff_p = Utils.readDeclaredField(EquipmentVariant.ROLLING_MACHINE_POWERED.getClass(),EquipmentVariant.ROLLING_MACHINE_POWERED,"def");
            Utils.writeDeclaredField(IEnumMachine.Definition.class,deff_p,"tile",TileRollingMachinePoweredChanged.class,true);

            IEnumMachine.Definition deff_m = Utils.readDeclaredField(EquipmentVariant.ROLLING_MACHINE_MANUAL.getClass(),EquipmentVariant.ROLLING_MACHINE_MANUAL,"def");
            Utils.writeDeclaredField(IEnumMachine.Definition.class,deff_m,"tile", TileRollingMachineManualChanged.class,true);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void preInit() {

        //RegistryConfig.setRegistryConfig(TTMain.modId,)
        RegistrationHelper HELPER = Utils.readDeclaredField(BCBuildersBlocks.class,null,"HELPER");
        List<Block> bcBlocks = Utils.readDeclaredField(RegistrationHelper.class,HELPER,"blocks");
        List<Item> bcItems = Utils.readDeclaredField(RegistrationHelper.class,HELPER,"items");
        Block target = null;
        Item targetI = null;
        for (Block b : bcBlocks) {
            if (b instanceof BlockQuarry) {
                target = b;
                break;
            }
        }
        for (Item i : bcItems) {
            if (i instanceof ItemBlock) {
                if (((ItemBlock)i).getBlock() instanceof BlockQuarry) {
                    targetI = i;
                    break;
                }
            }
        }
        if (target != null) {
            bcBlocks.remove(target);
        }

        if (targetI != null) {
            bcItems.remove(targetI);
        }
        //       enforces short circuit evaluation to bypass the needed config or smth at RegistrationHelper.java line:130 V
        BCBuildersBlocks.quarry = (BlockQuarry) HELPER.addBlockAndItem(new RFBlockQuarry(Material.IRON,"block.quarry"),true);

        Utils.writeDeclaredField(ModMediumProvider.class,null,"allMedia",new ModMedium[] {
                new ModMedium(4, "minecraft:clay", 0, 5, new String[0], 0, 0),
                new ModMedium(6, "emergingtechnology:biocharblock", 0, 10, new String[0], 0, 0)
        },false);

        char[][][] map = new char[][][]{{{}}};
        List<MultiBlockPattern> patterns = Utils.readDeclaredField(TileFluxTransformer.class,null,"patterns");
        patterns.clear();
        patterns.add(new MultiBlockPattern(map));
    }

    public void init() {
        EmergingTechnologyConfig.HYDROPONICS_MODULE.GROWBED.growBedWaterUsePerCycle = 0;
        ExcavatorHandler.mineralList.clear();
        RegistrationHelper HELPER = Utils.readDeclaredField(BCBuildersBlocks.class,null,"HELPER");
        HELPER.registerTile(RFTileQuarry.class,"tile.quarry");

        // Makes it so that railcraft uses tfc fresh water instead of vanilla water
        Utils.writeDeclaredField(Fluids.WATER.getClass(),Fluids.WATER,"tag","fresh_water",true);
        //GameRegistry.registerTileEntity(ModifiedCO2Filter.class, new ResourceLocation(TTMain.modId, ModifiedCO2Filter.class.getSimpleName()));
        //GameRegistry.registerTileEntity(TileInscriberModified.class,new ResourceLocation(TTMain.modId,TileInscriberModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEFluidPlacerModified.class,new ResourceLocation(TTMain.modId,TEFluidPlacerModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEHydroponicBed.class,new ResourceLocation(TTMain.modId,TEHydroponicBed.class.getSimpleName()));
        GameRegistry.registerTileEntity(TECropBaseHydroponic.class,new ResourceLocation(TTMain.modId,TECropBaseHydroponic.class.getSimpleName()));
        GameRegistry.registerTileEntity(HarvesterTileEntityModified.class,new ResourceLocation(TTMain.modId,HarvesterTileEntityModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileRollingMachinePoweredChanged.class,new ResourceLocation(TTMain.modId,TileRollingMachinePoweredChanged.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileRollingMachineManualChanged.class,new ResourceLocation(TTMain.modId,TileRollingMachineManualChanged.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEFluidPumpAlternativeModified.class,new ResourceLocation(TTMain.modId,TEFluidPumpAlternativeModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileEntityModifiedMechanicalPump.class,new ResourceLocation(TTMain.modId,TileEntityModifiedMechanicalPump.class.getSimpleName()));
        GameRegistry.registerTileEntity(DrainFluidPlacer.class,new ResourceLocation(TTMain.modId,DrainFluidPlacer.class.getSimpleName()));
        GameRegistry.registerTileEntity(TECrucibleCAP.class,new ResourceLocation(TTMain.modId,TECrucibleCAP.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEBlastFurnaceModified.class,new ResourceLocation(TTMain.modId,TEBlastFurnaceModified.class.getSimpleName()));
        //GameRegistry.registerTileEntity(ModifiedWheelTileEntity.class,new ResourceLocation(TTMain.modId,ModifiedWheelTileEntity.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedWheelTileEntityIron.class,new ResourceLocation(TTMain.modId,ModifiedWheelTileEntityIron.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedWheelTileEntitySteel.class,new ResourceLocation(TTMain.modId,ModifiedWheelTileEntitySteel.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityMetalPress.class, new ResourceLocation(TTMain.modId,ModifiedTileEntityMetalPress.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityPumpjackParent.class, new ResourceLocation(TTMain.modId, ModifiedTileEntityPumpjackParent.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityPumpjack.class, new ResourceLocation(TTMain.modId, ModifiedTileEntityPumpjack.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityCrusher.class, new ResourceLocation(TTMain.modId, ModifiedTileEntityCrusher.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityPowerLoom.class,new ResourceLocation(TTMain.modId, ModifiedTileEntityPowerLoom.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedTileEntityPowerLoomParent.class, new ResourceLocation(TTMain.modId, ModifiedTileEntityPowerLoomParent.class.getSimpleName()));
        GameRegistry.registerTileEntity(ModifiedCO2Filter.class, new ResourceLocation(TTMain.modId, ModifiedCO2Filter.class.getSimpleName()));

        TTIEContent.init();
        RockUtils.init();
    }

    public void postInit() {
        Utils.writeDeclaredField(Recipes$.class,Recipes$.MODULE$,"hadErrors",false,false); // supresses open computer recipe error message
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        event.player.sendMessage(new TextComponentString("To open the \u00a7equest menu\u00a7r press [\u00a7b" + BQ_Keybindings.openQuests.getDisplayName() + "\u00a7r]."));
    }

    // Replace FL climate stations
    public void registry(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new HarvesterModified());
        event.getRegistry().register(new HydroponicModified());
        event.getRegistry().register(new BlockClimateStationM(0).setCreativeTab(CreativeTabsTFC.CT_DECORATIONS).setRegistryName(FirmaLife.MOD_ID,"climate_station").setUnlocalizedName("firmalife.climate_station"));
        //event.getRegistry().register(new BlockInscriberModified().setRegistryName(AppEng.MOD_ID,"inscriber").setUnlocalizedName("appliedenergistics2.inscriber"));
        for (int i = 1;i < 6;i++) {
            event.getRegistry().register(new BlockClimateStationM(0).setCreativeTab(CreativeTabsTFC.CT_DECORATIONS).setRegistryName(FirmaLife.MOD_ID,"climate_station_"+i).setUnlocalizedName("firmalife.climate_station_"+i));
        }
        //event.getRegistry().register(new BlockInductionCrucibleCAP().setCreativeTab(CreativeTabsTFC.CT_MISC).setRegistryName(TFCTech.MODID,"induction_crucible").setUnlocalizedName(TFCTech.MODID + "." + "induction_crucible"));
    }


    final List<Tuple<Runnable, AtomicInteger>> scheduled = new ArrayList<>();

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        List<Tuple<Runnable, AtomicInteger>> remove = new ArrayList<>();
        synchronized (scheduled) {
            for (Tuple<Runnable, AtomicInteger> r : scheduled) {
                if (r.getSecond().addAndGet(-1) <= 0) {
                    r.getFirst().run();
                    remove.add(r);
                }
            }
            scheduled.removeAll(remove);
        }
        for(EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers()) {
            BlockPos pos = player.getPosition().down();
            IBlockState b = player.world.getBlockState(pos);
            if(b.getBlock() == IEContent.blockStoneDecoration ||
                    b.getBlock() == IEContent.blockStoneStair_concrete0 ||
                    b.getBlock() == IEContent.blockStoneStair_concrete1 ||
                    b.getBlock() == IEContent.blockStoneStair_concrete2 ||
                    b.getBlock() == IEContent.blockStoneStair_hempcrete ||
                    b.getBlock() == IIContent.blockConcreteDecoration ||
                    b.getBlock() == IIContent.blockConcreteSlabs ||
                    b.getBlock() == IIContent.blockIIConcreteStairs[0] ||
                    b.getBlock() == IIContent.blockIIConcreteStairs[1] ||
                    b.getBlock() == IIContent.blockIIConcreteStairs[2]) {
                player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(1),10,0,true,true));
            }
            if(b.getBlock() == IPContent.blockStoneDecoration) {
                player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(1),10,1,true,true));
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() instanceof DrainBlock) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te instanceof DrainFluidPlacer) {
                ((DrainFluidPlacer)te).onDestroy();
            }
        }
    }

    ItemStack rockTest = null;

    public boolean hasTestKit(EntityPlayer player) {
        if (rockTest == null) rockTest = new ItemStack(ItemLoaderHandler.rockTest);
        return player.inventory.hasItemStack(rockTest);
    }

    @SubscribeEvent
    public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() != null && (event.getHarvester() instanceof FakePlayer || (event.getHarvester().inventory != null && hasTestKit(event.getHarvester())))) {
            for (ItemStack i : event.getDrops()) {
                if (RockUtils.isRock(i)) {
                    RockTraces traces = RockUtils.getTrace(event.getWorld(),event.getPos());
                    if (traces != RockTraces.NONE) {
                        NBTTagCompound tag = i.getTagCompound();
                        if (tag == null) tag = new NBTTagCompound();
                        tag.setInteger("trace",traces.ordinal());
                        i.setTagCompound(tag);
                    }
                }
            }
        }
        //System.out.println("PISS " + RockUtils.getTrace(event.getWorld(),event.getPos()).getName());
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {

        if (Utils.placedInNonWorkingScaffold(event.getWorld(),event.getPos())) {
            event.setCanceled(true);
            return;
        }

        if (event.getWorld().isRemote) return;

        if (event.getPlacedBlock().getBlock().getRegistryName().getResourcePath().equals("asteroid_mining_station") && event.getWorld().provider.getDimension() != -30) {
            event.setCanceled(true);
        }

        event.getWorld().getMinecraftServer().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                TileEntity entity = event.getWorld().getTileEntity(event.getPos());
                if (entity instanceof TileEntityCO2Filter) {
                    synchronized (scheduled) {
                        scheduled.add(new Tuple<Runnable, AtomicInteger>(new Runnable() {
                            @Override
                            public void run() {
                                event.getWorld().setTileEntity(entity.getPos(), new ModifiedCO2Filter());
                            }
                        }, new AtomicInteger(1)));
                    }
                }
                if (entity instanceof TileEntityFluidPump) {
                    TileEntityFluidPump master = (TileEntityFluidPump) entity;
                    if (master.isDummy()) {
                        TileEntity tmp = event.getWorld().getTileEntity(event.getPos().down());
                        if (tmp instanceof TileEntityFluidPump) {
                            master = (TileEntityFluidPump)tmp;
                        }
                    }

                    master.placeCobble = false;
                    master.markDirty();
                }
                if (entity instanceof TileEntityFluidPumpAlternative) {
                    event.getWorld().setTileEntity(entity.getPos(),new TEFluidPumpAlternativeModified());
                }
                if (entity instanceof TileEntityMechanicalPump) {
                    event.getWorld().setTileEntity(entity.getPos(),new TileEntityModifiedMechanicalPump());
                }
                if (entity instanceof TileEntityFluidPlacer) {
                    event.getWorld().setTileEntity(entity.getPos(), new TEFluidPlacerModified());
                }
                /*if (entity instanceof TileInscriber) {
                    event.getWorld().setTileEntity(entity.getPos(), new TileInscriberModified(
                            Utils.readDeclaredField(AEBaseTile.class,entity,"forward"),
                            Utils.readDeclaredField(AEBaseTile.class,entity,"up")
                    ));
                }*/
                if (entity instanceof DrainTileEntity) {
                    if (entity != null && (entity.getWorld().provider.getDimension() == -26 || entity.getWorld().provider.getDimension() == -27)) {
                        event.getWorld().setTileEntity(entity.getPos(), new DrainSpaceTE());
                    }
                    List<TreeSet<Long>> restrictedSources = BananaFructa.TiagThings.Utils.getRestrictedFluidsInArea(entity.getWorld(), event.getPos(), 4);
                    if (Arrays.stream(EnumFacing.values()).anyMatch(f->{
                        if (f == EnumFacing.UP || f == EnumFacing.DOWN) return false;
                        Block b = entity.getWorld().getBlockState(entity.getPos().offset(f)).getBlock();
                        // block is air
                        if (b == Blocks.AIR) return true;
                        if ((b instanceof BlockLiquid || b instanceof IFluidBlock) && entity.getWorld().getBlockState(entity.getPos().offset(f)).getValue(BlockLiquid.LEVEL) != 0) return true;
                        Long l = BananaFructa.TiagThings.Utils.convertPosition(event.getPos().getX(), event.getPos().getZ());
                        for (TreeSet<Long> ts : restrictedSources) {
                            if (ts.contains(l)) {
                                return true; // is near water plaed by another drain
                            }
                        }
                        return false;
                    })) {
                        event.getWorld().setTileEntity(entity.getPos(), new DrainFluidPlacer());
                    };
                }
                if (entity instanceof TEInductionCrucible) {
                    event.getWorld().setTileEntity(entity.getPos(),new TEInductionCrucibleCAP());
                }
                else if (entity instanceof TECrucible) {
                    entity.getWorld().setTileEntity(entity.getPos(),new TECrucibleCAP());
                }
                if (entity instanceof TEBlastFurnace) {
                    event.getWorld().setTileEntity(entity.getPos(),new TEBlastFurnaceModified());
                }
                if (entity instanceof TileEntityWheelIron) {
                    event.getWorld().setTileEntity(entity.getPos(),new ModifiedWheelTileEntityIron(((TileEntityWheelIron)entity).facing));
                }
                if (entity instanceof TileEntityWheelSteel) {
                    event.getWorld().setTileEntity(entity.getPos(),new ModifiedWheelTileEntitySteel(((TileEntityWheelSteel)entity).facing));
                }
                if (entity instanceof TileEntityConnectorHV && !(entity instanceof TileEntityRelayHV)) {
                    event.getWorld().setTileEntity(entity.getPos(),new TransactionalTEConnectorHV(((TileEntityConnectorHV) entity).facing));
                } else if (entity instanceof TileEntityConnectorMV && !(entity instanceof TileEntityRelayMV)) {
                    event.getWorld().setTileEntity(entity.getPos(),new TransactionalTEConnectorMV(((TileEntityConnectorMV) entity).facing));
                } else if (entity instanceof TileEntityConnectorLV && !(entity instanceof TileEntityRelayLV)) {
                    event.getWorld().setTileEntity(entity.getPos(),new TransactionalTEConnectorLV(((TileEntityConnectorLV) entity).facing));
                }
                /*if (entity instanceof TileEntityCapacitorLV) {
                    event.getWorld().setTileEntity(entity.getPos(),new TileEntityCapacitorLVModified());
                }*/
                //if (entity instanceof TileRollingMachinePowered) {
                //    event.getWorld().setTileEntity(entity.getPos(), new TileRollingMachinePoweredChanged());
                //}
                //if (entity instanceof TileEntitySampleDrill) {
                //    event.getWorld().setTileEntity(entity.getPos(), new TileEntitySampleDrillModified());
                //}
                //if (entity instanceof HydroponicTileEntity) {
                    //event.getWorld().setTileEntity(entity.getPos(), new TEHydroponicBed());
                //}
                //if (entity instanceof HarvesterTileEntity) {
                //    event.getWorld().setTileEntity(entity.getPos(), new HarvesterTileEntityModified());
                //}
            }
        });
    }

    @SubscribeEvent
    public void onCreateSource(BlockEvent.CreateFluidSourceEvent event) {
        EnumFacing[] neighbours = new EnumFacing[]{EnumFacing.EAST,EnumFacing.WEST,EnumFacing.NORTH,EnumFacing.SOUTH};
        List<TreeSet<Long>> restrictionSets = Utils.getRestrictedFluidsInArea(event.getWorld(),event.getPos(),1);

        for (EnumFacing f : neighbours) {
            BlockPos p = event.getPos().offset(f);
            Long l = Utils.convertPosition(p.getX(),p.getZ());
            for (TreeSet<Long> ts : restrictionSets) {
                if (ts.contains(l)) {
                    event.setResult(Event.Result.DENY);
                    return;
                }
            }
        }
        if (event.getState().getBlock() == FluidsTFC.HOT_WATER.get().getBlock()) event.setResult(Event.Result.ALLOW);
    }

    private String[] forbiddenBlocks = new String[] {
            "<immersiveengineering:connector>",
            "<immersiveengineering:connector:2>",
            "<immersiveengineering:connector:4>",
            "<immersiveengineering:metal_device0>",
            "<immersiveengineering:metal_device0:1>",
            "<immersiveengineering:metal_device0:2>"
    };

    private Class<? extends TileEntity>[] targetTileEntities = new Class[] {
            TileEntityMetalPress.class,
            TileEntityPumpjack.class,
            TileEntityCrusher.class,
            TileEntityPowerLoom.class
    };

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            if (event.getItemStack().getItem() == IEContent.itemTool) {
                Minecraft.getMinecraft().displayGuiScreen(new PowerNetworkInfoGui());
                TTPacketHandler.wrapper.sendToServer(new MessagePowerNetworkAskInfo(event.getPos().getX(),event.getPos().getY(),event.getPos().getZ()));
            }
            return;
        }

        BlockPos blockPos = event.getPos().offset(event.getFace());
        BlockPos targetPos = event.getPos();
        TileEntity teT = event.getWorld().getTileEntity(targetPos);
        for (Class<? extends TileEntity> cte : targetTileEntities) {
            if (cte.isInstance(teT)) {
                for (String isname : forbiddenBlocks) {
                    ItemStack is = Utils.itemStackFromCTId(isname);
                    System.out.println(is);
                    if (event.getItemStack().getItem() == is.getItem() && (is.getMetadata() == event.getItemStack().getMetadata())) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        if (teT instanceof SimplifiedTileEntityMultiblockMetal) {
            event.getWorld().notifyBlockUpdate(event.getPos(),event.getWorld().getBlockState(event.getPos()),event.getWorld().getBlockState(event.getPos()),2);
            BlockPos masterPos = ((SimplifiedTileEntityMultiblockMetal) teT).master().getPos();
            event.getWorld().notifyBlockUpdate(masterPos,event.getWorld().getBlockState(masterPos),event.getWorld().getBlockState(masterPos),2);
        }

        // IEHAMMER LOGIC ON MUTLTBLOCKS AND PUMP
        if (event.getItemStack().getItem() == IEContent.itemTool) {
            TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
            if (tileEntity instanceof SignalSourceTileEntity) {
                SignalSourceTileEntity te = (SignalSourceTileEntity) tileEntity;
                if (event.getEntityPlayer().isSneaking()) te.signalLevel++;
                else te.signalLevel--;
                if (te.signalLevel > 15) te.signalLevel = 0;
                if (te.signalLevel < 0) te.signalLevel = 15;
                te.markDirty();
                event.getWorld().notifyNeighborsOfStateChange(event.getPos(),event.getWorld().getBlockState(event.getPos()).getBlock(),false);
            }
            if (tileEntity instanceof TileEntityFluidPump) {
                if(((TileEntityFluidPump) tileEntity).isDummy()) {
                    event.setCanceled(true);
                    return;
                }
            }
            if (event.getFace() == EnumFacing.DOWN) {
                if (tileEntity instanceof TileEntityFluidPumpAlternative) {
                    Block fluidBlock = event.getWorld().getBlockState(event.getPos().down()).getBlock();
                    if (fluidBlock == FluidsTFC.HOT_WATER.get().getBlock()) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
            if (tileEntity instanceof TileEntityNMPLM) {
                TileEntityNMPLM te = (TileEntityNMPLM) tileEntity;
                if (te.offset.length == 3 && te.master() != null) {
                    te = te.master();
                    te.changeProcess(event.getEntityPlayer());
                    event.getWorld().notifyBlockUpdate(te.getPos(),event.getWorld().getBlockState(te.getPos()),event.getWorld().getBlockState(te.getPos()),2);
                }
            }
            if (tileEntity instanceof TileEntityEUVPLM) {
                TileEntityEUVPLM te = (TileEntityEUVPLM) tileEntity;
                if (te.offset.length == 3 && te.master() != null) {
                    te = te.master();
                    te.changeProcess(event.getEntityPlayer());
                    event.getWorld().notifyBlockUpdate(te.getPos(),event.getWorld().getBlockState(te.getPos()),event.getWorld().getBlockState(te.getPos()),2);
                }
            }
            if (tileEntity instanceof TileEntityCCM) {
                TileEntityCCM te = (TileEntityCCM) tileEntity;
                if (te.offset.length == 3 && te.master() != null) {
                    te = te.master();
                    te.advanceMode();
                    event.getWorld().notifyBlockUpdate(te.getPos(),event.getWorld().getBlockState(te.getPos()),event.getWorld().getBlockState(te.getPos()),2);
                }
            }
            // NUCLEAR CRAFT MULTITOOL COMPAT WITH THE ENGINEERING HAMMER
            /*if (tileEntity instanceof IMultitoolLogic && !event.getWorld().isRemote) {
                ItemStack stack = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
                NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
                if (nbt != null) {
                    Vec3d hitVec = event.getHitVec();
                    boolean multitoolUsed = ((IMultitoolLogic)tileEntity).onUseMultitool(stack, event.getEntityPlayer(), event.getWorld(), event.getFace(), (float)hitVec.x, (float)hitVec.y, (float)hitVec.z);
                    nbt.setBoolean("multitoolUsed", multitoolUsed);
                    ((IMultitoolLogic) tileEntity).markTileDirty();
                }
            }*/
            synchronized (scheduled) {
                scheduled.add(new Tuple<>(new Runnable() {
                    @Override
                    public void run() {
                        BlockPos pos = event.getPos();
                        TileEntity te = event.getWorld().getTileEntity(pos);
                        if (te instanceof TileEntityPumpjack && !(te instanceof ModifiedTileEntityPumpjack || te instanceof ModifiedTileEntityPumpjackParent)) {
                            replaceMultiblock("IP:Pumpjack", pos, event.getWorld());
                        }
                        if (te instanceof TileEntityPowerLoom && !(te instanceof ModifiedTileEntityPowerLoom || te instanceof ModifiedTileEntityPowerLoomParent)) {
                            replaceMultiblock("att:powerloom", pos, event.getWorld());
                        }
                    }
                }, new AtomicInteger(1)));
            }
        }

        IBlockState blockState = event.getWorld().getBlockState(targetPos);

        // TE HYDROPONIC BED
        if (event.getItemStack().getItem() instanceof ItemSeedsTFC && blockState.getBlock() instanceof Hydroponic && blockState.getBlock().canSustainPlant(blockState,event.getWorld(),targetPos,null,null) && event.getFace() == EnumFacing.UP) {
            ItemSeedsTFC seedsTFC = (ItemSeedsTFC) event.getItemStack().getItem();
            ICrop crop = Utils.readDeclaredField(ItemSeedsTFC.class,seedsTFC,"crop");
            if (crop == null) return;
            event.getWorld().setBlockState(blockPos, BlockCropTFC.get(crop).getDefaultState());
            synchronized (scheduled) {
                scheduled.add(new Tuple<>(new Runnable() {
                    @Override
                    public void run() {
                        event.getWorld().setTileEntity(blockPos, new TECropBaseHydroponic());
                    }
                }, new AtomicInteger(1)));
            }
        }

        // FIRE PIT MULTIBLOCKS
        if (event.getEntityPlayer().isSneaking() && blockState.getBlock() instanceof BlockFirePit) {
            BlockPos behind = targetPos.offset(event.getFace().getOpposite());
            if (TTIEContent.clayOven.createStructure(event.getWorld(),behind,event.getFace(),event.getEntityPlayer())) {
                MultiblockHandler.fireMultiblockFormationEventPre(event.getEntityPlayer(), TTIEContent.clayOven, event.getPos(), event.getEntityPlayer().getHeldItemMainhand());
            } else if (TTIEContent.masonryHeater.createStructure(event.getWorld(),behind,event.getFace(),event.getEntityPlayer())){
                MultiblockHandler.fireMultiblockFormationEventPre(event.getEntityPlayer(), TTIEContent.masonryHeater, event.getPos(), event.getEntityPlayer().getHeldItemMainhand());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ElectrolyzerRecipe.recipeList.clear();
        PowerLoomRecipe.addRecipe(Utils.itemStackFromCTId("<tfcflorae:crop/product/sisal_cloth>",4),new IngredientStack(Utils.itemStackFromCTId("<tiagthings:sisal_woven_pirn>",1)),new IngredientStack(Utils.itemStackFromCTId("<tfcflorae:crop/product/sisal_string>",40)),Utils.itemStackFromCTId("<att:pirn>",1),500,256);
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfcflorae:crop/product/cotton_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:cotton_woven_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tfcflorae:crop/product/cotton_yarn>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfcflorae:crop/product/linen_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:linen_woven_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tfcflorae:crop/product/linen_string>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfcflorae:crop/product/hemp_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:hemp_woven_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tfcflorae:crop/product/hemp_string>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfcflorae:crop/product/yucca_canvas>",4),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:yucca_woven_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tfcflorae:crop/product/yucca_string>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<firmalife:pineapple_leather>",4),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:pineapple_woven_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<firmalife:pineapple_yarn>",24)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.removeRecipes(Utils.itemStackFromCTId("<tfc:crop/product/burlap_cloth>"));
        PowerLoomRecipe.removeRecipes(Utils.itemStackFromCTId("<tfc:animal/product/wool_cloth>"));
        PowerLoomRecipe.removeRecipes(Utils.itemStackFromCTId("<tfc:animal/product/silk_cloth>"));
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfc:crop/product/burlap_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<att:fiber_winded_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tiagthings:jute_string>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfc:animal/product/wool_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<att:wool_winded_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<tfc:animal/product/wool_yarn>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );
        PowerLoomRecipe.addRecipe(
                Utils.itemStackFromCTId("<tfc:animal/product/silk_cloth>",4),
                new IngredientStack(Utils.itemStackFromCTId("<att:silk_winded_pirn>",1)),
                new IngredientStack(Utils.itemStackFromCTId("<minecraft:string>",40)),
                Utils.itemStackFromCTId("<att:pirn>",1),
                500,256
        );

        String[] itemsTr = new String[]{
                "<tfcflorae:food/amaranth>",
                "<tfcflorae:food/amaranth_grain>",
                "<tfcflorae:food/buckwheat>",
                "<tfcflorae:food/buckwheat_grain>",
                "<tfcflorae:food/fonio>",
                "<tfcflorae:food/fonio_grain>",
                "<tfcflorae:food/millet>",
                "<tfcflorae:food/millet_grain>",
                "<tfcflorae:food/quinoa>",
                "<tfcflorae:food/quinoa_grain>",
                "<tfcflorae:food/spelt>",
                "<tfcflorae:food/spelt_grain>",
                "<tfc:food/barley>",
                "<tfc:food/barley_grain>",
                "<tfc:food/maize>",
                "<tfc:food/maize_grain>",
                "<tfc:food/oat>",
                "<tfc:food/oat_grain>",
                "<tfc:food/rice>",
                "<tfc:food/rice_grain>",
                "<tfc:food/rye>",
                "<tfc:food/rye_grain>",
                "<tfc:food/wheat>",
                "<tfc:food/wheat_grain>",
                "<tfcflorae:crop/product/rape>",
                "<tfcflorae:food/rape_seed>"
        };

        ThresherRecipe.recipeList.clear();

        for (int i = 0;i < itemsTr.length;i+=2) {
            ThresherRecipe.addRecipe(Utils.itemStackFromCTId(itemsTr[i+1]),new IngredientStack(Utils.itemStackFromCTId(itemsTr[i])),Utils.itemStackFromCTId("<tfc:straw>",3),5*20,50);
        }

        String[] flours = new String[] {
            "<tfcflorae:food/amaranth_flour>",
                "<tfcflorae:food/amaranth_grain>",
            "<tfcflorae:food/buckwheat_flour>",
                "<tfcflorae:food/buckwheat_grain>",
            "<tfcflorae:food/fonio_flour>",
                "<tfcflorae:food/fonio_grain>",
            "<tfcflorae:food/millet_flour>",
                "<tfcflorae:food/millet_grain>",
            "<tfcflorae:food/quinoa_flour>",
                "<tfcflorae:food/quinoa_grain>",
            "<tfcflorae:food/spelt_flour>",
                "<tfcflorae:food/spelt_grain>",
            "<tfc:food/barley_flour>",
                "<tfc:food/barley_grain>",
            "<tfc:food/cornmeal_flour>",
                "<tfc:food/maize_grain>",
            "<tfc:food/oat_flour>",
                "<tfc:food/oat_grain>",
            "<tfc:food/rice_flour>",
                "<tfc:food/rice_grain>",
            "<tfc:food/rye_flour>",
                "<tfc:food/rye_grain>",
            "<tfc:food/wheat_flour>",
                "<tfc:food/wheat_grain>",
            "<firmalife:chestnut_flour>",
                "<firmalife:roasted_chestnuts>",
                "<tfc:food/olive_paste>",
                "<tfc:food/olive>"

        };

        GristMillRecipe.recipeList.clear();

        for (int i = 0;i < flours.length;i+=2) {
            GristMillRecipe.addRecipe(
                    Utils.itemStackFromCTId(flours[i]),
                    new IngredientStack(Utils.itemStackFromCTId(flours[i+1],1)),
                    3*20,200
            );
        }

    }

    @SubscribeEvent
    public void eventReplaceBlocks(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) return;
        IChunkGenerator generator = ((ChunkProviderServer)event.getWorld().getChunkProvider()).chunkGenerator;

        if (generator instanceof ChunkProviderMoon) {
            ChunkProviderMoon chunkProviderMoon = (ChunkProviderMoon) generator;
            MapGenDungeon mapGenDungeon = Utils.readDeclaredField(ChunkProviderMoon.class,chunkProviderMoon,"dungeonGeneratorMoon");
            if (!(mapGenDungeon instanceof MapGenDungeonEmpty)) {
                Utils.writeDeclaredField(ChunkProviderMoon.class,chunkProviderMoon,"dungeonGeneratorMoon",new MapGenDungeonEmpty(),true);
            }
        }

        if (generator instanceof ChunkProviderMars) {
            ChunkProviderMars chunkProviderMoon = (ChunkProviderMars) generator;
            MapGenDungeon mapGenDungeon = Utils.readDeclaredField(ChunkProviderMars.class,chunkProviderMoon,"dungeonGenerator");
            if (!(mapGenDungeon instanceof MapGenDungeonEmpty)) {
                Utils.writeDeclaredField(ChunkProviderMars.class,chunkProviderMoon,"dungeonGenerator",new MapGenDungeonEmpty(),true);
            }
        }

        if (generator instanceof ChunkProviderVenus) {
            ChunkProviderVenus chunkProviderMoon = (ChunkProviderVenus) generator;
            MapGenDungeonVenus mapGenDungeon = Utils.readDeclaredField(ChunkProviderVenus.class,chunkProviderMoon,"dungeonGenerator");
            if (!(mapGenDungeon instanceof MapGenDungeonEmptyVenus)) {
                Utils.writeDeclaredField(ChunkProviderVenus.class,chunkProviderMoon,"dungeonGenerator",new MapGenDungeonEmptyVenus(),true);
            }
        }
    }

    @SubscribeEvent
    public void onPortal(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) return;

        // No longer needed but let's keep it here
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            List<ISchematicPage> schematicPages = SchematicRegistry.schematicRecipes;
            SchematicRegistry.addUnlockedPage(player,schematicPages.get(0));
            //SchematicRegistry.addUnlockedPage(player,schematicPages.get(1));
            //SchematicRegistry.addUnlockedPage(player,schematicPages.get(2));
            SchematicRegistry.addUnlockedPage(player,schematicPages.get(3));
            SchematicRegistry.addUnlockedPage(player,schematicPages.get(4));
            SchematicRegistry.addUnlockedPage(player,schematicPages.get(5));
        }
    }

    protected final List<String> bannedMultiblocks = new ArrayList<String>() {{
        add("IE:Excavator");
        add("IE:BlastFurnace");
        add("IE:BlastFurnaceAdvanced");
        add("II:Electrolyzer");
        add("IE:Lightningrod");
    }};

    @SubscribeEvent
    public void onIEMultiblock(MultiblockHandler.MultiblockFormEvent.Pre event) {
        if (bannedMultiblocks.contains(event.getMultiblock().getUniqueName())) event.setCanceled(true);
        synchronized (scheduled) {
            scheduled.add(new Tuple<>(new Runnable() {
                @Override
                public void run() {
                    BlockPos pos = event.getClickedBlock();
                    TileEntity te = event.getEntity().world.getTileEntity(pos);
                    if (te instanceof TileEntityDistillationTower.TileEntityDistillationTowerParent) {
                        ((TileEntityDistillationTower.TileEntityDistillationTowerParent) te).getTileForPos(16).getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).receiveEnergy(99999, false);
                        // The distillation tower need to have energy above 0 to process recipes
                        // This is easier than overwritting the tile entity
                    }
                }
            }, new AtomicInteger(1)));
        }
    }

    public static Map<String, ModifiedMultiblockInfo> modifiedMultiblocks = new HashMap<String, ModifiedMultiblockInfo>() {{
        put("IE:MetalPress",new ModifiedMultiblockInfo(
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityMetalPress();
                    }
                },
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityMetalPress();
                    }
                },
                7
        ));
        put("IP:Pumpjack",new ModifiedMultiblockInfo(
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return null;//new ModifiedTileEntityPumpjack();
                    }
                },
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityPumpjackParent();
                    }
                }
                ,70
        ));
        put("IE:Crusher",new ModifiedMultiblockInfo(
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityCrusher();
                    }
                },
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityCrusher();
                    }
                },
                43
        ));
        put("att:powerloom", new ModifiedMultiblockInfo(
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return null;
                    }
                },
                new ModifiedMultiblockInfo.TEProvider<TileEntityMultiblockMetal>() {
                    @Override
                    public TileEntityMultiblockMetal getTE(NBTTagCompound info) {
                        return new ModifiedTileEntityPowerLoomParent();
                    }
                },
                40
        ));

    }};

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onIEMultiblockPost(MultiblockHandler.MultiblockFormEvent.Post event) {
        synchronized (scheduled) {
            scheduled.add(new Tuple<>(new Runnable() {
                @Override
                public void run() {
                    replaceMultiblock(event.getMultiblock().getUniqueName(),event.getClickedBlock(),event.getEntity().world);
                }
            }, new AtomicInteger(1)));
        }
    }

    public void replaceMultiblock(String name, BlockPos source, World world) {
        //System.out.println(name);
        if (modifiedMultiblocks.containsKey(name)) {
            ModifiedMultiblockInfo info = modifiedMultiblocks.get(name);
            if (world.isRemote) return;
            TileEntityMultiblockMetal<?,?> metalPress = (TileEntityMultiblockMetal<?,?>) world.getTileEntity(source);
            for (int i = info.maxPos; i >= 0; i--) {
                //System.out.println(i + " ");
                BlockPos pos = metalPress.getBlockPosForPos(i);
                TileEntity te = world.getTileEntity(pos);
                if (te != null && te instanceof TileEntityMultiblockMetal<?,?>) {
                    TileEntityMultiblockMetal<?,?> comp = (TileEntityMultiblockMetal<?,?>) te;
                    int[] lastOffset = comp.offset;
                    //System.out.println(lastOffset[0] + " " + lastOffset[1] + " " + lastOffset[2]);
                    boolean lastFormed = comp.formed;
                    int lastPos = comp.field_174879_c;
                    boolean mirrored = comp.mirrored;
                    EnumFacing lastFacing = comp.facing;
                    TileEntity newTe = !source.equals(pos) ? info.childProvider.getTE(null) : info.parentProvider.getTE(null);
                    if (newTe == null) continue;
                    world.setTileEntity(pos, newTe);
                    TileEntityMultiblockMetal<?,?> m = (TileEntityMultiblockMetal<?,?>) world.getTileEntity(pos);
                    m.formed = lastFormed;
                    m.field_174879_c = lastPos;
                    m.offset = lastOffset;
                    m.mirrored = mirrored;
                    if (m instanceof TileEntityPowerLoom) ((TileEntityPowerLoom)m).setPos(lastPos);
                    m.setFacing(lastFacing);
                    m.markDirty();
                    IEUtils.notifyClientUpdate(world, pos);
                }
            }
        }
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        switch (ID) {
            case 0:
                if (te instanceof TileEntitySmallCoalBoiler) {
                    TileEntitySmallCoalBoiler smc = ((TileEntitySmallCoalBoiler) te).master();
                    return new ContainerSmallCoalBoiler(player.inventory,smc);
                }
                break;
            case 1:
                if (te instanceof TileEntityLathe) {
                    TileEntityLathe lathe = ((TileEntityLathe) te).master();
                    return new ContainerLathe(player.inventory,lathe);
                }
                break;
            case 2:
                if (te instanceof TileEntityLathe) {
                    TileEntityLathe lathe = ((TileEntityLathe) te).master();
                    return new ContainerLathePlanner(player.inventory,lathe);
                }
                break;
            case 3:
                if (te instanceof TileEntityMetalRoller) {
                    TileEntityMetalRoller roller = ((TileEntityMetalRoller) te).master();
                    return new ContainerMetalRoller(player.inventory,roller);
                }
                break;
            case 4:
                if (te instanceof TileEntityMagneticSeparator) {
                    TileEntityMagneticSeparator separator = ((TileEntityMagneticSeparator) te).master();
                    return new ContainerMagneticSeparatorGui(player.inventory,separator);
                }
                break;
            case 5:
                if (te instanceof ElectricMotorTileEntity) {
                    ElectricMotorTileEntity separator = ((ElectricMotorTileEntity) te);
                    return new ContainerElectricMotor(player.inventory,separator);
                }
                break;
            case 6:
                if (te instanceof TileEntitySiliconCrucible) {
                    TileEntitySiliconCrucible siliconCrucible = ((TileEntitySiliconCrucible) te).master();
                    return new ContainerSiliconCrucible(player.inventory,siliconCrucible);
                }
                break;
            case 7:
                if (te instanceof TileEntityMagnetizer) {
                    TileEntityMagnetizer magnetizer = ((TileEntityMagnetizer) te).master();
                    return new ContainerMagnetizer(player.inventory,magnetizer);
                }
                break;
            case 8:
                if (te instanceof TileEntityCokeOvenBattery) {
                    TileEntityCokeOvenBattery cokeOvenBattery = ((TileEntityCokeOvenBattery) te).master();
                    return new ContainerCokeOvenBattery(player.inventory,cokeOvenBattery);
                }
                break;
            case 9:
                if (te instanceof TileEntityCokerUnit) {
                    TileEntityCokerUnit cokerUnit = ((TileEntityCokerUnit) te).master();
                    return new ContainerCokerUnit(player.inventory,cokerUnit);
                }
                break;
            case 10:
                if (te instanceof TileEntityElectricFoodOven) {
                    TileEntityElectricFoodOven electricFoodOven = ((TileEntityElectricFoodOven) te).master();
                    return new ContainerElectricFoodOven(player.inventory,electricFoodOven);
                }
                break;
            case 11:
                if (te instanceof TileEntityFBR) {
                    TileEntityFBR fbr = ((TileEntityFBR) te).master();
                    return new ContainerFBR(player.inventory,fbr);
                }
                break;
            case 12:
                if (te instanceof TileEntityOpenHearthFurnace) {
                    TileEntityOpenHearthFurnace ohf = ((TileEntityOpenHearthFurnace) te).master();
                    return new ContainerOpenHearthFurnace(player.inventory,ohf);
                }
                break;
            case 13:
                if (te instanceof LoadSensorTileEntity) {
                    return new ContainerLoadSensor(player.inventory,(LoadSensorTileEntity) te);
                }
                break;
        }
        return null;
    }

    /**
     * Adapted from https://github.com/cadaverous-eris/Crates-Felt-Blu/blob/master/src/main/java/cfb/CratesFeltBlu.java
     */
    @SubscribeEvent
    public void crateHarvest(BlockEvent.HarvestDropsEvent event) {
        IBlockState state = event.getState();
        if (state.getBlock() instanceof BlockIIMetalDevice || state.getBlock() instanceof BlockDecorLabeledCrate) {
            List<ItemStack> drops = event.getDrops();
            List<ItemStack> inventory = new ArrayList<ItemStack>();

            for (ItemStack stack : event.getDrops()) {
                if (stack.getItem() instanceof ItemBlock && (((ItemBlock) stack.getItem()).getBlock() instanceof BlockIIMetalDevice || ((ItemBlock) stack.getItem()).getBlock() instanceof BlockDecorLabeledCrate) && stack.hasTagCompound() && stack.getTagCompound().hasKey("inventory", 9)) {
                    NBTTagList invTagList = stack.getTagCompound().getTagList("inventory", 10);
                    inventory.addAll(blusunrize.immersiveengineering.common.util.Utils.readInventory(invTagList, 27));
                    stack.getTagCompound().removeTag("inventory");
                    if (stack.getTagCompound().hasNoTags()) stack.setTagCompound(null);
                }
            }

            drops.addAll(inventory);
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
