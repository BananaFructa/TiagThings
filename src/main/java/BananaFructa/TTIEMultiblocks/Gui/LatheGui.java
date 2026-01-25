package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class LatheGui extends GuiIEContainerBase {

    TileEntityLathe tile;
    LatheAction[] displayedLatheActions;
    LatheActionFlag[] displayedLatheActionFlags;


    @Override
    public void initGui() {
        super.initGui();
        addButton(new ButtonActionLathe(0,guiLeft+53,guiTop+68,16,16,"Turn",LatheAction.TURN));
        addButton(new ButtonActionLathe(1,guiLeft+71,guiTop+68,16,16,"Groove",LatheAction.GROOVE));
        addButton(new ButtonActionLathe(2,guiLeft+89,guiTop+68,16,16,"Drill",LatheAction.DRILL));
        addButton(new ButtonActionLathe(3,guiLeft+107,guiTop+68,16,16,"Thread",LatheAction.THREAD));
        addButton(new ButtonPlannerLathe(4,guiLeft+21,guiTop+40,18,18,"Plans",tile.inventoryHandlers.get(0)));
        addButton(new ButtonShowJEI(5,guiLeft+26,guiTop+24,9,14,"Show Recipes"));
    }

    public LatheGui(InventoryPlayer inventoryPlayer, TileEntityLathe tile) {
        super(new ContainerLathe(inventoryPlayer,tile));
        this.tile = tile;
        ySize = 192;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButton button : this.buttonList) {
            boolean hovered = mouseX >= button.x && mouseY >= button.y && mouseX < button.x + button.width && mouseY < button.y + button.height;
            if (hovered) {
                tooltip.add(button.displayString);
            }
        }
        if (hovered(mouseX,mouseY,guiLeft+53,guiTop+50,34,15)) {
            tooltip.add("Input torque: " + String.format("%.1f", tile.inTorque) + "Nm.");
            tooltip.add(TextFormatting.AQUA + "Minimum torque is 10Nm.");
        }
        if (hovered(mouseX,mouseY,guiLeft+89,guiTop+50,34,15)) {
            tooltip.add("Input speed: " + String.format("%.1f", tile.inSpeed) + "RPM.");
            tooltip.add(TextFormatting.AQUA + "Minimum speed is 200RPM and the maximum speed is 300RPM.");
        }
        if (displayedLatheActions != null && displayedLatheActionFlags != null) {
            for (int i = 0;i < 3;i++) {
                if (hovered(mouseX,mouseY,guiLeft+61+19*i,guiTop+7,16,16)) tooltip.add(displayedLatheActions[i].name + " " + displayedLatheActionFlags[i].name);
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inTorque),guiLeft+66,guiTop+53,tile.inTorque >= 10 ? 0xff339900 : 0xffff6600);
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inSpeed),guiLeft+98,guiTop+53,tile.inSpeed >= 200 && tile.inSpeed < 300 ? 0xff339900 : 0xffff6600);
        RenderHelper.enableGUIStandardItemLighting();
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
    }

    private boolean hovered (int mx, int my, int x, int y, int w, int h) {
        return mx >= x && my >= y && mx < x + w && my < y + h;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        boolean displayeSet = false;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/lathe_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        ItemStack is = tile.inventoryHandlers.get(0).getStackInSlot(0);
        if (!is.isEmpty()) {
            NBTTagCompound tagCompound = is.getTagCompound();
            if (tagCompound != null) {
                if (tagCompound.hasKey("lathe")) {
                    NBTTagCompound latheC = tagCompound.getCompoundTag("lathe");
                    if (latheC.hasKey("recipe") && latheC.hasKey("actions") && latheC.hasKey("progress")) {
                        int[] actions = latheC.getIntArray("actions");
                        int r = latheC.getInteger("recipe");
                        if (TileEntityLathe.recipes.size() > r) {
                            TileEntityLathe.LatheRecipe latheRecipe = TileEntityLathe.recipes.get(r);
                            displayeSet = true;
                            displayedLatheActions = latheRecipe.actions;
                            displayedLatheActionFlags = latheRecipe.actionFlags;
                            for (int i = 0; i < 3; i++) {
                                LatheAction action = latheRecipe.actions[i];
                                LatheActionFlag actionFlag = latheRecipe.actionFlags[i];
                                drawScaledCustomSizeModalRect(guiLeft + 64 + i * 19, guiTop + 10, (129 + 30 * action.ordinal()), 224, 32, 32, 10, 10, 256, 256);
                                if (matchesAction(action.ordinal(), actions, actionFlag)) {
                                    GlStateManager.color(0, 0.6f, 0.2f);
                                } else {
                                    GlStateManager.color(1, 0.4f, 0);
                                }
                                drawTexturedModalRect(guiLeft + 59 + i * 19, guiTop + 7, 198, 22 * actionFlag.ordinal(), 20, 22);
                                GlStateManager.color(1, 1, 1, 1);

                            }

                            if (actions.length == 3) {
                                for (int i = 0; i < 3; i++) {
                                    if (actions[i] != -1) {
                                        drawScaledCustomSizeModalRect(guiLeft + 64 + i * 19, guiTop + 31, (129 + 30 * actions[i]), 224, 32, 32, 10, 10, 256, 256);
                                    }
                                }
                            }
                            int progress = latheC.getInteger("progress");
                            drawTexturedModalRect(guiLeft + 13 + progress, guiTop + 100, 176, 0, 5, 5);
                            drawTexturedModalRect(guiLeft + 13 + latheRecipe.targetProgress, guiTop + 94, 181, 0, 5, 5);

                        }

                    }
                }
            }
        }
        if (!displayeSet) {
            displayedLatheActionFlags = null;
            displayedLatheActionFlags = null;
        }
    }

    public boolean matchesAction(int action, int actions[], LatheActionFlag flag) {
        switch (flag){
            case FIRST:
                return actions[2] == action;
            case SECOND:
                return actions[1] == action;
            case LAST:
                return actions[0] == action;
            case ANY:
                for (int i = 0;i < 3;i++) if (actions[i] == action) return true; // a bit of a problem here but it doesn't matter
                return false;
            case NOT_LAST:
                return actions[1] == action || actions[2] == action;
        }
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(button.id,new NBTTagCompound()));
    }
}
