package com.bezbednost.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.User;
import com.bezbednost.service.UserService;

import certificateUtil.KeyStoreWriter;

@RestController
@RequestMapping("/keystore")
public class KeystoreController {
	
	@Autowired
    private UserService userService;
	

	@RequestMapping(value = "/getjks", method = RequestMethod.GET)
	public @ResponseBody HttpEntity<byte[]> downloadB(Principal principal) throws IOException {
		User u = (User) userService.loadUserByUsername(principal.getName());
		File file = null;

		if(u.getCertificate() != null) {
			file = getFile(u.getCertificate());
		}else {
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			String path = keyStoreWriter.testIt(u);
			u.setCertificate(path);
			userService.save(u);
			file = getFile(u.getCertificate());
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("filename", file.getName());
		
		byte[] bFile = readBytesFromFile(file.toString());
		return ResponseEntity.ok().headers(headers).body(bFile);
	    
	}
	
	private static byte[] readBytesFromFile(String filePath) {

		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;
		try {

			File file = new File(filePath);
			bytesArray = new byte[(int) file.length()];

			// read file into bytes[]
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return bytesArray;

	}
	
	private File getFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()){
            throw new FileNotFoundException("file with path: was not found.");
        }
        return file;
    }

}
