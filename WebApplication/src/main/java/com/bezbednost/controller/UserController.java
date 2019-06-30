package com.bezbednost.controller;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;
import com.bezbednost.service.AuthorityServiceInterface;
import com.bezbednost.service.UserService;
import com.google.gson.GsonBuilder;

import certificateUtil.KeyStoreWriter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezbednost.dto.userDTO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    public static String userName;

    @Autowired
    private AuthorityServiceInterface authorityService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private static String DATA_DIR_PATH = "./upload_directory/";
    private static String DATA_DIR_PATH_NON_ENCRYPTED = "./non_encrypted_directory/";


    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(userService.findAll()) ,HttpStatus.OK);
    }
    
    @GetMapping("/getAllFiles")
    public ResponseEntity<?> getFiles(Principal principal) throws IOException {
    	User user = userService.findByEmail(principal.getName());
    	userName = user.getUsername();
    	File user_folder = new File("./non_encrypted_directory/" + user.getEmail());
        List<String> filenames = new ArrayList<>();
        generateDirForEncrypt(user);
    	if (user_folder.exists()) {
    		
            final File folder = new File("./non_encrypted_directory/" + user.getEmail());

            List<File> result = new ArrayList<>();

            search(".*\\.rar", folder, result);
            
            for(File f: result) {
            	filenames.add(f.getName());
            }
            
            return new ResponseEntity<>(filenames ,HttpStatus.OK);
    	}else {
    		
    	    try{
    	    	user_folder.mkdir();
    	    } 
    	    catch(SecurityException se){
    	    	
    	    }
    	}
        
        return new ResponseEntity<>(filenames ,HttpStatus.OK);

    }
    
    private void generateDirForEncrypt(User user) {
    	File user_folder_encrpyt = new File("./upload_directory/" + user.getEmail());
    	
    	if (user_folder_encrpyt.exists()) {
    		System.out.println("POSTOJI DIR ZA USERA");
    	}else {
    	    try{
    	    	user_folder_encrpyt.mkdir();
    	    } 
    	    catch(SecurityException se){
    	    	
    	    }
    	}
    }

    public static void search(final String pattern, final File folder, List<File> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern) || f.getName().matches(".*\\.png") || f.getName().matches(".*\\.jpg") || f.getName().matches(".*\\.jpeg") || f.getName().matches(".*\\.zip")) {
                    result.add(f);
                }
            }

        }
    }
 
    
	@RequestMapping(value = "/download/{filename}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("filename") String filename, Principal principal) {
		File file = null;
		User user = userService.findByEmail(principal.getName());
		try {
			file = getFile(DATA_DIR_PATH_NON_ENCRYPTED + user.getEmail() + "/" + filename);			
		}
		catch (Exception e) {
			System.out.println("NOT_FOUND");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} 
		
//		File input= new File(UPLOAD_DIRECTORY, filename);
//		FileInputStream inStream = new FileInputStream(input);
//		decryptAndClose(inStream, outStream);

		
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
    
	
    @GetMapping("/getCurrentRole")
    public ResponseEntity<?> getRole(Principal principal){
    	User user = userService.findByEmail(principal.getName());
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(user.getAuthority()) ,HttpStatus.OK);
    }
    
    @GetMapping("/getCurrentUser")
    public ResponseEntity<?> getUser(Principal principal){
    	User user = userService.findByEmail(principal.getName());
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(user) ,HttpStatus.OK);
    }

    @PostMapping("/createAcc")
    public ResponseEntity<userDTO> createAccount(@RequestBody User user){

        Authority authority = authorityService.findByName("Regular");

        User u = userService.findByEmail(user.getEmail());
        if(u != null){
            return new ResponseEntity<userDTO>(HttpStatus.OK);
        }
        u = new User();
        u.setEmail(user.getEmail());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
       

        u.setActive(false);
		u.getUser_authorities().add(authority);
        u.setCertificate("");

        u = userService.save(u);
        
        User us = userService.findByEmail(u.getEmail());

		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		String path = keyStoreWriter.testIt(us);
		us.setCertificate(path);
		userService.save(us);

        return new ResponseEntity<userDTO>(new userDTO(u),HttpStatus.OK);
    }

    @PostMapping("/activate/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public void activateAccount(@PathVariable("id") int id){
        User user = userService.findById(id);
        if(user != null){
            user.setActive(!user.isActive());
            userService.save(user);
        }
    }
    
	public static void decryptAndClose(FileInputStream fis, FileOutputStream fos) 
    throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

		SecretKeySpec sks = new SecretKeySpec("1234567890123456".getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks);
		
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		
		//wrap input with buffer stream
		BufferedInputStream bis = new BufferedInputStream(cis); 
		
		//wrap output with buffer stream
		BufferedOutputStream bos = new BufferedOutputStream(fos);       
		
		int b;
		byte[] d = new byte[8];
		while((b = bis.read(d)) != -1) {
		    bos.write(d, 0, b);
		}
		bos.flush();
		bos.close();
		bis.close();
	}
	
}