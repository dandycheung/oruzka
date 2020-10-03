package io.vacco.oruzka;

import io.vacco.oruzka.core.OFn;
import io.vacco.oruzka.core.OFnBlock;
import io.vacco.oruzka.core.OFnSupplier;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class OFnSpec { static {
  context("Unsafe functions", () -> {
    it("Executes blocks, and fails",
        c -> c.expected(IllegalStateException.class),
        () -> OFnBlock.tryRun(() -> { throw new IllegalArgumentException(""); }));
    it("Executes blocks, and succeeds",
        () -> OFnBlock.tryRun(() -> System.out.println("I ran ok!"))
    );
    it("Executes functions, and fails",
        c -> c.expected(IllegalStateException.class),
        () -> OFn.tryApply(-1, n -> { throw new IllegalArgumentException(Integer.toString(n)); }));
    it("Executes suppliers, and fails",
        c -> c.expected(IllegalStateException.class),
        () -> OFnSupplier.tryGet(() -> { throw new IllegalArgumentException("error"); }));
  });
}}
