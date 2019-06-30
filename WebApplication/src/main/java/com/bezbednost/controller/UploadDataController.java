package com.bezbednost.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezbednost.model.User;
import com.bezbednost.service.UserService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@RestController
public class UploadDataController {
	
    @Autowired
    private UserService userService;
	
	private static String UPLOAD_DIRECTORY_ENCRYPTED = "./upload_directory/";
	private static String UPLOAD_DIRECTORY_NON_ENCRYPTED = "./non_encrypted_directory/";

	
	@PostMapping("/upload")
	public String fileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Principal principal) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException
	{
    	User user = userService.findByEmail(UserController.userName);
    	
		File output= new File(UPLOAD_DIRECTORY_ENCRYPTED + user.getEmail() + "/", file.getOriginalFilename());
		FileOutputStream outStream = new FileOutputStream(output);
		encryptAndClose(outStream, file);   
		
		try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIRECTORY_NON_ENCRYPTED + user.getEmail() + "/" + file.getOriginalFilename());
            Files.write(path, bytes);
			
		}catch (IOException e) {
            e.printStackTrace();
        }
		
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
	    // Flush streams.
	    bos.flush();
	}

}
