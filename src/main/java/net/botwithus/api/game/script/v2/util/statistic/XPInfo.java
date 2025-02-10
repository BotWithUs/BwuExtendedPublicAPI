package net.botwithus.api.game.script.v2.util.statistic;

import java.text.NumberFormat;

import net.botwithus.api.util.Math;
import net.botwithus.api.util.StringUtils;
import net.botwithus.api.util.collection.PairList;
import net.botwithus.api.util.time.Stopwatch;
import net.botwithus.api.util.time.Timer;
import net.botwithus.api.util.time.enums.DurationStringFormat;
import net.botwithus.rs3.game.skills.Skills;


public class XPInfo {
    public Skills statType;

    private int startLvl, startXP, currentLvl, currentXP, xpUntilNextLevel;

    public XPInfo(Skills stat) {
        this.statType = stat;
        reset();
    }

    public void update() {
        this.currentLvl = statType.getLevel();
        this.currentXP = statType.getSkill().getExperience();
        this.xpUntilNextLevel = statType.getExperienceToNextLevel();
    }


    public void reset() {
        this.currentLvl = statType.getLevel();
        this.currentXP = statType.getSkill().getExperience();
        this.xpUntilNextLevel = statType.getExperienceToNextLevel();
        currentLvl = startLvl;
        currentXP = startXP;
    }

    public int getLevelsGained() {
        return currentLvl - startLvl;
    }

    public int getGainedXP() {
        return currentXP - startXP;
    }

    public int getXPHour(Stopwatch watch) {
        return Math.getUnitsPerHour(watch, getGainedXP());
    }

    public int getSecondsUntilLevel(Stopwatch watch) {
        return (int) ((((double) xpUntilNextLevel) / ((double) getXPHour(watch))) * 3600.0);
    }

    public PairList<String, String> getPairList(Stopwatch stopWatch) {
        PairList<String, String> list = new PairList<>();
        if (currentXP > startXP) {
            var name = StringUtils.toTitleCase(statType.toString());
            list.add(name + " Level: ", currentLvl + " (" + getLevelsGained() + " Gained)");
            list.add(name + " XP Gained: ", NumberFormat.getIntegerInstance().format(getGainedXP()) + " (" + NumberFormat.getIntegerInstance().format(getXPHour(stopWatch)) + "/Hour)");
            list.add(name + " TTL: ", Timer.secondsToFormattedString(getSecondsUntilLevel(stopWatch), DurationStringFormat.DESCRIPTION));
        }
        return list;
    }

    public Skills getSkillsType() {
        return statType;
    }
}