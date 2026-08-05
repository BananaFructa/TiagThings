package BananaFructa.OpenComputers.luaj;

import BananaFructa.TiagThings.Utils;
import li.cil.oc.Settings;
import li.cil.oc.api.machine.ExecutionResult;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.machine.Signal;
import li.cil.oc.server.machine.ArchitectureAPI;
import li.cil.oc.server.machine.luaj.*;
import li.cil.repack.org.luaj.vm2.*;
import li.cil.repack.org.luaj.vm2.lib.jse.JsePlatform;
import org.apache.commons.io.IOUtils;
import scala.Serializable;
import scala.collection.mutable.StringBuilder;
import scala.runtime.AbstractFunction1;
import scala.runtime.BoxedUnit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TiagLuaJLuaArchitecture extends LuaJLuaArchitecture {
    public int tier = 0;

    public TiagLuaJLuaArchitecture(Machine machine) {
        super(machine);
        LuaJAPI[] newapis = (LuaJAPI[])((Object[])(new LuaJAPI[]{new ComponentAPI(this), new ComputerAPI(this), new OSAPI(this), new SystemAPI(this), new UnicodeAPI(this), new UserdataAPI(this), new ResearchAPI(this)}));
        Utils.writeDeclaredField(LuaJLuaArchitecture.class,this,"apis",newapis,true);
    }

    public boolean initialize() {
        this.lua_$eq(JsePlatform.debugGlobals());
        this.lua().set("research", LuaValue.NIL);
        this.lua().set("package", LuaValue.NIL);
        this.lua().set("require", LuaValue.NIL);
        this.lua().set("io", LuaValue.NIL);
        this.lua().set("os", LuaValue.NIL);
        this.lua().set("luajava", LuaValue.NIL);
        this.lua().set("dofile", LuaValue.NIL);
        this.lua().set("loadfile", LuaValue.NIL);

        String machineSrc = "/assets/tiagthings/lua/machine.lua";

        InputStream is = li.cil.oc.server.machine.Machine.class.getResourceAsStream(machineSrc);
        try {
            String result = IOUtils.toString(is, StandardCharsets.UTF_8);
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(((LuaJAPI[]) Utils.readDeclaredField(LuaJLuaArchitecture.class, this, "apis"))).forEach(ArchitectureAPI::initialize);
        this.recomputeMemory(this.machine().host().internalComponents());
        LuaValue kernel = this.lua().load(li.cil.oc.server.machine.Machine.class.getResourceAsStream(machineSrc), "=machine", "t", this.lua());
        LuaThread thread = new LuaThread(this.lua(), kernel);
        Utils.writeDeclaredField(LuaJLuaArchitecture.class,this,"thread",thread,false);
        return true;
    }

}
