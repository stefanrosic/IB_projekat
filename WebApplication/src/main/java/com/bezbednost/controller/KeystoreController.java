package com.bezbednost.controller;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.rest.core.Path;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@PostMapping("/generate")
	public String generateJKS(@RequestParam("id") int id) {
		User user = userService.findById(id);
		
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		String path = keyStoreWriter.testIt(user);
		user.setCertificate(path);
		userService.save(user);
		
		return "ok";
	
	}
	

	@RequestMapping(value = "/getjks", method = RequestMethod.GET)
	public @ResponseBody HttpEntity<byte[]> downloadB(@RequestParam("id") int id) throws IOException {
		User u = userService.findById(id);
		File file;
		if(u.getCertificate() != null) {
			file = getFile(u.getCertificate());
			System.out.println("ok");
		}else {
			generateJKS(id);
			file = getFile(u.getCertificate());
		}
	    
	    byte[] document = FileCopyUtils.copyToByteArray(file);
	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(new MediaType("application", "octet-stream"));
	    header.set("Content-Disposition", "inline; filename=" + file.getName());
	    header.setContentLength(document.length);
	    return new HttpEntity<byte[]>(document, header);
	}
	
	private File getFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()){
            throw new FileNotFoundException("file with path: was not found.");
        }
        return file;
    }

}
