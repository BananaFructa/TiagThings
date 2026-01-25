package BananaFructa.ImmersiveEngineering;

import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.BlueprintCraftingRecipe;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.api.tool.IConfigurableTool;
import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityModWorkbench;
import blusunrize.immersiveengineering.common.gui.ContainerModWorkbench;
import blusunrize.immersiveengineering.common.gui.IESlot;
import blusunrize.immersiveengineering.common.gui.InventoryBlueprint;
import blusunrize.immersiveengineering.common.gui.InventoryShader;
import blusunrize.immersiveengineering.common.items.ItemEngineersBlueprint;
import blusunrize.immersiveengineering.common.util.inventory.IEItemStackHandler;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Iterator;

public class ModifiedContainerModWorkbench extends ContainerModWorkbench {

    private InventoryBlueprint inventoryBPoutput;

    public ModifiedContainerModWorkbench(InventoryPlayer inventoryPlayer, World world, TileEntityModWorkbench tile) {
        super(inventoryPlayer, world, tile);
    }

    @Override
    public void rebindSlots() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            Iterator var1 = this.inventorySlots.iterator();

            while(var1.hasNext()) {
                Slot slot = (Slot)var1.next();
                if (slot instanceof IESlot.Upgrades && ItemStack.areItemsEqual(Utils.readDeclaredField(IESlot.Upgrades.class,slot,"upgradeableTool"), this.inv.getStackInSlot(0))) {
                    return;
                }
            }
        }

        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();
        this.addSlotToContainer(new IESlot.ModWorkbench(this, this.inv, 0, 24, 22, 1));
        this.slotCount = 1;
        ItemStack tool = this.getSlot(0).getStack();
        int y;
        if (tool.getItem() instanceof IUpgradeableTool) {
            IItemHandler handler = (IItemHandler)tool.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (EnumFacing)null);
            if (handler instanceof IEItemStackHandler) {
                ((IEItemStackHandler)handler).setTile(this.tile);
            }

            Slot[] slots = ((IUpgradeableTool)tool.getItem()).getWorkbenchSlots(this, tool);
            if (slots != null) {
                Slot[] var4 = slots;
                y = slots.length;

                for(int var6 = 0; var6 < y; ++var6) {
                    Slot s = var4[var6];
                    this.addSlotToContainer(s);
                    ++this.slotCount;
                }
            }

            if (tool.hasCapability(CapabilityShader.SHADER_CAPABILITY, (EnumFacing)null)) {
                CapabilityShader.ShaderWrapper wrapper = (CapabilityShader.ShaderWrapper)tool.getCapability(CapabilityShader.SHADER_CAPABILITY, (EnumFacing)null);
                if (wrapper != null) {
                    this.shaderInv = new InventoryShader(this, wrapper);
                    this.addSlotToContainer(new IESlot.Shader(this, this.shaderInv, 0, 130, 32, tool));
                    ++this.slotCount;
                    this.shaderInv.shader = wrapper.getShaderItem();
                }
            }
        } else if (!(tool.getItem() instanceof IConfigurableTool)) {
            boolean blueprint = false;
            if (tool.getItem() instanceof ItemEngineersBlueprint) {
                blueprint = true;
                BlueprintCraftingRecipe[] recipes = ((ItemEngineersBlueprint)tool.getItem()).getRecipes(tool);
                inventoryBPoutput = new InventoryBlueprint(this, recipes);

                for(int i = 0; i < recipes.length; ++i) {
                    y = 21 + 2*18 - (i<10 ? (i/3) : (10/3) + (i-10)/10)*18;
                    int x = 118 + 2*18 - (i < 10 ? i%3 : (i-10)%10) * 18;
                    this.addSlotToContainer(new IESlot.BlueprintOutput(this, inventoryBPoutput, this.inv, i, x, y, recipes[i]));
                    ++this.slotCount;
                }
            }

            for(int i = 0; i < 6; ++i) {
                if (blueprint) {
                    this.addSlotToContainer(new IESlot.BlueprintInput(this, this.inv, inventoryBPoutput, i + 1, i % 2 == 0 ? 74 : 92, 21 + i / 2 * 18));
                } else {
                    this.addSlotToContainer(new Slot(this.inv, i + 1, i % 2 == 0 ? 74 : 92, 21 + i / 2 * 18));
                }

                ++this.slotCount;
            }
        }

        this.bindPlayerInv(this.inventoryPlayer);
        ImmersiveEngineering.proxy.reInitGui();
    }

    private void bindPlayerInv(InventoryPlayer inventoryPlayer) {
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 87 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 145));
        }

    }

}
