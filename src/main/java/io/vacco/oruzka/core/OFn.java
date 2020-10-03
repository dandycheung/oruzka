package io.vacco.oruzka.core;

/**
 * An unsafe function wrapper.
 */
public interface OFn<I, O> {

  O apply(I i) throws Exception;

  /**
   * Try to execute the provided function on an argument.
   *
   * @param i the argument.
   * @param fn the unsafe function.
   * @param <I> the argument's type.
   * @param <O> the unsafe function return type.
   * @return the result of the unsafe function.
   * @throws IllegalStateException if <code>fn</code> throws any exception.
   */
  static <I, O> O tryApply(I i, OFn<I, O> fn) {
    try {
      return fn.apply(i);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
