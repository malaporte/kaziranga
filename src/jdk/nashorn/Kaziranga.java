package jdk.nashorn;

public class Kaziranga
{
    private static boolean kaziranga = Kaziranga.class.getName().contains(".kaziranga.");

    /**
     * Checks whether the Nashorn engine is currently running as Kaziranga.
     * @return Whether we're running under Kaziranga.
     */
    public static boolean isKaziranga()
    {
        return kaziranga;
    }

    public static boolean isRestrictedSymbolName(String name)
    {
        return name.equals("getClass");
    }

    public static void notAvailableInKaziranga()
    {
        if (Kaziranga.isKaziranga()) {
            throw new KazirangaError("This method is not available under Kaziranga");
        }
    }
}
