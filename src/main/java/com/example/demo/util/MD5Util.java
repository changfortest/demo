package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 *
 * <p>
 * 说明: 约定:工程编码UTF-8，传输数据字节编码UTF-8，密钥32位，MD5数字签名校验32位。
 * 加密数据采用3DES对称加密采用ECB（Electronic Code Book，电子密码本）模式，
 * 给第三方的密钥经过Base64编码，加密后的密文也经过Base64编码。 MD5加密和sign数字签名都必须转换成小写。
 *
 * 使用org.apache.axis.encoding.Base64 加密解密，76个字符不换行与C#同步
 * </p>
 * <p>
 * 所属项目 51kapay接入平台
 * </p>
 * <p>
 * 所属模块 51kapay工具包
 * </p>
 * <p>
 * 功能说明
 * </p>
 * <p>
 * 公司 深圳市伍壹卡科技有限公司(www.51kapay.com)
 * </p>
 * <p>
 * 创建日期 2014-11-21下午01:53:35
 * </p>
 *
 * @version 1.0
 * @author 51ka.king
 */
public class MD5Util {
    /**
     * 32位MD5加密
     *
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getGeneral32BitMD5(String key)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(key.getBytes("UTF-8"));
        StringBuffer buf = new StringBuffer();
        for (byte b : md.digest())
            buf.append(String.format("%02x", b & 0xff));
        return buf.toString().toLowerCase();
    }
    public static String encryptByMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

}
