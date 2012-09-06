/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byttersoft.code;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 *
 * @author zhangjf
 */
public class Base64Util {

    public final static int BUFFER_SIZE = 4096;
    public final static String FILE_EXTENSION_TAG = "bytter";

    /**
     * 加密文件,无密钥
     *
     * @param toEncodeFilePath
     * @param encodedFilePath
     */
    public static void encodeFile(String toEncodeFilePath, String encodedFilePath)  throws Exception{
        encodeFile(toEncodeFilePath, encodedFilePath, "bytter");
    }

    /**
     * 加密文件，按指定密钥加密
     *
     * @param toEncodeFilePath
     * @param encodedFilePath
     * @param key
     */
    public static void encodeFile(String toEncodeFilePath, String encodedFilePath, String key) throws Exception{
        byte[] buffer = new byte[BUFFER_SIZE];
        OutputStream encodeOutput = new Base64OutputStream(new FileOutputStream(encodedFilePath), true);//加密文件流
        if (key != null) {
        	byte[] head = Base64.encodeBase64(key.getBytes());//加密头部密钥
        	encodeOutput.write(head, 0, head.length);//密钥、文件流合并
        } 
        
        int bytesRead = -1;
        InputStream input = new FileInputStream(toEncodeFilePath);
        try {
	        while ((bytesRead = input.read(buffer, 0, BUFFER_SIZE)) > 0) {
	            encodeOutput.write(buffer, 0, bytesRead);
	        }
        } finally {
        	input.close();
        	encodeOutput.close();
        }
    }

    /**
     * 解密文件，无密钥
     *
     * @param encodedFilePath
     * @param decodedFilePath
     */
    public static void decodeFile(String encodedFilePath, String decodedFilePath) throws Exception{
        decodeFile(encodedFilePath, decodedFilePath, "bytter");
    }

    /**
     * 解密文件，按指定密钥解密
     *
     * @param encodedFilePath
     * @param decodedFilePath
     * @param key
     */
    public static void decodeFile(String encodedFilePath, String decodedFilePath, String key)  throws Exception {
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream decodeInput = new Base64InputStream(new FileInputStream(encodedFilePath), false);//解密文件流
        try {
	        if (key != null) {
	            byte[] encodeHead = Base64.encodeBase64(key.getBytes());//加密头部密钥
	            byte[] head = new byte[encodeHead.length];
	            decodeInput.read(head, 0, head.length); //解析头部密钥
	            if (!(new String(encodeHead).equals(new String(head)))) { //密钥错误
	                throw new RuntimeException("非法密钥，解密失败！");
	            }
	        }
        } finally {
        	decodeInput.close();
        }
        
        OutputStream output = new FileOutputStream(decodedFilePath);
        try {
	        int bytesRead = -1;
	        while ((bytesRead = decodeInput.read(buffer, 0, BUFFER_SIZE)) > 0) {
	            output.write(buffer, 0, bytesRead);
	        }
        } finally {
        	output.close();
        }
    }
}
