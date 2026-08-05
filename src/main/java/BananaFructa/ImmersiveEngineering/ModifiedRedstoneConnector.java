package BananaFructa.ImmersiveEngineering;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityPLC;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConnectorRedstone;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class ModifiedRedstoneConnector extends TileEntityConnectorRedstone {


    public ModifiedRedstoneConnector(TileEntityConnectorRedstone original) {
        this.facing = original.facing;
        this.ioMode = original.ioMode;
        this.redstoneChannel = original.redstoneChannel;
        this.rsDirty = original.rsDirty;
        this.wireNetwork = original.getNetwork();
        markDirty();
    }

    public void updateInput(byte[] signals) {
        TileEntityConnectorRedstone con = (TileEntityConnectorRedstone) (Object)this;
        BlockPos pos = con.getPos().offset(con.facing);
        TileEntity te = con.getWorld().getTileEntity(pos);
        if (te instanceof TileEntityPLC) {
            int p = ((TileEntityPLC)te).field_174879_c;
            TileEntityPLC master = ((TileEntityPLC) te).master();
            if (master != null) {
                master.updateInputs(p,signals);
                return;
            }
        }
        super.updateInput(signals);
    }


}






