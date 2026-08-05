package BananaFructa.OpenComputers.luac;

import BananaFructa.OpenComputers.ResearchManager;
import BananaFructa.TiagThings.Utils;
import li.cil.oc.api.Driver;
import li.cil.oc.api.driver.DriverItem;
import li.cil.oc.api.driver.item.Memory;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.server.machine.ArchitectureAPI;
import li.cil.oc.server.machine.luac.*;
import li.cil.repack.com.naef.jnlua.LuaState;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.IOUtils;
import scala.MatchError;
import scala.Option;
import scala.Serializable;
import scala.Some;
import scala.runtime.AbstractFunction2;
import scala.runtime.BoxedUnit;
import scala.runtime.BoxesRunTime;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TiagNativeLuaArchitecture extends NativeLua53Architecture {

    public int tier = -1;
    public int researchRemaining = 5000;

    public TiagNativeLuaArchitecture(Machine machine) {
        super(machine);
        NativeLuaAPI[] newapis = (NativeLuaAPI[])((Object[])(new NativeLuaAPI[]{new ComponentAPI(this), new ComputerAPI(this), new OSAPI(this), new SystemAPI(this), new UnicodeAPI(this), new UserdataAPI(this), new ResearchAPI(this), Utils.readDeclaredField(NativeLuaArchitecture.class,this,"persistence")}));
        Utils.writeDeclaredField(NativeLuaArchitecture.class,this,"apis",newapis,true);
    }

    @Override
    public ExecutionResult runThreaded(boolean isSynchronizedReturn) {
        ResearchManager.addThread(Thread.currentThread(),tier);
        ExecutionResult result = super.runThreaded(isSynchronizedReturn);
        ResearchManager.removeThread(Thread.currentThread());
        return result;
    }

    static Method memoryInBytes = Utils.getDeclaredMethod(NativeLuaArchitecture.class,"memoryInBytes", Iterable.class);

    public boolean recomputeMemory(Iterable<ItemStack> components) {
        int memoryBytes = 0;
        try {
            memoryBytes = (int) memoryInBytes.invoke(this,components);
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
        LuaState state = this.lua();
        if (state != null) {
            LuaState l = state;
            if (li.cil.oc.Settings.get().limitMemory()) {
                l.setTotalMemory(Integer.MAX_VALUE);
                if (this.kernelMemory() > 0) {
                    boolean researching = ResearchManager.isResearching(this.lua());
                    int usedMemory = this.lua().getTotalMemory() - this.lua().getFreeMemory();
                    if (researching && false) { // TODO: removefalse
                        l.setTotalMemory(usedMemory + researchRemaining);
                    } else {
                        l.setTotalMemory(this.kernelMemory() + (int) Math.ceil((double) memoryBytes * this.ramScale()));
                    }
                }

                return memoryBytes > 0;
            }
        }

        return memoryBytes > 0;
    }

    public boolean initialize() {
        Option var1 = this.factory().createState();
        if (var1 == null) {
            this.lua_$eq((LuaState)null);
            this.machine().crash("native libraries not available");
            return false;
        } else if (var1 instanceof Some) {
            Some var3 = (Some)var1;
            LuaState value = (LuaState)var3.x();
            this.lua_$eq(value);
            this.ramScale_$eq(this.lua().getPointerWidth() >= 8 ? li.cil.oc.Settings.get().ramScaleFor64Bit() : (double)1.0F);

            String machineSrc = "/assets/tiagthings/lua/machine.lua";

            Arrays.stream(((NativeLuaAPI[]) Utils.readDeclaredField(NativeLuaArchitecture.class, this, "apis"))).forEach(ArchitectureAPI::initialize);
            try {
                this.lua().load(li.cil.oc.server.machine.Machine.class.getResourceAsStream(machineSrc), "=machine", "t");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.lua().newThread();
            return true;
        } else {
            throw new MatchError(var1);
        }
    }
}
