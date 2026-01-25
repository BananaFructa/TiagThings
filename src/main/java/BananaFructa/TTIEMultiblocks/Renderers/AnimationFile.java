package BananaFructa.TTIEMultiblocks.Renderers;

import BananaFructa.TTIEMultiblocks.Utils.MultiblockAnimation.AnimationGroup;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.List;

/*
    JSON representation of the blockbench animation file
 */
public class AnimationFile {

    @SerializedName("format_version")
    public String format_version;

    @SerializedName("animations")
    public Map<String, AnimationData> animations;

    public static class AnimationData {

        @SerializedName("loop")
        public boolean loop;

        @SerializedName("animation_length")
        public float animation_length;

        @SerializedName("bones")
        public Map<String,Map<String,Map<String, List<Double>>>> bones;

    }

}
