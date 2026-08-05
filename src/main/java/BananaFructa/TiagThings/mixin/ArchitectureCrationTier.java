package BananaFructa.TiagThings.mixin;

import BananaFructa.OpenComputers.luac.TiagNativeLuaArchitecture;
import BananaFructa.OpenComputers.luaj.TiagLuaJLuaArchitecture;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.machine.Architecture;
import li.cil.oc.api.machine.MachineHost;
import li.cil.oc.integration.opencomputers.DriverCPU;
import li.cil.oc.server.machine.Machine;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "li.cil.oc.server.machine.Machine")
public class ArchitectureCrationTier {

    @Shadow(remap = false)
    private Architecture architecture;

    @Inject(method = "onHostChanged", at = @At("TAIL"), remap = false)
    public void onHostChanged(CallbackInfo ci) {
        Machine machine = (Machine) (Object) this;
        MachineHost host = machine.host();
        for (ItemStack stack : host.internalComponents()) {
            DriverItem di = Driver.driverFor(stack,host.getClass());
            if (di instanceof DriverCPU) {
                DriverCPU cpu = (DriverCPU) di;
                Architecture architecture = machine.architecture();
                if (architecture instanceof TiagLuaJLuaArchitecture) {
                    ((TiagLuaJLuaArchitecture) architecture).tier = cpu.tier(stack);
                    System.out.println("SET TIER " + cpu.tier(stack));
                    break;
                }
                if (architecture instanceof TiagNativeLuaArchitecture) {
                    ((TiagNativeLuaArchitecture) architecture).tier = cpu.tier(stack);
                    System.out.println("SET TIER " + cpu.tier(stack));
                    break;
                }
            }
        }
    }

}
