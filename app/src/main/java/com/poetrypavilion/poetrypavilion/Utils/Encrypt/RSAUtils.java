package com.poetrypavilion.poetrypavilion.Utils.Encrypt;

import com.poetrypavilion.poetrypavilion.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final String CHARSET = "UTF-8";
    private static final String RSA_ALGORITHM = "RSA";


    private static Map<String, String> createKeys(int keySize) {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     *
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     */
    private static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     *
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     */

    private static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }


    public static void test(String[] args) throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTT_KKYS4_rgZzvoYdcvFCdolD11CknMb5B0EO2YFPxeJ1gWOlUtvohwAPjOuczVV8VQXfo3p8G9clSKEs2kQYN6osk7wsUvzNkiNZZfdDxXTTWZ_OO-gmPUaoSLbbju4c04t3UlLY7Gy_0N879CADt12928XKgJxBeCO6qqjURQIDAQAB";
        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJNP8ophLj-uBnO-hh1y8UJ2iUPXUKScxvkHQQ7ZgU_F4nWBY6VS2-iHAA-M65zNVXxVBd-jenwb1yVIoSzaRBg3qiyTvCxS_M2SI1ll90PFdNNZn8476CY9RqhIttuO7hzTi3dSUtjsbL_Q3zv0IAO3Xb3bxcqAnEF4I7qqqNRFAgMBAAECgYAj3Gh8y1-I3QJxFsZrUNnuhhCwVTAqgoflg6CQzt4t30xbsp3hVyR1nLelEXch-tJ-q-v8Hz3oDJb5Wo7x7kh5BbinzM2STHt2wXTFDrn0hoTr-pnqW2GuN4J3neCedbFwhcALl4r40KgkBxPEkN2WH0x3PqOusCSddebqtkN12QJBANcbopasZnwHMqkJB4P_qlFklFxMDSeUtmjbV__prA6M8w8ArhHxv6G-pG7lE_TZjacPHEjbWIirsSzfF1HUuRsCQQCvUP5lVudX2IJ5PJ5FNloQQ_BOliNCy4AdEKq_N9rWSsqYV4ub3YPHoc39HU2RKgsuqDEjDzOdtRpg9NjLM94fAkADPH0zuyBHmWwfsu0UBVKwqsy4nsRL5ibgaPxn80INbf1yKlZ0GqkBa_bZXETymBtTehy40-P_GmYuAKU_K-4lAkEAndkf5CB11TtEZmI04n5IYwK4mBlIEDjn1cOO6PIiX_UhKj8QnTq6DwFZVT-P-lbPkKew7i8euy03Xfq2Q2ck4wJBAL0w3ghGrd8sHZsws3j8b3b3lTjEaEEG3l6TqNZoHe7HD1H1BYcA_hGhKR7J4uE8DPpbf-MNqBVt_7GYgOKWE1Q";
//        Map<String, String> keyMap = RSAUtils.createKeys(1024);
//        String publicKey = keyMap.get("publicKey");
//        String privateKey = keyMap.get("privateKey");
//        System.out.println("公钥: \n\r" + publicKey);
//        System.out.println("私钥： \n\r" + privateKey);

        System.out.println("公钥加密 —— 私钥解密");
        String str = "MIGfMA0GCSqGSIb3DQEBAAoGBAJNP8ophLj-uBnO-hh1y8UJ2i";

        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("密文：\r\n" + encodedData);
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("解密后文字: \r\n" + decodedData);
    }
}