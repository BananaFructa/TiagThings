package BananaFructa.ImmersiveEngineering;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.IConfigurableTool;
import blusunrize.immersiveengineering.api.tool.IConfigurableTool.ToolConfig.ToolConfigBoolean;
import blusunrize.immersiveengineering.api.tool.IConfigurableTool.ToolConfig.ToolConfigFloat;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonCheckbox;
import blusunrize.immersiveengineering.client.gui.elements.GuiSliderIE;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityModWorkbench;
import blusunrize.immersiveengineering.common.gui.ContainerModWorkbench;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;

public class ModifiedGuiModWorkbench extends GuiIEContainerBase
{
    TileEntityModWorkbench workbench;
    NBTTagCompound lastMessage;

    public ModifiedGuiModWorkbench(InventoryPlayer inventoryPlayer, World world, TileEntityModWorkbench tile) {
        super(new ModifiedContainerModWorkbench(inventoryPlayer, world, tile));
        this.workbench = tile;
        this.ySize = 168;
    }

    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        Slot s = this.inventorySlots.getSlot(0);
        if (s != null && s.getHasStack() && s.getStack().getItem() instanceof IConfigurableTool) {
            ItemStack stack = s.getStack();
            IConfigurableTool tool = (IConfigurableTool)stack.getItem();
            int buttonid = 0;
            IConfigurableTool.ToolConfig.ToolConfigBoolean[] boolArray = tool.getBooleanOptions(stack);
            int var8;
            if (boolArray != null) {
                IConfigurableTool.ToolConfig.ToolConfigBoolean[] var6 = boolArray;
                int var7 = boolArray.length;

                for(var8 = 0; var8 < var7; ++var8) {
                    IConfigurableTool.ToolConfig.ToolConfigBoolean b = var6[var8];
                    this.buttonList.add(new GuiButtonCheckbox(buttonid++, this.guiLeft + b.x, this.guiTop + b.y, tool.fomatConfigName(stack, b), b.value));
                }
            }

            IConfigurableTool.ToolConfig.ToolConfigFloat[] floatArray = tool.getFloatOptions(stack);
            if (floatArray != null) {
                IConfigurableTool.ToolConfig.ToolConfigFloat[] var12 = floatArray;
                var8 = floatArray.length;

                for(int var13 = 0; var13 < var8; ++var13) {
                    IConfigurableTool.ToolConfig.ToolConfigFloat f = var12[var13];
                    this.buttonList.add(new GuiSliderIE(buttonid++, this.guiLeft + f.x, this.guiTop + f.y, 80, tool.fomatConfigName(stack, f), f.value));
                }
            }
        }

    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Slot s = this.inventorySlots.getSlot(0);
        if (s != null && s.getHasStack() && s.getStack().getItem() instanceof IConfigurableTool) {
            ItemStack stack = s.getStack();
            IConfigurableTool tool = (IConfigurableTool)stack.getItem();
            NBTTagCompound message = new NBTTagCompound();
            IConfigurableTool.ToolConfig.ToolConfigBoolean[] boolArray = tool.getBooleanOptions(stack);
            int iBool = 0;
            IConfigurableTool.ToolConfig.ToolConfigFloat[] floatArray = tool.getFloatOptions(stack);
            int iFloat = 0;
            Iterator var12 = this.buttonList.iterator();

            while(var12.hasNext()) {
                GuiButton button = (GuiButton)var12.next();
                if (button instanceof GuiButtonCheckbox && boolArray != null) {
                    message.setBoolean("b_" + boolArray[iBool++].name, ((GuiButtonCheckbox)button).state);
                }

                if (button instanceof GuiSliderIE && floatArray != null) {
                    message.setFloat("f_" + floatArray[iFloat++].name, (float)((GuiSliderIE)button).sliderValue);
                }
            }

            if (!message.equals(this.lastMessage)) {
                ImmersiveEngineering.packetHandler.sendToServer(new MessageTileSync(this.workbench, message));
            }

            this.lastMessage = message;
        }

    }

    public void func_73863_a(int mx, int my, float partial) {
        super.func_73863_a(mx, my, partial);

        for(int i = 0; i < ((ContainerModWorkbench)this.inventorySlots).slotCount; ++i) {
            Slot s = this.inventorySlots.getSlot(i);
            if (s instanceof IESlot.BlueprintOutput && !s.getHasStack()) {
                BlueprintCraftingRecipe recipe = ((IESlot.BlueprintOutput)s).recipe;
                if (recipe != null && !recipe.output.isEmpty() && this.isPointInRegion(s.xPos, s.yPos, 16, 16, mx, my)) {
                    ArrayList<String> tooltip = new ArrayList();
                    //tooltip.add(recipe.output.getRarity().getColor() + recipe.output.getDisplayName());
                    tooltip.addAll(recipe.output.getTooltip(mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL));
                    ArrayList<ItemStack> inputs = new ArrayList();
                    IngredientStack[] var9 = recipe.inputs;
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        IngredientStack stack = var9[var11];
                        ItemStack toAdd = Utils.copyStackWithAmount(stack.getRandomizedExampleStack((long)this.mc.player.ticksExisted), stack.inputSize);
                        if (!toAdd.isEmpty()) {
                            boolean isNew = true;
                            Iterator var15 = inputs.iterator();

                            while(var15.hasNext()) {
                                ItemStack ss = (ItemStack)var15.next();
                                if (OreDictionary.itemMatches(ss, toAdd, true)) {
                                    ss.grow(toAdd.getCount());
                                    isNew = false;
                                    break;
                                }
                            }

                            if (isNew) {
                                inputs.add(toAdd.copy());
                            }
                        }
                    }

                    Iterator var17 = inputs.iterator();
                    tooltip.add("");
                    while(var17.hasNext()) {
                        ItemStack ss = (ItemStack)var17.next();
                        tooltip.add(TextFormatting.GRAY.toString() + ss.getCount() + "x " + ss.getDisplayName());
                    }

                    ClientUtils.drawHoveringText(tooltip, mx, my, this.fontRenderer);
                    RenderHelper.enableGUIStandardItemLighting();
                }
            }
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("immersiveengineering:textures/gui/workbench.png");
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int i;
        Slot s;
        for(i = 0; i < ((ContainerModWorkbench)this.inventorySlots).slotCount; ++i) {
            s = this.inventorySlots.getSlot(i);
            ClientUtils.drawColouredRect(this.guiLeft + s.xPos - 1, this.guiTop + s.yPos - 1, 17, 1, 1998725666);
            ClientUtils.drawColouredRect(this.guiLeft + s.xPos - 1, this.guiTop + s.yPos + 0, 1, 16, 1998725666);
            ClientUtils.drawColouredRect(this.guiLeft + s.xPos + 16, this.guiTop + s.yPos + 0, 1, 17, 2006555033);
            ClientUtils.drawColouredRect(this.guiLeft + s.xPos + 0, this.guiTop + s.yPos + 16, 16, 1, 2006555033);
            ClientUtils.drawColouredRect(this.guiLeft + s.xPos + 0, this.guiTop + s.yPos + 0, 16, 16, 2000962628);
        }

        for(i = 0; i < ((ContainerModWorkbench)this.inventorySlots).slotCount; ++i) {
            s = this.inventorySlots.getSlot(i);
            if (s instanceof IESlot.BlueprintOutput && !s.getHasStack()) {
                ItemStack ghostStack = ((IESlot.BlueprintOutput)s).recipe.output;
                if (!ghostStack.isEmpty()) {
                    this.zLevel = 200.0F;
                    this.itemRender.zLevel = 200.0F;
                    FontRenderer font = ghostStack.getItem().getFontRenderer(ghostStack);
                    if (font == null) {
                        font = this.fontRenderer;
                    }

                    this.itemRender.renderItemAndEffectIntoGUI(ghostStack, this.guiLeft + s.xPos, this.guiTop + s.yPos);
                    this.zLevel = 0.0F;
                    this.itemRender.zLevel = 0.0F;
                    GlStateManager.depthFunc(516);
                    ClientUtils.drawColouredRect(this.guiLeft + s.xPos + 0, this.guiTop + s.yPos + 0, 16, 16, -1154272461);
                    GlStateManager.depthFunc(515);
                }
            }
        }

    }
}
