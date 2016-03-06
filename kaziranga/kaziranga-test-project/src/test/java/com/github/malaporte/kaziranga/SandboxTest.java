package com.github.malaporte.kaziranga;

import static org.testng.Assert.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.internal.runtime.Undefined;
import org.testng.annotations.*;

public class SandboxTest
{
    private ScriptEngine createEngine()
    {
        return new ScriptEngineManager().getEngineByName("kaziranga");
    }

    @BeforeMethod
    public void beforeTest() {
        QuotaEnforcer.register(new ResourceQuota(0, 0));
    }

    @AfterMethod
    public void afterTest() {
        QuotaEnforcer.unregister();
    }

    @Test
    public void javaScriptCodeCannotAccessPackagesNashornExtensions() throws Exception
    {
        assertEquals(createEngine().eval("Packages"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessRootComRootPackage() throws Exception
    {
        Object ret = createEngine().eval("com;");
        assertNull(ret);
    }

    @Test
    public void javaScriptCodeCannotAccessRootEduRootPackage() throws Exception
    {
        Object ret = createEngine().eval("edu;");
        assertNull(ret);
    }

    @Test
    public void javaScriptCodeCannotAccessRootJavaPackage() throws Exception
    {
        Object ret = createEngine().eval("java;");
        assertNull(ret);
    }

    @Test
    public void javaScriptCodeCannotAccessRootJavafxPackage() throws Exception
    {
        Object ret = createEngine().eval("javafx;");
        assertNull(ret);
    }

    @Test
    public void javaScriptCodeCannotAccessRootJavaxPackage() throws Exception
    {
        Object ret = createEngine().eval("javax;");
        assertNull(ret);
    }

    @Test
    public void javaScriptCodeCannotAccessRootOrgRootPackage() throws Exception
    {
        Object ret = createEngine().eval("org;");
        assertNull(ret);
    }

    @Test(expectedExceptions = ScriptException.class)
    public void javaScriptCodeCannotAccessGetClassMethod() throws Exception
    {
        Object ret = createEngine().eval("'foo'.getClass();");
    }

    @Test(expectedExceptions = ScriptException.class)
    public void javaScriptCodeCannotExecuteProcessesUsingBackQuote() throws Exception
    {
        createEngine().eval("`echo foo`;");
    }

    @Test
    public void javaScriptCodeCannotAccessArgumentNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("arguments"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessPrintNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("print"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessLoadNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("load"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessLoadWithNewGlobalNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("loadWithNewGlobal"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessExitNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("exit"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessQuitNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("quit"), null);
    }

    @Test
    public void javaScriptCodeCannotAccessJsAdapterNashornExtension() throws Exception
    {
        assertEquals(createEngine().eval("JSAdapter"), null);
    }
}
