package BananaFructa.TiagThings;

import BananaFructa.AppliedEnergistics.EntityChargedQuartzModified;
import BananaFructa.ImmersiveIntelligence.*;
import BananaFructa.NCCraft.SelectiveArrayListNC;
import BananaFructa.RailcraftModifications.RFTileBlockCrusher;
import BananaFructa.TFCTech.TEInductionCrucibleCAP;
import BananaFructa.TiagThings.Commands.Wikis;
import BananaFructa.TiagThings.Items.FluidLoaderHandler;
import BananaFructa.TiagThings.Items.ItemLoaderHandler;
import BananaFructa.TiagThings.Proxy.CommonProxy;
import appeng.core.localization.GuiText;
import appeng.items.materials.MaterialType;
import blusunrize.immersiveengineering.api.crafting.BottlingMachineRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.compat.crafttweaker.BottlingMachine;
import com.cleanroommc.multiblocked.api.recipe.RecipeLogic;
import com.cleanroommc.multiblocked.api.tile.ControllerTileEntity;
import com.lumintorious.ambiental.api.TemperatureRegistry;
import com.lumintorious.ambiental.capability.TemperatureCapability;
import com.lumintorious.ambiental.modifiers.*;
import mctmods.immersivetechnology.common.ITContent;
import mctmods.immersivetechnology.common.blocks.BlockITFluid;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import mods.railcraft.common.carts.EntityLocomotive;
import nc.init.NCFluids;
import nc.integration.crafttweaker.CTRemoveRecipe;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import net.minecraft.item.*;

@Mod(modid = TTMain.modId,version = TTMain.version,name = TTMain.name,dependencies = "after:railcraft;after:buildcraftbuilders;after:firmalife;after:immersiveintelligence;before:nuclearcraft;after:immersivetech;after:galacticraftcore;after:immersiveengineering;before:tfcflorae")
public class TTMain {

    // There are no recipes present for the items as they are handled by craft twaker in the modpack

    public static final String modId = "tiagthings";
    public static final String name = "Tiag Things";
    public static final String version = "1.2.12";

    public static TTMain INSTANCE;

    public TiagThingWorldStorage worldStorage;

    public static Fluid ttAir,nitrogenSolution,potassiumSolution,phosphorusSolution,liquidHelium3,venusGas,ttChlorine;

    public static List<Item> items = new ArrayList<Item>() {{
        add(Items.LEATHER_HELMET);
        add(Items.LEATHER_CHESTPLATE);
        add(Items.LEATHER_LEGGINGS);
        add(Items.LEATHER_BOOTS);
    }};

    static {
        BananaFructa.TiagThings.Utils.writeDeclaredField(MaterialType.CERTUS_QUARTZ_CRYSTAL_CHARGED.getClass(), MaterialType.CERTUS_QUARTZ_CRYSTAL_CHARGED, "droppedEntity", EntityChargedQuartzModified.class, false);
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(state, pos, player, "steam_radiator", 21, 0.06f)));
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(state, pos, player, "electric_heater", 21, 0.06f)));
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(state, pos, player, "ac_indoor_unit", -10, 0.06f)));
        TemperatureRegistry.BLOCKS.register(((state, pos, player) -> tempHeater(state, pos, player, "ac_outdoor_unit", 21, 0.06f)));
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

    static {
        /*
        try {
            String options = "resourcePacks:[\"Tiag.zip\"]";
            File f = new File(Minecraft.getMinecraft().mcDataDir, "options.txt");
            String text = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            if (!text.contains("Tiag.zip")) {
                if (!text.contains("resourcePacks")) {
                    FileWriter w = new FileWriter(f,true);
                    w.write(options);
                    w.close();
                } else {
                    if (text.split("resourcePacks:")[1].toCharArray()[1] != ']') {
                        text.replace("resourcePacks:[","resourcePacks:[\"Tiag.zip\",");
                    } else {
                        text.replace("resourcePacks:[","resourcePacks:[\"Tiag.zip\"");
                    }
                    f.delete();
                    f.createNewFile();
                    FileWriter w = new FileWriter(f);
                    w.write(text);
                    w.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }

    static public TileEntityModifier tempHeater(IBlockState state, BlockPos pos, EntityPlayer player,String registryName,float temp,float potency) {
        //if (!OxygenUtil.checkTorchHasOxygen(player.world,pos)) return null;
        if (state.getBlock().getRegistryName() != null && state.getBlock().getRegistryName().getResourcePath().equals(registryName)) {
            TileEntity te = player.getEntityWorld().getTileEntity(pos);
            if (te instanceof ControllerTileEntity) {
                ControllerTileEntity controllerTileEntity = (ControllerTileEntity) te;
                if (controllerTileEntity.recipeLogic() != null && controllerTileEntity.recipeLogic().getStatus() == RecipeLogic.Status.WORKING) {
                    return new TileEntityModifier(registryName,temp,potency);
                }
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
        IIContent.itemLightEngineerHelmet = new ModifiedItemLightHelmet();
        IIContent.itemLightEngineerLeggings = new ModifiedItemLightLeggings();
        IIContent.itemLightEngineerChestplate = new ModifiedItemLightChestplate();
        IIContent.itemLightEngineerBoots = new ModifiedItemLightBoots();
        IIPacketHandler.registerPackets();
        FluidLoaderHandler.addLiquid("cooled_lava_mixture","blocks/cooled_lava_mixture_still","blocks/cooled_lava_mixture_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("water_tungsten_mixture","blocks/water_tungsten_mixture_still","blocks/water_tungsten_mixture_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("butane","blocks/butane_still","blocks/butane_flow", Material.WATER,true,false);
        FluidLoaderHandler.addLiquid("ethane","blocks/ethane_still","blocks/ethane_flow", Material.WATER,true,false);
        FluidLoaderHandler.addLiquid("ethene","blocks/ethene_still","blocks/ethene_flow", Material.WATER,true,false);
        FluidLoaderHandler.addLiquid("ammonium_hydroxide","blocks/ammonium_hydroxide_still","blocks/ammonium_hydroxide_flow", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("beryllium_sulfide_solution","blocks/beryllium_sulfide_solution","blocks/beryllium_sulfide_solution", Material.WATER,false,false);
        FluidLoaderHandler.addLiquid("beryllium_hydroxide_solution","blocks/beryllium_hydroxide_solution","blocks/beryllium_hydroxide_solution", Material.WATER,false,false);
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
        FluidLoaderHandler.addLiquid("hot_fluoromethane","blocks/hot_fluoromethane","blocks/hot_fluoromethane", Material.AIR,true,true);
        FluidLoaderHandler.addLiquid("cold_fluoromethane","blocks/cold_fluoromethane","blocks/hot_fluoromethane", Material.AIR,true,true);
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
        GameRegistry.registerTileEntity(RFTileBlockCrusher.class,new ResourceLocation(modId, RFTileBlockCrusher.class.getSimpleName()));
        GameRegistry.registerTileEntity(TEInductionCrucibleCAP.class,new ResourceLocation(modId,TEInductionCrucibleCAP.class.getSimpleName()));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        VenusBlocks.crashedProbe = Blocks.AIR;
        IIPotions.infrared_vision = Potion.getPotionById(16);
        BottlingMachineRecipe.removeRecipes(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood"));
        BottlingMachineRecipe.addRecipe(Utils.getStackWithMetaName(IIContent.itemMaterial, "pulp_wood"), new IngredientStack("dustWood"), new FluidStack(FluidsTFC.FRESH_WATER.get(), 250));
        AsteroidsItems.astroMiner.setMaxStackSize(64);
        proxy.postInit();
    }

    @SidedProxy(modId = TTMain.modId,clientSide = "BananaFructa.TiagThings.Proxy.ClientProxy",serverSide = "BananaFructa.TiagThings.Proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void serverStaring(FMLServerStartingEvent event) {
        worldStorage = TiagThingWorldStorage.get(event.getServer().getWorld(0));
        event.registerServerCommand(new Wikis());
    }

}
