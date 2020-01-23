package io.vacco.ufn;

public class UFn {

  public static <T> T tryRt(UFnSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static void tryRun(UFnBlock block) {
    tryRt(() -> {
      block.run();
      return null;
    });
  }
}
