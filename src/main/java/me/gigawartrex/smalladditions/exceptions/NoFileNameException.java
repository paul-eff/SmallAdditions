package me.gigawartrex.smalladditions.exceptions;

/**
 * Exception for when no filename was specified.
 * @author Paul Ferlitz
 * @version 1.0 2020-12-29 Initial Version
 * @since 1.0
 */
public class NoFileNameException extends Exception
{
    public NoFileNameException()
    {
        super("NO specific FILENAME declared! Can't access any files.");
    }
}
