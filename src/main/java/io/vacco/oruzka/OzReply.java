package io.vacco.oruzka;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

/**
 * A reply is a simple wrapper object which represents an answer
 * to a request (the semantics of which are not modeled inside this class)
 * which has side effects (i.e. creating a file, writing content to a database, etc.).
 *
 * Therefore, the use of this class is only recommended for operations where side effects
 * are expected and need to be tracked.
 *
 * A reply provides:
 *
 * <ul>
 *   <li>Some sort of response data, as the type {@code T}, outlined below.</li>
 *   <li>A status which indicates if the request failed or succeeded.</li>
 *   <li>Optional error data in the form of a {@code Throwable}, if an error occurred.</li>
 *   <li>An optional message which provides more information about this reply.</li>
 *   <li>An optional warning set, in case the underlying code issues them.</li>
 * </ul>
 *
 * The type {@code T} is provided as a placeholder for the kind of answer that
 * the caller expects to receive from the operation that created this reply.
 *
 * For example, if a service class provides the following method:
 *
 * {@code public Reply<File> copy(File source, File dest) throws IOException}
 *
 * The resulting payload of the reply, retrieved by calling {@code getData} could be:
 *
 * <ul>
 *   <li>
 *     The same file reference, echoed back in case the caller is not interested in
 *     knowing more details about the target copied file {@code dest}.
 *   </li>
 *   <li>
 *     The {@code dest} reference to the copied file, in case the caller needs more
 *     information about what happened to the destination file.
 *   </li>
 *   <li>
 *     A different kind of {@code File} reference. For example, the top level directory
 *     where the destination file was copied to.
 *   </li>
 * </ul>
 *
 * While it could be argued that this is a simplistic scenario, the real advantage of a
 * reply lies within the the provided payload type {@code T}, which gives code
 * authors freedom to choose the kind of data that would be suitable for a response, plus the
 * contextual diagnostic data associated with the reply itself.
 *
 * In addition, a reply can include a collection of warning messages to signal implementation
 * details that a consumer should keep in mind. These could be, for example, API method
 * signature deprecation messages for a certain component library.
 *
 * Note that warnings should mostly be used to indicate out-of-band messages to the client code
 * calls, and should not affect the execution flow of the latter.
 *
 * @param <T> the target type expected from a successful reply, if any.
 * @author Jesus Zazueta
 * @since 1.0
 */
public class OzReply<T> {

  public enum Status {OK, BAD, UNKNOWN}

  public static final String MESSAGE_DEFAULT = "no.additional.information";
  public static final String MESSAGE_INVALID_RESPONSE_DATA = "response.data.must.not.be.null";

  private T data;
  private Status status;
  private Object error;
  private String message;
  private final Collection<String> warnings = new HashSet<>();

  /** @return the reply's data, if any. */
  public T getData() { return data; }
  private void setData(T data) {
    if (data == null) {
      bad(new IllegalArgumentException(MESSAGE_INVALID_RESPONSE_DATA));
    } else {
      this.data = data;
      this.status = Status.OK;
    }
  }

  /** @return the response status, if any. */
  public Status getStatus() {
    return status == null ? Status.UNKNOWN : status;
  }

  /** @return the error data, if any. */
  public Object getError() { return error; }
  private void setError(Object error) {
    this.error = error;
    this.status = Status.BAD;
  }

  /** @return the error message, if any. */
  public String getMessage() {
    if (message != null && message.trim().length() > 0) {
      return this.message;
    } else if (error instanceof Throwable) {
      String tMsg = ((Throwable) error).getMessage();
      if (tMsg != null && tMsg.trim().length() > 0) {
        return tMsg;
      }
    }
    return MESSAGE_DEFAULT;
  }

  /** @return any warnings issued for this reply, if any. */
  public Collection<String> getWarnings() { return warnings; }

  /**
   * Signal a successful command.
   * @param data the command result's payload.
   * @return this reply.
   */
  public OzReply<T> ok(T data) {
    setData(data);
    return this;
  }

  /**
   * Builder convenience method.
   * @param data the command result's payload.
   * @param <T> the command result data.
   * @return a new reply.
   */
  public static <T> OzReply<T> asOk(T data) {
    return new OzReply<T>().ok(data);
  }

  /**
   * Signal an error in a command.
   * @param error the root cause of the error.
   * @return this reply.
   */
  public OzReply<T> bad(Object error) {
    setError(error);
    return this;
  }

  /**
   * Builder convenience method.
   * @param error an immediate root cause.
   * @return a new bad reply.
   */
  public static OzReply<?> asBad(Object error) {
    return new OzReply<>().bad(error);
  }

  /**
   * Signal an error in a command, with an additional explanation.
   * @param error        the root cause of the error.
   * @param errorMessage
   *   an optional explanation of the error. May be <code>null</code>.
   *   When <code>errorMessage</code> is not <code>null</code> and not
   *   empty, it will take priority over <code>error</code>'s internal
   *   message, only when <code>error</code> is an instance of
   *   <code>Throwable</code>.
   * @return this reply.
   */
  public OzReply<T> bad(Object error, String errorMessage) {
    setError(error);
    this.message = errorMessage;
    return this;
  }

  /**
   * Convenience status retrieval method.
   * @return {@code true} if the command was successful, {@code false} otherwise.
   */
  public boolean ok() { return this.status == Status.OK; }

  /**
   * Convenience status retrieval method.
   * @return {@code true} if the command failed, {@code false} otherwise.
   */
  public boolean bad() { return this.status == Status.BAD; }

  /**
   * Convenience status retrieval method.
   * @return {@code true} if the command includes warning messages, {@code false} otherwise.
   */
  public boolean warning() { return !this.warnings.isEmpty(); }

  /**
   * Signal an execution warning in a command, with additional information.
   * @param message the cause for the warning, if any.
   * @return this reply.
   */
  public OzReply<T> warning(String message) {
    warnings.add(message != null ? message :
        "This reply signaled a warning without providing cause. please verify your code."
    );
    return this;
  }

  /**
   * Convenience chaining method to process a reply as a series of processing steps. For example:
   *
   * <pre>
   * OzReply&lt;String&gt; r = OzReply.asOk(123L)
   *   .then(num -&gt; num.toString());
   * </pre>
   *
   * @param fn a reply transform function which receives the last input result, and yields a new reply.
   * @param <O> the final output result type.
   * @return
   *  a reply in either <code>ok</code> or <code>bad</code> state, which depends
   *  on the processing outcomes of each link in the chain. When the reply is in
   *  <code>bad</code> state, it will include the last error that occurred in the
   *  chain.
   */
  public <O> OzReply<O> then(Function<T, OzReply<O>> fn) {
    if (this.ok()) {
      try {
        return fn.apply(this.data);
      } catch (Exception x) {
        return new OzReply<O>().bad(x);
      }
    }
    return new OzReply<O>().bad(this.error);
  }

  @Override
  public String toString() {
    return String.format(
        "%s[stat: %s, msg: %s, err: %s, warn: %s]",
        this.getClass().getSimpleName(),
        this.status, this.message, this.error, this.warnings
    );
  }
}
