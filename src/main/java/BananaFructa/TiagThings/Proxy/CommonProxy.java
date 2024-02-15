package BananaFructa.TiagThings.Proxy;

import BananaFructa.AppliedEnergistics.TileInscriberModified;
import BananaFructa.BCModifications.RFBlockQuarry;
import BananaFructa.BCModifications.RFTileQuarry;
import BananaFructa.EmergingTechnologies.HarvesterModified;
import BananaFructa.EmergingTechnologies.HarvesterTileEntityModified;
import BananaFructa.EmergingTechnologies.HydroponicModified;
import BananaFructa.EmergingTechnologies.TEHydroponicBed;
import BananaFructa.FLModifications.BlockClimateStationM;
import BananaFructa.Galacticraft.MapGenDungeonEmpty;
import BananaFructa.Galacticraft.MapGenDungeonEmptyVenus;
import BananaFructa.ImmersiveEngineering.TEFluidPlacerModified;
import BananaFructa.ImmersiveEngineering.TEFluidPumpAlternativeModified;
import BananaFructa.ImmersiveIntelligence.ModifiedCO2Filter;
import BananaFructa.RailcraftModifications.RFBlockRockCrusher;
import BananaFructa.RailcraftModifications.TileRollingMachineManualChanged;
import BananaFructa.RailcraftModifications.TileRollingMachinePoweredChanged;
import BananaFructa.TFC.BlockInductionCrucibleCAP;
import BananaFructa.TFC.TEBlastFurnaceModified;
import BananaFructa.TFC.TECrucibleCAP;
import BananaFructa.TFC.TEInductionCrucibleCAP;
import BananaFructa.TTIEMultiblocks.Gui.ContainerSmallCoalBoiler;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityEUVPLM;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityNMPLM;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import BananaFructa.TerraFirmaCraft.TECropBaseHydroponic;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import BananaFructa.Uem.DrainFluidPlacer;
import BananaFructa.Uem.DrainSpaceTE;
import BananaFructa.UnecologicalMethods.DrainBlock;
import BananaFructa.UnecologicalMethods.DrainTileEntity;
import appeng.tile.AEBaseTile;
import appeng.tile.misc.TileInscriber;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPlacer;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPump;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.builders.block.BlockQuarry;
import buildcraft.lib.registry.RegistrationHelper;
import com.eerussianguy.firmalife.FirmaLife;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import flaxbeard.immersivepetroleum.common.IPContent;
import io.moonman.emergingtechnology.config.EmergingTechnologyConfig;
import io.moonman.emergingtechnology.machines.hydroponic.Hydroponic;
import io.moonman.emergingtechnology.providers.ModMediumProvider;
import io.moonman.emergingtechnology.providers.classes.ModMedium;
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
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.te.TEBlastFurnace;
import net.dries007.tfc.objects.te.TECrucible;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityCO2Filter;
import tfctech.TFCTech;
import tfctech.objects.tileentities.TEInductionCrucible;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommonProxy implements IGuiHandler {

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
        GameRegistry.registerTileEntity(ModifiedCO2Filter.class, new ResourceLocation(TTMain.modId, ModifiedCO2Filter.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileInscriberModified.class,new ResourceLocation(TTMain.modId,TileInscriberModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEFluidPlacerModified.class,new ResourceLocation(TTMain.modId,TEFluidPlacerModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEHydroponicBed.class,new ResourceLocation(TTMain.modId,TEHydroponicBed.class.getSimpleName()));
        GameRegistry.registerTileEntity(TECropBaseHydroponic.class,new ResourceLocation(TTMain.modId,TECropBaseHydroponic.class.getSimpleName()));
        GameRegistry.registerTileEntity(HarvesterTileEntityModified.class,new ResourceLocation(TTMain.modId,HarvesterTileEntityModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileRollingMachinePoweredChanged.class,new ResourceLocation(TTMain.modId,TileRollingMachinePoweredChanged.class.getSimpleName()));
        GameRegistry.registerTileEntity(TileRollingMachineManualChanged.class,new ResourceLocation(TTMain.modId,TileRollingMachineManualChanged.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEFluidPumpAlternativeModified.class,new ResourceLocation(TTMain.modId,TEFluidPumpAlternativeModified.class.getSimpleName()));
        GameRegistry.registerTileEntity(DrainFluidPlacer.class,new ResourceLocation(TTMain.modId,DrainFluidPlacer.class.getSimpleName()));
        GameRegistry.registerTileEntity(TECrucibleCAP.class,new ResourceLocation(TTMain.modId,TECrucibleCAP.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEBlastFurnaceModified.class,new ResourceLocation(TTMain.modId,TEBlastFurnaceModified.class.getSimpleName()));

        TTIEContent.init();
    }

    public void postInit() {

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
                    b.getBlock() == IIContent.blockConcreteSlabs||
                    b.getBlock() == IIContent.blockIIConcreteStairs[0]||
                    b.getBlock() == IIContent.blockIIConcreteStairs[1]||
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
        if (event.getWorld().getBlockState(event.getPos()) instanceof DrainBlock) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te instanceof DrainFluidPlacer) {
                ((DrainFluidPlacer)te).onDestroy();
            }
        }
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
                    scheduled.add(new Tuple<Runnable,AtomicInteger>(new Runnable() {
                        @Override
                        public void run() {
                            event.getWorld().setTileEntity(entity.getPos(),new ModifiedCO2Filter());
                        }
                    },new AtomicInteger(1)));
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
                if (entity instanceof TileEntityFluidPlacer) {
                    event.getWorld().setTileEntity(entity.getPos(), new TEFluidPlacerModified());
                }
                if (entity instanceof TileInscriber) {
                    event.getWorld().setTileEntity(entity.getPos(), new TileInscriberModified(
                            Utils.readDeclaredField(AEBaseTile.class,entity,"forward"),
                            Utils.readDeclaredField(AEBaseTile.class,entity,"up")
                    ));
                }
                if (entity instanceof DrainTileEntity) {
                    if (entity != null && (entity.getWorld().provider.getDimension() == -26 || entity.getWorld().provider.getDimension() == -27)) {
                        event.getWorld().setTileEntity(entity.getPos(), new DrainSpaceTE());
                    }
                    if (Arrays.stream(EnumFacing.values()).anyMatch(f->{
                        if (f == EnumFacing.UP || f == EnumFacing.DOWN) return false;
                        return entity.getWorld().getBlockState(entity.getPos().offset(f)).getBlock() == Blocks.AIR;
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

    // TODO: maybe improve this a bit in the future
    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;

        BlockPos blockPos = event.getPos().offset(event.getFace());
        BlockPos targetPos = event.getPos();

        // EHAMMER LOGIC ON MUTLTBLOCKS AND PUMP
        if (event.getItemStack().getItem() == IEContent.itemTool) {
            TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
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
        }

        IBlockState blockState = event.getWorld().getBlockState(targetPos);

        // TE HYDROPONIC BED
        if (event.getItemStack().getItem() instanceof ItemSeedsTFC && blockState.getBlock() instanceof Hydroponic && blockState.getBlock().canSustainPlant(blockState,event.getWorld(),targetPos,null,null) && event.getFace() == EnumFacing.UP) {
            ItemSeedsTFC seedsTFC = (ItemSeedsTFC) event.getItemStack().getItem();
            ICrop crop = Utils.readDeclaredField(ItemSeedsTFC.class,seedsTFC,"crop");
            if (crop == null) return;
            event.getWorld().setBlockState(blockPos, BlockCropTFC.get(crop).getDefaultState());
            scheduled.add(new Tuple<>(new Runnable(){
                @Override
                public void run() {
                    event.getWorld().setTileEntity(blockPos,new TECropBaseHydroponic());
                }
            },new AtomicInteger(1)));
        }

        // FIRE PIT MULTIBLOCKS
        if (event.getEntityPlayer().isSneaking() && blockState.getBlock() instanceof BlockFirePit) {
            BlockPos behind = targetPos.offset(event.getFace().getOpposite());
            if(!TTIEContent.clayOven.createStructure(event.getWorld(),behind,event.getFace(),event.getEntityPlayer())){
                TTIEContent.masonryHeater.createStructure(event.getWorld(),behind,event.getFace(),event.getEntityPlayer());
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
                new IngredientStack(Utils.itemStackFromCTId("<minecraft:string>",88)),
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
        add("TT:ClayOven");
        add("TT:MasonryHeater");
        add("IE:Lightningrod");
    }};

    @SubscribeEvent
    public void onIEMultiblock(MultiblockHandler.MultiblockFormEvent.Pre event) {
        if (bannedMultiblocks.contains(event.getMultiblock().getUniqueName())) event.setCanceled(true);
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 0) {
            TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
            if (te instanceof TileEntitySmallCoalBoiler) {
                TileEntitySmallCoalBoiler smc = ((TileEntitySmallCoalBoiler) te).master();
                return new ContainerSmallCoalBoiler(player.inventory,smc);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
