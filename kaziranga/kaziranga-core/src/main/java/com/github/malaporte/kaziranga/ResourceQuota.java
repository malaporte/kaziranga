package com.github.malaporte.kaziranga;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.atomic.AtomicLong;

public class ResourceQuota
{
    // Sadly, the call needed to monitor memory allocations made by a thread is specific to
    // Oracle's implementation. Under other JVMs we won't be able to enforce memory quotas.
    private static final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    private static final ThreadMXBean sunThreadMxBean = threadMxBean instanceof com.sun.management.ThreadMXBean ? (com.sun.management.ThreadMXBean) threadMxBean
                                                                                                                : null;
    private long initialMemoryQuota;
    private AtomicLong cpuTimeInNanoseconds;
    private AtomicLong memoryUsage;
    private final boolean monitorCpuTime;
    private final boolean monitorMemoryUsage;

    public ResourceQuota(long cpuTimeInMilliseconds,
                         long memoryUsageInBytes)
    {
        this.cpuTimeInNanoseconds = new AtomicLong(cpuTimeInMilliseconds * 1000000);
        this.initialMemoryQuota = memoryUsageInBytes;
        this.memoryUsage = new AtomicLong(memoryUsageInBytes);

        monitorCpuTime = cpuTimeInMilliseconds > 0;
        monitorMemoryUsage = memoryUsageInBytes > 0;

        if (monitorMemoryUsage && sunThreadMxBean == null) {
            throw new UnsupportedOperationException("Memory quota monitoring is not supported on this JVM implementation!");
        }
    }

    // WatchDogs decrement the quota values every time they are checked. Once the
    // values reach 0 an exception is thrown. By decoupling this task from the Quota
    // it is possible to use the same quota for several script invocations.
    public WatchDog createWatchDog()
    {
        return new WatchDog();
    }

    public class WatchDog
    {
        private static final int CHECK_EVERY = 25;

        private long threadId = Thread.currentThread().getId();
        private boolean initialized = false;
        private long lastCpuTime;
        private long lastAllocatedBytes;
        private int cycle = 0;

        public void check()
        {
            assert (Thread.currentThread().getId() == threadId);

            if (!initialized) {
                // We read the initial values for CPU and memory only at the first check,
                // so as to avoid including resource consumption from script compilation.
                if (monitorCpuTime) {
                    lastCpuTime = readCpuTime(threadId);
                }
                if (monitorMemoryUsage) {
                    lastAllocatedBytes = readAllocatedBytes(threadId);
                }

                initialized = true;
                return;
            }

            if (++cycle % CHECK_EVERY != 0) {
                return;
            }

            if (monitorCpuTime) {
                checkCpuTime();
            }

            if (monitorMemoryUsage) {
                checkAllocatedBytes();
            }
        }

        private void checkCpuTime()
        {
            long current = readCpuTime(threadId);
            long delta = current - lastCpuTime;
            lastCpuTime = current;

            if (cpuTimeInNanoseconds.addAndGet(-delta) < 0) {
                throw new CpuExceededQuotaException("CPU usage has exceeded the allowed quota");
            }
        }

        private void checkAllocatedBytes()
        {
            long current = readAllocatedBytes(threadId);
            long delta = current - lastAllocatedBytes;
            lastAllocatedBytes = current;

            if (memoryUsage.addAndGet(-delta) < 0) {
                throw new MemoryExceededQuotaException("Memory usage has exceeded the allowed quota of " + (initialMemoryQuota / (1024 * 1024)) + " megs");
            }
        }
    }

    private static long readCpuTime(long threadId)
    {
        // We must convert from nanoseconds to milliseconds
        return threadMxBean.getThreadCpuTime(threadId);
    }

    private static long readAllocatedBytes(long threadId)
    {
        return ((com.sun.management.ThreadMXBean) threadMxBean).getThreadAllocatedBytes(threadId);
    }
}
