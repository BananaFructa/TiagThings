package BananaFructa.TiagThings;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;
import java.util.Objects;

public class ChunkPos implements Serializable {

    public int x,z;

    public ChunkPos(int x,int z) {
        this.x= x;
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChunkPos) {
            ChunkPos other = (ChunkPos) obj;
            return x == other.x && z == other.z;
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("x",x);
        nbt.setInteger("z",z);
        return nbt;
    }

    public static ChunkPos fromNBT(NBTTagCompound nbt) {
        int x = nbt.getInteger("x");
        int z = nbt.getInteger("z");
        return new ChunkPos(x,z);
    }
}
