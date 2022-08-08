package BananaFructa.TiagThings;

import java.lang.reflect.Field;

public class Utils {
    public static void WriteDeclaredField(Class<?> targetType,Object target,String name,Object value) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            f.set(target,value);
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
