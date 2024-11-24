package net.botwithus.api.util.statistics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.botwithus.rs3.game.skills.Skill;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StatisticsHandler {

    private final Gson gson = new Gson();
    private final File historyFile;
    private final Skill trackedSkill;
    private Instant startTime;
    private int startingXp;
    private StatisticsHistory statisticsHistory;
    private final Map<String, Integer> customCounters = new HashMap<>();

    /**
     * Constructs a StatisticsHandler with a specific history file path and optional skill tracking.
     *
     * @param historyFilePath Path to the statistics history file.
     * @param trackedSkill    The skill to track experience for, or null if no skill tracking is needed.
     */
    public StatisticsHandler(String historyFilePath, Skill trackedSkill) {
        File tempHistoryFile;
        Skill tempTrackedSkill;

        try {
            System.out.println("Initializing StatisticsHandler...");

            // Assign history file (conditionally initialize)
            tempHistoryFile = new File(historyFilePath);
            System.out.println("History file path: " + tempHistoryFile.getAbsolutePath());

            // Assign tracked skill
            tempTrackedSkill = trackedSkill;
            System.out.println("Tracked skill: " + (tempTrackedSkill != null ? tempTrackedSkill.toString() : "null"));

            // Set start time and starting experience
            this.startTime = Instant.now();
            this.startingXp = tempTrackedSkill != null ? tempTrackedSkill.getExperience() : 0;
            System.out.println("Starting XP: " + this.startingXp);

            // Load statistics history
            this.statisticsHistory = loadStatisticsHistory();
            System.out.println("StatisticsHandler initialized successfully.");

        } catch (Throwable t) {
            System.err.println("Error initializing StatisticsHandler: " + t.getMessage());
            t.printStackTrace();

            // Fallback values
            tempHistoryFile = new File("default_statistics.json");
            tempTrackedSkill = null; // No skill tracking on error
            this.statisticsHistory = new StatisticsHistory();
            this.startingXp = 0; // Reset XP in case of failure
        }

        // Final assignments
        this.historyFile = tempHistoryFile;
        this.trackedSkill = tempTrackedSkill;
    }

    /**
     * Loads the statistics history from the JSON file.
     *
     * @return The loaded StatisticsHistory or a new empty one if the file does not exist.
     */
    private StatisticsHistory loadStatisticsHistory() {
        if (!historyFile.exists()) {
            return new StatisticsHistory();
        }
        try (FileReader reader = new FileReader(historyFile)) {
            Type historyType = new TypeToken<StatisticsHistory>() {}.getType();
            return gson.fromJson(reader, historyType);
        } catch (IOException e) {
            e.printStackTrace();
            return new StatisticsHistory();
        }
    }

    /**
     * Saves the statistics history back to the JSON file.
     */
    private void saveStatisticsHistory() {
        try (FileWriter writer = new FileWriter(historyFile)) {
            gson.toJson(statisticsHistory, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new session's data to the statistics history.
     *
     * @param sessionStats The session statistics to add.
     */
    public void addNewSession(SessionStatistics sessionStats) {
        statisticsHistory.addSession(sessionStats);
        saveStatisticsHistory();
    }

    /**
     * Increments a custom counter by a specified amount.
     *
     * @param counterName The name of the counter.
     * @param amount      The amount to increment by.
     */
    public void incrementCounter(String counterName, int amount) {
        customCounters.put(counterName, customCounters.getOrDefault(counterName, 0) + amount);
    }

    /**
     * Increments a custom counter by 1.
     *
     * @param counterName The name of the counter.
     */
    public void incrementCounter(String counterName) {
        incrementCounter(counterName, 1);
    }

    /**
     * Retrieves the value of a custom counter.
     *
     * @param counterName The name of the counter.
     * @return The value of the counter.
     */
    public int getCounterValue(String counterName) {
        return customCounters.getOrDefault(counterName, 0);
    }

    /**
     * Generates a summary of the current session's statistics.
     *
     * @return The current session's statistics.
     */
    public SessionStatistics generateSessionSummary() {
        Instant endTime = Instant.now();
        Duration runtime = Duration.between(startTime, endTime);
        int xpGained = trackedSkill != null ? trackedSkill.getExperience() - startingXp : 0;

        // Include xpGained in the metrics map
        Map<String, Integer> sessionMetrics = new HashMap<>(customCounters);
        sessionMetrics.put("xpGained", xpGained);

        return new SessionStatistics(
                LocalDateTime.now().minus(runtime),
                LocalDateTime.now(),
                sessionMetrics
        );
    }

    /**
     * Finalizes the current session and saves its data.
     */
    public void finalizeSession() {
        SessionStatistics sessionStats = generateSessionSummary();
        addNewSession(sessionStats);
        resetSession();
    }

    /**
     * Resets the session counters and start time for a new session.
     */
    public void resetSession() {
        startTime = Instant.now();
        startingXp = trackedSkill != null ? trackedSkill.getExperience() : 0;
        customCounters.clear();
    }

    /**
     * Retrieves the detailed statistics history.
     *
     * @return The full statistics history.
     */
    public StatisticsHistory getDetailedHistory() {
        return statisticsHistory;
    }
}