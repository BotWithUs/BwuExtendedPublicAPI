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

}
