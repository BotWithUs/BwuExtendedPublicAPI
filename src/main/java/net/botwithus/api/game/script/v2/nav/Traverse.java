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

}
