package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.*;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMemoryFormatter extends STEMM_ClusterClient<TileEntityMemoryFormatter, SimplifiedMultiblockRecipe> {

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:unf_controller_circuit_board>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:controller_circuit_board>")},new FluidStack[0],1024,60*20));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<appliedenergistics2:memory_card>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:launch_software_memory_card>")},new FluidStack[0],1024,375*20));
    }};

    public TileEntityMemoryFormatter() {
        super(TTIEContent.memoryFormatter, 16000, false,recipes);
    }

    @Override
    public void update() {
        if (this.field_174879_c == 5) this.world.setTileEntity(getPos(), new TileEntityMemoryFormatter_AE2(this));
        super.update();
    }

    @Override
    public int getProcessingPowerForRecipe(SimplifiedMultiblockRecipe recipe) {
        if (recipe == recipes.get(0)) return 8;
        if (recipe == recipes.get(1)) return 80;
        return 0;
    }

    // TODO: this is bad
    public static int getProcessingPowerForRecipe_static(SimplifiedMultiblockRecipe recipe) {
        if (recipe == recipes.get(0)) return 8;
        if (recipe == recipes.get(1)) return 80;
        return 0;
    }

    @Override
    protected int getPosOfPort() {
        return 5;
    }

    @Override
    public void initPorts() {
        addEnergyPort(2);
        int input = registerItemHandler(1, new boolean[]{true}, new boolean[]{false});
        registerItemPort(9, input, PortType.INPUT, EnumFacing.NORTH);
        registerItemPort(6, -1, PortType.OUTPUT, EnumFacing.SOUTH);
    }
}
