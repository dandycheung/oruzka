package io.vacco.oruzka;

/**
 * An unsafe function wrapper.
 */
public class OFn {

  /**
   * Try to execute the provided function supplier, returning
   * a response.
   *
   * @param supplier the unsafe code to run.
   * @param <T> a potential return type <code>T</code>.
   * @return <code>T</code> if the supplier code runs correctly.
   * @throws IllegalStateException if <code>supplier</code> throws any exception.
   */
  public static <T> T tryRt(OFnSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Try to execute the provided function block.
   *
   * @param block the code to run.
   * @throws IllegalStateException if <code>block</code> throws any exception.
   */
  public static void tryRun(OFnBlock block) {
    tryRt(() -> {
      block.run();
      return null;
    });
  }
}
