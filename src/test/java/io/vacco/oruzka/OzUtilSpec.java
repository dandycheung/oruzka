package io.vacco.oruzka;

import io.vacco.oruzka.util.OzPatchLeft;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.util.*;

import static j8spec.J8Spec.*;
import static org.junit.Assert.*;
import static io.vacco.oruzka.util.OzMaps.*;

@RunWith(J8SpecRunner.class)
public class OzUtilSpec {

  public enum Color { blue }

  public static class Flop {
    public int foo;
    public String meep;
    public String[] cats;
    public static Flop of(int foo, String meep, String ... cats) {
      Flop f = new Flop();
      f.foo = foo;
      f.meep = meep;
      f.cats = cats;
      return f;
    }
  }

  static {

    Map<String, Object> m0 = mapOf(
        kv("server", mapOf(
            kv("host", "myserver.com"),
            kv("port", 80)
        ))
    );
    Map<String, Object> m1 = concurrentMapOf(
        kv("devMode", true),
        kv("server", mapOf(
            kv("host", "localhost")
        ))
    );

    Flop f0 = Flop.of(1, "meep", "fido", "garfield");
    Flop f1 = Flop.of(2, "moop", "momo", "garfield");
    Flop f2 = Flop.of(42, null, "felix");

    describe("Utilities", () -> {
      it("can build and merge map structures",
          () -> new OzPatchLeft().onMultiple(m0, m1).ifPresent(System.out::println)
      );
      it("can merge objects from right to left", () -> {
        Optional<Flop> f = new OzPatchLeft().onMultiple(f0, f1, f2);
        assertTrue(f.isPresent());
        System.out.println(f.get());
      });
    });
  }
}
