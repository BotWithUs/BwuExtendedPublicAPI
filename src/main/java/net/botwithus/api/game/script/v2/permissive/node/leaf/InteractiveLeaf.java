package net.botwithus.api.game.script.v2.permissive.node.leaf;


import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import net.botwithus.api.game.script.v2.permissive.node.LeafNode;
import net.botwithus.rs3.game.annotations.Interactable;
import net.botwithus.rs3.script.Script;

public class InteractiveLeaf<T extends Interactable<T>> extends LeafNode {


    private T target;
    private int optionIndex = -1;
    private String optionText = "";
    private Runnable successAction = null;
    private Callable<Boolean> successCallable = null;

    /**
     * Constructor to initialize InteractiveLeaf with a target.
     *
     * @param script  The parent script.
     * @param successAction The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, Runnable successAction) {
        super(script);
        this.successAction = successAction;
    }

    /***
     * Constructor to initialize InteractiveLeaf with a target and a description.
     * @param script The parent script.
     * @param desc The description of the leaf node.
     * @param successAction The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, String desc, Runnable successAction) {
        super(script, desc);
        this.successAction = successAction;
    }

    /***
     * Constructor to initialize InteractiveLeaf with a target, a description, and a definedIn string.
     * @param script The parent script.
     * @param desc The description of the leaf node.
     * @param definedIn The definedIn string of the leaf node.
     * @param successAction The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, String desc, String definedIn, Runnable successAction) {
        super(script, desc, definedIn);
        this.successAction = successAction;
    }

    /***
     * Constructor to initialize InteractiveLeaf with a target and a callable logic.
     * @param script The parent script.
     * @param successCallable The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, Callable<Boolean> successCallable) {
        super(script);
        this.successCallable = successCallable;
    }

    /***
     * Constructor to initialize InteractiveLeaf with a target, a description, and a callable logic.
     * @param script The parent script.
     * @param desc The description of the leaf node.
     * @param successCallable The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, String desc, Callable<Boolean> successCallable) {
        super(script, desc);
        this.successCallable = successCallable;
    }

    /***
     * Constructor to initialize InteractiveLeaf with a target, a description, a definedIn string, and a callable logic.
     * @param script The parent script.
     * @param desc The description of the leaf node.
     * @param definedIn The definedIn string of the leaf node.
     * @param successCallable The callable logic for the leaf node.
     */
    public InteractiveLeaf(Script script, String desc, String definedIn, Callable<Boolean> successCallable) {
        super(script, desc, definedIn);
        this.successCallable = successCallable;
    }

    private Callable<Boolean> interact() {
        return () -> {
            if (optionIndex > -1) {
                var val = target.interact(optionIndex);
                if (successAction != null) {
                    successAction.run();
                } else if (successCallable != null) {
                    try {
                        if (successCallable.call()) {
                            return val;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return val;
            } else if (!optionText.isEmpty()) {
                var val = target.interact(optionText);
                if (successAction != null) {
                    successAction.run();
                } else if (successCallable != null) {
                    try {
                        if (successCallable.call()) {
                            return val;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return val;
            }
            return false;
        };
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    /**
     * Interact with the target using a specific option index.
     *
     * @param option The option index to interact with.
     * @return true if the interaction was successful, false otherwise.
     */
    public boolean interact(int option) {
        return target.interact(option);
    }

    /**
     * Interact with the target using a specific option string.
     *
     * @param option The option string to interact with.
     * @return true if the interaction was successful, false otherwise.
     */
    public boolean interact(String option) {
        return target.interact(option);
    }

    /**
     * Interact with the target using a regex pattern.
     * Note: This feature is not yet implemented.
     *
     * @param pattern The pattern to match interaction options.
     * @return true if the interaction was successful, false otherwise.
     * @throws UnsupportedOperationException This feature is not yet implemented
     */
    public boolean interact(Pattern pattern) {
        // return target.interact(pattern);
        throw new UnsupportedOperationException("Pattern-based interaction is not yet implemented");
    }
}
