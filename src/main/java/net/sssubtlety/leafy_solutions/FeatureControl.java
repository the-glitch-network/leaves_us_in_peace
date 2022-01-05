package net.sssubtlety.leafy_solutions;

public final class FeatureControl {
    private static boolean matchLeavesTypes = true;
    private static boolean matchLogsToLeaves = true;
    private static boolean ignorePersistentLaves = true;
    private static int leavesDecayDelay = 3;
    private static boolean updateDiagonalLeaves = true;

    public static boolean shouldMatchLeavesTypes() {
        return matchLeavesTypes;
    }

    public static boolean shouldMatchLogsToLeaves() {
        return matchLogsToLeaves;
    }

    public static boolean shouldIgnorePersistentLaves() {
        return ignorePersistentLaves;
    }

    public static int getLeavesDecayDelay() {
        return leavesDecayDelay;
    }

    public static boolean shouldUpdateDiagonalLeaves() {
        return updateDiagonalLeaves;
    }

    private FeatureControl() throws InstantiationException {
        throw new InstantiationException("FeatureControl is uninstantiable!");
    }
}
