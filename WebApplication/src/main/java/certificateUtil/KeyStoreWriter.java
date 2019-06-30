package certificateUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;

import com.bezbednost.model.User;
import com.bezbednost.service.UserService;


public class KeyStoreWriter {
	
	
	private KeyStore keyStore;
	
	public KeyStoreWriter() {
		try {
			keyStore = KeyStore.getInstance("JKS");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
	
	public void loadKeyStore(String fileName, char[] password) {
		try {
			if(fileName != null) 
				keyStore.load(new FileInputStream(fileName), password);
			else
				//ako nema postojeceg KS fajla u koji cemo dodavati, vec se kreira novi,
				//mora da se pozove load, pri cemu je prvi parametar sada null
				keyStore.load(null, password);
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveKeyStore(String fileName, char[] password) {
		try {
			keyStore.store(new FileOutputStream(fileName), password);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
		try {
			keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
	}
	
	
	public String testIt(User user) {
		try {
			//kreiramo generator i generisemo kljuceve i sertifiakt
			CertificateGenerator gen = new CertificateGenerator();
			//par kljuceva
			KeyPair keyPair = gen.generateKeyPair();
			
			//datumi
			SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = iso8601Formater.parse("2018-12-31");
			Date endDate = iso8601Formater.parse("2028-12-31");
			
			//podaci o vlasniku i izdavacu posto je self signed 
			//klasa X500NameBuilder pravi X500Name objekat koji nam treba
			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.CN, "Goran Sladic");
		    builder.addRDN(BCStyle.SURNAME, user.getEmail());
		    builder.addRDN(BCStyle.GIVENNAME, "Goran");
		    builder.addRDN(BCStyle.O, "UNS-FTN");
		    builder.addRDN(BCStyle.OU, "Katedra za informatiku");
		    builder.addRDN(BCStyle.C, "RS");
		    builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
		    //UID (USER ID) je ID korisnika
		    builder.addRDN(BCStyle.UID, "123445");
			
		    //Serijski broj sertifikata
			String sn="1";
			//kreiraju se podaci za issuer-a
			IssuerData issuerData = new IssuerData(keyPair.getPrivate(), builder.build());
			//kreiraju se podaci za vlasnika
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), builder.build(), sn, startDate, endDate);
			
			//generise se sertifikat
			X509Certificate cert = gen.generateCertificate(issuerData, subjectData);
			
			//kreira se keystore, ucitava ks fajl, dodaje kljuc i sertifikat i sacuvaju se izmene
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			keyStoreWriter.loadKeyStore(null, "test".toCharArray());
//			keyStoreWriter.write(user.getEmail().concat("s_jks"), keyPair.getPrivate(), user.getPassword().toCharArray(), cert);
//			keyStoreWriter.saveKeyStore("./data/"+user.getEmail()+"s_jks.jks", user.getPassword().toCharArray());
			
			keyStoreWriter.write(user.getEmail().concat("s_jks"), keyPair.getPrivate(), "test".toCharArray(), cert);
			keyStoreWriter.saveKeyStore("./data/"+user.getEmail()+"s_jks.jks", "test".toCharArray());
			String path = "./data/"+user.getEmail()+"s_jks.jks";
			return path;
			
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	
//	public static void main(String[] args) {
//		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
//		keyStoreWriter.testIt(new User());
//	}
}
