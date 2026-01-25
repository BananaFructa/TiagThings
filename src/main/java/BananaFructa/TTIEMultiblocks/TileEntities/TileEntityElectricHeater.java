package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityElectricHeater extends SimplifiedTileEntityMultiblockMetal<TileEntityElectricHeater, SimplifiedMultiblockRecipe> {

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[0],new ItemStack[0],new FluidStack[0],2000,1));
    }};

    public TileEntityElectricHeater() {
        super(TTIEContent.electricHeaterMultiblock, 16000,true,recipes);
    }

    @Override
    public void initPorts() {
        addEnergyPort(2);
        addRedstonePorts(0);
    }
}
