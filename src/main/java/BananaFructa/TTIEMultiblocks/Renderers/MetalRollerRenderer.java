package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityMetalRoller;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySteamEngine;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class MetalRollerRenderer extends AnimatedOBJTileRenderer<TileEntityMetalRoller> {
    public MetalRollerRenderer() {
        super(new AnimationGroup("bones",new Vector3f(0,0,0),
                new AnimationGroup("roller_1",new Vector3f(24.25f,10,5.25f),1),
                new AnimationGroup("roller_2",new Vector3f(24.25f,10,10.75f),2),
                new AnimationGroup("roller_3",new Vector3f(24.25f,13,8),3)
        ),1000);
        try {
            loadAnimation("metal_roller.animation.json",1.0f,3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
