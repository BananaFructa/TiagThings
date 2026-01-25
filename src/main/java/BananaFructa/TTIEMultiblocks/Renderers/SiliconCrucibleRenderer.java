package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityLathe;
import BananaFructa.TTIEMultiblocks.TileEntities.TileEntitySiliconCrucible;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;

import javax.vecmath.Vector3f;
import java.io.IOException;

public class SiliconCrucibleRenderer extends AnimatedOBJTileRenderer<TileEntitySiliconCrucible> {
    public SiliconCrucibleRenderer() {
        super(new AnimationGroup("bones",new Vector3f(0,0,0),
                new AnimationGroup("spinner",new Vector3f(8,56.5f,8),1)
        ),1000);
        try {
            loadAnimation("silicon_crucible.animation.json",4.0f,1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
