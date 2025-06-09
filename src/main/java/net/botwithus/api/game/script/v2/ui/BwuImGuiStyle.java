package net.botwithus.api.game.script.v2.ui;

import net.botwithus.rs3.imgui.ImGui;

/**
 * A utility class for applying consistent, modern styling to ImGui interfaces.
 * Provides various theme options and utility methods for style manipulation.
 */
public class BwuImGuiStyle {
    
    // Theme color constants
    private static final float[] DARK_BG = {0.15f, 0.16f, 0.21f, 1.0f};
    private static final float[] ACCENT_BLUE = {0.31f, 0.56f, 0.86f, 1.0f};
    private static final float[] ACCENT_GREEN = {0.26f, 0.59f, 0.42f, 1.0f};
    private static final float[] ACCENT_PURPLE = {0.58f, 0.35f, 0.92f, 1.0f};
    private static final float[] ACCENT_ORANGE = {0.95f, 0.55f, 0.20f, 1.0f};
    private static final float[] LIGHT_TEXT = {0.90f, 0.90f, 0.90f, 1.0f};
    private static final float[] MUTED_TEXT = {0.70f, 0.70f, 0.70f, 1.0f};
    private static final float[] SUBTLE_BG = {0.18f, 0.20f, 0.25f, 1.0f};
    private static final float[] HOVER_BG = {0.22f, 0.24f, 0.29f, 1.0f};
    private static final float[] ACTIVE_BG = {0.26f, 0.28f, 0.33f, 1.0f};
    
    private static final float[] BWU_DARK_BG = {0.08f, 0.09f, 0.12f, 1.0f};       
    private static final float[] BWU_ACCENT_BLUE = {0.38f, 0.59f, 0.92f, 1.0f};   
    private static final float[] BWU_LIGHT_BLUE = {0.50f, 0.67f, 0.94f, 1.0f};    
    private static final float[] BWU_DARK_BLUE = {0.20f, 0.40f, 0.75f, 1.0f};      
    private static final float[] BWU_PANEL_BG = {0.10f, 0.11f, 0.15f, 1.0f};       
    private static final float[] BWU_BORDER = {0.12f, 0.13f, 0.18f, 1.0f};        
    private static final float[] BWU_TITLE_BG = {0.05f, 0.05f, 0.08f, 1.0f};       
    private static final float[] BWU_TITLE_BG_ACTIVE = {0.05f, 0.10f, 0.20f, 1.0f};

    public static void applyBwuTheme() {
        // Window styling
        ImGui.PushStyleVar(ImGuiStyleVar.WindowRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FrameRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.GrabRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ScrollbarRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.TabRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.WindowPadding, 8.0f, 8.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ItemSpacing, 8.0f, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FramePadding, 5.0f, 3.0f);
        
        // Main window elements
        ImGui.PushStyleColor(ImGuiCol.WindowBg, BWU_DARK_BG[0], BWU_DARK_BG[1], BWU_DARK_BG[2], BWU_DARK_BG[3]);
        ImGui.PushStyleColor(ImGuiCol.Border, BWU_BORDER[0], BWU_BORDER[1], BWU_BORDER[2], BWU_BORDER[3]);
        
        // Title bar colors 
        ImGui.PushStyleColor(ImGuiCol.TitleBg, BWU_TITLE_BG[0], BWU_TITLE_BG[1], BWU_TITLE_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBgActive, BWU_TITLE_BG_ACTIVE[0], BWU_TITLE_BG_ACTIVE[1], BWU_TITLE_BG_ACTIVE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBgCollapsed, BWU_TITLE_BG[0], BWU_TITLE_BG[1], BWU_TITLE_BG[2], 0.8f);
        
        // Text colors
        ImGui.PushStyleColor(ImGuiCol.Text, LIGHT_TEXT[0], LIGHT_TEXT[1], LIGHT_TEXT[2], LIGHT_TEXT[3]);
        ImGui.PushStyleColor(ImGuiCol.TextDisabled, MUTED_TEXT[0], MUTED_TEXT[1], MUTED_TEXT[2], MUTED_TEXT[3]);
        
        // Button colors
        ImGui.PushStyleColor(ImGuiCol.Button, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.ButtonHovered, BWU_LIGHT_BLUE[0], BWU_LIGHT_BLUE[1], BWU_LIGHT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ButtonActive, BWU_DARK_BLUE[0], BWU_DARK_BLUE[1], BWU_DARK_BLUE[2], 1.0f);
        
        // Frame backgrounds
        ImGui.PushStyleColor(ImGuiCol.FrameBg, BWU_PANEL_BG[0], BWU_PANEL_BG[1], BWU_PANEL_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgHovered, BWU_PANEL_BG[0] + 0.05f, BWU_PANEL_BG[1] + 0.05f, BWU_PANEL_BG[2] + 0.05f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgActive, BWU_PANEL_BG[0] + 0.10f, BWU_PANEL_BG[1] + 0.10f, BWU_PANEL_BG[2] + 0.10f, 1.0f);
        
        // Header, tabs, etc.
        ImGui.PushStyleColor(ImGuiCol.Header, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 0.55f);
        ImGui.PushStyleColor(ImGuiCol.HeaderHovered, BWU_LIGHT_BLUE[0], BWU_LIGHT_BLUE[1], BWU_LIGHT_BLUE[2], 0.6f);
        ImGui.PushStyleColor(ImGuiCol.HeaderActive, BWU_LIGHT_BLUE[0], BWU_LIGHT_BLUE[1], BWU_LIGHT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.Tab, BWU_DARK_BG[0], BWU_DARK_BG[1], BWU_DARK_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TabHovered, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.TabActive, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 0.6f);
        
        // Other elements
        ImGui.PushStyleColor(ImGuiCol.CheckMark, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrab, BWU_ACCENT_BLUE[0], BWU_ACCENT_BLUE[1], BWU_ACCENT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrabActive, BWU_LIGHT_BLUE[0], BWU_LIGHT_BLUE[1], BWU_LIGHT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrab, BWU_PANEL_BG[0] + 0.10f, BWU_PANEL_BG[1] + 0.10f, BWU_PANEL_BG[2] + 0.10f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabHovered, BWU_PANEL_BG[0] + 0.15f, BWU_PANEL_BG[1] + 0.15f, BWU_PANEL_BG[2] + 0.15f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabActive, BWU_PANEL_BG[0] + 0.20f, BWU_PANEL_BG[1] + 0.20f, BWU_PANEL_BG[2] + 0.20f, 1.0f);
    }
    

    public static void applyModernDarkTheme() {
        // Window styling
        ImGui.PushStyleVar(ImGuiStyleVar.WindowRounding, 5.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FrameRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.GrabRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ScrollbarRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.TabRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.WindowPadding, 8.0f, 8.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ItemSpacing, 10.0f, 5.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FramePadding, 6.0f, 4.0f);
        
        // Colors
        ImGui.PushStyleColor(ImGuiCol.WindowBg, DARK_BG[0], DARK_BG[1], DARK_BG[2], DARK_BG[3]);
        ImGui.PushStyleColor(ImGuiCol.Border, 0.15f, 0.16f, 0.19f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBg, 0.13f, 0.14f, 0.18f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBgActive, 0.2f, 0.22f, 0.27f, 1.0f);
        
        // Text colors
        ImGui.PushStyleColor(ImGuiCol.Text, LIGHT_TEXT[0], LIGHT_TEXT[1], LIGHT_TEXT[2], LIGHT_TEXT[3]);
        ImGui.PushStyleColor(ImGuiCol.TextDisabled, MUTED_TEXT[0], MUTED_TEXT[1], MUTED_TEXT[2], MUTED_TEXT[3]);
        
        // Button colors
        ImGui.PushStyleColor(ImGuiCol.Button, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.ButtonHovered, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ButtonActive, ACCENT_BLUE[0] * 0.8f, ACCENT_BLUE[1] * 0.8f, ACCENT_BLUE[2] * 0.8f, 1.0f);
        
        // Frame backgrounds
        ImGui.PushStyleColor(ImGuiCol.FrameBg, SUBTLE_BG[0], SUBTLE_BG[1], SUBTLE_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgHovered, HOVER_BG[0], HOVER_BG[1], HOVER_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgActive, ACTIVE_BG[0], ACTIVE_BG[1], ACTIVE_BG[2], 1.0f);
        
        // Header, tabs, etc.
        ImGui.PushStyleColor(ImGuiCol.Header, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.55f);
        ImGui.PushStyleColor(ImGuiCol.HeaderHovered, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.7f);
        ImGui.PushStyleColor(ImGuiCol.HeaderActive, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.9f);
        ImGui.PushStyleColor(ImGuiCol.Tab, 0.17f, 0.18f, 0.23f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TabHovered, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.7f);
        ImGui.PushStyleColor(ImGuiCol.TabActive, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.85f);
        
        // Other elements
        ImGui.PushStyleColor(ImGuiCol.CheckMark, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrab, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrabActive, ACCENT_BLUE[0], ACCENT_BLUE[1], ACCENT_BLUE[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrab, 0.3f, 0.32f, 0.37f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabHovered, 0.36f, 0.38f, 0.43f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabActive, 0.4f, 0.42f, 0.47f, 1.0f);
    }
    

    public static void applyGreenTheme() {
        ImGui.PushStyleVar(ImGuiStyleVar.WindowRounding, 5.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FrameRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.GrabRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ScrollbarRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.TabRounding, 4.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.WindowPadding, 8.0f, 8.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ItemSpacing, 10.0f, 5.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.FramePadding, 6.0f, 4.0f);
        
        ImGui.PushStyleColor(ImGuiCol.WindowBg, DARK_BG[0], DARK_BG[1], DARK_BG[2], DARK_BG[3]);
        ImGui.PushStyleColor(ImGuiCol.Border, 0.15f, 0.16f, 0.19f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBg, 0.13f, 0.14f, 0.18f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TitleBgActive, 0.2f, 0.22f, 0.27f, 1.0f);
        
        // Text colors
        ImGui.PushStyleColor(ImGuiCol.Text, LIGHT_TEXT[0], LIGHT_TEXT[1], LIGHT_TEXT[2], LIGHT_TEXT[3]);
        ImGui.PushStyleColor(ImGuiCol.TextDisabled, MUTED_TEXT[0], MUTED_TEXT[1], MUTED_TEXT[2], MUTED_TEXT[3]);
        
        ImGui.PushStyleColor(ImGuiCol.Button, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.ButtonHovered, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ButtonActive, ACCENT_GREEN[0] * 0.8f, ACCENT_GREEN[1] * 0.8f, ACCENT_GREEN[2] * 0.8f, 1.0f);
        
        // Frame backgrounds
        ImGui.PushStyleColor(ImGuiCol.FrameBg, SUBTLE_BG[0], SUBTLE_BG[1], SUBTLE_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgHovered, HOVER_BG[0], HOVER_BG[1], HOVER_BG[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.FrameBgActive, ACTIVE_BG[0], ACTIVE_BG[1], ACTIVE_BG[2], 1.0f);
        
        ImGui.PushStyleColor(ImGuiCol.Header, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.55f);
        ImGui.PushStyleColor(ImGuiCol.HeaderHovered, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.7f);
        ImGui.PushStyleColor(ImGuiCol.HeaderActive, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.9f);
        ImGui.PushStyleColor(ImGuiCol.Tab, 0.17f, 0.18f, 0.23f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.TabHovered, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.7f);
        ImGui.PushStyleColor(ImGuiCol.TabActive, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.85f);
        
        ImGui.PushStyleColor(ImGuiCol.CheckMark, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrab, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.SliderGrabActive, ACCENT_GREEN[0], ACCENT_GREEN[1], ACCENT_GREEN[2], 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrab, 0.3f, 0.32f, 0.37f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabHovered, 0.36f, 0.38f, 0.43f, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.ScrollbarGrabActive, 0.4f, 0.42f, 0.47f, 1.0f);
    }
    
    /**
     * Resets all pushed styles by popping them.
     * Should be called at the end of a frame when styles have been applied.
     */
    public static void popAllStyles() {
        ImGui.PopStyleVar(8);  // Pop all 8 style vars pushed
        ImGui.PopStyleColor(24); // Pop all colors pushed (increased by 1 for TitleBgCollapsed)
    }
    
    /**
     * Applies styling to a window section/child window with slight different background
     */
    public static void applyPanelStyle() {
        ImGui.PushStyleColor(ImGuiCol.ChildBg, BWU_PANEL_BG[0], BWU_PANEL_BG[1], BWU_PANEL_BG[2], 0.9f);
        ImGui.PushStyleVar(ImGuiStyleVar.ChildRounding, 3.0f);
        ImGui.PushStyleVar(ImGuiStyleVar.ChildBorderSize, 1.0f);
        ImGui.PushStyleColor(ImGuiCol.Border, BWU_BORDER[0], BWU_BORDER[1], BWU_BORDER[2], 1.0f);
    }
    
    /**
     * Removes panel styling
     */
    public static void popPanelStyle() {
        ImGui.PopStyleColor(2);
        ImGui.PopStyleVar(2);
    }
    
    /**
     * Creates a styled gradient button with a modern look
     * 
     * @param label The button label
     * @param accentType The type of accent color (0-3: blue, green, purple, orange)
     * @return True if the button was clicked, false otherwise
     */
    public static boolean gradientButton(String label, int accentType) {
        float[] accent;
        
        // Select the accent color based on type
        accent = switch (accentType) {
            case 1 -> ACCENT_GREEN;
            case 2 -> ACCENT_PURPLE;
            case 3 -> ACCENT_ORANGE;
            default -> ACCENT_BLUE;
        };
        
        // Push custom button style
        ImGui.PushStyleVar(ImGuiStyleVar.FrameRounding, 4.0f);
        ImGui.PushStyleColor(ImGuiCol.Button, accent[0], accent[1], accent[2], 0.8f);
        ImGui.PushStyleColor(ImGuiCol.ButtonHovered, accent[0] + 0.05f, accent[1] + 0.05f, accent[2] + 0.05f, 0.9f);
        ImGui.PushStyleColor(ImGuiCol.ButtonActive, accent[0] - 0.1f, accent[1] - 0.1f, accent[2] - 0.1f, 1.0f);
        
        // Draw the button
        boolean result = ImGui.Button(label);
        
        // Pop the style
        ImGui.PopStyleColor(3);
        ImGui.PopStyleVar(1);
        
        return result;
    }
    
    /**
     * Gets a preset accent color array by index
     * 
     * @param accentType The type of accent (0-3: blue, green, purple, orange)
     * @return Array of RGBA values for the accent color
     */
    public static float[] getAccentColor(int accentType) {
        return switch (accentType) {
            case 1 -> ACCENT_GREEN.clone();
            case 2 -> ACCENT_PURPLE.clone();
            case 3 -> ACCENT_ORANGE.clone();
            default -> ACCENT_BLUE.clone();
        };
    }
} 