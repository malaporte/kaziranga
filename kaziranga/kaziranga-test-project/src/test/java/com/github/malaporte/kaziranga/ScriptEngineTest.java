package com.github.malaporte.kaziranga;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.testng.annotations.Test;

public class ScriptEngineTest
{
    @Test
    public void scriptEngineCanSuccessfullyLoadLargeCodebase() throws Exception
    {
        QuotaEnforcer.register(new ResourceQuota(0, 0));
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("kaziranga");
            engine.eval(new InputStreamReader(getClass().getResourceAsStream("jquery.js")));
            engine.eval(new InputStreamReader(getClass().getResourceAsStream("underscore.js")));
        } finally {
            QuotaEnforcer.unregister();
        }
    }
}
