package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockClass;
import appeng.api.networking.IGridHost;
import appeng.block.networking.BlockCableBus;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityComputerClusterUnit_AE2 extends TileEntityAE2CompatMultiblock<TileEntityComputerClusterUnit> implements IGridHost {
    public TileEntityComputerClusterUnit_AE2() {
        super(TTIEContent.computerClusterUnit);
    }

    public TileEntityComputerClusterUnit_AE2(TileEntityMultiblockPart<?> oldPart) {
        super(TTIEContent.computerClusterUnit,oldPart);
    }
}
