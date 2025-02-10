package net.botwithus.api.game.script.v2.permissive.base;

import net.botwithus.api.game.script.v2.base.DelayableScript;
import net.botwithus.api.game.script.v2.permissive.node.Branch;
import net.botwithus.api.game.script.v2.permissive.node.TreeNode;
import net.botwithus.api.game.script.v2.permissive.node.leaf.ChainedActionLeaf;
import net.botwithus.internal.scripts.ScriptDefinition;
import net.botwithus.rs3.script.config.ScriptConfig;

public abstract class PermissiveScript extends DelayableScript {

    public PermissiveScript(String scriptName, ScriptConfig scriptConfig, ScriptDefinition scriptDef) {
        super(scriptName, scriptConfig, scriptDef);
    }

    private Branch rootTask;
    private ChainedActionLeaf activeChainedAction = null;

    /***
     * Main game tick logic
     */
    @Override
    public void doRun() {
        if (!onPreTick()) {
            return;
        }

        // If we have an active chained action, continue executing it
        if (activeChainedAction != null) {
            try {
                activeChainedAction.execute();
                if (activeChainedAction.validate()) {
                    // Chain completed successfully
                    activeChainedAction = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                activeChainedAction = null;
            }
            return;
        }

        if (rootTask != null) {
            traverseAndExecute(rootTask);
        } else {
            rootTask = getRootNode();
        }
    }

    /**
     * Traverses the tree, executes all nodes, and checks for ChainedActionLeaf nodes.
     * If a ChainedActionLeaf is found and not validated, it becomes the active chained action.
     */
    private void traverseAndExecute(TreeNode node) {
        if (node == null) {
            return;
        }

        // Execute the node first
        try {
            node.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Check if it's a ChainedActionLeaf that needs to become active
        if (node instanceof ChainedActionLeaf chainedAction && !chainedAction.validate()) {
            activeChainedAction = chainedAction;
            return;
        }

        // Continue traversal if not a leaf node
        if (!node.isLeaf()) {
            if (node.validate()) {
                traverseAndExecute(node.successNode());
            } else {
                traverseAndExecute(node.failureNode());
            }
        }
    }

    public abstract Branch getRootNode();

    /***
     * Code that runs before the main game tick logic
     */
    public boolean onPreTick() {
        return true;
    }
}
