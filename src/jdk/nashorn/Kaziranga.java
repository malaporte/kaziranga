package jdk.nashorn;

public class Kaziranga {
    private static boolean kaziranga = Kaziranga.class.getName().contains(".kaziranga.");

    /**
     * Checks whether the Nashorn engine is currently running as Kaziranga.
     * @return Whether we're running under Kaziranga.
     */
    public static boolean isKaziranga() {
        return kaziranga;
    }

    public static void notAvailableInKaziranga() throws IllegalAccessError {
        if (kaziranga) {
            throw new IllegalAccessError("This method is not available under Kaziranga");
        }
    }
}
