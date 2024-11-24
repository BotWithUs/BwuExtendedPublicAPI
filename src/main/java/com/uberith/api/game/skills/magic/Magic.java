package com.uberith.api.game.skills.magic;

import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.SelectableAction;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;

/**
 * Exposes methods for interacting with the Magic skill, including spell-casting functionalities.
 */
public final class Magic {

    private Magic() {
        // Private constructor to prevent instantiation
    }

    private static final int CRYSTALLISE_SPELL_ID = 109641781;

    /**
     * Casts the Crystallise spell on a specified game object, such as a tree or resource node.
     *
     * @param targetName The name of the target object to cast Crystallise on.
     * @return True if the spell was successfully cast; false otherwise.
     */
    public static boolean castCrystallise(String targetName) {
        SceneObject targetObject = findTargetObject(targetName);
        if (targetObject == null) {
            System.out.println("[Magic] No valid target found with name: " + targetName);
            return false;
        }

        // Select Crystallise spell
        if (!MiniMenu.interact(SelectableAction.SELECTABLE_COMPONENT.getType(), 0, -1, CRYSTALLISE_SPELL_ID)) {
            System.out.println("[Magic] Failed to select Crystallise spell.");
            return false;
        }

        // Attempt to cast Crystallise on the target
        boolean castSuccessful = targetObject.getCoordinate() != null && MiniMenu.interact(
                SelectableAction.SELECT_OBJECT.getType(),
                targetObject.getId(),
                targetObject.getCoordinate().getX(),
                targetObject.getCoordinate().getY()
        );

        if (castSuccessful) {
            System.out.println("[Magic] Successfully cast Crystallise on " + targetObject.getName() +
                    " at (" + targetObject.getCoordinate().getX() + ", " +
                    targetObject.getCoordinate().getY() + ").");
            Execution.delay(500); // Fixed delay of 500ms
            return true;
        } else {
            System.out.println("[Magic] Failed to cast Crystallise on " + targetObject.getName());
            return false;
        }
    }

    /**
     * Searches for a game object by name in the game scene.
     *
     * @param targetName The name of the target object to locate.
     * @return The SceneObject representing the target, or null if not found.
     */
    private static SceneObject findTargetObject(String targetName) {
        return SceneObjectQuery.newQuery()
                .name(targetName)
                .results()
                .nearest();
    }
}
