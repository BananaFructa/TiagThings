package BananaFructa.TiagThings.mixin;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.pabilo8.immersiveintelligence.client.model.IIModelRegistry;
import pl.pabilo8.immersiveintelligence.client.util.amt.parts.AMTQuads;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

@Mixin(AMTQuads.class)
public class TryCatchAMTQuads {

    @Shadow
    protected int listID;

    @Shadow
    @Final
    protected BakedQuad[] quads;

    @Shadow
    protected IIColor bakedColor;

    @Shadow
    protected boolean hasLighting;

    @Shadow
    @Final
    protected static Vec3i NO_LIGHTING_NORMAL;

    @Inject(method = "draw", at = @At("HEAD"), remap = false, cancellable = true)
    public void draw(Tessellator tes, BufferBuilder buf, CallbackInfo ci){
        try {
            if (listID != -1)
                GlStateManager.callList(listID);
            else {
                listID = GLAllocation.generateDisplayLists(1);
                GL11.glNewList(listID, GL11.GL_COMPILE_AND_EXECUTE);

                if (quads.length > 0) {
                    buf.begin(7, DefaultVertexFormats.ITEM);

                    //Use forge's trick to put colored quads to the buffer
                    //Since it's inside a GLCallList, the speed will get boosted significantly

                    if (bakedColor != IIColor.WHITE) {
                        float[] floats = bakedColor.getFloatRGB();
                        for (BakedQuad quad : quads) {
                            buf.addVertexData(quad.getVertexData());
                            buf.putColorRGB_F4(floats[0], floats[1], floats[2]);
                            Vec3i vec3i = hasLighting ? (quad.getFace().getDirectionVec()) : NO_LIGHTING_NORMAL;
                            buf.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
                        }
                        tes.draw();
                        buf.setTranslation(0, 0, 0);

                        buf.begin(7, DefaultVertexFormats.ITEM);
                        buf.addVertexData(IIModelRegistry.QUAD_EMPTY.getVertexData());
                        buf.putColorRGB_F4(1, 1, 1);
                        buf.putNormal((float) NO_LIGHTING_NORMAL.getX(), (float) NO_LIGHTING_NORMAL.getY(), (float) NO_LIGHTING_NORMAL.getZ());
                    } else
                        //Else use the ancient method to place them as the scripture says
                        for (BakedQuad quad : quads) {
                            buf.addVertexData(quad.getVertexData());
                            buf.putColorRGB_F4(1, 1, 1);
                            Vec3i vec3i = hasLighting ? (quad.getFace().getDirectionVec()) : NO_LIGHTING_NORMAL;
                            buf.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ());
                        }
                    tes.draw();
                    buf.setTranslation(0, 0, 0);
                }

                GL11.glEndList();
            }
        } catch (Exception e) {
            buf.finishDrawing();
        }
        ci.cancel();
    }

}
