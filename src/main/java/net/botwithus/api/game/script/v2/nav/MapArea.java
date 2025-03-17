package net.botwithus.api.game.script.v2.nav;

import net.botwithus.rs3.game.Coordinate;

public class MapArea {
    public static final int getRegionId2(Coordinate coords) {
        return getRegionId2(coords.getX(), coords.getY());
    }

    public static final int getRegionId2(final int x, final int y) {
        return ((((x >> 6) & 0xff) << 8) + ((y >> 6) & 0xff));
    }
}
