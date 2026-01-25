package BananaFructa.TTIEMultiblocks.Renderers;

import blusunrize.immersiveengineering.api.MultiblockHandler;

import java.util.HashMap;

public class MultiblockAnimation {

    public HashMap<String,InterpolationJob> interpolationJobs;
    public float totalTime;

    public MultiblockAnimation(HashMap<String,InterpolationJob> interpolationJobs) {
        this.interpolationJobs = interpolationJobs;
        totalTime = this.interpolationJobs.values().stream().findFirst().get().animationTime;
    }

}
