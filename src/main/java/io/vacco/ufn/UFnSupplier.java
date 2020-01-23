package io.vacco.ufn;

@FunctionalInterface
public interface UFnSupplier<T> {
  T get() throws Exception;
}
