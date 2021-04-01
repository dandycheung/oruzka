package io.vacco.oruzka.hash;

import io.vacco.oruzka.core.*;
import java.io.*;
import java.security.*;

public class OzMd5 {

  private static final String MD5 = "MD5";

  public static String md5SumOf(InputStream is) {
    return OFn.tryApply(is, is0 -> {
      MessageDigest md = MessageDigest.getInstance(MD5);
      try (DigestInputStream digestInputStream = new DigestInputStream(is0, md)) {
        byte[] buffer = new byte[4096];
        while (digestInputStream.read(buffer) > -1) {} // pass
      }
      byte[] hash = md.digest();
      return OzArrays.bytesToHex(hash);
    });
  }

  public static String md5SumOf(byte[] bytes) {
    return OFn.tryApply(bytes, b0 -> {
      MessageDigest md = MessageDigest.getInstance(MD5);
      md.update(b0);
      return OzArrays.bytesToHex(md.digest());
    });
  }

  public static String md5SumOf(File f) {
    return OFn.tryApply(f, f0 -> OzMd5.md5SumOf(new FileInputStream(f0)));
  }

}
