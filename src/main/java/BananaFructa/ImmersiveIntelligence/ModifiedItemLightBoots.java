package BananaFructa.ImmersiveIntelligence;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerBoots;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerChestplate;

public class ModifiedItemLightBoots extends ItemIILightEngineerBoots {

    public ModifiedItemLightBoots() {
        super();
        for (Item i : IIContent.ITEMS) {
            if (i instanceof ItemIILightEngineerBoots && this != i) {
                IIContent.ITEMS.remove(i);
                break;
            }
        }
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (this.getUpgrades(stack).hasKey("flippers") && player.isInWater() && player.isSprinting()) {
            ItemNBTHelper.setBoolean(stack, "flippin", true);
        } else if (ItemNBTHelper.hasKey(stack, "flippin")) {
            ItemNBTHelper.remove(stack, "flippin");
        }

        Material mat = world.getBlockState(player.getPosition()).getMaterial();
        Material matDown = world.getBlockState(player.getPosition().down()).getMaterial();
        boolean rackets = this.getUpgrades(stack).hasKey("snow_rackets");
        if (!rackets || mat != Material.SNOW && mat != Material.CRAFTED_SNOW) {
            if (rackets && (matDown == Material.ICE || matDown == Material.PACKED_ICE)) {
                player.move(MoverType.SELF, player.motionX * 5.0, player.motionY, player.motionZ * 5.0);
                player.motionX *= 0.5;
                player.motionZ *= 0.5;
            } else if (ItemNBTHelper.hasKey(stack, "rackets")) {
                ItemNBTHelper.remove(stack, "rackets");
            }
        } else {
            ItemNBTHelper.setBoolean(stack, "rackets", true);
        }

    }

}
