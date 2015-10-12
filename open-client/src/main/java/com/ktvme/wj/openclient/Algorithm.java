package com.ktvme.wj.openclient;

import java.io.PrintStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public final class Algorithm
{
  private static final String DESEDE = "DESede";

  public static String md5Encrypt(String string)
  {
    try
    {
      MessageDigest alg = MessageDigest.getInstance("MD5");
      alg.update(string.getBytes());
      byte[] userPwd = alg.digest();
      return byteArr2HexStr(userPwd);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static String TrippleEncrypt(String content, String desKey)
  {
    try
    {
      byte[] data = content.getBytes("utf-8");
      byte[] key = desKey.getBytes("utf-8");

      String algorithm = "DESede";
      DESedeKeySpec keyspec = new DESedeKeySpec(key);
      SecretKeyFactory keyfactory = 
        SecretKeyFactory.getInstance(algorithm);
      Security.addProvider(new BouncyCastleProvider());
      Key enkey = keyfactory.generateSecret(keyspec);
      Cipher c1 = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");
      c1.init(1, enkey, new IvParameterSpec(Hex.decode("0102030405060708")));
      byte[] cipherByte = c1.doFinal(data);
      return byteArr2HexStr(cipherByte);
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  public static String TrippleDecrypt(String content, String desKey)
  {
    try
    {
      byte[] data = hexStr2ByteArr(content);
      byte[] key = desKey.getBytes("utf-8");

      String algorithm = "DESede";
      DESedeKeySpec keyspec = new DESedeKeySpec(key);
      SecretKeyFactory keyfactory = 
        SecretKeyFactory.getInstance(algorithm);
      Security.addProvider(new BouncyCastleProvider());
      Key enkey = keyfactory.generateSecret(keyspec);
      Cipher c1 = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");
      c1.init(2, enkey, new IvParameterSpec(Hex.decode("0102030405060708")));
      byte[] cipherByte = c1.doFinal(data);
      return new String(cipherByte, "utf-8");
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }

  private static String byteArr2HexStr(byte[] bytea) throws Exception
  {
    String sHex = "";
    int iUnsigned = 0;
    StringBuffer sbHex = new StringBuffer();
    for (int i = 0; i < bytea.length; i++) {
      iUnsigned = bytea[i];
      if (iUnsigned < 0) {
        iUnsigned += 256;
      }
      if (iUnsigned < 16) {
        sbHex.append("0");
      }
      sbHex.append(Integer.toString(iUnsigned, 16));
    }
    sHex = sbHex.toString();
    return sHex;
  }

  private static byte[] hexStr2ByteArr(String sHex)
    throws Exception
  {
    if (sHex.length() % 2 != 0) {
      sHex = "0" + sHex;
    }
    byte[] bytea = new byte[sHex.length() / 2];

    String sHexSingle = "";
    for (int i = 0; i < bytea.length; i++) {
      sHexSingle = sHex.substring(i * 2, i * 2 + 2);
      bytea[i] = (byte)Integer.parseInt(sHexSingle, 16);
    }
    return bytea;
  }

  public static void main(String[] args) throws Exception {
    String desKey = "nattaaaanattaaaanattaaaa";
    String content = "sdfdsfs你好啊dfdsf";

    String s = TrippleEncrypt(content, desKey);
    System.out.println("the s is: " + s);
    System.out.println(TrippleDecrypt(s, desKey));

    System.out.println(md5Encrypt("ADFRMBjd54D"));
  }
}