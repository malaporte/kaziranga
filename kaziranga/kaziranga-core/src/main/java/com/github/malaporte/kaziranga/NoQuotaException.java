package com.github.malaporte.kaziranga;

public class NoQuotaException extends KazirangaException
{
    public NoQuotaException()
    {
        super("No quota is being enforced for the current thread.");
    }
}
