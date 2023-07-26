package BananaFructa.TiagThings;

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

}
