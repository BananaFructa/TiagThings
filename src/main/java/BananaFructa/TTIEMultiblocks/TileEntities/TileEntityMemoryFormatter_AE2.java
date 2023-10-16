package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;

public class TileEntityMemoryFormatter_AE2 extends TileEntityAE2CompatMultiblock<TileEntityMemoryFormatter> {
    public TileEntityMemoryFormatter_AE2() {
        super(TTIEContent.memoryFormatter);
    }

    public TileEntityMemoryFormatter_AE2(TileEntityMultiblockPart<?> oldPart) {
        super(TTIEContent.memoryFormatter, oldPart);
    }
}
