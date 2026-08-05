package BananaFructa.TiagThings.mixin;

import li.cil.repack.org.luaj.vm2.LuaValue;
import li.cil.repack.org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LuaValue.class)
public class LuaValueInvokeDelay {

    private static int delay = 3;

    @Inject(method = "invoke()Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke1(CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

    @Inject(method = "invoke(Lli/cil/repack/org/luaj/vm2/Varargs;)Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke2(Varargs args, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

    @Inject(method = "invoke([Lli/cil/repack/org/luaj/vm2/LuaValue;)Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke3(LuaValue[] args, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

    @Inject(method = "invoke(Lli/cil/repack/org/luaj/vm2/LuaValue;Lli/cil/repack/org/luaj/vm2/Varargs;)Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke4(LuaValue arg, Varargs varargs, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

    @Inject(method = "invoke([Lli/cil/repack/org/luaj/vm2/LuaValue;Lli/cil/repack/org/luaj/vm2/Varargs;)Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke5(LuaValue[] args, Varargs varargs, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

    @Inject(method = "invoke(Lli/cil/repack/org/luaj/vm2/LuaValue;Lli/cil/repack/org/luaj/vm2/LuaValue;Lli/cil/repack/org/luaj/vm2/Varargs;)Lli/cil/repack/org/luaj/vm2/Varargs;", at = @At("HEAD"), remap = false)
    public void invoke6(LuaValue arg1, LuaValue arg2, Varargs varargs, CallbackInfoReturnable<Varargs> cir) {
        //ThreadThrottleManager.throttle();
    }

}
