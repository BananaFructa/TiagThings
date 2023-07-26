package BananaFructa.ImmersiveIntelligence;

import BananaFructa.ImmersiveIntelligence.network.MessageItemKeybind;
import BananaFructa.TiagThings.Proxy.ClientProxy;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.google.common.base.Predicate;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerLeggings;

import java.util.List;

public class ModifiedItemLightLeggings extends ItemIILightEngineerLeggings implements EnergyHelper.IIEEnergyItem {

    public static int exoskeleton_energy_usage = 200;

    public ModifiedItemLightLeggings() {
        super();
        for (Item i : IIContent.ITEMS) {
            if (i instanceof ItemIILightEngineerLeggings && this != i) {
                IIContent.ITEMS.remove(i);
                break;
            }
        }
    }

    public boolean hasUpgrade(ItemStack stack, String upgrade) {
        return this.getUpgrades(stack).hasKey(upgrade);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (this.hasUpgrade(stack, "exoskeleton")) {
            int rammingCooldown = ItemNBTHelper.getInt(stack, "rammingCooldown") - 1;
            if (rammingCooldown > 0) {
                ItemNBTHelper.setInt(stack, "rammingCooldown", rammingCooldown);
            } else {
                ItemNBTHelper.remove(stack, "rammingCooldown");
            }

            if (world.isRemote && ((ClientProxy)TTMain.proxy).exoSkeleton.isPressed()) {
                IIPacketHandler.wrapper.sendToServer(new MessageItemKeybind(2));
            }

            int mode = ItemNBTHelper.getInt(stack, "exoskeletonMode");
            int energy = (int)((float)mode / 2.0F * (float)exoskeleton_energy_usage);
            if (mode > 0 && player.isSprinting() && this.extractEnergy(stack, energy, false) == energy) {
                player.getFoodStats().addStats(0, 20.0F);
                if (mode == 2 && player.distanceWalkedOnStepModified > 8.0F && player.isPotionActive(IIPotions.movement_assist)) {
                    List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox(), (Predicate)null);
                    if (mobs.size() > 0) {
                        mobs.forEach((entityMob) -> {
                            entityMob.knockBack(player, 3.0F, (double) MathHelper.sin(player.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(player.rotationYaw * 0.017453292F)));
                            entityMob.attackEntityFrom(IEDamageSources.crusher, (float)player.getTotalArmorValue());
                        });
                        player.getArmorInventoryList().forEach((s) -> {
                            s.damageItem(2, player);
                        });
                        player.removePotionEffect(IIPotions.movement_assist);
                        ItemNBTHelper.setInt(stack, "rammingCooldown", 40);
                    }
                }

                if (ItemNBTHelper.getInt(stack, "rammingCooldown") <= 0 && world.getTotalWorldTime() % 4L == 0L) {
                    player.addPotionEffect(new PotionEffect(IIPotions.movement_assist, 4, mode, false, false));
                }
            }
        }

    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return 4096;
    }
}
