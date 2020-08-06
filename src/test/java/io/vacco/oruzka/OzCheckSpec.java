package io.vacco.oruzka;

import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import static j8spec.J8Spec.*;
import static org.junit.Assert.*;

enum MyErrors { OOPS_I_FLOPPED }

@RunWith(J8SpecRunner.class)
public class OzCheckSpec { static {
  it("Parses default root cause constants.", () -> {
    assertTrue(OzCheck.valueOf(OzCheck.GENERAL_ERROR.toString()).equals(OzCheck.GENERAL_ERROR));
    assertTrue(OzCheck.valueOf(OzCheck.MISSING_DATA.toString()).equals(OzCheck.MISSING_DATA));
    assertTrue(OzCheck.valueOf(OzCheck.CONDITION_NOT_SATISFIED.toString()).equals(OzCheck.CONDITION_NOT_SATISFIED));
  });
  it("Encodes an enum constant as a string", () ->
      assertFalse(OzCheck.err(MyErrors.OOPS_I_FLOPPED).contains("_"))
  );
  it("Encodes a null enum constant as a default constant.", () ->
      assertTrue(OzCheck.err(null).equals(OzCheck.err(OzCheck.GENERAL_ERROR)))
  );
  it("Checks that an argument is effectively not null.", () ->
      OzCheck.notNull(new Integer []{})
  );
  it("Checks that an argument is effectively not null, with a custom error constant.", () ->
      OzCheck.notNull(new Integer []{}, MyErrors.OOPS_I_FLOPPED)
  );
  it("Fails that an argument is not null, with no root cause provided.",
      c -> c.expected(IllegalStateException.class), () -> OzCheck.notNull(null));
  it("Fails that an argument is not null, with a default root cause as a message.", () -> {
    try { OzCheck.notNull(null); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertNotNull(e.getMessage());
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.MISSING_DATA)));
    }
  });
  it("Fails that an argument is not null, with a constant root cause provided.", () -> {
    try { OzCheck.notNull(null, MyErrors.OOPS_I_FLOPPED); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertTrue(e.getMessage().equals(OzCheck.err(MyErrors.OOPS_I_FLOPPED)));
    }
  });
  it("Fails that an argument is not null, with a default root cause constant if invoked incorrectly.", () -> {
    try { OzCheck.notNull(null, (Enum) null); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.MISSING_DATA)));
    }
  });
  it("Fails that an argument is not null, with a custom root cause message.", () -> {
    String msg = "Oops, we flopped";
    try { OzCheck.notNull(null, msg); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertTrue(e.getMessage().equals(msg));
    }
  });
  it("Fails that an argument is not null, with a default root cause message if invoked incorrectly.", () -> {
    try { OzCheck.notNull(null, (String) null); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.MISSING_DATA)));
    }
  });
  it("Fails that an argument is not null, with a default root cause message if invoked with an empty message.", () -> {
    try { OzCheck.notNull(null, ""); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.MISSING_DATA)));
    }
  });

  it("Checks that an argument is effectively true.", () -> OzCheck.isTrue(true));
  it("Checks that an argument is effectively true, with a custom error constant.", () ->
    OzCheck.isTrue(true, MyErrors.OOPS_I_FLOPPED)
  );
  it("Fails a false argument.", c -> c.expected(IllegalStateException.class), () -> OzCheck.isTrue(false));
  it("Fails a false argument, with a missing root cause constant.", () -> {
    try { OzCheck.isTrue(false, (Enum) null); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertNotNull(e.getMessage());
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.CONDITION_NOT_SATISFIED)));
    }
  });
  it("Fails a false argument, with a missing root cause message.", () -> {
    try { OzCheck.isTrue(false, (String) null); }
    catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
      assertNotNull(e.getMessage());
      assertTrue(e.getMessage().equals(OzCheck.err(OzCheck.CONDITION_NOT_SATISFIED)));
    }
  });
}}
