package me.gigawartrex.smalladditions.files;

public class NoFileNameException extends Exception {

  private static final long serialVersionUID = 1L;

  public NoFileNameException() {
    super("NO specific FILENAME declared! Can't access any files.");
  }
}
