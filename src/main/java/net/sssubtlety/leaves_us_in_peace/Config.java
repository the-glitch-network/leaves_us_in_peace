package net.sssubtlety.leaves_us_in_peace;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static net.sssubtlety.leaves_us_in_peace.LeavesUsInPeace.NAMESPACE;

@me.shedaniel.autoconfig.annotation.Config(name = NAMESPACE)
public class Config implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean matchLeavesTypes = FeatureControl.Defaults.matchLeavesTypes;

    @ConfigEntry.Gui.Tooltip
    public boolean matchLogsToLeaves = FeatureControl.Defaults.matchLogsToLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean ignorePersistentLeaves = FeatureControl.Defaults.ignorePersistentLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean accelerateLeavesDecay = FeatureControl.Defaults.accelerateLeavesDecay;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    public DecayDelay decayDelay = new DecayDelay();

    @ConfigEntry.Gui.Tooltip
    public boolean updateDiagonalLeaves = FeatureControl.Defaults.updateDiagonalLeaves;

    @ConfigEntry.Gui.Tooltip
    public boolean doDecayingLeavesEffects = FeatureControl.Defaults.doDecayingLeavesEffects;

    public static class DecayDelay {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.BoundedDiscrete(max = 100)
        public int minimum = FeatureControl.Defaults.minDecayDelay;

        @ConfigEntry.BoundedDiscrete(max = 100)
        @ConfigEntry.Gui.Tooltip
        public int maximum = FeatureControl.Defaults.maxDecayDelay;
    }
}
