package BananaFructa.TiagThings.MainMenu;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class Animation {

    public double timeToComplete;
    public String pathSequence;
    public double elapsedTime = 0;
    public int sequenceLength = 0;
    public double timeInBetweenFrames;

    public Animation (double timeToComplete,String pathToFrameSequence,int sequenceLength) {
        pathSequence = pathToFrameSequence;
        this.timeToComplete = timeToComplete;
        this.sequenceLength = sequenceLength;
        this.timeInBetweenFrames = timeToComplete / sequenceLength;
    }

    private int getCurrentFrame() {
        return (int)(elapsedTime / timeInBetweenFrames);
    }

    public void draw(Minecraft mc, GuiScreen screen, int x, int z,int width,int length) {

        int frame = getCurrentFrame();

        if (frame < sequenceLength) {
            mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId,pathSequence + "/frame-" + frame + ".png"));
            if (Minecraft.getDebugFPS() != 0) elapsedTime += 1.0/Minecraft.getDebugFPS();
        } else {
            mc.renderEngine.bindTexture(new ResourceLocation(TTMain.modId,pathSequence + "/final.png"));
        }
        screen.drawTexturedModalRect(x,z,0,0,width,length);

    }

}
