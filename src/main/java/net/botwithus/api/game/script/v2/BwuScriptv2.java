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

    // Map for states, with a name key for each state
    private final Map<String, State> states = new HashMap<>();
    private State currentState;

    public BwuScriptv2(String scriptName, ScriptConfig scriptConfig, ScriptDefinition scriptDef) {
        super(scriptName, scriptConfig, scriptDef);
    }

    /**
     * Initialize the script with the given states.
     * @param state The states to initialize the script with.
     */
    public void initStates(State... state){
        this.currentState = state[0];
        for (State s : state) {
            states.put(s.getName(), s);
        }
    }

    /**
     * Get the state with the given name.
     * @param name The name of the state to get.
     * @return The state with the given name.
     */
    public State getState(String name){
        return states.get(name);
    }

    @Override
    public boolean onInitialize() {
        this.sgc = new BwuGraphicsContext(getConsole(), this);
        try {
            performLoadPersistentData();
        } catch (Exception e) {
            LOG.atSevere().withCause(e).log("Failed to load persistent data");
        }
        return super.initialize();
    }

    @Override
    public Branch getRootNode() {
        return currentState != null ? currentState.getNode() : null;
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

    public State getCurrentState() {
        return currentState;
    }

    // Methods for managing states
    public void addState(State... state) {
        for (State s : state) {
            states.put(s.getName(), s);
        }
    }

    public void setCurrentState(String name) {
        currentState = states.get(name);
    }

    public String getStatus() {
        return currentState != null ? currentState.getStatus() : null;
    }

    public boolean setStatus(String status) {
        if (currentState != null) {
            currentState.setStatus(status);
            return true;
        }
        return false;
    }

    public static class State {
        private String name, status;
        private Branch node;

        public State(String name) {
            this.name = name;
        }

        public State(String name, Branch node) {
            this.name = name;
            this.node = node;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Branch getNode() {
            return node;
        }

        public void setNode(Branch node) {
            this.node = node;
        }
    }
}
