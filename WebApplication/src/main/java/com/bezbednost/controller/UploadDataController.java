package com.bezbednost.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@RestController
public class UploadDataController {
	
	private static String UPLOAD_DIRECTORY = "./upload_directory/";
	
	@PostMapping("/upload")
	public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException
	{

		File output= new File(UPLOAD_DIRECTORY, file.getOriginalFilename());
		FileOutputStream outStream = new FileOutputStream(output);
		encryptAndClose(outStream, file);   
		
		
//		File input= new File(UPLOAD_DIRECTORY, file.getOriginalFilename());
//		FileInputStream inStream = new FileInputStream(input);
//		decryptAndClose(inStream, outStream);

        return "USPESAN UPLOAD I ZIP ENKRIPTOVAN!";
	}
	
	public static void encryptAndClose(FileOutputStream fos, MultipartFile file) 
	        throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

	    // Length is 16 byte
	    SecretKeySpec sks = new SecretKeySpec("1234567890123456".getBytes(), "AES");
	    // Create cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, sks);      

	    // Wrap the output stream for encoding
	    CipherOutputStream cos = new CipherOutputStream(fos, cipher);       

	    //wrap output with buffer stream
	    BufferedOutputStream bos = new BufferedOutputStream(cos);     

	    //wrap input with buffer stream
	    BufferedInputStream bis = new BufferedInputStream(file.getInputStream()); 

	    // Write bytes
	    int b;
	    byte[] d = new byte[8];
	    while((b = bis.read(d)) != -1) {
	        bos.write(d, 0, b);
	    }
	    // Flush and close streams.
	    bos.flush();
	    //bos.close();
	    //bis.close();
	}
	
//	public static void decryptAndClose(FileInputStream fis, FileOutputStream fos) 
//	        throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
//
//	    SecretKeySpec sks = new SecretKeySpec("1234567890123456".getBytes(), "AES");
//	    Cipher cipher = Cipher.getInstance("AES");
//	    cipher.init(Cipher.DECRYPT_MODE, sks);
//
//	    CipherInputStream cis = new CipherInputStream(fis, cipher);
//
//	    //wrap input with buffer stream
//	    BufferedInputStream bis = new BufferedInputStream(cis); 
//
//	    //wrap output with buffer stream
//	    BufferedOutputStream bos = new BufferedOutputStream(fos);       
//
//	    int b;
//	    byte[] d = new byte[8];
//	    while((b = bis.read(d)) != -1) {
//	        bos.write(d, 0, b);
//	    }
//	    bos.flush();
//	    bos.close();
//	    bis.close();
//	}

}
