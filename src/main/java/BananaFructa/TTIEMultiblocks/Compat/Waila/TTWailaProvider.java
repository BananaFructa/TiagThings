package BananaFructa.TTIEMultiblocks.Compat.Waila;

import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.STEMM_ClusterClient;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class TTWailaProvider implements IWailaDataProvider {

    // TODO: energy infor and other for AE2 compat tile entity

    public static void callbackRegister(IWailaRegistrar registrar) {
        TTWailaProvider provider = new TTWailaProvider();
        registrar.registerBodyProvider(provider, SimplifiedTileEntityMultiblockMetal.class);
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() instanceof TileEntityCoalBoiler) {
            TileEntityCoalBoiler te = ((TileEntityCoalBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Working at " + String.format("%.02f", 100-(100/15.0f)*te.redstoneLevel) + "%");
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                IItemHandler itemHandler = te.inventoryHandlers.get(0);
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());

                FluidTank inputTank = te.tanks.get(0);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(1);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");

            }
        }
        if (accessor.getTileEntity() instanceof TileEntityOilBoiler) {
            TileEntityOilBoiler te = ((TileEntityOilBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Working at " + String.format("%.02f", 100-(100/15.0f)*te.redstoneLevel) + "%");
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                FluidTank inputFluid = te.tanks.get(0);
                if (inputFluid == null || inputFluid.getFluid() == null) tooltip.add("Fuel Fluid Input: Empty");
                else tooltip.add("Fuel Fluid Input: " + inputFluid.getFluid().getLocalizedName() + " " + inputFluid.getFluidAmount() + " mb");

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(2);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");

            }
        }
        if (accessor.getTileEntity() instanceof TileEntityNMPLM) {
            TileEntityNMPLM te = ((TileEntityNMPLM)accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Current process: " + (te.process == 0 ? "um" : "nm"));
            }
        }
        if (accessor.getTileEntity() instanceof TileEntityEUVPLM) {
            TileEntityEUVPLM te = ((TileEntityEUVPLM)accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Current process: " + (te.process == 0 ? "um" : (te.process == 1 ? "nm" : "EUV")));
            }
        }
        if (accessor.getTileEntity() instanceof TileEntityGasCentrifuge) {
            TileEntityGasCentrifuge te = ((TileEntityGasCentrifuge) accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                FluidTank inputFluid = te.tanks.get(0);
                if (inputFluid == null || inputFluid.getFluid() == null) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + inputFluid.getFluid().getLocalizedName() + " " + inputFluid.getFluidAmount() + " mb");

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Primary Output: Empty");
                else tooltip.add("Primary Output: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(2);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Secondary Output: Empty");
                else tooltip.add("Secondary Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityComputerClusterController) {
            TileEntityComputerClusterController te = ((TileEntityComputerClusterController) accessor.getTileEntity());
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Cluster Units Paired: " + te.activeClusterUnits);
                tooltip.add("Processes running: " + te.lastUsedRequests + "/" + te.maxRequests);
                tooltip.add("Resource utilization: " + te.lastUsedProcessingPower + "/" + te.totalProcessingPower);
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityComputerClusterUnit) {
            TileEntityComputerClusterUnit te = (TileEntityComputerClusterUnit) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Pair status: " + (te.paired ? "PAIRED" : "UNPAIRED"));
                tooltip.add("Total processing resources: 4");
            }
        }

        if (accessor.getTileEntity() instanceof STEMM_ClusterClient) {
            STEMM_ClusterClient<?,?> te = (STEMM_ClusterClient<?,?>) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = (STEMM_ClusterClient<?,?>)te.master();
                tooltip.add("Cluster connection status: " + te.connected.getName());
                int usedPower = 0;
                if (te.processQueue.size() > 0) {
                    usedPower = te.getUsedPower();
                }
                tooltip.add("Using " + usedPower + " processing power");
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityMemoryFormatter) {
            TileEntityMemoryFormatter te = (TileEntityMemoryFormatter) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                if (te.isWorking()) {
                    float prog = (float)te.processQueue.get(0).processTick/(float)te.processQueue.get(0).maxTicks;
                    int hashes = (int)(20*prog);
                    int dots = 20 - hashes;
                    String hs = "";
                    String ds = "";
                    for (int i = 0;i < hashes;i++) hs +="#";
                    for (int i = 0;i < dots;i++) ds +="-";
                    tooltip.add("Progress " + String.format("%.2f",prog*100) + "% ["+hs+ds+"]");
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityClayOven) {
            TileEntityClayOven te = (TileEntityClayOven) accessor.getTileEntity();
            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                for (TileEntityMultiblockMetal.MultiblockProcess<SimplifiedMultiblockRecipe> process : te.processQueue) {
                    tooltip.add(process.recipe.getItemInputs().get(0).stack.getDisplayName() + ": " + (int)((process.maxTicks - process.processTick)/(60*20)) + " hour(s) remaining");
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntityOpenHearthFurnace) {
            TileEntityOpenHearthFurnace te = ((TileEntityOpenHearthFurnace)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                if (te.pigIronMb != 0) tooltip.add("Pig Iron: " + te.pigIronMb + " mb");
                if (te.tanks.get(1).getFluidAmount() != 0) {
                    if (te.tanks.get(1).getFluid().getFluid() == TileEntityOpenHearthFurnace.liquidSteel) {
                        tooltip.add("Steel: " + te.tanks.get(1).getFluidAmount() + " mb");
                    } else {
                        tooltip.add("Mild Steel: " + te.tanks.get(1).getFluidAmount() + " mb");
                    }
                }
                int idleTime = te.idleTime;
                if (idleTime > 0) {
                    tooltip.add("Converting to Mild Steel in: " + (int)((TileEntityOpenHearthFurnace.idleTimeToMildSteel - idleTime)/20) + " second");
                }
            }
        }

        if (accessor.getTileEntity() instanceof TileEntitySmallCoalBoiler) {
            TileEntitySmallCoalBoiler te = ((TileEntitySmallCoalBoiler)accessor.getTileEntity());

            if (te.offset.length == 3 && te.master() != null) {
                te = te.master();
                tooltip.add("Temperature: " + te.getTemperature() + "\u00b0C");
                IItemHandler itemHandler = te.inventoryHandlers.get(0);
                ItemStack stack = itemHandler.getStackInSlot(0);
                if (stack == null || stack.isEmpty()) tooltip.add("Input: Empty");
                else tooltip.add("Input: " + stack.getItem().getItemStackDisplayName(stack) + " x" + stack.getCount());

                FluidTank inputTank = te.tanks.get(1);
                if (inputTank == null || inputTank.getFluid() == null) tooltip.add("Fluid Input: Empty");
                else tooltip.add("Fluid Input: " + inputTank.getFluid().getLocalizedName() + " " + inputTank.getFluidAmount() + " mb");

                FluidTank outputTank = te.tanks.get(0);
                if (outputTank == null || outputTank.getFluid() == null) tooltip.add("Fluid Output: Empty");
                else tooltip.add("Fluid Output: " + outputTank.getFluid().getLocalizedName() + " " + outputTank.getFluidAmount() + " mb");
            }
        }

        return tooltip;
    }
}
