package BananaFructa.TTIEMultiblocks.Renderers;

import javax.vecmath.Vector3f;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class InterpolationJob {

    private List<Keyframe> keyframes;
    public float animationTime = 0;

    private int lastNext = 1;

    public InterpolationJob(List<Keyframe> keyframes) {
        this.keyframes = keyframes;
        this.animationTime = this.keyframes.get(this.keyframes.size()-1).time;
    }

    private Keyframe last() {
        return keyframes.get(lastNext-1);
    }

    private Keyframe next() {
        return keyframes.get(lastNext);
    }

    public Keyframe getCurrentFrame(float t) {
        while(next().time <= t || last().time > t) {
            lastNext = (lastNext+1)%keyframes.size();
            if (lastNext == 0) lastNext = 1;
        }
        Keyframe lastKey = last();
        Keyframe nextKey = next();
        Vector3f delta_Translation = new Vector3f();
        delta_Translation.sub(nextKey.translation,lastKey.translation);
        Vector3f delta_Rotation = new Vector3f();
        delta_Rotation.sub(nextKey.rotation,lastKey.rotation);
        float f = (t-lastKey.time)/(nextKey.time- lastKey.time);
        delta_Translation.scale(f);
        delta_Rotation.scale(f);
        delta_Translation.add(lastKey.translation);
        delta_Rotation.add(lastKey.rotation);
        return new Keyframe(t,delta_Translation,delta_Rotation);
    }

    public static class Keyframe {
        public float time;
        public Vector3f translation;
        public Vector3f rotation;

        public Keyframe(float time, Vector3f translation, Vector3f rotation) {
            this.time = time;
            this.translation = translation;
            this.rotation = rotation;
        }
    }

}
