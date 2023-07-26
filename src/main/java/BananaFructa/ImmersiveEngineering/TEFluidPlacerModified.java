package BananaFructa.ImmersiveEngineering;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityFluidPlacer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class TEFluidPlacerModified extends TileEntityFluidPlacer {

    public TEFluidPlacerModified() {
        super();
        this.tank = new GasFluidTank(this.tank.getCapacity());
    }

    @Override
    public void validate() {
        super.validate();
        this.tank = new GasFluidTank(this.tank.getCapacity());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        this.tank = new GasFluidTank(this.tank.getCapacity());
        ((GasFluidTank)this.tank).setFluidAmount(nbt.getInteger("tankUniversal"));
        super.readCustomNBT(nbt, descPacket);
    }


    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        nbt.setInteger("tankUniversal",tank.getFluidAmount());
        super.writeCustomNBT(nbt, descPacket);
    }

    @Override
    public void func_73660_a() {
        ((GasFluidTank)tank).dump(100);
    }

}
