package net.sssubtlety.leafy_solutions;

import java.util.Random;

public final class FeatureControl {
    private static boolean matchLeavesTypes = true;
    private static boolean matchLogsToLeaves = true;
    private static boolean ignorePersistentLaves = true;
    private static boolean accelerateLeavesDecay = true;
    private static int minDecayDelay = 10;
    private static int maxDecayDelay = 100;
    private static boolean updateDiagonalLeaves = true;
    private static boolean doDecayingLeavesEffects = false;

    public static boolean shouldMatchLeavesTypes() {
        return matchLeavesTypes;
    }

    public static boolean shouldMatchLogsToLeaves() {
        return matchLogsToLeaves;
    }

    public static boolean shouldIgnorePersistentLaves() {
        return ignorePersistentLaves;
    }

    public static boolean shouldAccelerateLeavesDecay() {
        return accelerateLeavesDecay;
    }

    public static int getDecayDelay(Random random) {
        return minDecayDelay < maxDecayDelay ?
             random.nextInt(minDecayDelay, maxDecayDelay) : maxDecayDelay;
    }

    public static boolean shouldUpdateDiagonalLeaves() {
        return updateDiagonalLeaves;
    }

    public static boolean shouldDoDecayingLeavesEffects() {
        return doDecayingLeavesEffects;
    }

    private FeatureControl() throws InstantiationException {
        throw new InstantiationException("FeatureControl is uninstantiable!");
    }
}
