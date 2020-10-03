package io.vacco.oruzka.core;

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

  /**
   * Try to execute the provided function block.
   *
   * @param block the code to run.
   * @throws IllegalStateException if <code>block</code> throws any exception.
   */
  static void tryRun(OFnBlock block) {
    try {
      block.run();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
