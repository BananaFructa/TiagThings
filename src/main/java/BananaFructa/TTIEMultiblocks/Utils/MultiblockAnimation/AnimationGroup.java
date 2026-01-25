package BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationGroup {

    public List<AnimationGroup> subGroups = new ArrayList<>();
    public Vector3f pivot;
    public int multiblockPosId = -1;
    public String name;
    public AnimationGroup(String name, Vector3f pivot, AnimationGroup... subGroups) {
        this.subGroups.addAll(Arrays.asList(subGroups));
        this.pivot = pivot;
        this.pivot.scale(1/16.0f);
        //this.pivot.sub(new Vector3f(0.5f,0,-0.5f));
        this.name = name;
    }

    public AnimationGroup(String name, Vector3f pivot, int id) {
        this.multiblockPosId = id;
        this.pivot = pivot;
        this.pivot.scale(1/16.0f);
        //this.pivot.sub(new Vector3f(-0.5f,0,-0.5f));
        this.name = name;
    }

    public boolean isFinal() {
        return multiblockPosId != -1;
    }

}
