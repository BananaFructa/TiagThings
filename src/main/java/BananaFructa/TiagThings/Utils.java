package BananaFructa.TiagThings;

import BananaFructa.tfcfarming.Config;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class Utils {
    public static <T> T readDeclaredField(Class<?> targetType, Object target, String name) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public static void writeDeclaredField(Class<?> targetType, Object target, String name, Object value,boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.set(target,value);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static void writeDeclaredDouble(Class<?> targetType, Object target, String name, double value,boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.setDouble(target,value);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public static Method getDeclaredMethod(Class<?> targetClass, String name, Class<?>... parameters) {
        try {
            Method m = targetClass.getDeclaredMethod(name, parameters);
            m.setAccessible(true);
            return m;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static ItemStack itemStackFromCTId(String id) {
        id = id.replace("<","").replace(">","");
        String[] s = id.split(":");
        Item item;
        int type = 0;
        if (s[s.length - 1].matches("[0-9]+")) {
            item = Item.REGISTRY.getObject(new ResourceLocation(String.join(":", Arrays.copyOfRange(s,0,s.length-1))));
            type = Integer.parseInt(s[s.length - 1]);
        } else {
            item = Item.REGISTRY.getObject(new ResourceLocation(id));
        }
        return new ItemStack(item,1,type);
    }
}
