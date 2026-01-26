package net.botwithus.api.game.script.v2.nav;

import com.google.common.flogger.FluentLogger;
import net.botwithus.api.game.script.v2.permissive.base.PermissiveScript;
import net.botwithus.rs3.game.Area;
import net.botwithus.rs3.game.Client;
import net.botwithus.rs3.game.Coordinate;
import net.botwithus.rs3.game.Distance;
import net.botwithus.rs3.game.minimenu.MiniMenu;
import net.botwithus.rs3.game.minimenu.actions.WalkAction;
import net.botwithus.rs3.game.movement.Movement;
import net.botwithus.rs3.game.movement.NavPath;
import net.botwithus.rs3.game.movement.TraverseEvent;
import net.botwithus.rs3.util.RandomGenerator;

public class Traverse {

    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public static boolean to(PermissiveScript script, Coordinate coordinate) {
        return to(script, coordinate, RandomGenerator.nextInt(12, 20));
    }

    public static boolean to(PermissiveScript script, Coordinate coordinate, int stepSize) {
        var player = Client.getLocalPlayer();
        if (player == null) {
            script.debug("[Traversev2#to]: Player is null");
            return false;
        }

        var pCoord = player.getCoordinate();
        if (pCoord != null && (pCoord.getX() > 6400 || pCoord.getY() > 12800)) {
            script.debug("[Traversev2#to]: Player is in an instance, attempting to walk to: " + coordinate.getX() + " | " + coordinate.getY());
            return bresenhamWalkTo(script, coordinate, true, stepSize);
        }
        TraverseEvent.State webMovement;
        script.debug("[Traversev2#to]: Player is not in an instance, attempting to walk to " + coordinate.getX() + " | " + coordinate.getY() + " | " + (webMovement = Movement.traverse(NavPath.resolve(coordinate))));

        if (webMovement == null || webMovement == TraverseEvent.State.FAILED || webMovement == TraverseEvent.State.NO_PATH) {
            script.debug("[Traverse#to]: WebMovement failed, attempting to walk to via bresenham: " + coordinate.getX() + " | " + coordinate.getY());
            return bresenhamWalkTo(script, coordinate, true, stepSize);
        }

        script.debug("[Traversev2#to]: Traversal failed.");
        return false;
    }

    public static boolean to(PermissiveScript script, Area area) {
        var coordinate = area.getRandomWalkableCoordinate();
        if (coordinate == null || (coordinate.getX() == 0 && coordinate.getY() == 0)) {
            script.debug("[Traversev2#to]: Area#getRandomWalkableCoordinate returned null or (0, 0), picking a random coordinate");
            coordinate = area.getRandomCoordinate();
        }
        return to(script, coordinate);
    }

    public static boolean bresenhamWalkTo(PermissiveScript script, Coordinate coordinate, boolean minimap, int stepSize) {
        var player = Client.getLocalPlayer();
        if (player == null) {
            script.debug("[Traversev2#bresenhamWalkTo]: Player is null");
            return false;
        } else {
            Coordinate currentCoordinate = player.getCoordinate();
            int dx = coordinate.getX() - currentCoordinate.getX();
            int dy = coordinate.getY() - currentCoordinate.getY();
            int distance = (int)Math.hypot(dx, dy);
            if (distance > stepSize) {
                script.debug("[Traversev2#bresenhamWalkTo]: Attempting to walk to " + coordinate.getX() + ", " + coordinate.getY() + ", using Bresenham's line algorithm. Distance of " + distance + " exceeded stepSize of " + stepSize);
                int stepX = currentCoordinate.getX() + dx * stepSize / distance;
                int stepY = currentCoordinate.getY() + dy * stepSize / distance;
                walkTo(script, new Coordinate(stepX, stepY, currentCoordinate.getZ()), minimap);
                return true;
            } else {
                script.debug("[Traversev2#bresenhamWalkTo]: Attempting to walk to " + coordinate.getX() + ", " + coordinate.getY() + ", using Bresenham's line algorithm. Distance of " + distance + " is within stepSize of " + stepSize);
                walkTo(script, coordinate, minimap);
                return true;
            }
        }
    }

    public static boolean walkTo(PermissiveScript script, Coordinate coordinate, boolean minimap) {
        if (coordinate == null) {
            script.debug("[Traversev2#walkTo]: Coordinate is null");
            return false;
        }
        var result = MiniMenu.interact(WalkAction.WALK.getType(), minimap ? 1 : 0, coordinate.getX(), coordinate.getY());
        script.debug("[Traversev2#walkTo]: Attempting to walk to " + coordinate.getX() + " | " + coordinate.getY() + " | " + result);
        script.delayUntil(() -> Distance.to(coordinate) < 5, 6);
        return result;
    }

    /**
     * Traverses to a location using NavPath with configurable movement flags and blocked areas.
     * Any area passed as blocked will be avoided by the pathfinder (walk steps, links, teleports).
     *
     * @param script the permissive script instance for debugging
     * @param coordinate the target coordinate to navigate to
     * @param flags movement flags (e.g., Movement.DISABLE_DIVE, Movement.ENABLE_SURGE, Movement.DISABLE_TELEPORTS)
     * @param blockedAreas areas to block from navigation (walk steps, links, teleports will avoid these areas)
     * @return true if navigation was successful, false if failed
     */
    public static boolean navPathTraverse(PermissiveScript script, Coordinate coordinate, int flags, Area... blockedAreas) {
        var player = Client.getLocalPlayer();
        if (player == null) {
            script.debug("[Traversev2#navPathTraverse]: Player is null");
            return false;
        }

        if (coordinate == null || (coordinate.getX() == 0 && coordinate.getY() == 0)) {
            script.debug("[Traversev2#navPathTraverse]: Coordinate is null or (0, 0)");
            return false;
        }

        NavPath path = NavPath.resolve(coordinate, flags, blockedAreas);
        TraverseEvent.State moveState = Movement.traverse(path);

        script.debug("[Traversev2#navPathTraverse]: Attempting to walk to " + coordinate.getX() + " | " + coordinate.getY() + " | " + moveState);

        if (moveState == null || moveState == TraverseEvent.State.FAILED || moveState == TraverseEvent.State.NO_PATH) {
            script.debug("[Traversev2#navPathTraverse]: NavPath failed, attempting Bresenham fallback");
            return bresenhamWalkTo(script, coordinate, true, RandomGenerator.nextInt(12, 20));
        }

        return moveState == TraverseEvent.State.FINISHED || moveState == TraverseEvent.State.IDLE || moveState == TraverseEvent.State.CONTINUE;
    }

    /**
     * Traverses to a location using NavPath with blocked areas and default flags.
     *
     * @param script the permissive script instance for debugging
     * @param coordinate the target coordinate to navigate to
     * @param blockedAreas areas to block from navigation (walk steps, links, teleports will avoid these areas)
     * @return true if navigation was successful, false if failed
     */
    public static boolean navPathTraverse(PermissiveScript script, Coordinate coordinate, Area... blockedAreas) {
        return navPathTraverse(script, coordinate, 0, blockedAreas);
    }

    /**
     * Traverses to an area using NavPath with configurable movement flags and blocked areas.
     *
     * @param script the permissive script instance for debugging
     * @param area the target area to navigate to
     * @param flags movement flags (e.g., Movement.DISABLE_DIVE, Movement.ENABLE_SURGE, Movement.DISABLE_TELEPORTS)
     * @param blockedAreas areas to block from navigation (walk steps, links, teleports will avoid these areas)
     * @return true if navigation was successful, false if failed
     */
    public static boolean navPathTraverse(PermissiveScript script, Area area, int flags, Area... blockedAreas) {
        var coordinate = area.getRandomWalkableCoordinate();
        if (coordinate == null || (coordinate.getX() == 0 && coordinate.getY() == 0)) {
            script.debug("[Traversev2#navPathTraverse]: Area not mapped, using random coordinate");
            coordinate = area.getRandomCoordinate();
        }
        return navPathTraverse(script, coordinate, flags, blockedAreas);
    }

    /**
     * Traverses to an area using NavPath with blocked areas and default flags.
     *
     * @param script the permissive script instance for debugging
     * @param area the target area to navigate to
     * @param blockedAreas areas to block from navigation (walk steps, links, teleports will avoid these areas)
     * @return true if navigation was successful, false if failed
     */
    public static boolean navPathTraverse(PermissiveScript script, Area area, Area... blockedAreas) {
        return navPathTraverse(script, area, 0, blockedAreas);
    }

}
