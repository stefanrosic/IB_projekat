package com.bezbednost.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Generated;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReadData {
	
	public static ArrayList<File> files = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		getDirectoryData();
		generateXML(files);
	}
	
	public static void  getDirectoryData() throws IOException {
		
		System.out.println("Input the path of directory: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String path = reader.readLine();
		
		File[] directory = new File(path).listFiles();

		for (File file: directory) {
		  if (file.isFile()) {
			  files.add(file);
			  //primer jednog Path-a za uneti(u nasem projektu se nalazi): ./data
		  } 
		}
		
		for (File file : files) {
			System.out.println(file.getName());
		}
		
		
		
	}
	
	public static void generateXML(ArrayList<File> files) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			
			Element user = doc.createElement("user");
			user.appendChild(doc.createTextNode("Bonko"));
			rootElement.appendChild(user);
			
			Element photos = doc.createElement("photos");
			rootElement.appendChild(photos);
			
			for (File f : files) {
				Element photo = doc.createElement("photo");
				photos.appendChild(photo);
				
				Element photoName = doc.createElement("photoName");
				photoName.appendChild(doc.createTextNode(f.getName()));
				photo.appendChild(photoName);
				
				Element photoSize = doc.createElement("photoSize");
				photoSize.appendChild(doc.createTextNode(String.valueOf(f.length() / 1024)));
				photo.appendChild(photoSize);
				
				Element photoHash = doc.createElement("hashCode");
				photoHash.appendChild(doc.createTextNode(String.valueOf(f.hashCode())));
				photo.appendChild(photoHash);
				
			}
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
		
			
			Element dateElement = doc.createElement("date");
			dateElement.appendChild(doc.createTextNode(dateFormat.format(date)));
			rootElement.appendChild(dateElement);
			
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\Users\\Boris\\Desktop\\photos.xml"));
			transformer.transform(source, result);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
