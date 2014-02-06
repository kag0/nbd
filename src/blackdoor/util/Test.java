package blackdoor.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.swing.text.PlainDocument;
import javax.xml.bind.DatatypeConverter;

//import org.apache.commons.io.FileUtils;





import blackdoor.auth.AuthTicket;
import blackdoor.util.Crypto.EncryptionResult;
import blackdoor.util.Crypto.InvalidKeyLengthException;
import blackdoor.util.Watch.StopWatch;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//byte[] b = new byte[]{23, 57, 23, 0};
		//byte[] a = new byte[]{(byte) 0x1111, 12, 3,4};
		//System.out.println(a);
		//System.out.println(b);
		//byte[] c = Misc.XOR(a, b);
		//System.out.println(c);
		//EncryptionResult x = ExperimentalCrypto.getSHEBlock(, );
		//arrayTest(b);
		//testStuff();
		//System.out.println(InetAddress.getLocalHost().getAddress().length);
		fileHashTest();
		//ticketTest();
		//cryptoTest();
		
	}
	public static void fileHashTest(){
		try {
			File file = new File("D:/Users/nfischer3/Videos/Escape Plan 2013 HDTV AC3 XViD - OLDTiMERS.avi");
			System.out.println(Misc.bytesToHex(Hash.getFileHash(file)));
			System.out.println(Misc.bytesToHex(Hash.getSHA256(Files.readAllBytes(file.toPath()))));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void ticketTest(){
		AuthTicket tic1 = null;
		try {
			tic1 = new AuthTicket("jim", 120, InetAddress.getLocalHost(), (byte) 6);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] pass = Hash.getSHA256("pass".getBytes());
		//System.out.println(Misc.bytesToHex(pass));
		byte[] ticket = tic1.generate(pass);
		//System.out.println(Misc.bytesToHex(ticket));
		SHE cipher = new SHE();
		SHE.EncryptionResult result = new SHE.EncryptionResult(ticket);
		cipher.init(result.getIv(), pass);
		System.out.println("Test: " + Misc.bytesToHex(cipher.doFinal(result.getText())));
		AuthTicket tic2 = new AuthTicket(pass, ticket);
		System.out.println(tic1);
		System.out.println(tic2);
	}
	
	
	public static void cryptoTest(){
		byte[] IV = new byte[32];
		byte[] bigIV = new byte[64];
		byte[] plainText = new byte[1000];
		//byte[] plainText2 = new byte[100000000];
		byte[] key = new byte[32];
		for(int i = 0; i < plainText.length; i++){
			plainText[i] = (byte) ((i/32) +1);
		}
		
		
		
		
		
		
		long total=0;
		double average;
		StopWatch time = new StopWatch(false);
		SHE cipher = new SHE();
		byte[] cipherText;
		byte[] cipherTemp;
		byte[] cipherTemp2;
		IV = cipher.init(key);
		System.out.println("T1");
		cipherTemp = cipher.update(Arrays.copyOfRange(plainText, 0, 72));
		System.out.println("T2");
		cipherTemp2 = cipher.doFinal(Arrays.copyOfRange(plainText, 72, plainText.length));
		cipherText = new byte[cipherTemp.length + cipherTemp2.length];
		System.arraycopy(cipherTemp, 0, cipherText, 0, cipherTemp.length);
		System.arraycopy(cipherTemp2, 0, cipherText, cipherTemp.length,	cipherTemp2.length);
		System.out.println(Misc.bytesToHex(cipherText));
		SHE.EncryptionResult cipherResult = new SHE.EncryptionResult(IV, cipherText);
		System.out.println(Misc.bytesToHex(cipherResult.simpleSerial()));
		System.out.println(cipherResult);
		//cipher.init(IV, key);
		//System.out.println(Misc.bytesToHex(cipher.doFinal(cipherText)));
		SHE.EncryptionResult cipherResult2 = new SHE.EncryptionResult(cipherResult.simpleSerial());
		System.out.println(cipherResult2);
		System.out.println(Misc.bytesToHex(cipherResult2.getText()));
		cipher.init(cipherResult2.getIv(), key);
		
		System.out.println(Misc.bytesToHex(cipher.doFinal(cipherResult2.getText())));
		

//		for(int i = 0; i < 2; i++){
//			System.out.println("Start");
//			time.mark();
//			//Misc.cleanXOR(IV, key);
//			//System.arraycopy(IV, 0, bigIV, 32, IV.length);
//			cipher.init(IV, key);
//			cipherText = cipher.doFinal(plainText);
//			//SHE.doSHE(plainText, key, IV);
//			//cipher.init(IV, key);
//			//decryptedText = cipher.doFinal(cipherText);
//			total+=time.checkNS();
//			
//		}
//		System.out.println(total);
//		average = total/2;
//		System.out.println("Normal: " + average);
//		System.out.println(100/(average/1000000000) + "MB/s");
//		for(int i = 0; i < 3; i++){
//			time.mark();
//			cipher.init(IV, key);
//			cipherText = cipher.doFinalWithInterrupts(plainText);
//			//cipher.init(IV, key);
//			//decryptedText = cipher.doFinalWithInterrupts(cipherText);
//			total+=time.checkNS();
//		}
//		System.out.println(total);
//		average = total/3;
//		System.out.println("interrupt: " + average);
		
		//System.out.println("plaintext: " + Misc.bytesToHex(plainText));
		
		
		
		
		//System.out.println("ciphertex: " + Misc.bytesToHex(cipherText));
		//cipher.init(IV, key);
		//byte[] decryptedText = cipher.doFinal(cipherText);
		//System.out.println("decrytext: " + Misc.bytesToHex(decryptedText));
	}
	
	public static void arrayTest(byte[] arr){
		for(int i = 0; i < arr.length; i++){
			arr[i] = (byte) ~ arr[i];
		}
	}
	

}
