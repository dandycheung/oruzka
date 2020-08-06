package io.vacco.oruzka;

/**
 * An unsafe block of code.
 */
public interface OFnBlock {

  /**
   * Run potentially unsafe code, with prior knowledge
   * that the former may throw any kind of exception.
   *
   * @throws Exception if any error occurs.
   */
  void run() throws Exception;
}
