package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySteamEngine extends AnimatedOBJTileEntity<TileEntitySteamEngine, SimplifiedMultiblockRecipe>{

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{

    }};

    public TileEntitySteamEngine() {
        super(TTIEContent.steamEngine, 0, false,recipes,TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.STEAM_ENGINE.getMeta()),
                new AnimationGroup("bones",new Vector3f(0,0,0),
                        new AnimationGroup("flywheele",new Vector3f(72,24.0f,21.0f),2),
                        new AnimationGroup("transmission_arm",new Vector3f(0,0,0),
                                new AnimationGroup("lever",new Vector3f(46.75f,24,-8),4),
                                new AnimationGroup("horizontal_lever",new Vector3f(0,0,0),3)
                        ),
                        new AnimationGroup("transmission_arm2",new Vector3f(0,0,0),
                                new AnimationGroup("lever2",new Vector3f(64.5f,23.75f,5),5),
                                new AnimationGroup("bone",new Vector3f(0,0,0),1)
                        ),
                        new AnimationGroup("no_idea_what_these_are",new Vector3f(16,38,25),6)),
                6);
        try {
            loadAnimation("steamengine.mirrored.animation.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initPorts() {
    }

    @Override
    public void update() {
        super.update();
        setAnimation("running_mirrored");
        setAnimationSpeed(6);
        IEUtils.notifyClientUpdate(world,pos);
    }
}
