package com.github.malaporte.kaziranga;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class KazirangaScriptEngineFactory implements ScriptEngineFactory
{
    private static final List<String> names;
    private NashornScriptEngineFactory nashorn = new NashornScriptEngineFactory();

    static {
        names = immutableList("kaziranga", "Kaziranga");
    }

    @Override
    public String getEngineName()
    {
        return (String) getParameter(ScriptEngine.ENGINE);
    }

    @Override
    public String getEngineVersion()
    {
        return (String) getParameter(ScriptEngine.ENGINE_VERSION);
    }

    @Override
    public List<String> getExtensions()
    {
        return nashorn.getExtensions();
    }

    @Override
    public String getLanguageName()
    {
        return (String) getParameter(ScriptEngine.LANGUAGE);
    }

    @Override
    public String getLanguageVersion()
    {
        return (String) getParameter(ScriptEngine.LANGUAGE_VERSION);
    }

    @Override
    public String getMethodCallSyntax(final String obj,
                                      final String method,
                                      final String... args)
    {
        return nashorn.getMethodCallSyntax(obj, method, args);
    }

    @Override
    public List<String> getMimeTypes()
    {
        return nashorn.getMimeTypes();
    }

    @Override
    public List<String> getNames()
    {
        return Collections.unmodifiableList(names);
    }

    @Override
    public String getOutputStatement(final String toDisplay)
    {
        return nashorn.getOutputStatement(toDisplay);
    }

    @Override
    public Object getParameter(final String key)
    {
        switch (key) {
            case ScriptEngine.ENGINE:
                return "Kaziranga, based on Oracle Nashorn";
            default:
                return nashorn.getParameter(key);
        }
    }

    @Override
    public String getProgram(final String... statements)
    {
        return nashorn.getProgram(statements);
    }

    @Override
    public ScriptEngine getScriptEngine()
    {
        return nashorn.getScriptEngine();
    }

    private static List<String> immutableList(final String... elements)
    {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
}
