package com.github.malaporte.kaziranga;

public class KazirangaException extends RuntimeException
{
    public KazirangaException(String message)
    {
        super(message);
    }

    public KazirangaException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
