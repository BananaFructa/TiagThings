package BananaFructa.RailcraftModifications;

import BananaFructa.TiagThings.Utils;
import mods.railcraft.common.blocks.logic.Logic;
import mods.railcraft.common.blocks.logic.RockCrusherLogic;

public class RFRockCrusherLogic extends RockCrusherLogic {

    public static double energyPerStep;

    static {
        energyPerStep = (double)Utils.readDeclaredField(RockCrusherLogic.class,null,"CRUSHING_POWER_COST_PER_STEP") / 5.0;
    }


    public RFRockCrusherLogic(Logic.Adapter adapter) {
        super(adapter);
    }

    @Override
    protected boolean doProcessStep() {
        return this.useInternalCharge(energyPerStep);
    }
}
