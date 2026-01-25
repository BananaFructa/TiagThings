package BananaFructa.TTIEMultiblocks.Gui;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Netowrk.MessageGuiEvent;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.common.util.Utils;
import net.dries007.tfc.client.button.GuiButtonAnvilPlanIcon;
import net.dries007.tfc.client.button.GuiButtonPage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    Renders the same thing as GuiAnvilPlan.java but adapted for the lathe
 */
public class LathePlannerGui extends GuiIEContainerBase {

    TileEntityLathe tile;
    List<ItemStack> possibleOutputs = new ArrayList<>();
    List<Integer> indexes = new ArrayList<>();
    ItemStack inputStack;
    GuiButton buttonLeft;
    GuiButton buttonRight;
    int page = 0;

    @Override
    public void initGui() {
        super.initGui();
        ItemStack stack = tile.inventoryHandlers.get(0).getStackInSlot(0);
        inputStack = stack;
        for (int i = 0;i < TileEntityLathe.recipes.size();i++) {
            if (TileEntityLathe.recipes.get(i).getItemInputs().get(0).stack.getItem() == stack.getItem()){
                possibleOutputs.add(TileEntityLathe.recipes.get(i).getItemOutputs().get(0));
                indexes.add(i);
            }
        }
        constructPage(page);
    }

    public LathePlannerGui(InventoryPlayer inventoryPlayer, TileEntityLathe tile) {
        super(new ContainerLathePlanner(inventoryPlayer,tile));
        this.tile = tile;
        ySize = 192;
    }



    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ArrayList<String> tooltip = new ArrayList();
        RenderHelper.enableGUIStandardItemLighting();
        for (GuiButton button : this.buttonList) {
            if (button instanceof LathePlanButton) {
                boolean hovered = mouseX >= button.x && mouseY >= button.y && mouseX < button.x + button.width && mouseY < button.y + button.height;
                if (hovered) {
                    tooltip.add(((LathePlanButton) button).item.getDisplayName());
                }
            }
        }
        if(!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer, guiLeft + xSize, - 1);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtils.bindTexture("tfc:textures/gui/anvil_plan.png");
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format("tfc.tooltip.anvil_plan") + ": " + inputStack.getDisplayName();
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    public void constructPage(int page) {
        buttonList.clear();
        int id = 0;
        for (int i = page * 18; i < (page + 1) * 18 && i < possibleOutputs.size(); i++)
        {
            int posX = 7 + (i % 9) * 18;
            int posY = 25 + ((i % 18) / 9) * 18;
            addButton(new LathePlanButton(id++, guiLeft + posX, guiTop + posY,18,18,"",possibleOutputs.get(i),indexes.get(i)));
        }
        buttonLeft = addButton(new GuiButtonPage(id++, guiLeft + 7, guiTop + 65, GuiButtonPage.Type.LEFT, "tfc.tooltip.previous_page"));
        buttonRight = addButton(new GuiButtonPage(id++, guiLeft + 154, guiTop + 65, GuiButtonPage.Type.RIGHT, "tfc.tooltip.next_page"));
        if (possibleOutputs.size() <= 18) {
            buttonLeft.visible = false;
            buttonRight.visible = false;
        } else {
            if (page <= 0)
            {
                buttonLeft.enabled = false;
            }
            if ((page + 1) * 18 >= possibleOutputs.size())
            {
                buttonRight.enabled = false;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == buttonLeft) {
            page--;
            constructPage(page);
            return;
        }
        if (button == buttonRight) {
            page++;
            constructPage(page);
            return;
        }
        if (button instanceof LathePlanButton) {
            TTPacketHandler.wrapper.sendToServer(new MessageGuiEvent(((LathePlanButton)button).recipeIndex,new NBTTagCompound()));
        }
    }
}
