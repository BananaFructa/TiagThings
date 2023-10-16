package BananaFructa.TiagThings;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityRocketScaffold;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public static boolean placedInNonWorkingScaffold(World world, BlockPos pos) {
        TileEntityRocketScaffold teScaffold = null;
        for (int i = 0;i < 10;i++) {
            TileEntity te = world.getTileEntity(pos.offset(EnumFacing.EAST,i));
            if (te instanceof TileEntityRocketScaffold) {
                teScaffold = (TileEntityRocketScaffold) te;
                break;
            }
        }
        if (teScaffold == null || (teScaffold = teScaffold.master()) == null) return false;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int yLower = teScaffold.getBlockPosForPos(0).getY();
        int yUpper = teScaffold.getBlockPosForPos(11661).getY();
        if (y < yLower || y > yUpper) return false;
        BlockPos[] corners = new BlockPos[]{
                teScaffold.getBlockPosForPos(144),
                teScaffold.getBlockPosForPos(14),
                teScaffold.getBlockPosForPos(24),
                teScaffold.getBlockPosForPos(154)
        };
        
        for (BlockPos bp : corners) {
            int bx = bp.getX();
            int bz = bp.getZ();
            if (Math.abs(bx - x) > 9 || Math.abs(bz - z) > 9) return false;
        }

        return !teScaffold.isWorking();
    }
}
