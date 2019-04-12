package com.example.demoweb;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.crypto.Cipher;
import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
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
	public void encode() throws Exception {
		String msg = "Hello World!";
		Key publicKey = loadPublicKey();
// specify mode and padding instead of relying on defaults (use OAEP if available!)
		Cipher cipher=Cipher.getInstance("RSA");
// init with the *public key*!
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
// encrypt with known character encoding, you should probably use hybrid cryptography instead
		byte[] encryptedMessage = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));


		System.out.println(Base64.getEncoder().encodeToString(encryptedMessage));
	}


	@Test
	public void decode() throws Exception {
		String encrypted = "Enj5tA2D0/hI9iXxakpM/aOsyV5Gi42/Y82fGEGOS+BQkV5UzELEKoT3QJuT+bmLQzlfMPLIOcuuO2Z/T2SDMznX7xFWg+8QgzrSz5yzY/Lc1JESsufgF54y8r/c3ECqUWA3rHNwmJjg7Pl37ElIzQz+1k2Xo6IgMxeosdOurpDfv6RJMe1OIlgg9UIwsRMTJUymxjmyAaWSS5PCmF4jsz85xl8Vh8UuJYJY2rCBMgGXJyrubpsxsNLFpZ26CxUeLOlRaULGevHYVpXt93unqP7+JdAYLCPzVuCPkvTiJD2H8oBy68sDhIICFlA0Ld0Ih1tSfm1IVQ4Hrtd3wKT86Q==";
		Cipher cipher=Cipher.getInstance("RSA");
		PrivateKey privateKey = loadPrivateKey();
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
		System.out.println(new String(decrypted));

	}

	public static PublicKey loadPublicKey() throws Exception {

		String publicKeyPEM = readFileToString("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/public.key");
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


	public static PrivateKey loadPrivateKey() throws Exception {
		String privateKeyPEM = readFileToString("/Users/simon/workspace_idea/security-demo/demo-web/src/main/resources/keystore/private-pkcs8.key");

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
}
