package io.vacco.oruzka;

import io.vacco.oruzka.core.OzReflect;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.HashSet;

import static j8spec.J8Spec.*;
import static org.junit.Assert.*;
import static io.vacco.oruzka.util.OzMaps.*;

@RunWith(J8SpecRunner.class)
public class OzReflectSpec { static {
  describe(OzReflect.class.getCanonicalName(), () -> {
    it("can convert primitive to wrapper class types", ()-> {
      assertEquals(OzReflect.toWrapperClass(byte.class), Byte.class);
      assertEquals(OzReflect.toWrapperClass(int.class), Integer.class);
      assertEquals(OzReflect.toWrapperClass(long.class), Long.class);

      assertEquals(OzReflect.toWrapperClass(char.class), Character.class);
      assertEquals(OzReflect.toWrapperClass(boolean.class), Boolean.class);
      assertEquals(OzReflect.toWrapperClass(float.class), Float.class);
      assertEquals(OzReflect.toWrapperClass(double.class), Double.class);
      assertEquals(OzReflect.toWrapperClass(short.class), Short.class);
    });
    it("can inspect values", () -> {
      assertTrue(OzReflect.isBaseType(1));
      assertTrue(OzReflect.isBaseType(1L));
      assertTrue(OzReflect.isBaseType((byte) 1));

      assertTrue(OzReflect.isBaseType(1.0));
      assertTrue(OzReflect.isBaseType(1.0f));

      assertTrue(OzReflect.isBaseType('T'));
      assertTrue(OzReflect.isBaseType(true));
      assertTrue(OzReflect.isBoolean(false));
      assertTrue(OzReflect.isBoolean(Boolean.TRUE));
      assertTrue(OzReflect.isBoolean(Boolean.FALSE));

      assertTrue(OzReflect.isCollection(Arrays.asList(1, 2, 3)));
      assertTrue(OzReflect.isCollection(
          mapOf(
              kv("test", new long[] {1, 2, 3, 4}),
              kv("set", new HashSet<>())
          )
      ));
      assertTrue(OzReflect.isCollection(new int[] {1, 2, 3, 4}));

      assertTrue(OzReflect.isBaseType("Hello world"));
      assertTrue(OzReflect.isBaseType(OzUtilSpec.Color.blue));
      assertFalse(OzReflect.isBaseType(new StringBuilder()));
    });
    it("can inspect value types", () -> {
      assertFalse(OzReflect.isPrimitiveOrWrapper(null));
      assertFalse(OzReflect.isPrimitiveOrWrapper(StringBuilder.class));

      assertTrue(OzReflect.isPrimitiveOrWrapper(Integer.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Long.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Short.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Character.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Boolean.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Float.class));
      assertTrue(OzReflect.isPrimitiveOrWrapper(Double.class));
    });
  });
}}
