package com.github.malaporte.kaziranga;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        ResourceQuota quota = new ResourceQuota(2000, 0);
        QuotaEnforcer.register(quota);

        ScriptEngine kaziranga = new ScriptEngineManager().getEngineByName("kaziranga");
        int res = (int) kaziranga.eval("1 + 1");
        System.out.println("Output is " + res);

        kaziranga.eval("while (true) { var i = 0; }");
    }

}
