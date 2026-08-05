package BananaFructa.TiagThings.mixin;

import BananaFructa.TiagThings.TTMain;
import li.cil.oc.OpenComputers;
import li.cil.oc.api.fs.FileSystem;
import li.cil.oc.common.Loot;
import li.cil.oc.common.Loot$;
import li.cil.repack.org.luaj.vm2.Varargs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scala.Option;
import scala.Serializable;
import scala.Tuple2;
import scala.collection.SeqLike;
import scala.collection.immutable.StringOps;
import scala.collection.mutable.ArrayBuffer;
import scala.collection.mutable.StringBuilder;
import scala.runtime.AbstractFunction0;
import scala.runtime.BoxedUnit;
import scala.runtime.BoxesRunTime;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

@Mixin(Loot$.class)
public class Disk {

    @Inject(method = "init",at = @At("HEAD"),cancellable = true,remap = false)
    private void init(CallbackInfo ci) {
        Properties list = new Properties();
        InputStream listStream = Loot.class.getResourceAsStream("/assets/tiagthings/disks/loot.properties");
        try {
            list.load(listStream);
            listStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String key : list.stringPropertyNames()) {
            System.out.println("KEY" + key);
            String value = list.getProperty(key);

            try {
                String[] var4 = value.split(":");
                Option var5 = scala.Array.unapplySeq(var4);
                if (!var5.isEmpty() && var5.get() != null && ((SeqLike) var5.get()).lengthCompare(3) == 0) {
                    String name = (String) ((SeqLike) var5.get()).apply(0);
                    String count = (String) ((SeqLike) var5.get()).apply(1);
                    String color = (String) ((SeqLike) var5.get()).apply(2);
                    Loot.globalDisks().$plus$eq(new Tuple2(Loot$.MODULE$.createLootDisk(name, key, false, li.cil.oc.util.Color.byOreName().get(color)), BoxesRunTime.boxToInteger((new StringOps(scala.Predef.augmentString(count))).toInt())));
                } else {
                    Option var10 = scala.Array.unapplySeq(var4);
                    if (!var10.isEmpty() && var10.get() != null && ((SeqLike) var10.get()).lengthCompare(2) == 0) {
                        String name = (String) ((SeqLike) var10.get()).apply(0);
                        String count = (String) ((SeqLike) var10.get()).apply(1);
                        Loot.globalDisks().$plus$eq(new Tuple2(Loot$.MODULE$.createLootDisk(name, key, false, Loot$.MODULE$.createLootDisk$default$4()), BoxesRunTime.boxToInteger((new StringOps(scala.Predef.augmentString(count))).toInt())));
                    } else {
                        Loot.globalDisks().$plus$eq(new Tuple2(Loot$.MODULE$.createLootDisk(value, key, false, Loot$.MODULE$.createLootDisk$default$4()), BoxesRunTime.boxToInteger(1)));
                    }
                }

            } catch (Throwable var14) {
                var14.printStackTrace();
                li.cil.oc.OpenComputers.log().warn((new StringBuilder()).append("Bad loot descriptor: ").append(value).toString(), var14);
            }
        }

        ci.cancel();
    }

    @Inject(method = "createLootDisk",at = @At("HEAD"),cancellable = true,remap = false)
    private void createEnvironment(String name, String path, boolean external, Option<EnumDyeColor> color, CallbackInfoReturnable<ItemStack> cir) {
        Loot$ loot$ = (Loot$)(Object)this;
        Callable callable = (Callable<FileSystem>) () -> li.cil.oc.api.FileSystem.fromClass(TTMain.class, "tiagthings", (new StringBuilder()).append("disks/").append(path).toString());

        final class NamelessClass_6 extends AbstractFunction0<EnumDyeColor> implements Serializable {
            public final EnumDyeColor apply() {
                return EnumDyeColor.SILVER;
            }

            public NamelessClass_6() {
            }
        }

        ItemStack stack = loot$.registerLootDisk(path, (EnumDyeColor)color.getOrElse(new NamelessClass_6()), callable, true);
        stack.setStackDisplayName(name);
        if (!external) {
            li.cil.oc.common.init.Items.registerStack(stack, path);
        }

        cir.setReturnValue(stack);
    }

}
