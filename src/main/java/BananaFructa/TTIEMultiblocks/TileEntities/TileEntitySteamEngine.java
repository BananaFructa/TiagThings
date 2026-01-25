package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamEngine extends AnimatedOBJTileEntity<TileEntitySteamEngine, SimplifiedMultiblockRecipe>{

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:low_quality_steam>"),20)}, new ItemStack[0], new FluidStack[0],0,1));
       add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:steam>"),20)}, new ItemStack[0], new FluidStack[0],0,1));
    }};

    private float speed = 0;

    public TileEntitySteamEngine() {
        super(TTIEContent.steamEngine, 0, false,recipes,TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.STEAM_ENGINE.getMeta()));
        setAnimation("running_mirrored");
        setAnimationSpeed(0);
    }

    @Override
    public void initPorts() {
        int fluidIn = registerFluidTank(1000);
        registerFluidPort(0,fluidIn,PortType.INPUT,EnumFacing.SOUTH);
        int rot = registerRotaryStorage();
        registerRotaryPort(34,rot,PortType.OUTPUT,EnumFacing.NORTH);
    }

    @Override
    public void update() {
        super.update();
        boolean needsUpdate = false;
        if (speed > 0.0f && speed < 6.0f) needsUpdate = true;
        if (world.isRemote) return;
        if (isWorking()) speed += 0.03f;
        else speed -= 0.03f;
        if (speed > 6.0f) speed = 6.0f;
        if (speed < 0.0f) speed = 0.0f;
        if (rotaryEnergies.size() > 0) {
            IRotaryEnergy r = rotaryEnergies.get(0);
            r.setRotationSpeed(10*speed);
            r.setTorque(90*speed/6);
            if (needsUpdate) IEUtils.notifyClientUpdate(world,pos);
        }
        setAnimation("running_mirrored");
        setAnimationSpeed(speed);
    }
}
