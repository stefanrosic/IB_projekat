package com.bezbednost.controller;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;
import com.bezbednost.service.AuthorityServiceInterface;
import com.bezbednost.service.UserService;
import com.google.gson.GsonBuilder;

import certificateUtil.KeyStoreWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private AuthorityServiceInterface authorityService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private static String DATA_DIR_PATH = "./upload_directory/";

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(userService.findAll()) ,HttpStatus.OK);
    }
    
    @GetMapping("/getAllFiles")
    public ResponseEntity<?> getFiles(Principal principal) {
    	//User user = userService.findByEmail(principal.getName());
        final File folder = new File("./upload_directory/");

        List<File> result = new ArrayList<>();

        search(".*\\.rar", folder, result);
        
        return new ResponseEntity<>(result ,HttpStatus.OK);

    }

    public static void search(final String pattern, final File folder, List<File> result) {
        for (final File f : folder.listFiles()) {

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern) || f.getName().matches(".*\\.png") || f.getName().matches(".*\\.jpg") || f.getName().matches(".*\\.jpeg")) {
                    result.add(f);
                }
            }

        }
    }
    
	@RequestMapping(value = "/download/{filename}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("filename") String filename) {

		File file = null;
		try {
			file = getFile(DATA_DIR_PATH + filename);			
		}
		catch (Exception e) {
			System.out.println("NOT_FOUND");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    
	
    @GetMapping("/getCurrentRole")
    public ResponseEntity<?> getRole(Principal principal){
    	User user = userService.findByEmail(principal.getName());
    	System.out.println(user.getAuthority());
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(user.getAuthority()) ,HttpStatus.OK);
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

}



