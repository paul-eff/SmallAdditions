package me.gigawartrex.smalladditions.exceptions;

/**
 * Exception for when no filename was specified.
 *
 * @author Paul Ferlitz
 */
public class NoFileNameException extends Exception
{
    /**
     * Class constructor.
     */
    public NoFileNameException()
    {
        super("NO specific FILENAME declared! Can't access any files.");
    }
}
