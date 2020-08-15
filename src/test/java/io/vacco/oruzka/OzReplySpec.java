package io.vacco.oruzka;

import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.util.Collections;
import java.util.List;

import static j8spec.J8Spec.*;
import static org.junit.Assert.*;
import static io.vacco.oruzka.OzReply.Status.*;

@RunWith(J8SpecRunner.class)
public class OzReplySpec { static {
  context("A reply", () -> {

    it("enumerates status values.", () -> {
      for (OzReply.Status status : values()) { System.out.println(status); }
    });
    it("can parse a status value.", () -> assertSame(valueOf("OK"), OK));
    it("has unknown status by default", () -> assertSame(new OzReply().getStatus(), UNKNOWN));
    it("holds an error message with no additional information by default.", () ->
        assertEquals(new OzReply().getMessage(), OzReply.MESSAGE_DEFAULT)
    );
    it("holds no error data.", () -> assertNull(new OzReply().getError()));
    it("default status is UNKNOWN, thus not in a failed or successful state.", () -> {
      OzReply r = new OzReply();
      assertFalse(r.ok());
      assertFalse(r.bad());
    });
    it("always indicates if it is successful.", () -> {
      OzReply<Long> r = new OzReply<Long>().ok(0L);
      assertTrue(r.ok());
      assertEquals(r.getStatus(), OK);
    });
    it("provides a convenience success method.", () -> {
      OzReply<Long> r = OzReply.asOk(123L);
      assertNotNull(r.getData());
    });
    it("always indicates if it is bad.", () -> {
      OzReply r = new OzReply().bad(new IllegalStateException());
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
    });

    it("always has an error message even if no error cause was provided.", () -> {
      OzReply r = new OzReply().bad(null);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("always has an error message when an exception error cause is set, and a custom message is provided.", () -> {
      String oopsMsg = "this method oops'ed";
      OzReply r = new OzReply().bad(new IllegalStateException("oops"), oopsMsg);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), oopsMsg);
    });
    it("always has an error message when an exception error cause is set, and no explicit message is provided.", () -> {
      String oops = "oops...";
      OzReply r = new OzReply().bad(new IllegalStateException(oops), null);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), oops);
    });
    it("always has an error message when an exception error cause is set, and no explicit message is provided in any payload.", () -> {
      OzReply r = new OzReply().bad(new IllegalStateException(), null);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("always has an error message when an error cause is set, and a custom message is provided.", () -> {
      String oopsMsg = "this method oops'ed";
      OzReply r = new OzReply().bad(new Object(), oopsMsg);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), oopsMsg);
    });
    it("always has an error message when an error cause is set, and no explicit message is provided.", () -> {
      String oops = "oops...";
      OzReply r = new OzReply().bad(new Object(), null);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("always has an error message when an error cause is set, and no explicit message is provided in any payload.", () -> {
      OzReply r = new OzReply().bad(new Object(), null);
      assertTrue(r.bad());
      assertEquals(r.getStatus(), BAD);
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("always has an OK status if a payload was assigned, and holds no additional error information.", () -> {
      OzReply<Long> r = new OzReply<Long>().ok(0L);
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
      assertNull(r.getError());
      assertTrue(r.ok());
      assertFalse(r.bad());
      assertEquals(r.getData(), Long.valueOf(0L));
    });
    it("always has an ERROR status, plus an error message if it fails with a single exception.", () -> {
      OzReply r = new OzReply().bad(new IllegalStateException("A processing error occurred."));
      assertTrue(r.bad());
      assertNotNull(r.getError());
      assertNotNull(r.getMessage());
      assertNotEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("always has an ERROR status, plus an error message, even if the command fails without specifying a cause.", () -> {
      OzReply r = new OzReply().bad(new IllegalStateException());
      assertTrue(r.bad());
      assertNotNull(r.getError());
      assertNotNull(r.getMessage());
      assertEquals(r.getMessage(), OzReply.MESSAGE_DEFAULT);
    });
    it("has an ERROR status, plus a non-default error message if the caller specifies one.", () -> {
      String myCustomMsg = "Something really bad happened.";
      OzReply r = new OzReply().bad(new IllegalStateException(myCustomMsg));
      assertNotNull(r.getError());
      assertTrue(r.bad());
      assertEquals(r.getMessage(), myCustomMsg);
    });
    it("guarantees that a failed command caused by an exception will always have a readable text message.", () -> {
      String oops = "";
      OzReply r = new OzReply().bad(new IllegalStateException(oops));
      assertNotNull(r.getMessage());
      assertTrue(r.getMessage().length() > 0);
    });
    it("guarantees that a failed command not caused by an exception will always have a readable text message.", () -> {
      String oops = "";
      OzReply r = new OzReply().bad(new Object(), oops);
      assertNotNull(r.getMessage());
      assertTrue(r.getMessage().length() > 0);
    });
    it("provides a convenience error method",
        () -> assertNotNull(OzReply.asBad(new IllegalStateException("Some error")).getError()));
    it("fails automatically if it is indicated to succeed with an invalid response payload.", () -> {
      OzReply<String> r = new OzReply<String>().ok(null);
      assertNotNull(r.getError());
      assertTrue(r.bad());
      assertEquals(r.getMessage(), OzReply.MESSAGE_INVALID_RESPONSE_DATA);
    });

    it("can include warnings if it succeeds.", () -> {
      OzReply<Integer> r = new OzReply<Integer>().ok(12345);
      r.warning("This service method call will be deprecated in the next version, so stop using it.");
      assertTrue(r.ok());
      assertTrue(r.warning());
      assertFalse(r.getWarnings().isEmpty());
    });
    it("can include warnings if it fails.", () -> {
      OzReply<Integer> r = new OzReply<Integer>().bad(new IllegalStateException());
      r.warning("This service method call will be deprecated in the next version, so stop using it.");
      assertTrue(r.bad());
      assertTrue(r.warning());
      assertFalse(r.getWarnings().isEmpty());
    });
    it("includes a default warning message when a warning is signaled without a cause.", () -> {
      OzReply r = new OzReply().bad(new IllegalStateException("oops"));
      r.warning(null);
      assertTrue(r.bad());
      assertTrue(r.warning());
      assertFalse(r.getWarnings().isEmpty());
    });
    it("has no warnings and does not indicate so if no warnings were issued at all.", () -> {
      OzReply<Long> r = new OzReply<Long>().ok(12345L);
      assertFalse(r.warning());
      assertTrue(r.getWarnings().isEmpty());
    });
    it("can be logged to the console.", () -> {
      OzReply<Integer> r = new OzReply<Integer>().ok(123);
      String rs = r.toString();
      assertNotNull(rs);
      assertTrue(rs.length() > 0);
    });

    it("can be chained and transformed to produce a different kind of response.", () -> {
      OzReply<List<String>> r = new OzReply<>().ok(123L)
          .then(num -> OzReply.asOk(num.toString()))
          .then(str -> OzReply.asOk(Collections.singletonList(str)));
      assertTrue(r.ok());
      assertFalse(r.getData().isEmpty());
      assertEquals(r.getData().size(), 1);
      assertEquals(r.getData().get(0), "123");
    });
    it("fails a whole process chain if the first reply is already failed.", () -> {
      OzReply<String> bad = new OzReply<String>()
          .bad(new IllegalStateException("can't keep going"))
          .then(str -> new OzReply<String>().bad(new IllegalStateException("This should not run")));
      assertTrue(bad.bad());
      assertEquals(bad.getMessage(), "can't keep going");
      assertNotEquals(bad.getMessage(), "This should not run");
    });
    it("breaks a processing chain when the first link fails.", () -> {
      OzReply<Integer> bad = OzReply.asOk("Cool")
          .then(str -> {
            throw new IllegalStateException("Not cool");
          });
      assertTrue(bad.bad());
      assertEquals(bad.getMessage(), "Not cool");
    });
  });
}}
