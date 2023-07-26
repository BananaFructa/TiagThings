package BananaFructa.ImmersiveIntelligence;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerChestplate;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;
public class ModifiedItemLightChestplate extends ItemIILightEngineerChestplate {

    public static int scuba_tank_usage = 20;

    public ModifiedItemLightChestplate() {
        super();
        for (Item i : IIContent.ITEMS) {
            if (i instanceof ItemIILightEngineerChestplate && this != i) {
                IIContent.ITEMS.remove(i);
                break;
            }
        }
    }

    public boolean hasUpgrade(ItemStack stack, String upgrade) {
        return this.getUpgrades(stack).hasKey(upgrade);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (player.getAir() != 300 && this.hasUpgrade(stack, "scuba") && this.hasUpgrade(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD), "gasmask")) {
            IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
            if (fluidHandler != null && world.getTotalWorldTime() % 20L == 0L) {
                FluidStack fs = fluidHandler.drain(scuba_tank_usage, true);
                if (fs != null && fs.amount > 0) {
                    player.setAir((int)Math.min((float)(player.getAir() + 40), 300.0F * ((float)fs.amount / (float)scuba_tank_usage)));
                }
            }
        }

        if (this.getUpgrades(stack).hasKey("camo_mesh") && world.getTotalWorldTime() % 10L == 0L) {
            Material material = world.getBlockState(player.getPosition()).getMaterial();
            if (player.isSneaking() && (material == Material.GRASS|| material == Material.LEAVES || material == Material.VINE)) {
                player.addPotionEffect(new PotionEffect(IIPotions.concealed, 15, 0, false, false));
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 15, 0, true, false));
            }
        }

    }

}
