package BananaFructa.TiagThings.mixin;

import BananaFructa.TiagThings.JEIContainerTogglable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EventBus.class)
public class JEIHide {

    @Inject(method = "post", at = @At("HEAD"),cancellable = true,remap = false)
    public void post(Event event, CallbackInfoReturnable<Boolean> cir) {
        if (    event instanceof GuiContainerEvent.DrawForeground ||
                event instanceof GuiScreenEvent.DrawScreenEvent.Post ||
                event instanceof GuiScreenEvent.InitGuiEvent.Post ||
                event instanceof GuiOpenEvent ||
                event instanceof  GuiScreenEvent.BackgroundDrawnEvent ||
                event instanceof GuiScreenEvent.ActionPerformedEvent ||
                event instanceof GuiScreenEvent.MouseInputEvent ||
                event instanceof GuiScreenEvent.KeyboardInputEvent
        ) {
            if (Minecraft.getMinecraft().currentScreen instanceof JEIContainerTogglable && !((JEIContainerTogglable) Minecraft.getMinecraft().currentScreen).shouldDisplay()) cir.setReturnValue(false);
        }
    }

}
