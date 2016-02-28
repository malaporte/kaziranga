package com.github.malaporte.kaziranga;

import org.testng.annotations.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import static org.testng.Assert.*;

public class QuotaTest {
    public void shouldExceedCpuQuota(String code) {
        StringReader reader = new StringReader(code);
        shouldExceedCpuQuota(reader);
    }

    public void shouldExceedCpuQuota(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        shouldExceedCpuQuota(reader);
    }

    public void shouldExceedCpuQuota(Reader reader)
    {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("kaziranga");

        ResourceQuota quota = new ResourceQuota(100, 0);
        QuotaEnforcer.register(quota);

        try {
            engine.eval(reader);
            assertTrue(false);
        } catch (CpuExceededQuotaException ex) {
            // OK
        } catch (ScriptException ex) {
            fail(ex.getMessage());
        } finally {
            QuotaEnforcer.unregister();
        }
    }

    public void shouldExceedMemoryQuota(String code) {
        StringReader reader = new StringReader(code);
        shouldExceedMemoryQuota(reader);
    }

    public void shouldExceedMemoryQuota(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        shouldExceedMemoryQuota(reader);
    }

    public void shouldExceedMemoryQuota(Reader reader)
    {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("kaziranga");

        ResourceQuota quota = new ResourceQuota(0, 2 * 1024 * 1024);
        QuotaEnforcer.register(quota);

        try {
            engine.eval(reader);
            assertTrue(false);
        } catch (MemoryExceededQuotaException ex) {
            // OK
        } catch (ScriptException ex) {
            fail(ex.getMessage());
        } finally {
            QuotaEnforcer.unregister();
        }
    }

    public void shouldNotExceedQuota(String code) {
        StringReader reader = new StringReader(code);
        shouldNotExceedQuota(reader);
    }

    public void shouldNotExceedQuota(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        shouldNotExceedQuota(reader);
    }

    public void shouldNotExceedQuota(Reader reader)
    {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("kaziranga");

        ResourceQuota quota = new ResourceQuota(0, 5 * 1024 * 1024);
        QuotaEnforcer.register(quota);

        try {
            engine.eval(reader);
        } catch (Throwable ex) {
            fail(ex.getMessage());
            // OK
        } finally {
            QuotaEnforcer.unregister();
        }
    }

    @Test
    public void cpuQuotasAreNotTriggeringForOKCode()
    {
        shouldNotExceedQuota("for (var i = 0; i < 1000; ++i) { var j = i + 10; }");
    }

    @Test
    public void cpuQuotasCannotBeExceededWithInfiniteLoops()
    {
        shouldExceedCpuQuota("for (;;);");
        shouldExceedCpuQuota("while (true);");
    }

    @Test
    public void memoryQuotasCannotBeExceededWithBigAllocations()
    {
        shouldExceedMemoryQuota("var strs = []; for (var i = 0; i < 100000; ++i) { strs.push['foobar' + i]; }");
    }

    @Test
    public void quotasCannotBeBypassedByCatchingTheException()
    {
        shouldExceedCpuQuota(getClass().getResourceAsStream("TryToCatchException.js"));
    }
}
