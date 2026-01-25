package BananaFructa.Uem;

import BananaFructa.UnecologicalMethods.Config;
import BananaFructa.UnecologicalMethods.DrainTank;
import BananaFructa.UnecologicalMethods.UEMContent;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.HashMap;

public class DrainPlacerTank extends DrainTank {
    private int universalCapacity = 0;
    private final static HashMap<String, Boolean> bannedCache = new HashMap();

    public DrainPlacerTank(int capacity) {
        super(capacity);
    }

    public int fillInternal(FluidStack resource, boolean doFill) {
        String rn = resource.getFluid().getBlock().getRegistryName().toString();
        int dif;
        int var6;
        if (bannedCache.containsKey(rn)) {
            if ((Boolean) bannedCache.get(rn)) {
                return 0;
            }

            String[] var4 = Config.bannedLiquids;
            dif = var4.length;

            for (var6 = 0; var6 < dif; ++var6) {
                String s = var4[var6];
                if (s.equals(rn)) {
                    bannedCache.put(rn, true);
                    return 0;
                }
            }

            bannedCache.put(rn, false);
        }

        if (this.universalCapacity + resource.amount <= this.getCapacity()) {
            if (doFill) {
                this.universalCapacity += resource.amount;
            }

            return resource.amount;
        } else {
            dif = this.getCapacity() - this.universalCapacity;
            if (doFill) {
                this.universalCapacity = this.getCapacity();
            }

            return dif;
        }
    }

    public void dump(int maxDrain) {
        if (maxDrain <= this.universalCapacity) {
            this.universalCapacity -= maxDrain;
        } else {
            int dif = this.universalCapacity;
            this.universalCapacity = 0;
        }

    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return fluid;
    }

    public int getFluidAmount() {
        return this.universalCapacity;
    }

    public boolean canDrain() {
        return false;
    }

    public void setFluidAmount(int amount) {
        this.universalCapacity = amount;
    }
}
