package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySteamEngine;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class LatheRenderer extends AnimatedOBJTileRenderer<TileEntityLathe> {
    public LatheRenderer() {
        super(new AnimationGroup("bones",new Vector3f(0,0,0),
                new AnimationGroup("holder",new Vector3f(28,7,8),1)
        ),1000);
        try {
            loadAnimation("lathe.animation.json",1.0f,1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
