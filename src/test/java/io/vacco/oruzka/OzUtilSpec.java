package io.vacco.oruzka;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import io.vacco.oruzka.core.OFnSupplier;
import io.vacco.oruzka.io.OzIo;
import io.vacco.oruzka.util.OzPatchLeft;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.StringWriter;
import java.net.URL;
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

    @Override public String toString() {
      return "Flop{" +
          "foo=" + foo +
          ", meep='" + meep + '\'' +
          ", cats=" + Arrays.toString(cats) +
          '}';
    }
  }

  public enum DataProvider { ClassPath, FileSystem }

  public static class MyConfig {
    public DataProvider provider;
    public String[] resourcePackages;
    public String hostName;
    public int port;

    @Override
    public String toString() {
      return "MyConfig{" +
          "provider=" + provider +
          ", resourcePackages=" + Arrays.toString(resourcePackages) +
          ", hostName='" + hostName + '\'' +
          ", port=" + port +
          '}';
    }
  }

  private static Map<String, Object> readYaml(URL url) {
    YamlReader r = new YamlReader(OzIo.loadFrom(url));
    return OFnSupplier.tryGet(() -> r.read(LinkedHashMap.class));
  }

  private static MyConfig fromMap(Map<String, Object> src) {
    StringWriter sw = new StringWriter();
    YamlWriter writer = new YamlWriter(sw);
    return OFnSupplier.tryGet(() -> {
      writer.getConfig().writeConfig.setWriteClassname(YamlConfig.WriteClassName.NEVER);
      writer.write(src);
      writer.close();
      YamlReader r = new YamlReader(sw.toString());
      return r.read(MyConfig.class);
    });
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
