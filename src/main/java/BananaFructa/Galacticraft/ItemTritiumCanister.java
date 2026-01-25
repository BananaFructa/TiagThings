package BananaFructa.Galacticraft;

import mcp.MethodsReturnNonnullByDefault;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTritiumCanister extends ItemCanisterGeneric implements ISortableItem {
    public ItemTritiumCanister(String assetName) {
        super(assetName);
        this.setAllowedFluid("tritium");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getMaxDamage() - itemStack.getItemDamage() == 0) {
            return "item.empty_liquid_canister";
        } else {
            return itemStack.getItemDamage() == 1 ? "item.tritium_canister" : "item.tritium_canister_partial";
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0) {
            tooltip.add(GCCoreUtil.translate("gui.message.tritium.name") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
        }

    }

    @Override
    public EnumSortCategoryItem getCategory(int i) {
        return EnumSortCategoryItem.CANISTER;
    }
}
