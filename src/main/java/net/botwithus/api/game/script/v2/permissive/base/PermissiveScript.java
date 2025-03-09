package net.botwithus.api.game.script.v2.permissive.base;

import lombok.Getter;
import lombok.Setter;
import net.botwithus.api.game.script.v2.base.DelayableScript;
import net.botwithus.api.game.script.v2.permissive.node.Branch;
import net.botwithus.api.game.script.v2.permissive.node.TreeNode;
import net.botwithus.api.game.script.v2.permissive.node.leaf.ChainedActionLeaf;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.script.config.ScriptConfig;

import java.util.HashMap;
import java.util.Map;

public abstract class PermissiveScript extends DelayableScript {
    private boolean debugMode = false;
    private State currentState;

    // Map for states, with a name key for each state
    private final Map<String, State> states = new HashMap<>();

    public PermissiveScript(String scriptName, ScriptConfig scriptConfig, ScriptDefinition scriptDef) {
        super(scriptName, scriptConfig, scriptDef);
    }

    private ChainedActionLeaf activeChainedAction = null;

    /***
     * Main game tick logic
     */
    @Override
    public void doRun() {
        debug("Processing game tick");

        if (!onPreTick()) {
            debug("Pre-tick failed, skipping main tick logic");
            return;
        }

        if (currentState != null) {
            debug("Current state: " + currentState.getName());
        }

        // If we have an active chained action, continue executing it
        if (activeChainedAction != null) {
            try {
                debug("Executing chained action: " + activeChainedAction.getDesc() + "(" + activeChainedAction.getProgress() + ")");
                activeChainedAction.execute();
                if (activeChainedAction.validate()) {
                    // Chain completed successfully
                    debug("Active chained action completed successfully");
                    activeChainedAction = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                debug("Active chained action failed, aborting: " + e.getMessage());
                activeChainedAction = null;
            }
            return;
        }

        try {
            traverseAndExecute(getRootNode());
        } catch (Exception e) {
            e.printStackTrace();
            debug("Root task traversal failed: " + e.getMessage());
        }
    }

    /**
     * Traverses the tree, executes all nodes, and checks for ChainedActionLeaf nodes.
     * If a ChainedActionLeaf is found and not validated, it becomes the active chained action.
     */
    private void traverseAndExecute(TreeNode node) {
        if (node == null) {
            debug("Node is null, skipping tree traversal");
            return;
        }

        // Check if it's a ChainedActionLeaf that needs to become active
        if (node instanceof ChainedActionLeaf chainedAction && !chainedAction.validate()) {
            debug("Chained action found, setting as active: " + chainedAction.getDesc());
            activeChainedAction = chainedAction;
            return;
        }

        // Continue traversal if not a leaf node
        if (!node.isLeaf()) {
            if (node.validate()) {
                debug("Node \"" + node.getDesc() + "\"validated, executing success node -> " + node.successNode().getDesc());
                traverseAndExecute(node.successNode());
            } else {
                debug("Node \"" + node.getDesc() + "\" failed validation, executing failure node -> " + node.failureNode().getDesc());
                traverseAndExecute(node.failureNode());
            }
        } else { // Execute the leaf node
            try {
                debug("Executing leaf node: " + node.getDesc());
                node.execute();
            } catch (Exception e) {
                e.printStackTrace();
                debug("Leaf node failed: " + e.getMessage());
            }
        }
    }

    public Branch getRootNode() {
        return currentState != null ? currentState.getNode() : null;
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

    /***
     * Code that runs before the main game tick logic
     */
    public boolean onPreTick() {
        return true;
    }

    public void debug(String message) {
        if (debugMode) {
            println("[Debug] " + message);
        }
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }


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
