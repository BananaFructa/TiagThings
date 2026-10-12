package BananaFructa.TiagThings.mixin;

// Used to supress errors or any other things of that kind that do not affect gameplay but are unavoidable at a given time

import BananaFructa.TiagThings.ChatMessageSuppressor;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiNewChat.class)
public class ChatMessageSupress {

    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("HEAD"), cancellable = true)
    private void onMessage(ITextComponent chatComponent, int chatLineId, CallbackInfo ci) {
        for (String p : ChatMessageSuppressor.suppressedPattern) {
            if (chatComponent.getUnformattedText().contains(p)) {
                ci.cancel();
                break;
            }
        }
    }

}
