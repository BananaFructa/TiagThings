package BananaFructa.ImmersiveIntelligence;

import BananaFructa.ImmersiveIntelligence.network.MessageItemKeybind;
import BananaFructa.TiagThings.Proxy.ClientProxy;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.tool.IElectricEquipment;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;

/*
    CODE FROM 0.3.0
 */

public class ModifiedItemLightHelmet extends ItemIILightEngineerHelmet implements EnergyHelper.IIEEnergyItem {

    public static int ir_headgear_energy_usage = 150;
    public static int technician_headgear_energy_usage = 50;

    public ModifiedItemLightHelmet() {
        super();
        for (Item i : IIContent.ITEMS) {
            if (i instanceof ItemIILightEngineerHelmet && this != i) {
                IIContent.ITEMS.remove(i);
                break;
            }
        }
    }

    public boolean hasUpgrade(ItemStack stack, String upgrade) {
        return this.getUpgrades(stack).hasKey(upgrade);
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        boolean hasEngineer = this.hasUpgrade(stack, "engineer_gear");
        boolean hasIR = hasEngineer || this.hasUpgrade(stack, "infiltrator_gear");
        boolean hasTech = hasEngineer || this.hasUpgrade(stack, "technician_gear");
        if (world.isRemote) {
            if (hasIR && ((ClientProxy)TTMain.proxy).irHeadset.isPressed()) {
                IIPacketHandler.wrapper.sendToServer(new MessageItemKeybind(1));
            }
        } else if ((hasIR || hasTech) && world.getTotalWorldTime() % 20L == 0L && ItemNBTHelper.getBoolean(stack, "headgearActive")) {
            int drain = hasEngineer ? ir_headgear_energy_usage : (hasTech ? technician_headgear_energy_usage : ir_headgear_energy_usage);
            if (this.extractEnergy(stack, drain, false) < drain) {
                return;
            }

            if (hasIR) {
                boolean isInDarkness = world.getLightBrightness(player.getPosition()) <= 0.5F;
                player.addPotionEffect(new PotionEffect(isInDarkness ? MobEffects.NIGHT_VISION : MobEffects.BLINDNESS, 240, 1, true, false));
            }
        }

    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return 4096;
    }
}
