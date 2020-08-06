package io.vacco.oruzka;

import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class OFnSpec { static {
  context("An Unsafe function", () -> {
    it("Executes code, and fails",
        c -> c.expected(IllegalStateException.class),
        () -> OFn.tryRun(() -> { throw new IllegalArgumentException(""); }));
    it("Executes code, and succeeds",
        () -> OFn.tryRun(() -> System.out.println("I ran ok!"))
    );
    it("Completes code coverage", OFn::new);
  });
}}
