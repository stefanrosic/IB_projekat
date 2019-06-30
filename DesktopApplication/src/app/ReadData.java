package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ReadData {
	
	public static ArrayList<File> files = new ArrayList<>();
	
	private static String path = new String();
	
	public static void main(String[] args) throws IOException {
		try {
			getDirectoryData();
			generateXML(files);
			createZip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void  getDirectoryData() throws IOException {
		
		System.out.println("Enter the path of directory: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		path = reader.readLine();
		
		File[] directory = new File(path).listFiles();

		for (File file: directory) {
		  if (file.isFile()) {
			  if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg") ||file.getName().endsWith(".gif")) {
				  files.add(file);  
			  }
		  }
		}

		
		
	}
	
	public static void generateXML(ArrayList<File> files) {
		
		
		
		try {
			
			System.out.println("Enter your name: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			String username = reader.readLine();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			
			Element user = doc.createElement("user");
			user.appendChild(doc.createTextNode(username));
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
			
			//--------------Signature--------------------------------
			
//			System.out.println("Enter your name: ");
//			BufferedReader reader_jks = new BufferedReader(new InputStreamReader(System.in));
//			
//			String jks_path = reader.readLine();
			
			PrivateKey pk = SignEnveloped.readPrivateKey();		
			Certificate cert = SignEnveloped.readCertificate();
			
			XMLSignature sig = new XMLSignature(doc,  null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			Transforms transforms = new Transforms(doc);
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_WITH_COMMENTS);
			
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
			sig.addKeyInfo(cert.getPublicKey());
			sig.addKeyInfo((X509Certificate) cert);
			
			rootElement.appendChild(sig.getElement());
			
			sig.sign(pk);	
			System.out.println("....... signed");
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path + "/photos.xml"));
			transformer.transform(source, result);
			files.add(new File(path + "/photos.xml"));
		
			
			for (File file : files) {
				System.out.println(file.getName());
			}
			
			System.out.println("....... ziped");
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createZip() throws IOException {
		FileOutputStream fos = new FileOutputStream(path + "/photos.zip");
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		
		for (File file : files) {
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipOut.putNextEntry(zipEntry);
			
			byte[] bytes = new byte[1024];
			int length;
			while((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			fis.close();
		}
		zipOut.close();
		fos.close();	
		
		File del = new File(path + "/photos.xml");
		del.delete();
		
	}
	

}
