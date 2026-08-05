package BananaFructa.TiagThings.mixin;


import li.cil.repack.org.luaj.vm2.LuaClosure;
import li.cil.repack.org.luaj.vm2.LuaValue;
import li.cil.repack.org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LuaClosure.class)
public class LuaClosureThrottle {

    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lli/cil/repack/org/luaj/vm2/lib/DebugLib;onInstruction(ILli/cil/repack/org/luaj/vm2/Varargs;I)V"), remap = false)
    public void execute(LuaValue[] stack, Varargs varargs, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle(Thread.currentThread());
    }

}
