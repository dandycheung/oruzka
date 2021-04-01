package io.vacco.oruzka;

import io.vacco.oruzka.io.OzIo;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class OzIoSpec { static {
  describe("I/O", () -> {
    it("can load text lines from a resource", () -> {
      File f = new File("./settings.gradle.kts");
      URL u = f.getAbsoluteFile().toURI().toURL();
      System.out.println(OzIo.loadFrom(u));
      System.out.println(Arrays.toString(OzIo.loadLinesFrom(u)));
    });
  });
}}
