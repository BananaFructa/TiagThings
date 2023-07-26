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
import BananaFructa.ImmersiveIntelligence.ModifiedCO2Filter;
import BananaFructa.RailcraftModifications.RFBlockRockCrusher;
import BananaFructa.RailcraftModifications.TileRollingMachineManualChanged;
import BananaFructa.RailcraftModifications.TileRollingMachinePoweredChanged;
import BananaFructa.TFCTech.BlockInductionCrucibleCAP;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TerraFirmaCraft.TECropBaseHydroponic;
import BananaFructa.TiagThings.ChunkPos;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import BananaFructa.UnecologicalMethods.DrainTileEntity;
import appeng.tile.AEBaseTile;
import appeng.tile.misc.TileInscriber;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPlacer;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPump;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.builders.block.BlockQuarry;
import buildcraft.lib.registry.RegistrationHelper;
import com.eerussianguy.firmalife.FirmaLife;
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
import mods.railcraft.common.blocks.ItemBlockRailcraft;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.machine.IEnumMachine;
import mods.railcraft.common.blocks.machine.equipment.EquipmentVariant;
import mods.railcraft.common.blocks.machine.equipment.TileRollingMachinePowered;
import mods.railcraft.common.blocks.multi.MultiBlockPattern;
import mods.railcraft.common.blocks.multi.TileFluxTransformer;
import mods.railcraft.common.carts.EntityLocomotive;
import mods.railcraft.common.fluids.Fluids;
import mods.railcraft.common.util.misc.Game;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.items.metal.ItemMetalBucket;
import net.dries007.tfc.util.json.JsonConfigRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.Util;
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
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.io.FileUtils;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.TileEntityCO2Filter;
import tfctech.TFCTech;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class CommonProxy {

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

        TTIEContent.init();

        //try {
        //    FileUtils.copyInputStreamToFile((InputStream) Objects.requireNonNull(JsonConfigRegistry.class.getClassLoader().getResourceAsStream("assets/tiagthings/ore_spawn_data.json")), new File((File) Utils.readDeclaredField(JsonConfigRegistry.class, JsonConfigRegistry.INSTANCE, "tfcConfigDir"), "ore_spawn_data.json"));
        //} catch (Exception err) {
        //    err.printStackTrace();
        //}
    }

    public void postInit() {

    }

    // Replace FL climate stations
    @SubscribeEvent
    public void registry(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new HarvesterModified());
        event.getRegistry().register(new HydroponicModified());
        event.getRegistry().register(new BlockClimateStationM(0).setCreativeTab(CreativeTabsTFC.CT_DECORATIONS).setRegistryName(FirmaLife.MOD_ID,"climate_station").setUnlocalizedName("firmalife.climate_station"));
        //event.getRegistry().register(new BlockInscriberModified().setRegistryName(AppEng.MOD_ID,"inscriber").setUnlocalizedName("appliedenergistics2.inscriber"));
        for (int i = 1;i < 6;i++) {
            event.getRegistry().register(new BlockClimateStationM(0).setCreativeTab(CreativeTabsTFC.CT_DECORATIONS).setRegistryName(FirmaLife.MOD_ID,"climate_station_"+i).setUnlocalizedName("firmalife.climate_station_"+i));
        }
        event.getRegistry().register(new BlockInductionCrucibleCAP().setCreativeTab(CreativeTabsTFC.CT_MISC).setRegistryName(TFCTech.MODID,"induction_crucible").setUnlocalizedName(TFCTech.MODID + "." + "induction_crucible"));
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
        if (event.getWorld().getTileEntity(event.getPos().up()) instanceof TileEntityFluidPumpAlternative) {
            scheduled.add(new Tuple<>(new Runnable() {
                @Override
                public void run() {
                    TileEntity entity = event.getWorld().getTileEntity(event.getPos().up());
                    if (entity instanceof TileEntityFluidPumpAlternative) {
                        validatePumpEntity(entity,event.getWorld(),event.getPos().up());
                    }
                }
            },new AtomicInteger(10)));
        }
    }


    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
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
                    validatePumpEntity(entity,entity.getWorld(),entity.getPos());
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
                if (entity != null && (entity.getWorld().provider.getDimension() == -26 || entity.getWorld().provider.getDimension() == -27) && entity instanceof DrainTileEntity) {
                    event.getWorld().setTileEntity(entity.getPos(), new DrainTileEntity());
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

    public void validatePumpEntity(TileEntity entity, World world, BlockPos pos) {
        Block fluidBlock = world.getBlockState(pos.down()).getBlock();
        if (fluidBlock == FluidsTFC.HOT_WATER.get().getBlock()) {

            boolean flowing = BlockFluidBase.getFlowDirection(world,pos.down()) != -1000.0D;
            boolean occupied = false;

            ChunkPos chunkPos = new ChunkPos(world.getChunkFromBlockCoords(pos).x, world.getChunkFromBlockCoords(pos).z);
            if (!flowing) {

                BlockPos reserverPos = TTMain.INSTANCE.worldStorage.getReserver(chunkPos);

                if (reserverPos != null) {
                    TileEntity reserverEntity = world.getTileEntity(reserverPos);
                    //                                                         make sure the pump wasnt destroyed and replaced V
                    if (reserverEntity != null)
                        if (reserverEntity instanceof TileEntityFluidPumpAlternative && reserverEntity != entity)
                            occupied = true;
                }
            } else {
                occupied = true;
            }

            if (occupied) {
                ((TileEntityFluidPumpAlternative) entity).sideConfig[0] = -1;
                entity.markDirty();
            } else {
                TTMain.INSTANCE.worldStorage.addReserver(chunkPos,pos);
            }
        }
    }

    //@SubscribeEvent
    //public void tickk(TickEvent.ServerTickEvent event) {
    //    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    //    List<Entity> entities = server.worlds[0].loadedEntityList;
    //    List<Entity> erem = new ArrayList<>();
    //    for (Entity e : entities) {
    //        if (e instanceof EntityLocomotive) {
    //            erem.add(e);
    //        }
    //    }
    //    for (Entity e : erem) {
    //        server.worlds[0].removeEntity(e);
    //   }
    //}

    List<BlockPos> invalidHotWaterPositions = new ArrayList<>();

    @SubscribeEvent
    public void onCreateSource(BlockEvent.CreateFluidSourceEvent event) {
        BlockPos pos = event.getPos();
        if (invalidHotWaterPositions.stream().anyMatch(o -> Math.abs(o.getX() - pos.getX()) + Math.abs(o.getY() - pos.getY()) + Math.abs(o.getZ() - pos.getZ()) <= 1)) {
            event.setResult(Event.Result.DENY);
        } else if (event.getState().getBlock() == FluidsTFC.HOT_WATER.get().getBlock()) event.setResult(Event.Result.ALLOW);
    }

    // TODO: maybe improve this a bit in the future
    @SubscribeEvent
    public void onBlockPlaced(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) return;

        BlockPos blockPos = event.getPos().offset(event.getFace());
        BlockPos targetPos = event.getPos();

        Block hotWater = FluidsTFC.HOT_WATER.get().getBlock();
        Block saltWater = FluidsTFC.SALT_WATER.get().getBlock();
        Block freshWater = FluidsTFC.FRESH_WATER.get().getBlock();

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
            //if (tileEntity)
        }

        IBlockState blockState = event.getWorld().getBlockState(targetPos);

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

        synchronized (scheduled) {
            Item i = event.getItemStack().getItem();

            Runnable r = () -> {
                if (event.getWorld().getBlockState(blockPos).getBlock() == Blocks.FLOWING_LAVA || event.getWorld().getBlockState(blockPos).getBlock() == Blocks.LAVA) {
                    event.getWorld().setBlockState(blockPos, Blocks.FLOWING_LAVA.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                } else if (event.getWorld().getBlockState(targetPos).getBlock() == Blocks.FLOWING_LAVA || event.getWorld().getBlockState(targetPos).getBlock() == Blocks.LAVA) {
                    event.getWorld().setBlockState(targetPos, Blocks.FLOWING_LAVA.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                }
            };

            Runnable rh = () -> {
                if (event.getWorld().getBlockState(blockPos).getBlock() == hotWater) {
                    event.getWorld().setBlockState(blockPos, hotWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                } else if (event.getWorld().getBlockState(targetPos).getBlock() == hotWater) {
                    event.getWorld().setBlockState(targetPos, hotWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                }
                invalidHotWaterPositions.remove(blockPos);
                invalidHotWaterPositions.remove(targetPos);
            };

            Runnable rs = () -> {
                if (event.getWorld().getBlockState(blockPos).getBlock() == saltWater) {
                    event.getWorld().setBlockState(blockPos, saltWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                } else if (event.getWorld().getBlockState(targetPos).getBlock() == saltWater) {
                    event.getWorld().setBlockState(targetPos, saltWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                }
                invalidHotWaterPositions.remove(blockPos);
                invalidHotWaterPositions.remove(targetPos);
            };

            Runnable rf = () -> {
                if (event.getWorld().getBlockState(blockPos).getBlock() == freshWater) {
                    event.getWorld().setBlockState(blockPos, freshWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                } else if (event.getWorld().getBlockState(targetPos).getBlock() == freshWater) {
                    event.getWorld().setBlockState(targetPos, freshWater.getDefaultState().withProperty(BlockLiquid.LEVEL, 14));
                }
                invalidHotWaterPositions.remove(blockPos);
                invalidHotWaterPositions.remove(targetPos);
            };

            if (i instanceof ItemBucket) {
                ItemBucket bucket = (ItemBucket) i;
                Block inBucket = Utils.readDeclaredField(ItemBucket.class, bucket, "field_77876_a");
                if (inBucket == Blocks.FLOWING_LAVA) {
                    scheduled.add(new Tuple<>(r, new AtomicInteger(100)));
                }
            }
            if (i instanceof UniversalBucket) {
                FluidStack fluidStack = (FluidStack.loadFluidStackFromNBT(event.getItemStack().getTagCompound()));
                if (fluidStack != null && fluidStack.getFluid().getBlock() == hotWater) {
                    scheduled.add(new Tuple<>(rh, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
                if (fluidStack != null && fluidStack.getFluid().getBlock() == saltWater) {
                    scheduled.add(new Tuple<>(rs, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
                if (fluidStack != null && fluidStack.getFluid().getBlock() == freshWater) {
                    scheduled.add(new Tuple<>(rf, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
            }
            if (i instanceof ItemMetalBucket) {
                IFluidHandler handler = event.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing) null);
                FluidStack fluidStack = handler.drain(1000, false);
                if (fluidStack.getFluid().getBlock() == Blocks.LAVA) {
                    scheduled.add(new Tuple<>(r, new AtomicInteger(100)));
                }
                if (fluidStack.getFluid().getBlock() == hotWater) {
                    scheduled.add(new Tuple<>(rh, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
                if (fluidStack.getFluid().getBlock() == saltWater) {
                    scheduled.add(new Tuple<>(rs, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
                if (fluidStack.getFluid().getBlock() == freshWater) {
                    scheduled.add(new Tuple<>(rf, new AtomicInteger(10)));
                    invalidHotWaterPositions.add(blockPos);
                    invalidHotWaterPositions.add(targetPos);
                }
            }
        }
    }

    class a extends Block {

        public a(Material blockMaterialIn, MapColor blockMapColorIn) {
            super(blockMaterialIn, blockMapColorIn);
        }

        @Override
        public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
            super.onEntityWalk(worldIn, pos, entityIn);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ElectrolyzerRecipe.recipeList.clear();
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
    }};

    @SubscribeEvent
    public void onIEMultiblock(MultiblockHandler.MultiblockFormEvent.Pre event) {
        if (bannedMultiblocks.contains(event.getMultiblock().getUniqueName())) event.setCanceled(true);
    }
}
