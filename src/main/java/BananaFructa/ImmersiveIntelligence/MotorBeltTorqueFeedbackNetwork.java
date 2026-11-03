package BananaFructa.ImmersiveIntelligence;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryConnector;
import pl.pabilo8.immersiveintelligence.api.rotary.MotorBeltNetwork;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MotorBeltTorqueFeedbackNetwork extends MotorBeltNetwork {



    private float torqueLoad = 0;

    public void removeFromNetwork(IRotaryConnector removedConnector) {
        Iterator<WeakReference<IRotaryConnector>> iterator = this.connectors.iterator();
        Set<MotorBeltNetwork> knownNets = new HashSet();

        while(iterator.hasNext()) {
            WeakReference<IRotaryConnector> conn = (WeakReference)iterator.next();
            IRotaryConnector start = (IRotaryConnector)conn.get();
            if (start != null && !knownNets.contains(start.getNetwork())) {
                MotorBeltNetwork newNet = new MotorBeltNetwork();
                updateConnectors(Utils.toCC(start), start.getConnectorWorld(), newNet);
                knownNets.add(newNet);
            }
        }

    }

    public void updateValues() {
        torqueLoad = 0;
        Set<IRotaryConnector> validReferences = (Set)this.connectors.stream().map(Reference::get).filter(Objects::nonNull).collect(Collectors.toSet());
        validReferences.stream().filter(ir -> ir instanceof IRotaryLoadConnector).map(ir -> ((IRotaryLoadConnector)ir).getLoadTorque()).forEach((load) -> {
            torqueLoad += load;
        });
    }

}
