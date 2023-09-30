package BananaFructa.TTIEMultiblocks.Compat.jei;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_1;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import nc.multiblock.qComputer.QuantumGate;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

import javax.swing.tree.TreeCellEditor;
import java.util.ArrayList;
import java.util.List;

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
                "Coal Boiler Fuels",
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
                "Oil Boiler Fuels",
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
                "Coal Boiler",
                TTJEICategory.COAL_BOILER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,35,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,89,6)
                },
                141,0,22,16,59,20
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
                141,3,48,36,50,20
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
                146,3,48,23,46,16
        );
        registry.addRecipeCategories(waterFilterCategory);

        oilBoilerCategory = new TTCategory(
                guiHelper,
                new ItemStack(TTIEContent.ttBlockMetalMultiblock,1,TTBlockTypes_MetalMultiblock.OIL_BOILER.getMeta()),
                "tiagthings:textures/gui/fluid_exchange.png",
                "Oil Boiler",
                TTJEICategory.OIL_BOILER,
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[0],
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,35,6)
                },
                new TTCategory.UniversalSlot[] {
                        new TTCategory.UniversalSlot(0,89,6)
                },
                141,0,22,16,59,20
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
                143,2,46,34,57,2
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
                143,2,46,34,57,2
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
                143,2,46,34,57,2
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
                141,0,85,34,28,0
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
                141,0,39,22,51,18
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
                141,0,39,22,50,18
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
                141,0,39,16,50,21
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
                141,0,46,21,47,20
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
                141,0,46,17,47,19
        );
        registry.addRecipeCategories(tresherCategory);
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
    }
}
