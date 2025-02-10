package net.botwithus.api.game.script.v2.ui;

public enum Color {

    BLACK(0.0f, 0.0f, 0.0f, 1.0f),
    WHITE(1.0f, 1.0f, 1.0f, 1.0f),
    RED(1.0f, 0.0f, 0.0f, 1.0f),
    GREEN(0.0f, 1.0f, 0.0f, 1.0f),
    BLUE(0.0f, 0.0f, 1.0f, 1.0f),
    YELLOW(1.0f, 1.0f, 0.0f, 1.0f),
    ORANGE(1.0f, 0.5f, 0.0f, 1.0f),
    PURPLE(0.5f, 0.0f, 0.5f, 1.0f),
    PINK(1.0f, 0.5f, 0.5f, 1.0f),
    CYAN(0.0f, 1.0f, 1.0f, 1.0f),
    MAGENTA(1.0f, 0.0f, 1.0f, 1.0f),
    LIME(0.0f, 1.0f, 0.0f, 1.0f),
    BROWN(0.6f, 0.4f, 0.2f, 1.0f),
    GRAY(0.5f, 0.5f, 0.5f, 1.0f),
    LIGHT_GRAY(0.8f, 0.8f, 0.8f, 1.0f),
    DARK_GRAY(0.2f, 0.2f, 0.2f, 1.0f),
    TURQUOISE(0.0f, 0.8f, 0.6f, 1.0f),
    LAVENDER(0.8f, 0.6f, 1.0f, 1.0f),
    CORAL(1.0f, 0.5f, 0.3f, 1.0f),
    OLIVE(0.5f, 0.5f, 0.0f, 1.0f),
    TEAL(0.0f, 0.5f, 0.5f, 1.0f),
    NAVY(0.0f, 0.0f, 0.5f, 1.0f),
    SALMON(0.98f, 0.5f, 0.45f, 1.0f),
    SEAFOAM(0.25f, 0.88f, 0.82f, 1.0f),
    SKY_BLUE(0.53f, 0.81f, 0.98f, 1.0f),
    GOLD(1.0f, 0.816f, 0.0f, 1.0f),
    SILVER(0.75f, 0.75f, 0.75f, 1.0f),
    BRONZE(0.8f, 0.5f, 0.2f, 1.0f),
    INDIGO(0.29f, 0.0f, 0.51f, 1.0f),
    AQUAMARINE(0.5f, 1.0f, 0.83f, 1.0f),
    CRIMSON(0.86f, 0.08f, 0.24f, 1.0f),
    DARK_GREEN(0.0f, 0.39f, 0.0f, 1.0f),
    DARK_RED(0.55f, 0.0f, 0.0f, 1.0f),
    DEEP_SKY_BLUE(0.0f, 0.75f, 1.0f, 1.0f);

    private final float v;
    private final float v1;
    private final float v2;
    private final float v3;

    Color(float v, float v1, float v2, float v3) {
        this.v = v;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public float getRed() {
        return v;
    }

    public float getGreen() {
        return v1;
    }

    public float getBlue() {
        return v2;
    }

    public float getAlpha() {
        return v3;
    }

    public static Color[] getColors() {
        return Color.values();
    }
}