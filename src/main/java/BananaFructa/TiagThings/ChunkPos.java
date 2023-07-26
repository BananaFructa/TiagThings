package BananaFructa.TiagThings;

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
}
