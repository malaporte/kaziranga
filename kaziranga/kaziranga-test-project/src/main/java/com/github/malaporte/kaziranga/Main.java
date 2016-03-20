package com.github.malaporte.kaziranga;

import javax.script.ScriptEngine;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        ResourceQuota quota = new ResourceQuota(2000, 0);
        QuotaEnforcer.register(quota);

        ScriptEngine engine = new KazirangaScriptEngineFactory().getScriptEngine();
        engine.eval("'foo'.getClass();");
    }
}
