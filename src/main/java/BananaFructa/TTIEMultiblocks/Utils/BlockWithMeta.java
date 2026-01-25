package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraft.block.Block;

import java.util.Objects;

public class BlockWithMeta {

    Block b;
    int meta;

    public BlockWithMeta(Block b,int meta) {
        this.b = b;
        this.meta = meta;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockWithMeta)) return false;
        BlockWithMeta bm = ((BlockWithMeta)obj);
        return bm.b == b && bm.meta == meta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(b, meta);
    }
}
