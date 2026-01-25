package BananaFructa.ImmersivePetroleum;

import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;

public class ModifiedTileEntityPumpjack extends TileEntityPumpjack {

    public ModifiedTileEntityPumpjack() {
        super();
        Utils.writeDeclaredField(TileEntityMultiblockMetal.class,this,"energyStorage",new FluxStorageAdvanced(0),true);
    }

}
