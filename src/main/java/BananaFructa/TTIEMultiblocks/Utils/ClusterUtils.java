package BananaFructa.TTIEMultiblocks.Utils;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityComputerClusterController;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityComputerClusterController_AE2;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IMachineSet;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import net.minecraft.tileentity.TileEntity;

public class ClusterUtils {

    public static ConnectionStatus requestProcessingPower(int processUnits, TileEntity tePort) {
        try {
            if (tePort instanceof TileEntityAE2CompatMultiblock) {
                AENetworkProxy proxy = ((TileEntityAE2CompatMultiblock)tePort).getProxy();
                IMachineSet machineSet = proxy.getGrid().getMachines(TileEntityComputerClusterController_AE2.class);
                int failedToRetrieve = 0;
                for (IGridNode node : machineSet) {
                    DimensionalCoord coord = node.getGridBlock().getLocation();
                    TileEntityComputerClusterController_AE2 teClusterController = (TileEntityComputerClusterController_AE2) coord.getWorld().getTileEntity(coord.getPos());
                    TileEntityComputerClusterController master = teClusterController.master();
                    if (master != null) {
                        if (master.isWorking() && master.doProcessRequest(processUnits)) return ConnectionStatus.OK;
                        else if (master.isWorking()) failedToRetrieve++;
                    }
                }
                if (failedToRetrieve > 0) return ConnectionStatus.INSUFFICIENT_RESOURCES;
            } else {
                return ConnectionStatus.HOST_NOT_FOUND;
            }
        } catch (GridAccessException e) {
            return ConnectionStatus.HOST_NOT_FOUND;
        }
        return ConnectionStatus.HOST_NOT_FOUND;
    }

    public static enum ConnectionStatus {
        OK,
        IDLE,
        HOST_NOT_FOUND,
        INSUFFICIENT_RESOURCES;

        public String getName() {
            String n = name().replace("_"," ");
            return n.charAt(0) + n.substring(1).toLowerCase();
        }
    }

}
