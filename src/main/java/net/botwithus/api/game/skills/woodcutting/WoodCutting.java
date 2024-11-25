package net.botwithus.api.game.skills.woodcutting;

import net.botwithus.api.game.hud.inventories.Backpack;
import net.botwithus.api.game.hud.inventories.LootInventory;
import net.botwithus.api.game.world.Coordinates;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.movement.Movement;
import net.botwithus.rs3.game.movement.NavPath;
import net.botwithus.rs3.game.movement.TraverseEvent;
import net.botwithus.rs3.game.queries.builders.items.GroundItemQuery;
import net.botwithus.rs3.game.queries.builders.objects.SceneObjectQuery;
import net.botwithus.rs3.game.scene.entities.object.SceneObject;
import net.botwithus.rs3.script.Execution;

import java.util.List;

public class WoodCutting {

    private static final List<String> WOOD_BOX_NAMES = List.of(
            "Wood box", "Oak wood box", "Willow wood box", "Teak wood box", "Maple wood box",
            "Acadia wood box", "Mahogany wood box", "Yew wood box", "Magic wood box", "Elder wood box"
    );

    private static final int CHOP_ANIMATION_ID = 21187;

    /**
     * Checks if an Elder Tree is available for chopping.
     *
     * @return True if an Elder Tree is nearby and available for interaction, false otherwise.
     */
    public boolean isElderTreeAvailable() {
        SceneObject elderTree = SceneObjectQuery.newQuery()
                .name("Elder tree")
                .option("Chop down")
                .hidden(false)
                .results()
                .nearest();
        return elderTree != null;
    }

    /**
     * Picks up a nearby bird's nest if available.
     *
     * @return True if a bird's nest was successfully picked up, false otherwise.
     */
    public boolean pickupBirdsNest() {
        var birdNestQuery = GroundItemQuery.newQuery().name("Bird's nest");
        var birdNest = birdNestQuery.results().nearestTo(Client.getLocalPlayer());

        if (birdNest != null) {
            if (birdNest.interact("Take")) {
                Execution.delay(1000); // Wait for potential loot interface
                if (LootInventory.isOpen()) {
                    LootInventory.take("Bird's nest");
                    LootInventory.close(true);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Attempts to fill a wood box if one exists in the inventory.
     *
     * @return True if the wood box was successfully filled, false otherwise.
     */
    public boolean attemptFillWoodBox() {
        for (String woodBox : WOOD_BOX_NAMES) {
            if (Backpack.contains(woodBox)) {
                if (Backpack.interact(woodBox, "Fill")) {
                    Execution.delay(1000); // Wait for inventory update
                    return !Backpack.isFull();
                }
            }
        }
        return false;
    }

    /**
     * Interacts with a tree to start chopping it.
     *
     * @param tree        The tree to interact with.
     * @param interaction The interaction option (e.g., "Chop down", "Cut down").
     * @return True if the interaction was successful, false otherwise.
     */
    public boolean interactWithTree(SceneObject tree, String interaction) {
        return tree.interact(interaction);
    }

    /**
     * Searches for the nearest tree of a specific type.
     *
     * @param treeName The name of the tree to search for.
     * @return The nearest tree that matches the specified name, or null if none are found.
     */
    public SceneObject findNearestTree(String treeName) {
        var playerCoordinate = Client.getLocalPlayer() != null
                ? Client.getLocalPlayer().getCoordinate()
                : null;
        if (playerCoordinate == null) return null;

        return SceneObjectQuery.newQuery()
                .name(treeName)
                .results()
                .nearestTo(Client.getLocalPlayer());
    }

    /**
     * Checks if the player is actively chopping a tree by verifying the current animation ID.
     *
     * @return True if the player is currently chopping, false otherwise.
     */
    public boolean isPlayerChopping() {
        return Client.getLocalPlayer() != null &&
                Client.getLocalPlayer().getAnimationId() == CHOP_ANIMATION_ID;
    }

    /**
     * Moves the player to a specified tree if they are not already within a given radius.
     *
     * @param treeCoordinates The coordinates of the tree.
     * @param searchRadius    The radius within which the player is considered close enough.
     * @return True if the player is within the radius, false otherwise.
     */
    public boolean moveToTreeIfFar(Coordinate treeCoordinates, int searchRadius) {
        var coordinates = new Coordinates();
        if (!coordinates.isPlayerWithinRadius(
                treeCoordinates.getX(),
                treeCoordinates.getY(),
                treeCoordinates.getZ(),
                searchRadius
        )) {
            // Add some randomization to simulate human-like movement
            int randomXOffset = (int) (Math.random() * 5) - 2; // Random offset between -2 and 2
            int randomYOffset = (int) (Math.random() * 5) - 2;

            var targetX = treeCoordinates.getX() + randomXOffset;
            var targetY = treeCoordinates.getY() + randomYOffset;

            // Use the Movement API to traverse to the target coordinate
            var targetCoordinate = new Coordinate(targetX, targetY, treeCoordinates.getZ());
            var path = NavPath.resolve(targetCoordinate);

            if (path != null) {
                TraverseEvent.State state = Movement.traverse(path);
                // Check the state to determine if traversal succeeded
                return state == TraverseEvent.State.FINISHED;
            }
        }
        return true;
    }


}
