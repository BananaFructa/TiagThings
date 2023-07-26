package BananaFructa.TTIEMultiblocks;

import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TTIEContent {

    public static BlockIEBase<TTBlockTypes_MetalMultiblock> ttBlockMetalMultiblock;

    static {
        ttBlockMetalMultiblock = new TTBlockMetalMultiblocks();
    }

    public static void init() {
        GameRegistry.registerTileEntity(TileEntityElectricHeater.class,new ResourceLocation(TTMain.modId,TileEntityElectricHeater.class.getSimpleName()));
        MultiblockHandler.registerMultiblock(MultiblockElectricHeater.instance);
    }

}
