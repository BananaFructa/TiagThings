package BananaFructa.TiagThings;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class SRBlockPos implements Serializable {
    public int x,y,z;

    SRBlockPos(BlockPos blockPos) {
        x = blockPos.getX();
        y = blockPos.getY();
        z = blockPos.getZ();
    }

    BlockPos toBlockPos() {
        return new BlockPos(x,y,z);
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("x",x);
        nbt.setInteger("y",y);
        nbt.setInteger("z",z);
        return nbt;
    }

    public static SRBlockPos fromNBT(NBTTagCompound nbt) {
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");
        return new SRBlockPos(new BlockPos(x,y,z));
    }

}
