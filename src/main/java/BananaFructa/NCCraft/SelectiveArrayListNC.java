package BananaFructa.NCCraft;

import nc.block.fluid.NCBlockFluid;
import net.minecraftforge.fluids.Fluid;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class SelectiveArrayListNC extends ArrayList<Pair<Fluid, NCBlockFluid>> {

    List<String> NCRemove = new ArrayList<String>() {{
        add("methanol");
        add("carbon_dioxide");
        add("carbon_monoxide");
        add("ethene");
        add("ammonia");
        add("hydrofluoric_acid");
        add("sulfuric_acid");
        add("ethanol");
    }};

    @Override
    public boolean add(Pair<Fluid, NCBlockFluid> fluidNCBlockFluidPair) {
        if (NCRemove.contains(fluidNCBlockFluidPair.getLeft().getName())) return false;
        return super.add(fluidNCBlockFluidPair);
    }
}
