package BananaFructa.RailcraftModifications;

import mods.railcraft.api.crafting.Crafters;
import mods.railcraft.common.blocks.machine.equipment.TileRollingMachineManual;
import mods.railcraft.common.util.inventory.InvTools;
import mods.railcraft.common.util.inventory.InventoryIterator;

public class TileRollingMachineManualChanged extends TileRollingMachineManual {

    public boolean canMakeMore() {
        if (!Crafters.rollingMachine().getRecipe(this.craftMatrix, this.world).isPresent()) {
            return false;
        } else {
            return this.useLast;
        }
    }

}
