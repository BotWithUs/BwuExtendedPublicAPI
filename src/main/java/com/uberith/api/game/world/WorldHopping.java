package com.uberith.api.game.world;

import net.botwithus.rs3.game.login.LoginManager;
import net.botwithus.rs3.game.login.WorldResult;
import net.botwithus.rs3.game.queries.builders.worlds.WorldQuery;
import net.botwithus.rs3.game.queries.results.ResultSet;
import net.botwithus.rs3.script.Execution;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A class that provides methods to interact with the world-hopping system.
 */
public final class WorldHopping {

    private static final Set<Integer> blacklistedWorlds = new HashSet<>();
    private static final Map<Integer, Long> blacklistExpiry = new HashMap<>();
    private static final int DEFAULT_MIN_PING = 1;
    private static final int DEFAULT_MAX_PING = 300;
    private static final int DEFAULT_MIN_POPULATION = 50;
    private static final int DEFAULT_MAX_POPULATION = 1500;
    private static final long DEFAULT_BLACKLIST_DURATION_MS = TimeUnit.MINUTES.toMillis(6);
    private static final long DEFAULT_HOP_DELAY_MS = 8000;

    private WorldHopping() {
    }

    public static boolean hopRandomWorld(
            boolean isMember,
            int minPing,
            int maxPing,
            int minPopulation,
            int maxPopulation
    ) {
        clearExpiredBlacklist();

        WorldQuery query = WorldQuery.newQuery()
                .ping(minPing, maxPing)
                .population(minPopulation, maxPopulation);

        if (isMember) {
            query.members();
        }

        ResultSet<?> availableWorlds = query.results();
        List<?> filteredWorlds = availableWorlds.stream()
                .filter(world -> !blacklistedWorlds.contains(getWorldId(world)))
                .toList();

        if (filteredWorlds.isEmpty()) {
            return false;
        }

        Object selectedWorld = filteredWorlds.get(new Random().nextInt(filteredWorlds.size()));
        return initiateWorldHop(selectedWorld);
    }

    public static boolean hopToWorld(int worldId) {
        clearExpiredBlacklist();

        if (blacklistedWorlds.contains(worldId)) {
            return false;
        }

        ResultSet<?> results = WorldQuery.newQuery()
                .id(worldId)
                .results();

        Object world = results.first();
        return world != null && initiateWorldHop(world);
    }

    public static void clearExpiredBlacklist() {
        long currentTime = System.currentTimeMillis();
        blacklistedWorlds.removeIf(worldId -> {
            Long expiryTime = blacklistExpiry.get(worldId);
            if (expiryTime != null && expiryTime <= currentTime) {
                blacklistExpiry.remove(worldId);
                return true;
            }
            return false;
        });
    }

    public static Set<Integer> getBlacklistedWorlds() {
        return Collections.unmodifiableSet(blacklistedWorlds);
    }

    private static void blacklistWorld(int worldId) {
        blacklistedWorlds.add(worldId);
        blacklistExpiry.put(worldId, System.currentTimeMillis() + DEFAULT_BLACKLIST_DURATION_MS);
    }

    private static boolean initiateWorldHop(Object worldInstance) {
        try {
            Class<?> worldClass = worldInstance.getClass();
            if (!"World".equals(worldClass.getSimpleName())) {
                return false;
            }

            Object world = worldClass.cast(worldInstance);
            WorldResult result = (WorldResult) LoginManager.class
                    .getMethod("hopWorld", worldClass)
                    .invoke(null, world);

            if (result == WorldResult.SUCCESS) {
                Integer worldId = getWorldId(world);
                if (worldId != null) {
                    blacklistWorld(worldId);
                    Execution.delay(DEFAULT_HOP_DELAY_MS);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Integer getWorldId(Object world) {
        try {
            return (Integer) world.getClass().getMethod("getId").invoke(world);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
