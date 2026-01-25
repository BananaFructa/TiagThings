package BananaFructa.ImmersiveEngineering;

import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.IEEnums;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCapacitorLV;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityCrusher;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileEntityCapacitorLVModified extends TileEntityCapacitorLV {

    BananaFructa.TiagThings.Utils.InstanceField<FluxStorage> energyStorage = Utils.getAccessibleField(TileEntityCapacitorLV.class,this,"energyStorage",false);

    @Override
    protected void transferEnergy(int side) {
        if (this.sideConfig[side] == IEEnums.SideConfig.OUTPUT) {
            EnumFacing fd = EnumFacing.getFront(side);
            BlockPos outPos = this.getPos().offset(fd);
            TileEntity tileEntity = blusunrize.immersiveengineering.common.util.Utils.getExistingTileEntity(this.world, outPos);
            if (tileEntity instanceof TileEntityMetalPress ||
                    tileEntity instanceof TileEntityPumpjack ||
                    tileEntity instanceof TileEntityCrusher ||
                    tileEntity instanceof TileEntityPowerLoom) return;
            int out = Math.min(this.getMaxOutput(), this.energyStorage.get().getEnergyStored());
            this.energyStorage.get().modifyEnergyStored(-EnergyHelper.insertFlux(tileEntity, fd.getOpposite(), out, false));

        }
    }
}
