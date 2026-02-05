package BananaFructa.TiagThings;

//import BananaFructa.AppliedEnergistics.EntityChargedQuartzModified;
import BananaFructa.ImmersiveEngineering.ModifiedTileEntityMetalPress;
import BananaFructa.ImmersiveIntelligence.*;
import BananaFructa.NCCraft.SelectiveArrayListNC;
import BananaFructa.RailcraftModifications.RFTileBlockCrusher;
import BananaFructa.TFC.TEInductionCrucibleCAP;
import BananaFructa.TTIEMultiblocks.Commands.GetTEPos;
import BananaFructa.TTIEMultiblocks.Commands.StructureGeneratorCommand;
import BananaFructa.TTIEMultiblocks.Renderers.*;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Commands.SetRock;
import BananaFructa.TiagThings.Commands.Wikis;
import BananaFructa.TiagThings.Items.FluidLoaderHandler;
import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import BananaFructa.TiagThings.Proxy.CommonProxy;
//import appeng.items.materials.MaterialType;
import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import com.lumintorious.ambiental.api.TemperatureRegistry;
import com.lumintorious.ambiental.capability.TemperatureCapability;
import com.lumintorious.ambiental.modifiers.*;
import mezz.jei.JustEnoughItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import nc.init.NCFluids;
import nc.integration.crafttweaker.CTRemoveRecipe;
import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.IFoodStatsTFC;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.objects.te.TEFirePit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.*;

@Mod(modid = TTMain.modId,version = TTMain.version,name = TTMain.name,dependencies = "after:railcraft;after:buildcraftbuilders;after:firmalife;before:immersiveintelligence;before:nuclearcraft;after:immersivetech;after:galacticraftcore;after:immersiveengineering;before:tfcflorae")
public class TTMain {

    // There are no recipes present for the items as they are handled by craft twaker in the modpack

    public static final String modId = "tiagthings";
    public static final String name = "Tiag Things";
    public static final String version = "1.4.0";

    public static TTMain INSTANCE;

    public TiagThingWorldStorage worldStorage;

    public static Fluid ttAir,nitrogenSolution,potassiumSolution,phosphorusSolution,liquidHelium3,venusGas,ttChlorine,butane,hotF,coldF,clarifiedWater,clarifiedSeaWater,treatedWaterNN,treatedWater,hySulfide;
    public static Fluid uhexf,uhexf1,uhexf2,uhexf3,uhexf4,uhexf5,uhexf6,uhexf7,uhexf8,uhexf9,uhexf10,uhexf11,uhexf12,uhexf13;

    public static List<Item> items = new ArrayList<Item>() {{
        add(Items.LEATHER_HELMET);
        add(Items.LEATHER_CHESTPLATE);
        add(Items.LEATHER_LEGGINGS);
        add(Items.LEATHER_BOOTS);
    }};

    static {
        //BananaFructa.TiagThings.Utils.writeDeclaredField(MaterialType.CERTUS_QUARTZ_CRYSTAL_CHARGED.getClass(), MaterialType.CERTUS_QUARTZ_CRYSTAL_CHARGED, "droppedEntity", EntityChargedQuartzModified.class, false);
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(TileEntitySteamRadiator.class,state, pos, player, "steam_radiator", 21, 0.06f)));
        TemperatureRegistry.BLOCKS.register((state, pos, player) -> tempHeater(TileEntityElectricHeater.class, state, pos, player, "electric_heater", 21, 0.06f));
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(TileEntityIndoorACUnit.class,state, pos, player, "ac_indoor_unit", -10, 0.06f)));
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(TileEntityOutdoorACUnit.class,state, pos, player, "ac_outdoor_unit", 21, 0.06f)));

        TemperatureRegistry.BLOCKS.register((state,pos,player)->{
            if (state.getBlock() instanceof BlockFirePit) {
                TEFirePit teFirePit = (TEFirePit) player.getEntityWorld().getTileEntity(pos);
                TileEntity below = player.getEntityWorld().getTileEntity(pos.offset(EnumFacing.DOWN));
                if (below instanceof TileEntityMasonryHeater) {
                    if (((TileEntityMasonryHeater) below).field_174879_c == 4) {
                        return new TileEntityModifier("masonry_heater", (teFirePit.getField(0) * 3 / 100.f), 0.06f);
                    }
                }
            }
            return null;
        });

        TemperatureRegistry.ENVIRONMENT.register((player) -> {
            List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(player.posX - 0.5, 0, player.posZ - 0.5, player.posX + 0.5, 2, player.posZ + 0.5));
            return new EnvironmentalModifier("player", 1.0F * entities.size(), 0.3F);
        });// other players
        TemperatureRegistry.ENVIRONMENT.register((player) -> {
            if (player.isPlayerSleeping()) return new EnvironmentalModifier("sleep", 10.8F, 0.3F);
            return null;
            // dQ/dt = kA*(delta_T/L)
            // k = 0.037 W/m*k
            // A = 4 m ^ 2
            // L = 2 cm
            // => delta_T ~= 10.8 K
        });// bed
        TemperatureRegistry.ENVIRONMENT.register((player) -> {

            Iterable<ItemStack> armor = player.getArmorInventoryList();
            int amount = 0;
            for (ItemStack stack : armor) {
                if (stack.getItem() instanceof ItemArmor) {
                    if (items.contains(stack.getItem())) {
                        amount++;
                    }
                }
            }

            if (amount == 0) return null;
            TemperatureCapability capability = (TemperatureCapability) player.getCapability(TemperatureCapability.CAPABILITY,null);
            if (capability == null) return null;
            if (!capability.isRising) { // not too realistic but mostly right
                return new EnvironmentalModifier("armor_other", 1, -amount*0.28f);
            }
            return null;

        });// leather armour

        TemperatureRegistry.ENVIRONMENT.register((player)->{
            ModifierStorage modifiers = new ModifierStorage();

            EquipmentModifier.getModifiers(player,modifiers);

            BaseModifier  modifier = modifiers.get("armor");
            if (modifier == null) return null;
            return new EnvironmentalModifier("armour_negate",-modifier.getChange(),-modifier.getPotency());
        });

        BananaFructa.TiagThings.Utils.writeDeclaredField(CTRemoveRecipe.class, null, "errored", true, false); // annoying ass error
    }

    static public TileEntityModifier tempHeater(Class<?extends SimplifiedTileEntityMultiblockMetal> teClass, IBlockState state, BlockPos pos, EntityPlayer player,String registryName,float temp,float potency) {
        TileEntity te = player.getEntityWorld().getTileEntity(pos);
        if (teClass.isInstance(te)) {
            SimplifiedTileEntityMultiblockMetal ste = (SimplifiedTileEntityMultiblockMetal) te;
            if (!ste.isDummy() && ste.isWorking()) {
                return new TileEntityModifier(registryName, temp, potency);
            }
        }
        return null;
    }

    public TTMain() {
        INSTANCE = this;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemLoaderHandler.preInit();
        //IIContent.itemLightEngineerHelmet = new ModifiedItemLightHelmet();
        //IIContent.itemLightEngineerLeggings = new ModifiedItemLightLeggings();
        //IIContent.itemLightEngineerChestplate = new ModifiedItemLightChestplate();
        //IIContent.itemLightEngineerBoots = new ModifiedItemLightBoots();
        //IIPacketHandler.registerPackets();
        TTPacketHandler.registerPackets();
        FluidLoaderHandler.addLiquid("cooled_lava_mixture","blocks/cooled_lava_mixture_still","blocks/cooled_lava_mixture_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("water_tungsten_mixture","blocks/water_tungsten_mixture_still","blocks/water_tungsten_mixture_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("ethane","blocks/ethane_still","blocks/ethane_flow", Material.WATER,true,false);
        FluidLoaderHandler.addLiquid("ethene","blocks/ethene_still","blocks/ethene_flow", Material.WATER,true,false);
        FluidLoaderHandler.addLiquid("ammonium_hydroxide","blocks/ammonium_hydroxide_still","blocks/ammonium_hydroxide_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("beryllium_sulfide_solution","blocks/beryllium_sulfide_solution","blocks/beryllium_sulfide_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("beryllium_hydroxide_solution","blocks/beryllium_hydroxide_solution","blocks/beryllium_hydroxide_solution", Material.WATER,false,false);
        butane = FluidLoaderHandler.addLiquid("butane","blocks/butane_still","blocks/butane_flow", Material.AIR,true,false);
        ttAir = FluidLoaderHandler.addLiquidNoBucket("tt_air","blocks/tt_air","blocks/tt_air", Material.WATER,true);
        FluidLoaderHandler.addLiquid("tt_liquid_air","blocks/tt_liquid_air","blocks/tt_liquid_air", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("ammonium_sulfate_solution","blocks/ammonium_sulfate_solution","blocks/ammonium_sulfate_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("phosphoric_acid","blocks/phosphoric_acid","blocks/phosphoric_acid", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("diammonium_phosphate_solution","blocks/diammonium_phosphate_solution","blocks/diammonium_phosphate_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("potassium_nitride_solution","blocks/potassium_nitride_solution","blocks/potassium_nitride_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranium_sulfate","blocks/uranium_sulfate","blocks/uranium_sulfate", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranyl_triammine","blocks/uranyl_triammine","blocks/uranyl_triammine", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranium_precipitate_mix","blocks/uranium_precipitate_mix","blocks/uranium_precipitate_mix", Material.WATER,false,false);
        ttChlorine = FluidLoaderHandler.addLiquid("tt_chlorine","blocks/tt_chlorine","blocks/tt_chlorine", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("acetylene","blocks/acetylene","blocks/acetylene", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("vinyl_acetate","blocks/vinyl_acetate","blocks/vinyl_acetate", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("hot_distilled_water","blocks/hot_distilled_water","blocks/hot_distilled_water", Material.WATER,false,false);
        hotF = FluidLoaderHandler.addLiquid("hot_fluoromethane","blocks/hot_fluoromethane","blocks/hot_fluoromethane", Material.AIR,true,true);
        coldF = FluidLoaderHandler.addLiquid("cold_fluoromethane","blocks/cold_fluoromethane","blocks/hot_fluoromethane", Material.AIR,true,true);
        nitrogenSolution = FluidLoaderHandler.addLiquid("nitrogen_nutrient_solution","blocks/nitrogen_nutrient_solution","blocks/nitrogen_nutrient_solution", Material.WATER,false,false);
        potassiumSolution = FluidLoaderHandler.addLiquid("potassium_nutrient_solution","blocks/potassium_nutrient_solution","blocks/potassium_nutrient_solution", Material.WATER,false,false);
        phosphorusSolution = FluidLoaderHandler.addLiquid("phosphorus_nutrient_solution","blocks/phosphorus_nutrient_solution","blocks/phosphorus_nutrient_solution", Material.WATER,false,false);

        FluidLoaderHandler.addLiquid("glycerol","blocks/glycerol","blocks/glycerols", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("maize_slurry","blocks/maize_slurry","blocks/maize_slurry", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("starch_water_suspension","blocks/starch_water_suspension","blocks/starch_water_suspension", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("bioplastic_resin","blocks/bioplastic_resin","blocks/bioplastic_resin", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("glycerol_starch_mix","blocks/glycerol_starch_mix","blocks/glycerol_starch_mix", Material.WATER,false,false);

        liquidHelium3 = FluidLoaderHandler.addLiquid("liquid_helium3","blocks/liquid_helium3","blocks/liquid_helium3", Material.WATER,false,false);

        venusGas = FluidLoaderHandler.addLiquidNoBucket("venusian_atmospheric_gas","blocks/venusian_atmospheric_gas","blocks/venusian_atmospheric_gas", Material.WATER,true);
        FluidLoaderHandler.addLiquid("liquid_venusian_atmospheric_gas","blocks/liquid_venusian_atmospheric_gas","blocks/liquid_venusian_atmospheric_gas", Material.WATER,false,false);

        FluidLoaderHandler.addLiquid("acetic_acid","blocks/acetic_acid","blocks/acetic_acid", Material.WATER,false,false);

        clarifiedWater = FluidLoaderHandler.addLiquid("clarified_water","blocks/clarified_water","blocks/clarified_water", Material.WATER,false,false);
        clarifiedSeaWater = FluidLoaderHandler.addLiquid("clarified_sea_water","blocks/clarified_sea_water","blocks/clarified_sea_water", Material.WATER,false,false);
        treatedWaterNN = FluidLoaderHandler.addLiquid("treated_water_nn","blocks/treated_water","blocks/treated_water", Material.WATER,false,false);
        uhexf = FluidLoaderHandler.addLiquid("uhexf","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf1 = FluidLoaderHandler.addLiquid("uhexf_1","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf2 = FluidLoaderHandler.addLiquid("uhexf_2","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf3 = FluidLoaderHandler.addLiquid("uhexf_3","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf4 = FluidLoaderHandler.addLiquid("uhexf_4","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf5 = FluidLoaderHandler.addLiquid("uhexf_5","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf6 = FluidLoaderHandler.addLiquid("uhexf_6","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf7 = FluidLoaderHandler.addLiquid("uhexf_7","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf8 = FluidLoaderHandler.addLiquid("uhexf_8","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf9 = FluidLoaderHandler.addLiquid("uhexf_9","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf10 = FluidLoaderHandler.addLiquid("uhexf_10","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf11 = FluidLoaderHandler.addLiquid("uhexf_11","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf12 = FluidLoaderHandler.addLiquid("uhexf_12","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        uhexf13 = FluidLoaderHandler.addLiquid("uhexf_13","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        treatedWater = FluidLoaderHandler.addLiquidDrinkable("treated_water","blocks/treated_water","blocks/treated_water", Material.WATER,false,false,(player) -> {
            if (player.getFoodStats() instanceof IFoodStatsTFC) {
                IFoodStatsTFC foodStats = (IFoodStatsTFC)player.getFoodStats();
                foodStats.addThirst(40.0F);
                foodStats.getNutrition().addBuff(FoodData.MILK);
            }

        });
        FluidLoaderHandler.addLiquid("uranyl_fluoride","blocks/uranyl_fluoride","blocks/uranyl_fluoride", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranyl_fluoride_1","blocks/uranyl_fluoride","blocks/uranyl_fluoride", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("utetf","blocks/uhexf","blocks/uhexf", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranium_precipitate_mix_1","blocks/uranium_precipitate_mix","blocks/uranium_precipitate_mix", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("uranium_precipitate_mix_2","blocks/uranium_precipitate_mix","blocks/uranium_precipitate_mix", Material.WATER,false,false);

        FluidLoaderHandler.addLiquid("bauxite_bd_mix","blocks/bauxite_bd_mix","blocks/bauxite_bd_mix", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("alumina_oxide_sol","blocks/alumina_oxide_sol","blocks/alumina_oxide_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("molten_cryolite","blocks/molten_cryolite","blocks/molten_cryolite", Material.LAVA,false,false);

        FluidLoaderHandler.addLiquid("hall_mix","blocks/hall_mix","blocks/hall_mix", Material.LAVA,false,false);

        FluidLoaderHandler.addLiquid("ticl","blocks/ticl","blocks/ticl", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("mgcl","blocks/mgcl","blocks/mgcl", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("mgclmixti","blocks/mgclmixti","blocks/mgclmixti", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("salt_solution","blocks/salt_solution","blocks/salt_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("sodium_zirconate","blocks/sodium_zirconate","blocks/sodium_zirconate", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("zirconia_precipitate","blocks/zirconia_precipitate","blocks/zirconia_precipitate", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("zrcl","blocks/zrcl","blocks/zrcl", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("mgzrcl","blocks/mgzrcl","blocks/mgzrcl", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("heated_oil","blocks/heated_oil","blocks/heated_oil", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("trichlorosilane","blocks/trichlorosilane","blocks/trichlorosilane", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("polyhcl","blocks/polyhcl","blocks/polyhcl", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("hcl","blocks/hcl","blocks/hcl", Material.WATER,false,false);

        FluidLoaderHandler.addLiquid("sour_oil","blocks/heated_oil","blocks/heated_oil", Material.WATER,false,false);
        hySulfide = FluidLoaderHandler.addLiquid("hydrogen_sulfide","blocks/hydrogen_sulfide","blocks/hydrogen_sulfide", Material.AIR, false, false);
        FluidLoaderHandler.addLiquid("sw_mixture","blocks/sw_mixture","blocks/sw_mixture", Material.WATER, false, false);
        FluidLoaderHandler.addLiquid("heated_sour_oil","blocks/heated_oil","blocks/heated_oil", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("copper_sulfate_sol","blocks/copper_sulfate_sol","blocks/copper_sulfate_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("iron_sulfate_copper","blocks/iron_sulfate_copper","blocks/iron_sulfate_copper", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("iron_sulfate_sol","blocks/iron_sulfate_sol","blocks/iron_sulfate_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("cy_sol","blocks/cy_sol","blocks/cy_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("aurcy_sod_hy","blocks/aurcy_sod_hy","blocks/aurcy_sod_hy", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("aqua_regia","blocks/aqua_regia","blocks/aqua_regia", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("hexplat_acid","blocks/hexplat_acid","blocks/hexplat_acid", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("amon_chl_sol","blocks/amon_chl_sol","blocks/amon_chl_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("amon_hexcl_sol","blocks/amon_hexcl_sol","blocks/amon_hexcl_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("tin_chl_sol","blocks/tin_chl_sol","blocks/tin_chl_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("nick_sulf_sol","blocks/nick_sulf_sol","blocks/nick_sulf_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("nick_hydr_sol","blocks/nick_hydr_sol","blocks/nick_hydr_sol", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("slv_cy_sol","blocks/slv_cy_sol","blocks/slv_cy_sol", Material.WATER,false,false);
        NCFluids.fluidPairList = new SelectiveArrayListNC(); // This crime was the only way
        //for (Block i : ITContent.registeredITBlocks) {
        //    if (i instanceof BlockITFluid) {
        //        if (((BlockITFluid)i).getFluid().getName().equals("chlorine")) {
        //            ITContent.registeredITBlocks.remove(i);
        //            break;
        //        }
        //    }
        //}
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        Coke.registerFuel();
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this,proxy);
        GameRegistry.registerTileEntity(RFTileBlockCrusher.class,new ResourceLocation(modId, RFTileBlockCrusher.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEInductionCrucibleCAP.class,new ResourceLocation(modId,TEInductionCrucibleCAP.class.getSimpleName()));
        FluidRegistry.addBucketForFluid(BananaFructa.TiagThings.Utils.fluidFromCTId("<liquid:argon>"));
        FMLInterModComms.sendMessage("waila", "register", "BananaFructa.TTIEMultiblocks.Compat.Waila.TTWailaProvider.callbackRegister");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityClarifier.class, new ClarifierRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySteamEngine.class, new SteamEngineRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySiliconCrucible.class, new SiliconCrucibleRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLathe.class, new LatheRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMetalPress.class,new NewMetalPressRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMetalRoller.class, new MetalRollerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMagneticSeparator.class, new MagneticSeparatorRenderer());
        //ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRocketScaffold.class, new ScaffoldRenderer());
        VenusBlocks.crashedProbe = Blocks.AIR;
        //IIPotions.infrared_vision = Potion.getPotionById(16);
        //BottlingMachineRecipe.removeRecipes(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood"));
        //BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood"), new IngredientStack("dustWood"), new FluidStack(FluidsTFC.FRESH_WATER.get(), 250));
        AsteroidsItems.astroMiner.setMaxStackSize(64);
        proxy.postInit();
    }

    @SidedProxy(modId = TTMain.modId,clientSide = "BananaFructa.TiagThings.Proxy.ClientProxy",serverSide = "BananaFructa.TiagThings.Proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void serverStaring(FMLServerStartingEvent event) {
        worldStorage = TiagThingWorldStorage.get(event.getServer().getWorld(0));
        event.registerServerCommand(new SetRock());
        event.registerServerCommand(new Wikis());
        event.registerServerCommand(new StructureGeneratorCommand());
        event.registerServerCommand(new GetTEPos());
    }

}
