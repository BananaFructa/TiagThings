package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import BananaFructa.TiagThings.TTMain;
import appeng.api.networking.IGridHost;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;

public class TileEntityComputerClusterController_AE2 extends TileEntityAE2CompatMultiblock<TileEntityComputerClusterController> implements IGridHost {
    public TileEntityComputerClusterController_AE2() {
        super(TTIEContent.computerClusterController);
    }

    public TileEntityComputerClusterController_AE2(TileEntityMultiblockPart<?> oldPart) {
        super(TTIEContent.computerClusterController, oldPart);
    }
}
