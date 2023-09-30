package BananaFructa.TTIEMultiblocks.Compat.Waila;

import BananaFructa.TTIEMultiblocks.TileEntities.*;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
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
        return tooltip;
    }
}
