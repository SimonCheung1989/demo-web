package com.example.demoweb;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs12.PKCS12KeyStore;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoWebApplicationTests {

	@Test
	public void contextLoads() {
	}


	@Test
	public void encodeWithPublicKey() throws Exception {
		String msg = "Hello World!";
		Key publicKey = loadPublicKey("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/localhost.pub");
		Cipher cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedMessage = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));


		System.out.println(Base64.getEncoder().encodeToString(encryptedMessage));
	}


	@Test
	public void decodeWithPrivateKey() throws Exception {
		String encrypted = "wOomV7x8YGMR28WWo5mms+0KezCUdtPWt1sa7oovW3qqACUXHqGHSh5+87JiexV3lT3Zw0oRrhqT5k5svD77htzuWyUYet8QdFrH08dScwdPphvsxSCK3FFOxh0i8nlxX18xyvVovH2g8wXbHiFyTlub2uzCmVOjrOznC6cc8irsUUnqpNWS6mzLWWCyIcKtwly2zlcVyRR/epCCBr5DoXWPEeMM087b7L23Ib88umTdbXbpE/mL2n660qjSPOqRM1sbVz5n6ANBzRAfLNOpu8a4KpWYGSiVJ8IWAjqyYyMnJo/0np0HWf6PP89AQP8/R9Y1mq5keidXi9pLAfQhSA==";
		Cipher cipher=Cipher.getInstance("RSA");
		PrivateKey privateKey = loadPrivateKey("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/localhost.key.pkcs8");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
		System.out.println(new String(decrypted));

	}

	@Test
	public void encodeWithPrivateKey() throws Exception {
		String msg = "Hello World!";
		Cipher cipher=Cipher.getInstance("RSA");
		PrivateKey privateKey = loadPrivateKey("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/localhost.key.pkcs8");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] encryptedMessage = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
		System.out.println(Base64.getEncoder().encodeToString(encryptedMessage));

	}


	@Test
	public void decodeWithPublicKey() throws Exception {
		String encrypted = "YDCoT+tceuSiO1pD5+Mj6hkm1PSiL1yCpuaNLepnDFzDn9nUkKFpHWn4CMi1n7ObB069JTjvbODH5SRgs7EeL9QsD2/qk6HrdXJpKZ8t0XbZbnTC5b2mT2S0+STgp7/aELvR1mayRX3OrkyW8AgR59ukU9iJQKDPXiz6f+u+lkvxuiqoDROfadooOJQsMEVpmIxbxtjXqxY1rjztd89ax2B4Ix3wXsCsGNZomFIxAtZrlSv1yvFgUMmjR10XJddSlZh9kPHqJTnEW8WgbHURDnCqTNQrXis2ZwrG6JWoHrpq1ZexPgGnoB7lWZJoB8VBoxZpQdIRh/LULMle77qv6g==";
		Key publicKey = loadPublicKey("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/localhost.pub");
		Cipher cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));


		System.out.println(new String(decrypted));

		publicKey = loadPublicKeyFromCertificate("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/localhost.crt");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));


		System.out.println(new String(decrypted));
	}

	public static PublicKey loadPublicKey(String filePath) throws Exception {

		String publicKeyPEM = readFileToString(filePath);
		// strip of header, footer, newlines, whitespaces
		publicKeyPEM = publicKeyPEM
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "");

		// decode to get the binary DER representation
		byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER));
		return publicKey;
	}


	public static PrivateKey loadPrivateKey(String filePath) throws Exception {
		String privateKeyPEM = readFileToString(filePath);

		// strip of header, footer, newlines, whitespaces
		privateKeyPEM = privateKeyPEM
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");

		// decode to get the binary DER representation
		byte[] privateKeyDER = Base64.getDecoder().decode(privateKeyPEM);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDER));
		return privateKey;
	}

	public static String readFileToString(String filePath) {
		String fileContent = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(new File(filePath));
			bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				fileContent += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fileReader) {
				try {
					fileReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return fileContent;
	}

	public static PublicKey loadPublicKeyFromCertificate(String certificatePath) {
		FileInputStream fin = null;
		PublicKey pk = null;
		try {
			fin = new FileInputStream(certificatePath);
			CertificateFactory f = CertificateFactory.getInstance("X.509");

			X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
			pk = certificate.getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null!=fin) {
				try {
					fin.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pk;
	}

}
