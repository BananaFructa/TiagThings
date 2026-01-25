package BananaFructa.TiagThings.Proxy;

import BananaFructa.ImmersiveEngineering.ModifiedGuiModWorkbench;
import BananaFructa.THAHModifications.TTChooseClimateGUi;
import BananaFructa.TTIEMultiblocks.Commands.AdjustOBJAnimatedPivot;
import BananaFructa.TTIEMultiblocks.ElectricMotorTileEntity;
import BananaFructa.TTIEMultiblocks.Gui.*;
import BananaFructa.TTIEMultiblocks.Gui.CokeOvenBattery.ContainerCokeOvenBattery;
import BananaFructa.TTIEMultiblocks.Gui.CokeOvenBattery.TileEntityCokeOvenBatteryGui;
import BananaFructa.TTIEMultiblocks.Gui.CokerUnit.ContainerCokerUnit;
import BananaFructa.TTIEMultiblocks.Gui.CokerUnit.TileEntityCokerUnitGui;
import BananaFructa.TTIEMultiblocks.Gui.ElectricfFoodOven.ContainerElectricFoodOven;
import BananaFructa.TTIEMultiblocks.Gui.ElectricfFoodOven.TileEntityElectricFoodOvenGui;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TiagThings.MainMenu.TTMainMenuGui;
import BananaFructa.TiagThings.RockTraces;
import BananaFructa.TiagThings.Utils;
import BananaFructa.thah.gui.ChooseClimateGui;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.client.gui.GuiModWorkbench;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import mcp.mobius.waila.api.event.WailaTooltipEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
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
        //BlockRendererDispatcher blockRendererDispatcher = BananaFructa.TiagThings.Utils.readDeclaredField(Minecraft.class,Minecraft.getMinecraft(),"blockRenderDispatcher");
        ClientRegistry.registerKeyBinding(irHeadset);
        ClientRegistry.registerKeyBinding(exoSkeleton);
        ClientCommandHandler.instance.registerCommand(new AdjustOBJAnimatedPivot());
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
        if (event.getGui() instanceof GuiModWorkbench) {
            event.setCanceled(true);
            Minecraft.getMinecraft().displayGuiScreen(new ModifiedGuiModWorkbench(Minecraft.getMinecraft().player.inventory,Minecraft.getMinecraft().world,Utils.readDeclaredField(GuiModWorkbench.class,event.getGui(),"workbench")));
        }
    }

    @SubscribeEvent
    public void onWailaTooltip(WailaTooltipEvent event) {
        if (event.getAccessor().getTileEntity() instanceof TileEntityCrusher) {
            for (String s : event.getCurrentTip()) {
                if (s.contains("IF")) event.getCurrentTip().remove(s);
                break;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
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
        NBTTagCompound nbtTagCompound = event.getItemStack().getTagCompound();
        if (nbtTagCompound != null) {
            if (nbtTagCompound.hasKey("lathe")) {
                List<String> list = event.getToolTip();
                list.add(2, TextFormatting.GRAY + "Turning in Progress");
            }
            if (nbtTagCompound.hasKey("roller")) {
                List<String> list = event.getToolTip();
                list.add(2, TextFormatting.GRAY + "Rolling in Progress");
                NBTTagCompound compound = nbtTagCompound.getCompoundTag("roller");
                if (compound.hasKey("failed")) {
                    boolean f = compound.getBoolean("failed");
                    if (f) {
                        list.add(3,TextFormatting.RED + "Deformed while rolling");
                    }
                }
            }
            if (nbtTagCompound.hasKey("trace")) {
                List<String> list = event.getToolTip();
                list.add(2,TextFormatting.DARK_GRAY + "Contains traces of " + RockTraces.values()[nbtTagCompound.getInteger("trace")].getName() + TextFormatting.DARK_GRAY + ".");
            }
        }
    }


    GuiIEContainerBase lastCache = null;
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(x,y,z));
        switch (ID) {
            case 0:
                if (te instanceof TileEntitySmallCoalBoiler) {
                    TileEntitySmallCoalBoiler smc = ((TileEntitySmallCoalBoiler) te).master();
                    return new SmallCoalBoilerGui(player.inventory,smc);
                }
                break;
            case 1:
                if (te instanceof TileEntityLathe) {
                    TileEntityLathe lathe = ((TileEntityLathe) te).master();
                    return new LatheGui(player.inventory,lathe);
                }
                break;
            case 2:
                if (te instanceof TileEntityLathe) {
                    TileEntityLathe lathe = ((TileEntityLathe) te).master();
                    return new LathePlannerGui(player.inventory,lathe);
                }
                break;
            case 3:
                if (te instanceof TileEntityMetalRoller) {
                    TileEntityMetalRoller roller = ((TileEntityMetalRoller) te).master();
                    return new MetalRollerGui(player.inventory,roller);
                }
                break;
            case 4:
                if (te instanceof TileEntityMagneticSeparator) {
                    TileEntityMagneticSeparator separator = ((TileEntityMagneticSeparator) te).master();
                    return new MagneticSeparatorGui(player.inventory,separator);
                }
            case 5:
                if (te instanceof ElectricMotorTileEntity) {
                    ElectricMotorTileEntity separator = ((ElectricMotorTileEntity) te);
                    return new ElectricalMotorGui(player.inventory,separator);
                }
                break;
            case 6:
                if (te instanceof TileEntitySiliconCrucible) {
                    TileEntitySiliconCrucible siliconCrucible = ((TileEntitySiliconCrucible) te).master();
                    return new SiliconCrucibleGui(player.inventory,siliconCrucible);
                }
                break;
            case 7:
                if (te instanceof TileEntityMagnetizer) {
                    TileEntityMagnetizer magnetizer = ((TileEntityMagnetizer) te).master();
                    return new MagnetizerGui(player.inventory,magnetizer);
                }
                break;
            case 8:
                if (te instanceof TileEntityCokeOvenBattery) {
                    TileEntityCokeOvenBattery cokeOvenBattery = ((TileEntityCokeOvenBattery) te).master();
                    try { // I FUCKING HATE THIS SHIT
                        TileEntityCokeOvenBatteryGui teco = new TileEntityCokeOvenBatteryGui(player.inventory, cokeOvenBattery);
                        lastCache = teco;
                    } catch (Exception err) {}
                    return lastCache;
                }
                break;
            case 9:
                if (te instanceof TileEntityCokerUnit) {
                    TileEntityCokerUnit cokerUnit = ((TileEntityCokerUnit) te).master();
                    try {
                        TileEntityCokerUnitGui gui = new TileEntityCokerUnitGui(player.inventory,cokerUnit);
                        lastCache = gui;
                    } catch (Exception err) {}
                    return lastCache;
                }
                break;
            case 10:
                if (te instanceof TileEntityElectricFoodOven) {
                    TileEntityElectricFoodOven electricFoodOven = ((TileEntityElectricFoodOven) te).master();
                    return new TileEntityElectricFoodOvenGui(player.inventory,electricFoodOven);
                }
                break;
            case 11:
                if (te instanceof TileEntityFBR) {
                    TileEntityFBR fbr = ((TileEntityFBR) te).master();
                    return new FBRGui(player.inventory,fbr);
                }
                break;
            case 12:
                if (te instanceof TileEntityOpenHearthFurnace) {
                    TileEntityOpenHearthFurnace ohf = ((TileEntityOpenHearthFurnace) te).master();
                    try {
                        OpenHearthFurnaceGui gui = new OpenHearthFurnaceGui(player.inventory,ohf);
                        lastCache = gui;
                    } catch (Exception err) {}
                    return lastCache;
                }
                break;
        }
        return null;
    }
}
