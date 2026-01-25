package BananaFructa.TTIEMultiblocks.Compat.jei;

import BananaFructa.TiagThings.TTMain;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class TTCategory implements IRecipeCategory<SimplifiedRecipeWrapper> {

    private IDrawable background;
    private IDrawable arrow;
    private IDrawable tankOverlay;
    private TTJEICategory categoryKind;
    public ItemStack catalyst;

    public String title;
    UniversalSlot[] itemSlotsIn,itemSlotsOut;
    UniversalSlot[] fluidSlotsIn,fluidSlotsOut;
    int arrowPosX,arrowPosY;

    public TTCategory(IGuiHelper helper, ItemStack catalyst, String backgroundPath, String title, TTJEICategory categoryKind, UniversalSlot[] itemSlotsIn, UniversalSlot[] itemSlotsOut, UniversalSlot[] fluidSlotsIn, UniversalSlot[] fluidSlotsOut) {
        background = helper.createDrawable(new ResourceLocation(backgroundPath),0,0,140,60);
        tankOverlay = helper.createDrawable(new ResourceLocation("tiagthings:textures/gui/tank_overlay.png"),0,0,20,51,-2,2,-2,2);
        this.categoryKind = categoryKind;
        this.catalyst = catalyst;
        this.itemSlotsIn = itemSlotsIn;
        this.fluidSlotsIn = fluidSlotsIn;
        this.itemSlotsOut = itemSlotsOut;
        this.fluidSlotsOut = fluidSlotsOut;
        this.title = title;
    }

    public TTCategory(IGuiHelper helper, ItemStack catalyst, String backgroundPath, String title, TTJEICategory categoryKind, UniversalSlot[] itemSlotsIn, UniversalSlot[] itemSlotsOut, UniversalSlot[] fluidSlotsIn, UniversalSlot[] fluidSlotsOut,int xSize,int ySize) {
        background = helper.createDrawable(new ResourceLocation(backgroundPath),0,0,xSize,ySize);
        tankOverlay = helper.createDrawable(new ResourceLocation("tiagthings:textures/gui/tank_overlay.png"),0,0,20,51,-2,2,-2,2);
        this.categoryKind = categoryKind;
        this.catalyst = catalyst;
        this.itemSlotsIn = itemSlotsIn;
        this.fluidSlotsIn = fluidSlotsIn;
        this.itemSlotsOut = itemSlotsOut;
        this.fluidSlotsOut = fluidSlotsOut;
        this.title = title;
    }

    public TTCategory(IGuiHelper helper, ItemStack catalyst, String backgroundPath, String title, TTJEICategory categoryKind, UniversalSlot[] itemSlotsIn, UniversalSlot[] itemSlotsOut, UniversalSlot[] fluidSlotsIn, UniversalSlot[] fluidSlotsOut,int arrowX,int arrowY,int arrowW,int arrowH,int arrowPosx,int arrowPosY, IDrawableAnimated.StartDirection direction) {
        background = helper.createDrawable(new ResourceLocation(backgroundPath),0,0,140,60);
        tankOverlay = helper.createDrawable(new ResourceLocation("tiagthings:textures/gui/tank_overlay.png"),0,0,20,51,-2,2,-2,2);
        this.categoryKind = categoryKind;
        this.catalyst = catalyst;
        this.itemSlotsIn = itemSlotsIn;
        this.fluidSlotsIn = fluidSlotsIn;
        this.title = title;
        this.arrow = helper.drawableBuilder(new ResourceLocation(backgroundPath),arrowX,arrowY,arrowW,arrowH).buildAnimated(200, direction,false);
        this.arrowPosX = arrowPosx;
        this.arrowPosY = arrowPosY;
        this.itemSlotsOut = itemSlotsOut;
        this.fluidSlotsOut = fluidSlotsOut;
    }

    public TTCategory(IGuiHelper helper, ItemStack catalyst, String backgroundPath, String title, TTJEICategory categoryKind, UniversalSlot[] itemSlotsIn, UniversalSlot[] itemSlotsOut, UniversalSlot[] fluidSlotsIn, UniversalSlot[] fluidSlotsOut, int xSize, int ySize, int arrowX, int arrowY, int arrowW, int arrowH, int arrowPosx, int arrowPosY, IDrawableAnimated.StartDirection direction) {
        background = helper.createDrawable(new ResourceLocation(backgroundPath),0,0,xSize,ySize);
        tankOverlay = helper.createDrawable(new ResourceLocation("tiagthings:textures/gui/tank_overlay.png"),0,0,20,51,-2,2,-2,2);
        this.categoryKind = categoryKind;
        this.catalyst = catalyst;
        this.itemSlotsIn = itemSlotsIn;
        this.fluidSlotsIn = fluidSlotsIn;
        this.title = title;
        this.arrow = helper.drawableBuilder(new ResourceLocation(backgroundPath),arrowX,arrowY,arrowW,arrowH).buildAnimated(200, direction,false);
        this.arrowPosX = arrowPosx;
        this.arrowPosY = arrowPosY;
        this.itemSlotsOut = itemSlotsOut;
        this.fluidSlotsOut = fluidSlotsOut;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        if (arrow != null) {
            arrow.draw(minecraft,arrowPosX,arrowPosY);
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getModName() {
        return TTMain.modId;
    }

    @Override
    public String getUid() {
        return "tiagthings."+categoryKind.getName();
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, SimplifiedRecipeWrapper recipeWrapper, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStackGroup = iRecipeLayout.getItemStacks();
        IGuiFluidStackGroup guiFluidStackGroup = iRecipeLayout.getFluidStacks();
        for (int i = 0; i < itemSlotsIn.length; i++) {
            UniversalSlot slot = itemSlotsIn[i];
            guiItemStackGroup.init(i, true, slot.x, slot.y);
            if (iIngredients.getInputs(ItemStack.class).size() > slot.ingredient)
                guiItemStackGroup.set(i, iIngredients.getInputs(ItemStack.class).get(slot.ingredient));
        }

        for (int i = 0; i < itemSlotsOut.length; i++) {
            UniversalSlot slot = itemSlotsOut[i];
            guiItemStackGroup.init(i + itemSlotsIn.length, true, slot.x, slot.y);
            if (iIngredients.getOutputs(ItemStack.class).size() > slot.ingredient)
                guiItemStackGroup.set(i+ itemSlotsIn.length, iIngredients.getOutputs(ItemStack.class).get(slot.ingredient));
        }

        int base = 0;

        for (int i = 0; i < fluidSlotsIn.length; i++) {
            UniversalSlot slot = fluidSlotsIn[i];
            if (iIngredients.getInputs(FluidStack.class).size() > slot.ingredient) {
                FluidStack stack = iIngredients.getInputs(FluidStack.class).get(slot.ingredient).get(0);
                if (stack != null) {
                    if (base < stack.amount) base = stack.amount;
                }
            }
        }

        for (int i = 0; i < fluidSlotsOut.length; i++) {
            UniversalSlot slot = fluidSlotsOut[i];
            if (iIngredients.getOutputs(FluidStack.class).size() > slot.ingredient) {
                FluidStack stack = iIngredients.getOutputs(FluidStack.class).get(slot.ingredient).get(0);
                if (stack != null) {
                    if (base < stack.amount) base = stack.amount;
                }
            }
        }

        for (int i = 0; i < fluidSlotsIn.length; i++) {
            UniversalSlot slot = fluidSlotsIn[i];
            guiFluidStackGroup.init(i, true, slot.x, slot.y, 16, 47, base, false, tankOverlay);
            if (iIngredients.getInputs(FluidStack.class).size() > slot.ingredient) {
                guiFluidStackGroup.set(i, iIngredients.getInputs(FluidStack.class).get(slot.ingredient));
            }
        }

        for (int i = 0; i < fluidSlotsOut.length; i++) {
            UniversalSlot slot = fluidSlotsOut[i];
            guiFluidStackGroup.init(i + fluidSlotsIn.length, true, slot.x, slot.y, 16, 47, base, false, tankOverlay);
            if (iIngredients.getOutputs(FluidStack.class).size() > slot.ingredient)
                guiFluidStackGroup.set(i+ fluidSlotsIn.length, iIngredients.getOutputs(FluidStack.class).get(slot.ingredient));
        }


    }

    public static class UniversalSlot {
        public int ingredient;
        public int x;
        public int y;
        public UniversalSlot(int ingredient,int x,int y) {
            this.ingredient = ingredient;
            this.x = x;
            this.y = y;
        }
    }
}
