package BananaFructa.TiagThings;

import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityRocketScaffold;
import BananaFructa.Uem.DrainFluidPlacer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class Utils {

    public static class InstanceField<T> {
        Field f;
        Object parent;

        public InstanceField(Field exposedField, Object parent) {
            this.f = exposedField;
            this.parent = parent;
        }

        public T get() {
            try {
                return (T)f.get(parent);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public T set(T value) {
            try {
                f.set(parent,value);
                return value;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

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

    public static <T> InstanceField<T> getAccessibleField(Class<?> targetType, Object target, String name, boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }

            return new InstanceField<T>(f,target);

        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
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

    public static ItemStack itemStackFromCTId(String id,int amount) {
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
        return new ItemStack(item,amount,type);
    }

    public static Fluid fluidFromCTId(String name) {
        return FluidRegistry.getFluid(name.replace("<","").replace(">","").replace("liquid:",""));
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

    private static long ft = 0L;

    static {
        for(int i = 27; i >= 0; --i) {
            ft >>= i;
            ft |= 1L;
            ft <<= i;
        }

    }

    public static long convertPosition(int x, int z) {
        long v = 0L;
        v |= (long)(x + 30000000);
        v <<= 28;
        v |= (long)(z + 30000000);
        return v;
    }

    public Tuple<Integer, Integer> getPosition(long v) {
        int z = (int)(v & ft) - 30000000;
        v >>= 28;
        int x = (int)(v & ft) - 30000000;
        return new Tuple(x, z);
    }

    public static List<TreeSet<Long>> getRestrictedFluidSources(Chunk chunk,int y) {
        List<TreeSet<Long>> sets = new ArrayList<>();
        for (TileEntity te : chunk.getTileEntityMap().values()) {
            if (te instanceof DrainFluidPlacer) {
                DrainFluidPlacer dfp = (DrainFluidPlacer) te;
                if (dfp.getPos().getY() != y) continue;
                sets.add(dfp.restrictedFluidSources);
            }
        }
        return sets;
    }

    public static List<TreeSet<Long>> getRestrictedFluidsInArea(World w,BlockPos p,int checkArea) {
        List<TreeSet<Long>> restrictionSets = new ArrayList<>();
        net.minecraft.util.math.ChunkPos origin = new net.minecraft.util.math.ChunkPos(p);

        for (int x = -checkArea; x<=checkArea;x++) {
            for (int z = -checkArea;z <= checkArea;z++) {
                restrictionSets.addAll(Utils.getRestrictedFluidSources(w.getChunkFromChunkCoords(origin.x+x,origin.z+z),p.getY()));
            }
        }

        return restrictionSets;
    }

    public static boolean ItemStacksEqual(ItemStack s1,ItemStack s2) {
        if (s1.getCount() != s2.getCount()) return false;
        if (s1.getMetadata() != s2.getMetadata()) return false;
        if (s1.getItem() != s2.getItem()) return false;
        return true;
    }

    public static boolean ItemStacksEqualNoCount(ItemStack s1,ItemStack s2) {
        if (s1.getMetadata() != s2.getMetadata()) return false;
        if (s1.getItem() != s2.getItem()) return false;
        return true;
    }
}
