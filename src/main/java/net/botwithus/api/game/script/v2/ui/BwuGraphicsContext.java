package net.botwithus.api.game.script.v2.ui;

import net.botwithus.api.game.script.v2.BwuScriptv2;
import net.botwithus.api.game.script.v2.permissive.ResultType;
import net.botwithus.api.game.script.v2.permissive.node.Branch;
import net.botwithus.api.game.script.v2.permissive.node.TreeNode;
import net.botwithus.api.game.script.v2.util.statistic.XPInfo;
import net.botwithus.api.util.StringUtils;
import net.botwithus.api.util.collection.Pair;
import net.botwithus.rs3.script.ScriptConsole;
import net.botwithus.api.util.time.Timer;
import net.botwithus.api.util.time.enums.DurationStringFormat;
import net.botwithus.rs3.imgui.ImGui;
import net.botwithus.rs3.imgui.ImGuiWindowFlag;
import net.botwithus.rs3.script.ScriptGraphicsContext;

import java.util.Arrays;
import java.util.Locale;

public class BwuGraphicsContext extends ScriptGraphicsContext {
    private final BwuScriptv2 script;
    private boolean renderOnlyActivePathCheckbox = true;
    private String branchNameFilter = "";

    public BwuGraphicsContext(ScriptConsole console, BwuScriptv2 script) {
        super(console);
        this.script = script;
    }

    @Override
    public void drawSettings() {
        if (ImGui.Begin(script.getName() + " Settings | " + script.getVersion(), 0)) {
            ImGui.PushStyleColor(5, 0.322f, 0.494f,0.675f, 0.400f);
            ImGui.PushStyleColor(7, 0.322f, 0.494f,0.675f, 0.200f);
            ImGui.PushStyleColor(18, 0.322f, 0.494f,0.720f, 0.800f);
            ImGui.PushStyleColor(21, 0.322f, 0.494f,0.675f, 0.400f);

            if (ImGui.BeginTabBar("Bot", 0)) {
                if (script.getBuildableUI() != null)
                    script.getBuildableUI().buildUI();
                if (ImGui.BeginTabItem("Stats", 0)) {
                    ImGui.Text(script.getName() + " " + script.getVersion());
                    ImGui.Separator();
                    if (ImGui.Button("Reset Stats")) {
                        script.botStatInfo.resetStats();
                    }

                    if (script.botStatInfo != null) {
                        if (script.botStatInfo.xpInfoMap != null) {
                            for (var key : script.botStatInfo.xpInfoMap.keySet()) {
                                XPInfo model = script.botStatInfo.xpInfoMap.get(key);

                                var pairs = model.getPairList(script.STOPWATCH);
                                if (pairs.size() > 0) {
                                    ImGui.SeparatorText(StringUtils.toTitleCase(model.getSkillsType().name()));
                                    for (Pair<String, String> infoUI : pairs)
                                        ImGui.Text(infoUI.getLeft() + infoUI.getRight());
                                }
                            }
                            ImGui.Separator();
                        }
                        if (script.botStatInfo.displayInfoMap != null) {
                            for (String key : script.botStatInfo.displayInfoMap.keySet()) {
                                ImGui.Text(key + script.botStatInfo.displayInfoMap.get(key));
                            }
                        }
                        ImGui.Separator();

                        ImGui.Text("Runtime: " + Timer.secondsToFormattedString(script.STOPWATCH.elapsed() / 1000, DurationStringFormat.CLOCK));
                        ImGui.Text("Interaction failsafe: " + Timer.secondsToFormattedString(script.brokenSessionFailsafeTimer.getRemainingTimeInSeconds(), DurationStringFormat.CLOCK));
                        ImGui.Text("Current Task: " + script.getCurrentState().getStatus());

                        ImGui.Separator();

                        ImGui.Text("Execution Status: " + (script.isActive() ? "Active" : script.isPaused() ? "Paused" : "Stopped"));
                    }
                    ImGui.EndTabItem();
                }
                if (script.getVersion().startsWith("v2")) {
                    if (script.getRootNode() != null && ImGui.BeginTabItem("Logic Table", 0)) {
                        branchNameFilter = ImGui.InputText("Branch Name Filter", branchNameFilter);


                        ImGui.Text("Current State: " + script.getCurrentState().getName());
                        ImGui.Text("Current Status: " + script.getCurrentState().getStatus());
                        if (branchNameFilter != null && !branchNameFilter.isBlank()) {
//                            var task = script.getRootTask().rootNode().findTaskByDescription(branchNameFilter);
//                            renderFilteredTreeTable(task);
                            renderTreeTable(script.getRootNode(), renderOnlyActivePathCheckbox);

                        } else {
                            renderOnlyActivePathCheckbox = ImGui.Checkbox("Render Only Active Path", renderOnlyActivePathCheckbox);
                            renderTreeTable(script.getRootNode(), renderOnlyActivePathCheckbox);
                        }

                        ImGui.EndTabItem();
                    }
                    if (script.getRootNode() != null && ImGui.BeginTabItem("Logic Tree", 0)) {
//                        var previousScanRate = expireRate;
//                        expireRate = ImGui.InputInt("Logic Expire Rate Millis", expireRate);
//                        if (expireRate != previousScanRate) {
//                            script.updateResultExpirationTrigger = true;
//                        }

                        renderOnlyActivePathCheckbox = ImGui.Checkbox("Render Only Active Path", renderOnlyActivePathCheckbox);
                        ImGui.Text("Current Status: " + script.getCurrentState().getStatus());
                        renderTextTree(script.getRootNode(), 1, true, renderOnlyActivePathCheckbox);
                        ImGui.EndTabItem();

                    }
                }
            }
            ImGui.EndTabBar();

            ImGui.PopStyleColor(4);
        }
        ImGui.End();
    }

    @Override
    public void drawOverlay() {
        super.drawOverlay();

        if (script.getBuildableUI() != null)
            script.getBuildableUI().buildOverlay();
    }


    void renderTextTree(TreeNode node, int depth, boolean isPath, boolean renderOnlyActivePath) {
        var pipeSize = ImGui.CalcTextSize("|").getX();
        var symbol = " \uf586 ";
        for (int i = 0; i < depth; i++) {
            ImGui.Spacing(10 - (i < depth - 1 ? pipeSize : 0) - (i == depth - 1 && isPath ? ImGui.CalcTextSize(symbol).getX() : 0), 5);
            ImGui.SameLine();
            if (i < depth - 1) {
                ImGui.Text("|");
                ImGui.SameLine();
            }
        }
        if (node != null) {
            if (!node.isLeaf()) {
                if (branchNameFilter.isBlank() || node.getDesc().contains(branchNameFilter)) {
                    if (isPath) {
                        ImGui.PushStyleColor(0, Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), Color.GREEN.getAlpha());
                        ImGui.Text(symbol + node.getDesc() + ": " + node.getLatestValidate().getResultType());
                        ImGui.PopStyleColor();
                    } else {
                        ImGui.Text(node.getDesc() + ": " + node.getLatestValidate().getResultType());
                    }
                }
                if (!renderOnlyActivePath || (renderOnlyActivePath && isPath)) {
                    renderTextTree(node.successNode(), depth + 1, node.getLatestValidate().getResultType() == ResultType.MET, renderOnlyActivePath);
                    renderTextTree(node.failureNode(), depth + 1, node.getLatestValidate().getResultType() == ResultType.NOT_MET, renderOnlyActivePath);
                }
            } else if (branchNameFilter.isBlank() || node.getDesc().contains(branchNameFilter)) {
                var slimDec = node.getDesc().contains("[") ? node.getDesc().substring(0, node.getDesc().indexOf('[')) : node.getDesc();
                var desc = slimDec.isBlank() ? node.getClass().getSimpleName() : node.getDesc();
                if (isPath) {
                    ImGui.PushStyleColor(0, 0, 1, 0, 1);
                    ImGui.Text(symbol + desc);
                    ImGui.PopStyleColor();
                } else {
                    ImGui.Text(desc);
                }
            }
        } else {
            ImGui.Text("Node is null");
        }
    }

    private void renderTreeTable(TreeNode task, boolean renderOnlyActivePathCheckbox) {
        if (ImGui.BeginTable("Task Table", 5, 0)) { // 5 columns

            // Setup headers
            ImGui.TableSetupColumn("Type", 0);
            ImGui.TableSetupColumn("Name", 0);
            ImGui.TableSetupColumn("Permissives", 0);
            ImGui.TableSetupColumn("Success Path", 0);
            ImGui.TableSetupColumn("Failure Path", 0);
            ImGui.TableHeadersRow();

            if (task != null) {
                // Render rows
                renderTaskRow(task, true, 0, renderOnlyActivePathCheckbox);
            } else {
                ImGui.Text("Node is null");
            }
            ImGui.EndTable();
        }
    }

    private void renderTaskRow(TreeNode task, boolean isPath, int depth, boolean renderOnlyActivePath) {
        if (!branchNameFilter.isBlank() && !task.getDesc().toLowerCase(Locale.ROOT).contains(branchNameFilter.toLowerCase(Locale.ROOT)) &&
                !task.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains(branchNameFilter.toLowerCase(Locale.ROOT))) {
            if (!task.isLeaf() && task.successNode() != null && task.failureNode() != null) {
                renderTaskRow(task.successNode(), task.getLatestValidate().getResultType() == ResultType.MET, depth + 1, false);
                renderTaskRow(task.failureNode(), task.getLatestValidate().getResultType() == ResultType.NOT_MET, depth + 1, false);
            }
            return;
        }

        ImGui.TableNextRow();
        ImGui.TableNextColumn();
        ImGui.Separator();

        // Type
        ImGui.Text(depth == 0 ? "Root" : (!task.isLeaf() ? "Branch" : "Leaf"));

        // Name
        ImGui.TableNextColumn();
        ImGui.Separator();
        var symbol = "\uf586";
        if (!task.isLeaf()) {
            if (isPath) {
                ImGui.PushStyleColor(0, Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), Color.GREEN.getAlpha());
                ImGui.Text(" " + symbol + " " + task.getDesc() + ": " + task.getLatestValidate().getResultType());
                ImGui.PopStyleColor();
            } else {
                ImGui.Text(task.getDesc() + ": " + task.getLatestValidate().getResultType());
            }
        } else {
            ImGui.Text(task.getDesc().isEmpty() ? task.getClass().getSimpleName() : task.getDesc());
        }

        // Permissives, Success, and Failure Paths
        if (!task.isLeaf()) {
            Branch branch = (Branch) task;

            // Permissives
            ImGui.TableNextColumn();
            ImGui.Separator();
            if (branch.getInterlocks() != null) {
                int i = 0;
                for (var interlock : branch.getInterlocks()) {
                    boolean isActive = interlock.isActive();
                    if (isActive) {
                        ImGui.PushStyleColor(0, Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), Color.GREEN.getAlpha());
                    }
                    ImGui.Text(interlock.getName() + ": " + (isActive ? "MET" : "NOT_MET"));

                    for (var permissive : interlock.getPermissives()) {
                        ImGui.Text("    "+ permissive.getName() + ": " + permissive.getLastResult().getResultType().name());
                    }
                    if (isActive) {
                        ImGui.PopStyleColor();
                    }
                    
                    if (branch.getInterlocks().length > 1 && i < branch.getInterlocks().length - 1)
                        ImGui.SeparatorText("OR");
                    i++;
                }
            }

            // Success Path
            ImGui.TableNextColumn();
            ImGui.Separator();
            var leafDesc = task.successNode().getDesc().isBlank() ? task.successNode().getClass().getSimpleName() : task.successNode().getDesc();
            ImGui.Text(task.successNode().isLeaf() ? leafDesc : task.successNode().getDesc());

            // Failure Path
            ImGui.TableNextColumn();
            ImGui.Separator();
            leafDesc = task.failureNode().getDesc().isBlank() ? task.failureNode().getClass().getSimpleName() : task.failureNode().getDesc();
            ImGui.Text(task.failureNode().isLeaf() ? leafDesc : task.failureNode().getDesc());

            if (!renderOnlyActivePath || (renderOnlyActivePath && isPath)) {
                renderTaskRow(task.successNode(), task.getLatestValidate().getResultType() == ResultType.MET, depth + 1, renderOnlyActivePath);
                renderTaskRow(task.failureNode(), task.getLatestValidate().getResultType() == ResultType.NOT_MET, depth + 1, renderOnlyActivePath);
            }
        } else {
            ImGui.TableNextColumn();
            ImGui.Separator();
            ImGui.TableNextColumn();
            ImGui.Separator();
            ImGui.TableNextColumn();
            ImGui.Separator();
        }
    }

    @Override
    public void drawScriptConsole() {
        String scriptName = script.getName();
        String windowId = "Script Console##" + scriptName;

        if (ImGui.Begin(windowId, ImGuiWindowFlag.None.getValue())) {
            ImGui.PushStyleColor(5, 0.322f, 0.494f, 0.675f, 0.400f);
            ImGui.PushStyleColor(7, 0.322f, 0.494f, 0.675f, 0.200f);
            ImGui.PushStyleColor(18, 0.322f, 0.494f, 0.720f, 0.800f);
            ImGui.PushStyleColor(21, 0.322f, 0.494f, 0.675f, 0.400f);
            
            if (ImGui.Button("Clear")) {
                script.getConsole().clear();
            }

            ImGui.SameLine();
            script.getConsole().setScrollToBottom(ImGui.Checkbox("Scroll to bottom", script.getConsole().isScrollToBottom()));

            if (ImGui.BeginChild("##console_lines_" + scriptName, -1.0F, -1.0F, true, 0)) {
                for (int i = 0; i < 200; ++i) {
                    int lineIndex = (script.getConsole().getLineIndex() + i) % 200;
                    if (script.getConsole().getConsoleLines()[lineIndex] != null) {
                        ImGui.Text("%s", script.getConsole().getConsoleLines()[lineIndex]);
                    }
                }

                if (script.getConsole().isScrollToBottom()) {
                    ImGui.SetScrollHereY(1.0F);
                }

                ImGui.EndChild();
            }
            
            ImGui.PopStyleColor(4);
        }

        ImGui.End();
    }
}
