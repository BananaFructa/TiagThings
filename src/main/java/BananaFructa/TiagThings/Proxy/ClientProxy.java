package BananaFructa.TiagThings.Proxy;

import BananaFructa.EmergingTechnologies.HarvesterTileEntityModified;
import BananaFructa.THAHModifications.TTChooseClimateGUi;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TiagThings.MainMenu.TTMainMenuGui;
import BananaFructa.thah.gui.ChooseClimateGui;
import blusunrize.immersiveengineering.common.IEContent;
import io.moonman.emergingtechnology.machines.harvester.Harvester;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ClientProxy extends CommonProxy {

    @SideOnly(Side.CLIENT)
    public static KeyBinding irHeadset = new KeyBinding("IR Headset", Keyboard.KEY_V, "Immersive Intelligence");
    @SideOnly(Side.CLIENT)
    public static KeyBinding exoSkeleton = new KeyBinding("Exo Skeleton Settings", Keyboard.KEY_G, "Immersive Intelligence");

    /*static {
        // one of the deadly sins below
        try {
            String options = "resourcePacks:[\"Tiag.zip\"]";
            File f = new File(Minecraft.getMinecraft().mcDataDir, "options.txt");
            String text = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
            if (!text.contains("Tiag.zip")) {
                if (!text.contains("resourcePacks")) {
                    FileWriter w = new FileWriter(f,true);
                    w.write(options);
                    w.close();
                } else {
                    if (text.split("resourcePacks:")[1].toCharArray()[1] != ']') {
                        text.replace("resourcePacks:[","resourcePacks:[\"Tiag.zip\",");
                    } else {
                        text.replace("resourcePacks:[","resourcePacks:[\"Tiag.zip\"");
                    }
                    f.delete();
                    f.createNewFile();
                    FileWriter w = new FileWriter(f);
                    w.write(text);
                    w.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public ClientProxy() {
        super();
    }

    //        super.preInit();
    //        if (!Minecraft.getMinecraft().gameSettings.resourcePacks.contains("Tiag.zip")) {
    //            Minecraft.getMinecraft().gameSettings.resourcePacks.add("Tiag.zip");
    //        }
    @Override
    public void init() {
        super.init();
        ClientRegistry.registerKeyBinding(irHeadset);
        ClientRegistry.registerKeyBinding(exoSkeleton);
    }

    @Override
    public void postInit() {
        super.postInit();
        TTIEContent.clientInit();
    }

    @SubscribeEvent
    public void Tick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && !(Minecraft.getMinecraft().currentScreen instanceof TTMainMenuGui)) {
            Minecraft.getMinecraft().displayGuiScreen(new TTMainMenuGui());
        }
    }

    @SubscribeEvent
    public void onGui(GuiOpenEvent event) {
        if (event.getGui() instanceof ChooseClimateGui) {
            event.setCanceled(true);
            Minecraft.getMinecraft().displayGuiScreen(new TTChooseClimateGUi());
        }
    }

    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == IEContent.itemTool && event.getItemStack().getMetadata() == 0) {
            List<String> list = event.getToolTip();
            String s = I18n.format("desc.immersiveengineering.info.multiblockForbidden", new Object[0]);
            if (!GuiScreen.isShiftKeyDown()) {
                list.add(1, s + " " + I18n.format("desc.immersiveengineering.info.holdShift", new Object[0]));
            } else {
                list.add(1, s);

                for (int i = 0; i < bannedMultiblocks.size(); ++i) {
                    list.add(2, TextFormatting.DARK_GRAY + " " + I18n.format("desc.immersiveengineering.info.multiblock." + bannedMultiblocks.get(i), new Object[0]));
                }
            }
        }
    }
}
