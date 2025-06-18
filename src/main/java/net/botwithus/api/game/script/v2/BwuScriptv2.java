package net.botwithus.api.game.script.v2;


import java.util.HashMap;
import java.util.Map;

import com.google.common.flogger.FluentLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.botwithus.api.game.script.v2.permissive.base.PermissiveScript;
import net.botwithus.api.game.script.v2.permissive.node.Branch;
import net.botwithus.api.game.script.v2.ui.BwuGraphicsContext;
import net.botwithus.api.game.script.v2.ui.interfaces.BuildableUI;
import net.botwithus.api.game.script.v2.util.statistic.BotStat;
import net.botwithus.api.util.time.Stopwatch;
import net.botwithus.api.util.time.Timer;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.script.config.ScriptConfig;

public abstract class BwuScriptv2 extends PermissiveScript {
    public static final FluentLogger LOG = FluentLogger.forEnclosingClass();
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public final Stopwatch STOPWATCH = new Stopwatch();
    public BotStat botStatInfo = new BotStat();
    public Timer brokenSessionFailsafeTimer = new Timer(600000, 600000);


    public BwuScriptv2(String scriptName, ScriptConfig scriptConfig, ScriptDefinition scriptDef) {
        super(scriptName, scriptConfig, scriptDef);
    }



    @Override
    public boolean onInitialize() {
        var init = super.onInitialize();
        this.sgc = new BwuGraphicsContext(getConsole(), this);
        try {
            performLoadPersistentData();
        } catch (Exception e) {
            LOG.atSevere().withCause(e).log("Failed to load persistent data");
        }
        return init;
    }

    public void performSavePersistentData() {
        try {
            JsonObject obj = new JsonObject();

            savePersistentData(obj);
            println("Settings: " + obj);

            configuration.addProperty(getName() + "|Settings", obj.toString());
        } catch (Exception e) {
            LOG.atSevere().withCause(e).log("Failed to save persistent data");
        }
    }

    public void performLoadPersistentData() {
        try {
            var settingKey = getName() + "|Settings";
            if (getConfiguration().getProperty(settingKey) != null && !getConfiguration().getProperty(settingKey).equals("null")) {
                var obj = gson.fromJson(getConfiguration().getProperty(settingKey), JsonObject.class);
                loadPersistentData(obj);
            }
        } catch (Exception e) {
            LOG.atSevere().withCause(e).log("Failed to load persistent data");
        }
    }

    public abstract BuildableUI getBuildableUI();
    public abstract void savePersistentData(JsonObject obj);
    public abstract void loadPersistentData(JsonObject obj);
    public abstract String getVersion();

}
