package BananaFructa.TiagThings.mixin;

import li.cil.repack.org.luaj.vm2.LuaThread;
import li.cil.repack.org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LuaThread.class)
public class Test {

    @Inject(method = "resume", at = @At("HEAD"), remap = false)
    public void a(Varargs args, CallbackInfoReturnable<Varargs> cir) {
        //System.out.println("THREAD RESUMED");
    }

}
