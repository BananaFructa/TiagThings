package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_2;
import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TiagThings.Utils;
import micdoodle8.mods.galacticraft.core.GCFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;

import java.util.ArrayList;
import java.util.List;

public class TileEntitySiliconCrucible extends AnimatedOBJTileEntity<TileEntitySiliconCrucible, SimplifiedMultiblockRecipe>{

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:polysilicon>",32),Utils.itemStackFromCTId("<tfc:metal/dust/boron>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:argon>"),500)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:pboule>")},new FluidStack[0],300,20*60,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:polysilicon>",32),Utils.itemStackFromCTId("<tiagthings:bjt_mix>")},new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:argon>"),500)},new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:bjtboule>")},new FluidStack[0],300,20*60,true));
    }};


    public TileEntitySiliconCrucible() {
        super(TTIEContent.siliconCrucible, 16000, false,recipes,TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.SILICON_CRUCIBLE.getMeta()));
        setAnimation("spinning");
    }

    public float progress() {
        if (this.processQueue.isEmpty()) return 0;
        return (float)this.processQueue.get(0).processTick/this.processQueue.get(0).maxTicks;
    }

    @Override
    public void initPorts() {
        addEnergyPort(1);
        int inputItem = registerItemHandler(2,new boolean[]{true,true},new boolean[]{true,true});
        registerItemPort(0,inputItem,PortType.INPUT,EnumFacing.EAST);
        int outputItem = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(0,outputItem,PortType.OUTPUT,EnumFacing.WEST);
        int fluidIn = registerFluidTank(1000);
        registerFluidPort(0,fluidIn,PortType.INPUT,EnumFacing.SOUTH);
    }

    @Override
    public void update() {
        super.update();
        if (isWorking()) {
            setAnimationSpeed(1);
        } else {
            setAnimationSpeed(0);
        }
    }
}
