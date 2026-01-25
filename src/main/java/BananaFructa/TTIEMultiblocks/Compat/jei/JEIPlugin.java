package BananaFructa.TTIEMultiblocks.Compat.jei;

import BananaFructa.TTIEMultiblocks.*;
import BananaFructa.TTIEMultiblocks.Gui.LatheAction;
import BananaFructa.TTIEMultiblocks.Gui.LatheActionFlag;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.compat.jei.metalpress.MetalPressRecipeCategory;
import blusunrize.immersiveengineering.common.util.compat.jei.metalpress.MetalPressRecipeWrapper;
import com.google.common.collect.Collections2;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import nc.multiblock.qComputer.QuantumGate;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import javax.swing.tree.TreeCellEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin {
    public static List<Tuple<List<SimplifiedMultiblockRecipe>,TTJEICategory>> recipesToRegister = new ArrayList<>();

    public static TTCategory coalBoilerBurnCategory;
    public static TTCategory coalBoilerCategory;
    public static TTCategory oilBoilerBurnCategory;
    public static TTCategory flareStackCategory;
    public static TTCategory clarifierRecipe;
    public static TTCategory waterFilterCategory;
    public static TTCategory oilBoilerCategory;
    public static TTCategory umplmCategory;
    public static TTCategory nmplmCategory;
    public static TTCategory euvplmCategory;
    public static TTCategory gasCentrifugeCategory;
    public static TTCategory steamRadiatorCategory;
    public static TTCategory indoorAcCategory;
    public static TTCategory outdoorAcCategory;
    public static TTCategory electricOvenCategory;
    public static TTCategory tresherCategory;
    public static TTCategory ccm;
    public static TTCategory fbr;
    public static TTCategory shaftFurnace;
    public static TTCategory openHearthFurnace;
    public static TTCategory magneticSeparator;
    public static TTCategory memoryFormatter;
    public static TTCategory openHearthFurnaceFuelSolid;
    public static TTCategory openHearthFurnaceFuelLiquid;
    public static TTCategory openHearthFurnaceSit;
    public static TTCategory clayOven;
    public static TTCategory smallCoalFuel;
    public static TTCategory smallCoalBoiler;
    public static TTCategory lathe;
    public static TTCategory metal_roller;
    public static TTCategory silicon_crucible;
    public static TTCategory cokerUnit;
    public static TTCategory cokeOvenBattery,cokeOvenBatterySolid,cokeOvenBatteryLiquid;
    public static TTCategory electricFoodOven;
    public static TTCategory magnetizer;
    public static TTCategory steamEngine;

    void registerRecipes() {
        recipesToRegister.add(new Tuple<>(TileEntityCoalBoiler.burnRecipes,TTJEICategory.COAL_BOILER_BURN));
        recipesToRegister.add(new Tuple<>(new ArrayList<SimplifiedMultiblockRecipe>(){{add(TileEntityOilBoiler.coalBurnRecipe);}},TTJEICategory.OIL_BOILER_BURN));
        recipesToRegister.add(new Tuple<>(TileEntityFlareStack.recipes,TTJEICategory.FLARE_STACK));
        recipesToRegister.add(new Tuple<>(new ArrayList<SimplifiedMultiblockRecipe>(){{add(TileEntityCoalBoiler.waterBurnRecipe);}},TTJEICategory.COAL_BOILER));
        recipesToRegister.add(new Tuple<>(TileEntityClarifier.recipes,TTJEICategory.CLARIFIER));
        recipesToRegister.add(new Tuple<>(TileEntityWaterFilter.recipes,TTJEICategory.WATER_FILTER));
        recipesToRegister.add(new Tuple<>(new ArrayList<SimplifiedMultiblockRecipe>(){{add(TileEntityOilBoiler.waterBurnRecipe);}},TTJEICategory.OIL_BOILER));
        recipesToRegister.add(new Tuple<>(TileEntityUMPLM.recipes,TTJEICategory.UM_PL_MACHINE));
        recipesToRegister.add(new Tuple<>(TileEntityNMPLM.recipes,TTJEICategory.NM_PL_MACHINE));
        recipesToRegister.add(new Tuple<>(TileEntityEUVPLM.recipes,TTJEICategory.EUV_PL_MACHINE));
        recipesToRegister.add(new Tuple<>(TileEntityGasCentrifuge.recipes,TTJEICategory.GAS_CENTRIFUGE));
        recipesToRegister.add(new Tuple<>(TileEntitySteamRadiator.recipes,TTJEICategory.STEAM_RADIATOR));
        recipesToRegister.add(new Tuple<>(TileEntityIndoorACUnit.recipes,TTJEICategory.INDOOR_AC));
        recipesToRegister.add(new Tuple<>(TileEntityOutdoorACUnit.recipes,TTJEICategory.OUTDOOR_AC));
        recipesToRegister.add(new Tuple<>(TileEntityElectricOven.recipes,TTJEICategory.ELECTRIC_OVEN));
        recipesToRegister.add(new Tuple<>(TileEntityTresher.recipes, TTJEICategory.TRESHER));
        recipesToRegister.add(new Tuple<>(TileEntityCCM.recipes.stream().map(i->(SimplifiedMultiblockRecipe)i).collect(Collectors.toList()),TTJEICategory.CCM));
        recipesToRegister.add(new Tuple<>(TileEntityFBR.recipes,TTJEICategory.FBR));
        recipesToRegister.add(new Tuple<>(TileEntityShaftFurnace.recipes,TTJEICategory.SHAFT_FURNACE));
        recipesToRegister.add(new Tuple<>(TileEntityOpenHearthFurnace.centralisedRecipes,TTJEICategory.OPEN_HEARTH_FURNACE));
        recipesToRegister.add(new Tuple<>(TileEntityMagneticSeparator.recipes,TTJEICategory.MAGNETIC_SEPARATOR));
        //recipesToRegister.add(new Tuple<>(TileEntityMemoryFormatter.recipes,TTJEICategory.MEMORY_FORMATTER));
        recipesToRegister.add(new Tuple<>(TileEntityOpenHearthFurnace.solidBurningRecipes,TTJEICategory.OPHF_SOLID));
        recipesToRegister.add(new Tuple<>(TileEntityOpenHearthFurnace.liquidBurningRecipes,TTJEICategory.OPHF_LIQUID));
        recipesToRegister.add(new Tuple<>(TileEntityOpenHearthFurnace.sitRecipes,TTJEICategory.OPHF_SIT));
        recipesToRegister.add(new Tuple<>(TileEntityClayOven.recipes,TTJEICategory.CLAY_OVEN));
        recipesToRegister.add(new Tuple<>(TileEntitySmallCoalBoiler.burnRecipes,TTJEICategory.SMALL_COAL_FUEL));
        recipesToRegister.add(new Tuple<>(new ArrayList<SimplifiedMultiblockRecipe>(){{add(TileEntitySmallCoalBoiler.waterBurnRecipe);add(TileEntitySmallCoalBoiler.waterBurnRecipe2);add(TileEntitySmallCoalBoiler.waterBurnRecipe3);add(TileEntitySmallCoalBoiler.waterBurnRecipe4);}},TTJEICategory.SMALL_COAL_BOILER));
        recipesToRegister.add(new Tuple<>(TileEntityLathe.recipes.stream().map(i->(SimplifiedMultiblockRecipe)i).collect(Collectors.toList()), TTJEICategory.LATHE));
        recipesToRegister.add(new Tuple<>(TileEntityMetalRoller.recipes,TTJEICategory.METAL_ROLLER));
        recipesToRegister.add(new Tuple<>(TileEntitySiliconCrucible.recipes,TTJEICategory.SILICON_CRUCIBLE));
        recipesToRegister.add(new Tuple<>(TileEntityCokerUnit.recipesJoined,TTJEICategory.COKER_UNIT));
        recipesToRegister.add(new Tuple<>(TileEntityCokeOvenBattery.cokeRecipes,TTJEICategory.COKE_OVEN_BATTERY));
        recipesToRegister.add(new Tuple<>(TileEntityElectricFoodOven.recipes,TTJEICategory.ELECTRIC_FOOD_OVEN));
        recipesToRegister.add(new Tuple<>(TileEntityMagnetizer.recipes,TTJEICategory.MAGNETIZER));
        recipesToRegister.add(new Tuple<>(TileEntityCokeOvenBattery.coalBurnRecipes,TTJEICategory.COKE_OVEN_BATTERY_SOLID));
        recipesToRegister.add(new Tuple<>(TileEntityCokeOvenBattery.liquidBurnRecipes,TTJEICategory.COKE_OVEN_BATTERY_LIQUID));
        recipesToRegister.add(new Tuple<>(TileEntitySteamEngine.recipes,TTJEICategory.STEAM_ENGINE));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registerRecipes();
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();

        coalBoilerBurnCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1, TTBlockTypes_MetalMultiblock.COAL_BOILER.getMeta()),
                "tiagthings:textures/gui/coal_boiler_burn.png",
                "Pulverised Coal Boiler Fuels",
                TTJEICategory.COAL_BOILER_BURN,
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,22,21)
                },
                new TTCategory.UniversalSlot[0], // item outputs
                new TTCategory.UniversalSlot[0], // fluid inputs
                new TTCategory.UniversalSlot[0] // fluid outputs
        );
        registry.addRecipeCategories(coalBoilerBurnCategory);

        oilBoilerBurnCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1, TTBlockTypes_MetalMultiblock.OIL_BOILER.getMeta()),
                "tiagthings:textures/gui/oil_boiler_burn.png",
                "Atomised Oil Boiler Fuels",
                TTJEICategory.OIL_BOILER_BURN,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,11,7)
                },
                new TTCategory.UniversalSlot[0]
        );
        registry.addRecipeCategories(oilBoilerBurnCategory);

        flareStackCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.FLARE_STACK.getMeta()),
                "tiagthings:textures/gui/flare_stack_burn.png",
                "Flare Stack",
                TTJEICategory.FLARE_STACK,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,6)
                },
                new TTCategory.UniversalSlot[0]
        );
        registry.addRecipeCategories(flareStackCategory);

        coalBoilerCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.COAL_BOILER.getMeta()),
                "tiagthings:textures/gui/fluid_exchange.png",
                "Pulverised Coal Boiler",
                TTJEICategory.COAL_BOILER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,35,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,89,6)
                },
                141,0,22,16,59,20, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(coalBoilerCategory);

        clarifierRecipe = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.CLARIFIER.getMeta()),
                "tiagthings:textures/gui/clarifier.png",
                "Clarifier",
                TTJEICategory.CLARIFIER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,106,6)
                },
                141,3,48,36,50,20, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(clarifierRecipe);

        waterFilterCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.WATER_FILTER.getMeta()),
                "tiagthings:textures/gui/water_filter_gui.png",
                "Water Filter",
                TTJEICategory.WATER_FILTER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,22,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,102,6)
                },
                146,3,48,23,46,16, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(waterFilterCategory);

        oilBoilerCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.OIL_BOILER.getMeta()),
                "tiagthings:textures/gui/fluid_exchange.png",
                "Atomised Oil Boiler",
                TTJEICategory.OIL_BOILER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,35,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,89,6)
                },
                141,0,22,16,59,20, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(oilBoilerCategory);

        umplmCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.UM_PHOTOLITHOGRAPHY_MACHINE.getMeta()),
                "tiagthings:textures/gui/litography.png",
                "um Lithography Machine",
                TTJEICategory.UM_PL_MACHINE,
                new TTCategory.UniversalSlot[] {
                    new TTCategory.UniversalSlot(0,36,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,107,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                143,2,46,34,57,2, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(umplmCategory);

        nmplmCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.NMPLM.getMeta()),
                "tiagthings:textures/gui/litography.png",
                "nm Lithography Machine",
                TTJEICategory.NM_PL_MACHINE,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,36,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,107,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                143,2,46,34,57,2, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(nmplmCategory);

        euvplmCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.EUVPLM.getMeta()),
                "tiagthings:textures/gui/litography.png",
                "EUV Lithography Machine",
                TTJEICategory.EUV_PL_MACHINE,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,36,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,107,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                143,2,46,34,57,2, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(euvplmCategory);

        gasCentrifugeCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.GAS_CENTRIFUGE.getMeta()),
                "tiagthings:textures/gui/gas_centrifuge_gui.png",
                "Gas Centrifuge",
                TTJEICategory.GAS_CENTRIFUGE,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,9,6)
                },
                new TTCategory.UniversalSlot[] {
                  new TTCategory.UniversalSlot(0,85,6),
                  new TTCategory.UniversalSlot(1,114,6)
                },
                141,0,85,34,28,0, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(gasCentrifugeCategory);

        steamRadiatorCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.STEAM_RADIATOR.getMeta()),
                "tiagthings:textures/gui/radiator_gui.png",
                "Steam Radiator",
                TTJEICategory.STEAM_RADIATOR,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,30,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,94,6)
                },
                141,0,39,22,51,18, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(steamRadiatorCategory);

        indoorAcCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.INDOOR_AC_UNIT.getMeta()),
                "tiagthings:textures/gui/radiator_gui.png",
                "Indoor AC unit",
                TTJEICategory.INDOOR_AC,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,30,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,94,6)
                },
                141,0,39,22,50,18, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(indoorAcCategory);

        outdoorAcCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.OUTDOOR_AC_UNIT.getMeta()),
                "tiagthings:textures/gui/outdoor_ac.png",
                "Outdoor AC unit",
                TTJEICategory.OUTDOOR_AC,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,30,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,94,6)
                },
                141,0,39,16,50,21, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(outdoorAcCategory);

        electricOvenCategory =  new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.ELECTRIC_OVEN.getMeta()),
                "tiagthings:textures/gui/electric_oven.png",
                "Electric Oven",
                TTJEICategory.ELECTRIC_OVEN,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,97,20)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                141,0,46,21,47,20, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(electricOvenCategory);

        tresherCategory =  new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_1,1, TTBlockTypes_MetalMultiblock_1.TRESHER.getMeta()),
                "tiagthings:textures/gui/tresher.png",
                "Tresher",
                TTJEICategory.TRESHER,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,97,20)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                141,0,46,17,47,19, IDrawableAnimated.StartDirection.LEFT
        );
        //registry.addRecipeCategories(tresherCategory);

        ccm = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.CCM.getMeta()),
                "tiagthings:textures/gui/ccm.png",
                "Continuous Casting Machine",
                TTJEICategory.CCM,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,107,21)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                143,2,80,20,25,20, IDrawableAnimated.StartDirection.LEFT


        );
        registry.addRecipeCategories(ccm);

        fbr = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.FBR.getMeta()),
                "tiagthings:textures/gui/fbr.png",
                "Fluidised Bed Reactor",
                TTJEICategory.FBR,
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,37,6)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,9,6)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,115,6)
                },
                142,1,80,50,30,1, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(fbr);

        shaftFurnace = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.SHAFT_FURNACE.getMeta()),
                "tiagthings:textures/gui/shaft_furnace.png",
                "Shaft Furnace",
                TTJEICategory.SHAFT_FURNACE,
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,33,6)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,107,6)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                141,1,80,51,26,0, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(shaftFurnace);

        openHearthFurnace = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE.getMeta()),
                "tiagthings:textures/gui/open_hearth_furnace.png",
                "Open Hearth Furnace",
                TTJEICategory.OPEN_HEARTH_FURNACE,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,10,6)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,114,6)
                },
                141,1,80,81,30,8, IDrawableAnimated.StartDirection.LEFT
        ){
            @Override
            public void setRecipe(IRecipeLayout iRecipeLayout, SimplifiedRecipeWrapper recipeWrapper, IIngredients iIngredients) {
                super.setRecipe(iRecipeLayout, recipeWrapper, iIngredients);
                IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
                IGuiFluidStackGroup guiFluidStackGroup = iRecipeLayout.getFluidStacks();
                guiFluidStackGroup.init(fluidSlotsOut.length + fluidSlotsIn.length, true, 40, 26, 59, 8, 1, false, null);
                guiFluidStackGroup.set(fluidSlotsOut.length + fluidSlotsIn.length, new ArrayList<FluidStack>() {{
                    add(new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1));
                }});
            }
        };
        //registry.addRecipeCategories(openHearthFurnace);

        magneticSeparator = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.MAGNETIC_SEPARATOR.getMeta()),
                "tiagthings:textures/gui/magnetic_separator.png",
                "Magnetic Separator",
                TTJEICategory.MAGNETIC_SEPARATOR,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,33,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,107,33)
                },
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0],
                142,1,97,38,26,13, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(magneticSeparator);

        memoryFormatter = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.MEMORY_FORMATTER.getMeta()),
                "tiagthings:textures/gui/memory_formatter.png",
                "Memory Formatter",
                TTJEICategory.MEMORY_FORMATTER,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,97,20)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                141,1,46,16,47,20, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(memoryFormatter);

        openHearthFurnaceFuelLiquid = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE.getMeta()),
                "tiagthings:textures/gui/oil_boiler_burn.png",
                "Open Hearth Furnace Fuel",
                TTJEICategory.OPHF_LIQUID,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,11,7)
                },
                new TTCategory.UniversalSlot[0]
        );
        registry.addRecipeCategories(openHearthFurnaceFuelLiquid);

        openHearthFurnaceFuelSolid = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE.getMeta()),
                "tiagthings:textures/gui/coal_boiler_burn.png",
                "Open Hearth Furnace Fuel",
                TTJEICategory.OPHF_SOLID,
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,22,21)
                },
                new TTCategory.UniversalSlot[0], // item outputs
                new TTCategory.UniversalSlot[0], // fluid inputs
                new TTCategory.UniversalSlot[0] // fluid outputs
        );
        registry.addRecipeCategories(openHearthFurnaceFuelSolid);

        openHearthFurnaceSit = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.OPEN_HEARTH_FURNACE.getMeta()),
                "tiagthings:textures/gui/open_hearth_furnace_sit.png",
                "Open Hearth Furnace Decarburization",
                TTJEICategory.OPHF_SIT,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,114,6)
                },
                179,34,43,16,68,41, IDrawableAnimated.StartDirection.LEFT
        ){
            @Override
            public void setRecipe(IRecipeLayout iRecipeLayout, SimplifiedRecipeWrapper recipeWrapper, IIngredients iIngredients) {
                super.setRecipe(iRecipeLayout, recipeWrapper, iIngredients);
                IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
                IGuiFluidStackGroup guiFluidStackGroup = iRecipeLayout.getFluidStacks();
                guiFluidStackGroup.init(fluidSlotsOut.length + fluidSlotsIn.length, true, 40, 26, 59, 8, 1, false, null);
                guiFluidStackGroup.set(fluidSlotsOut.length + fluidSlotsIn.length, new ArrayList<FluidStack>() {{
                    add(new FluidStack(recipeWrapper.fluidInputs.get(0).getFluid(),1));
                }});
            }
        };
        registry.addRecipeCategories(openHearthFurnaceSit);

        clayOven = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.CLAY_OVEN.getMeta()),
                "tiagthings:textures/gui/clay_oven.png",
                "Clay Oven",
                TTJEICategory.CLAY_OVEN,
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,26,28)
                },
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,108,28)
                },
                new TTCategory.UniversalSlot[0], // fluid inputs
                new TTCategory.UniversalSlot[0], // fluid outputs
                141,1,45,16,61,29, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(clayOven);

        smallCoalFuel = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.SMALL_COAL_BOILER.getMeta()),
                "tiagthings:textures/gui/small_coal_boiler_fuel.png",
                "Coal Boiler Fuel",
                TTJEICategory.SMALL_COAL_FUEL,
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,13,20)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0], // fluid inputs
                new TTCategory.UniversalSlot[0] // fluid outputs
        );
        registry.addRecipeCategories(smallCoalFuel);

        smallCoalBoiler = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.SMALL_COAL_BOILER.getMeta()),
                "tiagthings:textures/gui/small_coal_boiler.png",
                "Coal Boiler",
                TTJEICategory.SMALL_COAL_BOILER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,11,6)
                }, // fluid inputs
                new TTCategory.UniversalSlot[]{
                        new TTCategory.UniversalSlot(0,113,6)
                }, // fluid outputs
                141,1,78,28,31,8, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(smallCoalBoiler);

        lathe = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.LATHE.getMeta()),
                "tiagthings:textures/gui/lathe_gui_recp.png",
                "Lathe",
                TTJEICategory.LATHE,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,16,62),
                        new TTCategory.UniversalSlot(1,132,62)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,16,44)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                166,85
        );
        registry.addRecipeCategories(lathe);

        metal_roller = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.METAL_ROLLER.getMeta()),
                "tiagthings:textures/gui/metal_roller_recp.png",
                "Metal Roller",
                TTJEICategory.METAL_ROLLER,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,35,28)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,107,28)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                159,73,177,1,47,16,55,29, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(metal_roller);

        silicon_crucible = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_2,1, TTBlockTypes_MetalMultiblock_2.SILICON_CRUCIBLE.getMeta()),
                "tiagthings:textures/gui/silicon_crucible_recp.png",
                "Czochralski Crucible",
                TTJEICategory.SILICON_CRUCIBLE,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,37,17),
                        new TTCategory.UniversalSlot(1,37,39)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,112,28)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,11,13)
                },
                new TTCategory.UniversalSlot[0],
                159,73,234,3,21,55,60,10, IDrawableAnimated.StartDirection.TOP
        );
        registry.addRecipeCategories(silicon_crucible);

        cokerUnit = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.COKER_UNIT.getMeta()),
                "tiagthings:textures/gui/coker_unit_recp.png",
                "Delayed Coker Unit",
                TTJEICategory.COKER_UNIT,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,44,25)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,98,25)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,4,11),
                        new TTCategory.UniversalSlot(1,25,11)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,120,11)
                },
                140,60,
                9,62,120,53,10,3, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(cokerUnit);

        cokeOvenBattery = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.COKE_OVEN_BATTERY.getMeta()),
                "tiagthings:textures/gui/coke_oven_battery_recp.png",
                "Coke Oven Battery",
                TTJEICategory.COKE_OVEN_BATTERY,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(1,27,9)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,99,35)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                140,60,
                0,60,44,44,50,9, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(cokeOvenBattery);

        cokeOvenBatteryLiquid = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.COKE_OVEN_BATTERY.getMeta()),
                "tiagthings:textures/gui/oil_boiler_burn.png",
                "Coke Oven Battery Fuel",
                TTJEICategory.COKE_OVEN_BATTERY_LIQUID,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,11,7)
                },
                new TTCategory.UniversalSlot[0]
        );
        registry.addRecipeCategories(cokeOvenBatteryLiquid);

        cokeOvenBatterySolid = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.COKE_OVEN_BATTERY.getMeta()),
                "tiagthings:textures/gui/coal_boiler_burn.png",
                "Coke Oven Battery Fuel",
                TTJEICategory.COKE_OVEN_BATTERY_SOLID,
                new TTCategory.UniversalSlot[] { // item inputs
                        new TTCategory.UniversalSlot(0,22,21)
                },
                new TTCategory.UniversalSlot[0], // item outputs
                new TTCategory.UniversalSlot[0], // fluid inputs
                new TTCategory.UniversalSlot[0] // fluid outputs
        );
        registry.addRecipeCategories(cokeOvenBatterySolid);

        electricFoodOven = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.ELECTRIC_FOOD_OVEN.getMeta()),
                "tiagthings:textures/gui/electric_food_oven_recp.png",
                "Electric Baking Oven",
                TTJEICategory.ELECTRIC_FOOD_OVEN,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,26,20)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,97,20)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                140,60,
                1,62,47,19,46,17, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(electricFoodOven);

        magnetizer = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_4,1, TTBlockTypes_MetalMultiblock_4.MAGNETIZER.getMeta()),
                "tiagthings:textures/gui/magnetizer_gui_recp.png",
                "Magnetiser",
                TTJEICategory.MAGNETIZER,
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,25,22)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,98,22)
                },
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                140,60,
                144,4,47,4,48,28, IDrawableAnimated.StartDirection.LEFT
        );
        registry.addRecipeCategories(magnetizer);

        steamEngine = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock_3,1, TTBlockTypes_MetalMultiblock_3.STEAM_ENGINE.getMeta()),
                "tiagthings:textures/gui/steam_engine.png",
                "Steam Engine",
                TTJEICategory.STEAM_ENGINE,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,5,6)
                },
                new TTCategory.UniversalSlot[0]
        );
        registry.addRecipeCategories(steamEngine);

    }

    @Override
    public void register(IModRegistry registry) {

        for (Tuple<List<SimplifiedMultiblockRecipe>,TTJEICategory> recipesInCategory : recipesToRegister) {

            List<SimplifiedRecipeWrapper> wrappers = new ArrayList<>();

            for (SimplifiedMultiblockRecipe recipe : recipesInCategory.getFirst()) {
                SimplifiedRecipeWrapper wrapper;

                switch (recipesInCategory.getSecond()) {
                    case COAL_BOILER_BURN:
                    case OIL_BOILER_BURN:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Burns for " + (recipe.getTotalProcessTime() / 20.0f) + "s", 50, 10, 0xff000000);
                                minecraft.fontRenderer.drawString("Needs " + (int)(recipe.getTotalProcessEnergy()/recipe.totalProcessTime) + " RF/t", 50, 22, 0xff000000);
                            }
                        };
                        break;
                    case OPHF_LIQUID:
                    case OPHF_SOLID:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Burns for " + (recipe.getTotalProcessTime() / 20.0f) + "s", 50, 10, 0xff000000);
                            }
                        };
                        break;
                    case FLARE_STACK:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Burns in " + (recipe.getTotalProcessTime()/20.0f) + "s",50,10,0xff000000);
                            }
                        };
                        break;
                    case COAL_BOILER:
                    case OIL_BOILER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",61,12,0xff000000);
                            }
                        };
                        break;
                    case CLARIFIER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",51,6,0xff000000);
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",51,16,0xff000000);
                            }
                        };
                        break;
                    case WATER_FILTER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",47,6,0xff000000);
                            }
                        };
                        break;
                    case UM_PL_MACHINE:
                    case NM_PL_MACHINE:
                    case EUV_PL_MACHINE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",37,42,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",37 + minecraft.fontRenderer.getStringWidth((recipe.getTotalProcessTime()/20.0f) + "s") + 5,42,0xff000000);
                            }
                        };
                        break;
                    case GAS_CENTRIFUGE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",30,36,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",30,46,0xff000000);
                            }
                        };
                        break;
                    case STEAM_RADIATOR:
                    case INDOOR_AC:
                    case OUTDOOR_AC:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",51,6,0xff000000);
                            }
                        };
                        break;
                    case ELECTRIC_OVEN:
                    case TRESHER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",47,41,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",47,51,0xff000000);
                            }
                        };
                        break;
                    case CCM:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Mode: " + ((TileEntityCCM.CCMRecipe)recipe).ccmMode.getName(),47,6,0xff000000);
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",47,41,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",47,51,0xff000000);
                            }
                        };
                        break;
                    /*case MEMORY_FORMATTER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",47,41,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",47,51,0xff000000);
                                minecraft.fontRenderer.drawString("Resource utilization: " + TileEntityMemoryFormatter.getProcessingPowerForRecipe_static(recipe),6,6,0xff000000);
                            }
                        };
                        break;*/
                    case FBR:
                    case SHAFT_FURNACE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",30,35,0xff000000);
                            }
                        };
                        break;
                    case MAGNETIC_SEPARATOR:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",27,34,0xff000000);
                                minecraft.fontRenderer.drawString(">5",68,3,0xff000000);
                                minecraft.fontRenderer.drawString("20-40",100,3,0xff000000);
                                //minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",27,34,0xff000000);
                            }
                        };
                        break;
                    case OPHF_SIT:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Converts in 40s.",6,6,0xff000000);
                            }
                        };
                        break;
                    case CLAY_OVEN:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                int h = (int)((recipe.getTotalProcessTime())/(60*20));
                                minecraft.fontRenderer.drawString("Cooks in " + h + " hour" + (h != 1 ? "s." : "."),53,13,0xff000000);
                            }
                        };
                        break;
                    case SMALL_COAL_BOILER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",31,40,0xffffffff);
                            }
                        };
                        break;
                    case SMALL_COAL_FUEL:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Burns in " + (recipe.getTotalProcessTime()/20.0f) + "s",55,25,0xffffffff);
                            }
                        };
                        break;
                    case LATHE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                TileEntityLathe.LatheRecipe latheRecipe = (TileEntityLathe.LatheRecipe)recipe;
                                GlStateManager.pushMatrix();
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                                ClientUtils.bindTexture("tiagthings:textures/gui/lathe_gui.png");
                                int guiLeft = -5;
                                int guiTop = -5;
                                Gui.drawScaledCustomSizeModalRect(guiLeft+53,guiTop+68,(129+30*0),224,30,30,16,16,256,256);
                                Gui.drawScaledCustomSizeModalRect(guiLeft+71,guiTop+68,(129+30*1),224,30,30,16,16,256,256);
                                Gui.drawScaledCustomSizeModalRect(guiLeft+89,guiTop+68,(129+30*2),224,30,30,16,16,256,256);
                                Gui.drawScaledCustomSizeModalRect(guiLeft+107,guiTop+68,(129+30*3),224,30,30,16,16,256,256);
                                for (int i = 0; i < 3; i++) {
                                    LatheAction action = latheRecipe.actions[i];
                                    LatheActionFlag actionFlag = latheRecipe.actionFlags[i];
                                    Gui.drawScaledCustomSizeModalRect(guiLeft + 64 + i * 19, guiTop + 10, (129 + 30 * action.ordinal()), 224, 32, 32, 10, 10, 256, 256);
                                    GlStateManager.color(0, 0.6f, 0.2f);
                                    drawTexturedModalRect(guiLeft + 59 + i * 19, guiTop + 7, 198, 22 * actionFlag.ordinal(), 20, 22);
                                    GlStateManager.color(1, 1, 1, 1);

                                }

                                for (int i = 0; i < 3; i++) {
                                    Gui.drawScaledCustomSizeModalRect(guiLeft + 64 + i * 19, guiTop + 31, (129 + 30 * latheRecipe.actions[i].ordinal()), 224, 32, 32, 10, 10, 256, 256);
                                }
                                minecraft.fontRenderer.drawString(">10",61,47,0xff000000);
                                minecraft.fontRenderer.drawString("200-300",93,47,0xff000000);
                                GlStateManager.popMatrix();
                            }
                        };
                        break;
                    case METAL_ROLLER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                ClientUtils.bindTexture("tiagthings:textures/gui/metal_roller_gui.png");
                                int guiLeft = -8;
                                int guiTop = -6;
                                Gui.drawScaledCustomSizeModalRect(guiLeft + 70,guiTop + 58,(142+30*0),212,30,30,16,16,256,256);
                                Gui.drawScaledCustomSizeModalRect(guiLeft + 89,guiTop + 58,(142+30*1),212,30,30,16,16,256,256);
                                minecraft.fontRenderer.drawString(">20",18,50,0xff000000);
                                minecraft.fontRenderer.drawString("80-100",18,62,0xff000000);
                            }
                        };
                        break;
                    case SILICON_CRUCIBLE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",91,51,0xff000000);
                                minecraft.fontRenderer.drawString((int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",91,63,0xff000000);
                            }
                        };
                        break;
                    case COKER_UNIT:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",44,47,0xff000000);
                            }
                        };
                        break;
                    case COKE_OVEN_BATTERY:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s",99,9,0xff000000);
                            }
                        };
                        break;
                    case COKE_OVEN_BATTERY_SOLID:
                    case COKE_OVEN_BATTERY_LIQUID:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("Burns for " + (recipe.getTotalProcessTime() / 20.0f) + "s", 50, 10, 0xff000000);
                            }
                        };
                        break;
                    case ELECTRIC_FOOD_OVEN:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s  " + (int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",26,44,0xff000000);
                            }
                        };
                        break;
                    case MAGNETIZER:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString((recipe.getTotalProcessTime()/20.0f) + "s  " + (int)(recipe.totalProcessEnergy/(float)recipe.getTotalProcessTime()) + " RF/t",26,44,0xff000000);
                            }
                        };
                        break;
                    case STEAM_ENGINE:
                        wrapper = new SimplifiedRecipeWrapper(recipe) {
                            @Override
                            public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
                                minecraft.fontRenderer.drawString("/"+(recipe.getTotalProcessTime()/20.0f) + "s",25,24,0xff000000);
                                minecraft.fontRenderer.drawString("90 Nm",83,21,0xff000000);
                                minecraft.fontRenderer.drawString("60 RPM",83,33,0xff000000);
                            }
                        };
                        break;
                    default:
                        wrapper = new SimplifiedRecipeWrapper(recipe);
                }

                wrappers.add(wrapper);
            }
            registry.addRecipes(wrappers,"tiagthings." + recipesInCategory.getSecond().getName());
        }

        registry.addRecipeCategoryCraftingItem(coalBoilerBurnCategory.catalyst, coalBoilerBurnCategory.getUid());
        registry.addRecipeCategoryCraftingItem(oilBoilerBurnCategory.catalyst, oilBoilerBurnCategory.getUid());
        registry.addRecipeCategoryCraftingItem(flareStackCategory.catalyst, flareStackCategory.getUid());
        registry.addRecipeCategoryCraftingItem(coalBoilerCategory.catalyst, coalBoilerCategory.getUid());
        registry.addRecipeCategoryCraftingItem(clarifierRecipe.catalyst, clarifierRecipe.getUid());
        registry.addRecipeCategoryCraftingItem(waterFilterCategory.catalyst, waterFilterCategory.getUid());
        registry.addRecipeCategoryCraftingItem(oilBoilerCategory.catalyst,oilBoilerCategory.getUid());
        registry.addRecipeCategoryCraftingItem(umplmCategory.catalyst, umplmCategory.getUid());
        registry.addRecipeCategoryCraftingItem(nmplmCategory.catalyst, nmplmCategory.getUid());
        registry.addRecipeCategoryCraftingItem(euvplmCategory.catalyst, euvplmCategory.getUid());
        registry.addRecipeCategoryCraftingItem(gasCentrifugeCategory.catalyst,gasCentrifugeCategory.getUid());
        registry.addRecipeCategoryCraftingItem(steamRadiatorCategory.catalyst,steamRadiatorCategory.getUid());
        registry.addRecipeCategoryCraftingItem(indoorAcCategory.catalyst,indoorAcCategory.getUid());
        registry.addRecipeCategoryCraftingItem(outdoorAcCategory.catalyst,outdoorAcCategory.getUid());
        registry.addRecipeCategoryCraftingItem(electricOvenCategory.catalyst, electricOvenCategory.getUid());
        registry.addRecipeCategoryCraftingItem(tresherCategory.catalyst, tresherCategory.getUid());
        registry.addRecipeCategoryCraftingItem(ccm.catalyst, ccm.getUid());
        registry.addRecipeCategoryCraftingItem(fbr.catalyst, fbr.getUid());
        registry.addRecipeCategoryCraftingItem(shaftFurnace.catalyst,shaftFurnace.getUid());
        registry.addRecipeCategoryCraftingItem(openHearthFurnace.catalyst,openHearthFurnace.getUid());
        registry.addRecipeCategoryCraftingItem(magneticSeparator.catalyst,magneticSeparator.getUid());
        registry.addRecipeCategoryCraftingItem(memoryFormatter.catalyst,memoryFormatter.getUid());
        registry.addRecipeCategoryCraftingItem(openHearthFurnaceFuelSolid.catalyst,openHearthFurnaceFuelSolid.getUid());
        registry.addRecipeCategoryCraftingItem(openHearthFurnaceFuelLiquid.catalyst,openHearthFurnaceFuelLiquid.getUid());
        registry.addRecipeCategoryCraftingItem(openHearthFurnaceSit.catalyst,openHearthFurnaceSit.getUid());
        registry.addRecipeCategoryCraftingItem(clayOven.catalyst,clayOven.getUid());
        registry.addRecipeCategoryCraftingItem(smallCoalBoiler.catalyst, smallCoalBoiler.getUid());
        registry.addRecipeCategoryCraftingItem(smallCoalFuel.catalyst,smallCoalFuel.getUid());
        registry.addRecipeCategoryCraftingItem(lathe.catalyst, lathe.getUid());
        registry.addRecipeCategoryCraftingItem(metal_roller.catalyst, metal_roller.getUid());
        registry.addRecipeCategoryCraftingItem(silicon_crucible.catalyst,silicon_crucible.getUid());
        registry.addRecipeCategoryCraftingItem(cokerUnit.catalyst,cokerUnit.getUid());
        registry.addRecipeCategoryCraftingItem(cokeOvenBattery.catalyst, cokeOvenBattery.getUid());
        registry.addRecipeCategoryCraftingItem(cokeOvenBattery.catalyst, cokeOvenBatterySolid.getUid());
        registry.addRecipeCategoryCraftingItem(cokeOvenBattery.catalyst, cokeOvenBatteryLiquid.getUid());
        registry.addRecipeCategoryCraftingItem(electricFoodOven.catalyst, electricFoodOven.getUid());
        registry.addRecipeCategoryCraftingItem(magnetizer.catalyst, magnetizer.getUid());
        registry.addRecipeCategoryCraftingItem(steamEngine.catalyst,steamEngine.getUid());
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)0).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)0).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)0).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)0).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

}
