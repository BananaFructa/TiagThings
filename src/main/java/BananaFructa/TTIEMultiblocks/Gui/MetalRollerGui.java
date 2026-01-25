package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMetalRoller;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySmallCoalBoiler;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class MetalRollerGui extends GuiIEContainerBase {

    TileEntityMetalRoller tile;

    public MetalRollerGui(InventoryPlayer inventoryPlayer, TileEntityMetalRoller tile) {
        super(new ContainerMetalRoller(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(new ButtonActionMetalRoller(0,guiLeft + 70,guiTop + 58,16,16,"Roll",MetalRollerAction.ROLL));
        addButton(new ButtonActionMetalRoller(1,guiLeft + 89,guiTop + 58,16,16,"Tighten Roller",MetalRollerAction.TIGHTEN_ROLLER));
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
        if (hovered(mouseX,mouseY,guiLeft+13,guiTop+55,41,9)) {
            tooltip.add("Input torque: " + String.format("%.1f", tile.inTorque) + "Nm.");
            tooltip.add(TextFormatting.AQUA + "Minimum torque is 20Nm.");
        }
        if (hovered(mouseX,mouseY,guiLeft+13,guiTop+67,41,9)) {
            tooltip.add("Input speed: " + String.format("%.1f", tile.inSpeed) + "RPM.");
            tooltip.add(TextFormatting.AQUA + "Minimum speed is 80RPM and the maximum speed is 100RPM.");
        }
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inTorque),guiLeft+26,guiTop+55,tile.inTorque >= 20 ? 0xff339900 : 0xffff6600);
        mc.fontRenderer.drawString(""+ String.format("%.1f", tile.inSpeed),guiLeft+26,guiTop+68,tile.inSpeed >= 80 && tile.inSpeed < 100 ? 0xff339900 : 0xffff6600);
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
        RenderHelper.enableGUIStandardItemLighting();
    }

    private boolean hovered (int mx, int my, int x, int y, int w, int h) {
        return mx >= x && my >= y && mx < x + w && my < y + h;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tiagthings:textures/gui/metal_roller_gui.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if (tile.inventoryHandlers.size() > 0) {
            ItemStack is = tile.inventoryHandlers.get(0).getStackInSlot(0);
            if (is.isEmpty()) return;
            if (!is.hasTagCompound()) return;
            NBTTagCompound compound = is.getTagCompound();
            if (!compound.hasKey("roller")) return;
            NBTTagCompound roller = compound.getCompoundTag("roller");
            if (!roller.hasKey("progress") || !roller.hasKey("tight")) return;
            float p = roller.getFloat("progress");
            float t = roller.getFloat("tight");
            drawTexturedModalRect(this.guiLeft + (int)(109 + (159-109) * t),this.guiTop+60,176,21,5,5);
            drawTexturedModalRect(this.guiLeft+63,this.guiTop+35,177,1,(int)(47*p),16);
        }
        //drawTexturedModalRect(this.guiLeft+49,this.guiTop+18,3,172,(int)(78*progres),28);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(button.id,new NBTTagCompound()));
    }
}
