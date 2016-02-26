package com.github.malaporte.kaziranga;

public class QuotaEnforcer
{
    private static ThreadLocal<ResourceQuota.WatchDog> currentWatchDog = new ThreadLocal<ResourceQuota.WatchDog>();

    public static void check()
    {
        ResourceQuota.WatchDog watchDog = getCurrentWatchDog();

        // Code running under Kaziranga should always provide a resource quota to enforce.
        // This is to avoid having non-checked code since it can be hard to track all objects
        // that might end up being a callback to JavaScript code. If you need to run JS code
        // without enforcing a quota, please use the Nashorn engine directly.
        if (watchDog == null) {
            throw new NoQuotaException();
        }
        
        watchDog.check();
    }

    public static void register(ResourceQuota quota)
    {
        if (getCurrentWatchDog() != null) {
            throw new UnsupportedOperationException("Cannot register a resource quota in a thread that already has one");
        }

        currentWatchDog.set(quota.createWatchDog());
    }

    public static void unregister()
    {
        if (getCurrentWatchDog() == null) {
            throw new UnsupportedOperationException("The current thread has no registered resource quota");
        }

        currentWatchDog.set(null);
    }

    private static ResourceQuota.WatchDog getCurrentWatchDog()
    {
        return currentWatchDog.get();
    }
}
