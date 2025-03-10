package net.botwithus.api.game.script.v2.permissive.node;


import java.util.Arrays;
import java.util.concurrent.Callable;

import net.botwithus.api.game.script.v2.permissive.Interlock;
import net.botwithus.rs3.script.Script;

public class Branch extends TreeNode {
    private Interlock[] interlocks = null;
    private Interlock activeInterlock = null;

    private Callable<Interlock[]> interlocksC;
    private TreeNode successNode, failureNode;
    private Callable<TreeNode> successNodeC, failureNodeC;

    public Branch (Script script, String desc, Interlock... Interlocks) {
        super(script, desc);
        this.interlocks = Interlocks;
    }

    public Branch(Script script, String desc, Callable<TreeNode> successNode, TreeNode failureNode, Interlock... interlocks) {
        super(script, desc);
        this.interlocks = interlocks;
        this.successNodeC = successNode;
        this.failureNode = failureNode;
    }
    public Branch(Script script, String desc, TreeNode successNode, Callable<TreeNode> failureNode, Interlock... interlocks) {
        super(script, desc);
        this.interlocks = interlocks;
        this.successNode = successNode;
        this.failureNodeC = failureNode;
    }
    public Branch(Script script, String desc, Callable<TreeNode> successNode, Callable<TreeNode> failureNode, Interlock... interlocks) {
        super(script, desc);
        this.interlocks = interlocks;
        this.successNodeC = successNode;
        this.failureNodeC = failureNode;
    }
    public Branch(Script script, String desc, TreeNode successNode, TreeNode failureNode, Interlock... interlocks) {
        super(script, desc);
        this.interlocks = interlocks;
        this.successNode = successNode;
        this.failureNode = failureNode;
    }
    public Branch(Script script, String desc, TreeNode successNode, TreeNode failureNode, Callable<Interlock[]> interlocks) {
        super(script, desc);
        this.interlocksC = interlocks;
        this.successNode = successNode;
        this.failureNode = failureNode;
    }
    public Branch(Script script, String desc, String definedIn, Callable<TreeNode> successNode, TreeNode failureNode, Interlock... interlocks) {
        super(script, desc, definedIn);
        this.interlocks = interlocks;
        this.successNodeC = successNode;
        this.failureNode = failureNode;
    }
    public Branch(Script script, String desc, String definedIn, TreeNode successNode, Callable<TreeNode> failureNode, Interlock... interlocks) {
        super(script, desc, definedIn);
        this.interlocks = interlocks;
        this.successNode = successNode;
        this.failureNodeC = failureNode;
    }
    public Branch(Script script, String desc, String definedIn, Callable<TreeNode> successNode, Callable<TreeNode> failureNode, Interlock... interlocks) {
        super(script, desc, definedIn);
        this.interlocks = interlocks;
        this.successNodeC = successNode;
        this.failureNodeC = failureNode;
    }
    public Branch(Script script, String desc, String definedIn, TreeNode successNode, TreeNode failureNode, Interlock... interlocks) {
        super(script, desc, definedIn);
        this.interlocks = interlocks;
        this.successNode = successNode;
        this.failureNode = failureNode;
    }
    public Branch(Script script, String desc, String definedIn, TreeNode successNode, TreeNode failureNode, Callable<Interlock[]> interlocks) {
        super(script, desc, definedIn);
        this.interlocksC = interlocks;
        this.successNode = successNode;
        this.failureNode = failureNode;
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean validate() {
        updateInterlocks();
        if (interlocks == null || interlocks.length == 0) {
            return false;
        }
        activeInterlock = Arrays.stream(interlocks).filter(Interlock::isActive).findFirst().orElse(null);
        setLatestValidate(activeInterlock != null);
        return activeInterlock != null;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode successNode(){
        if (successNodeC != null) {
            try {
                successNode = successNodeC.call();
            } catch (Exception e){
//                log.log(Level.SEVERE, "Failed to determine the result of the Callable<successNodeC>", e);
            }
        }
        return successNode;
    }

    /** {@inheritDoc} */
    @Override
    public TreeNode failureNode() {
        if (failureNodeC != null) {
            try {
                failureNode = failureNodeC.call();
            } catch (Exception e){
//                log.log(Level.SEVERE, "Failed to determine the result of the Callable<failureNodeC>", e);
            }
        }
        return failureNode;
    }

    public Interlock[] getInterlocks() {
        return interlocks;
    }

    public Interlock[] updateInterlocks() {
        if (interlocksC != null) {
            try {
                interlocks = interlocksC.call();
            } catch (Exception e){
//                log.log(Level.SEVERE, "Failed to determine the result of the Callable<interlocksC>", e);
            }
        }
        return interlocks;
    }

    /**
     * Creates a copy of this branch with new success and failure nodes
     * @param newSuccessNode The new success node
     * @param newFailureNode The new failure node
     * @return A new Branch with the same interlocks but different success/failure nodes
     */
    public Branch newWithNodes(TreeNode newSuccessNode, TreeNode newFailureNode) {
        return new Branch(
                getScript(),
                getDesc(),
                getDefinedIn(),
                newSuccessNode,
                newFailureNode,
                interlocks != null ? interlocks.clone() : null
        );
    }

    /**
     * Creates a copy of this branch with new success and failure nodes using Callables
     * @param newSuccessNode The new success node callable
     * @param newFailureNode The new failure node callable
     * @return A new Branch with the same interlocks but different success/failure nodes
     */
    public Branch newWithNodes(Callable<TreeNode> newSuccessNode, Callable<TreeNode> newFailureNode) {
        return new Branch(
                getScript(),
                getDesc(),
                getDefinedIn(),
                newSuccessNode,
                newFailureNode,
                interlocks != null ? interlocks.clone() : null
        );
    }

    /**
     * Creates a copy of this branch with a new success node
     * @param newSuccessNode The new success node
     * @return A new Branch with the same interlocks and failure node but different success node
     */
    public Branch newWithSuccessNode(TreeNode newSuccessNode) {
        return new Branch(
                getScript(),
                getDesc(),
                getDefinedIn(),
                newSuccessNode,
                failureNode,
                interlocks != null ? interlocks.clone() : null
        );
    }

    /**
     * Creates a copy of this branch with a new failure node
     * @param newFailureNode The new failure node
     * @return A new Branch with the same interlocks and success node but different failure node
     */
    public Branch newWithFailureNode(TreeNode newFailureNode) {
        return new Branch(
                getScript(),
                getDesc(),
                getDefinedIn(),
                successNode,
                newFailureNode,
                interlocks != null ? interlocks.clone() : null
        );
    }

    /**
     * Sets the children nodes of this branch
     * @param successNode The new success node
     * @param failureNode The new failure node
     */
    public void setChildrenNodes(TreeNode successNode, TreeNode failureNode) {
        this.successNode = successNode;
        this.failureNode = failureNode;
    }
}
