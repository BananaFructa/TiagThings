package BananaFructa.TFCTech;

import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import mods.railcraft.common.fluids.Fluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tfctech.objects.tileentities.TEInductionCrucible;

import javax.annotation.Nullable;

public class TEInductionCrucibleCAP extends TEInductionCrucible {

    DrainTankCrucibleWrapper wrapper = new DrainTankCrucibleWrapper(this);

    @Override
    public void update() {
        super.func_73660_a();
        TileEntity te = this.world.getTileEntity(this.getPos().down());
        if (te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,EnumFacing.UP)) {
            IFluidHandler fluidHandler = (IFluidHandler) te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,EnumFacing.UP);
            int accepted = fluidHandler.fill(wrapper.getFluid(),false);
            FluidStack drain = wrapper.drain(accepted,true);
            if (drain != null) {
                fluidHandler.fill(drain,true);
                this.markForBlockUpdate();
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (facing == null || facing==EnumFacing.DOWN))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (facing == null || facing==EnumFacing.DOWN))
            return (T)wrapper;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        wrapper = new DrainTankCrucibleWrapper(this);
    }
}
