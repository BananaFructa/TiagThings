package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class RotaryStorageWrapper extends RotaryStorage {

    RotaryStorage inner;
    PortType type;

    public RotaryStorageWrapper(RotaryStorage inner, PortType type) {
        super();
        this.inner = inner;
    }

    public float getTorque() {
        return inner.getTorque();
    }

    public void setTorque(float torque) {
        inner.setTorque(torque);
    }

    public float getRotationSpeed() {
        return inner.getRotationSpeed();
    }

    public void setRotationSpeed(float rpm) {
        inner.setRotationSpeed(rpm);
    }

    public IRotaryEnergy.RotationSide getSide(@Nullable EnumFacing facing) {
        return (type == PortType.INPUT ? RotationSide.INPUT : RotationSide.OUTPUT);
    }

}
