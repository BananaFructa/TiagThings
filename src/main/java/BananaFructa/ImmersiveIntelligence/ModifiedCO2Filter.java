package BananaFructa.ImmersiveIntelligence;

import BananaFructa.TiagThings.TTMain;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.TileEntityCO2Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifiedCO2Filter extends TileEntityCO2Filter {

    TileEntityOxygenSealer oxygenSealerThis = null;

    static List<TileEntityOxygenSealer> reservedOxygenSealers = new ArrayList<>();
    public ModifiedCO2Filter() {
        super();
        facing = EnumFacing.UP;
    }

    @Override
    public void func_73660_a() {
        if (isDummy()) return;
        EnumFacing ff = this.facing == EnumFacing.UP ? EnumFacing.NORTH : this.facing;
        TileEntity tile = this.world.getTileEntity(this.pos.up().offset(ff));
        if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ff.getOpposite())) {
            IFluidHandler capability = (IFluidHandler)tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, ff.getOpposite());
            if (capability != null) {
                if (world.provider.getDimension() == 0) capability.fill(new FluidStack(TTMain.ttAir, 10), true);
                else if (world.provider.getDimension() == -31) capability.fill(new FluidStack(TTMain.venusGas, 10), true);
                else if (OxygenUtil.checkTorchHasOxygen(world,pos)) {
                    TileEntityOxygenSealer oxygenSealer = TileEntityOxygenSealer.getNearestSealer(world,pos.getX(),pos.getY(),pos.getZ());
                    if (oxygenSealer != null) {
                        if (oxygenSealer != oxygenSealerThis) {
                            reservedOxygenSealers.remove(oxygenSealerThis);
                            oxygenSealerThis = null;
                        }
                        if (oxygenSealerThis == null && !reservedOxygenSealers.contains(oxygenSealer)) {
                            oxygenSealerThis = oxygenSealer;
                            reservedOxygenSealers.add(oxygenSealer);
                        }
                        if (oxygenSealerThis != null) {
                            capability.fill(new FluidStack(IIContent.gasCO2,3),true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (oxygenSealerThis != null) reservedOxygenSealers.remove(oxygenSealerThis);
    }
}
