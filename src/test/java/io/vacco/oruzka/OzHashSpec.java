package io.vacco.oruzka;

import io.vacco.oruzka.core.OzArrays;
import io.vacco.oruzka.hash.OzMd5;
import io.vacco.oruzka.hash.OzXxHash;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.stream.IntStream;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class OzHashSpec {
  static {
    describe("Hashing", () -> {
      it("can hash an array of strings", () -> {
        OzArrays.toStringConcat("Hello", "World")
            .ifPresent(bytes -> System.out.println(OzMd5.md5SumOf(bytes)));
      });
      it("can compute the MD5 sum of a file",  () -> {
        System.out.println(OzMd5.md5SumOf(new File("./build.gradle.kts")));
      });
      it("can compute XXHash values for byte arrays", () -> {
        int seed = 42;
        long seedL = 42L;
        OzArrays.toStringConcat(IntStream.range(0, 256).boxed().toArray()).ifPresent(bytes -> {
          System.out.println(OzXxHash.hash32(bytes, 0, bytes.length, seed));
          System.out.println(OzXxHash.hash64(bytes, 0, bytes.length, seedL));
        });
      });
    });
  }
}
