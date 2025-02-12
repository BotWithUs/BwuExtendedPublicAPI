package net.botwithus.api.game.script.v2.permissive.node.leaf;

import net.botwithus.api.game.script.v2.permissive.node.LeafNode;
import net.botwithus.rs3.script.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ChainedActionLeaf extends LeafNode {
    private final List<Action> actions;
    private int currentActionIndex = 0;
    private int currentTicks = 0;
    private boolean validationState = false;

    public ChainedActionLeaf(Script script, Action... actions) {
        super(script);
        this.actions = new ArrayList<>(List.of(actions));
    }

    public ChainedActionLeaf(Script script, String desc, Action... actions) {
        super(script, desc);
        this.actions = new ArrayList<>(List.of(actions));
    }

    @Override
    public void execute() {
        if (currentActionIndex >= actions.size()) {
            // All actions completed successfully
            validationState = true;
            return;
        }

        Action currentAction = actions.get(currentActionIndex);
        
        try {
            if (currentAction.callable.call()) {
                // Action succeeded, move to next action
                currentActionIndex++;
                currentTicks = 0;
                validationState = currentActionIndex >= actions.size();
            } else {
                currentTicks++;
                if (currentTicks >= currentAction.timeoutTicks) {
                    // Action timed out, fail the chain
                    validationState = false;
                    currentActionIndex = 0;
                    currentTicks = 0;
                } else {
                    // Still waiting for action to complete
                    validationState = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            validationState = false;
            currentActionIndex = 0;
            currentTicks = 0;
        }
    }

    @Override
    public boolean validate() {
        return validationState;
    }

    public static class Action {
        private final Callable<Boolean> callable;
        private final int timeoutTicks;

        public Action(Callable<Boolean> callable, int timeoutTicks) {
            this.callable = callable;
            this.timeoutTicks = timeoutTicks;
        }
    }

    public static class Builder {
        private final List<Action> actions = new ArrayList<>();
        private final Script script;
        private String description;

        public Builder(Script script) {
            this.script = script;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addAction(Callable<Boolean> callable, int timeoutTicks) {
            actions.add(new Action(callable, timeoutTicks));
            return this;
        }

        public ChainedActionLeaf build() {
            Action[] actionsArray = actions.toArray(new Action[0]);
            return description != null ? 
                new ChainedActionLeaf(script, description, actionsArray) :
                new ChainedActionLeaf(script, actionsArray);
        }
    }
}
