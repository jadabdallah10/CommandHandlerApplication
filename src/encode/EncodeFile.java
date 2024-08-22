package encode;


import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;



import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Base64;
import java.net.URL;


@XmlRootElement(name = "sendFilePath", namespace = "http://encode/")
@WebService
	public class EncodeFile {
		
	//Method to read the encoded file and return it as a String	
	@WebMethod
		 public String encode(String filepath) {
		        // Define the input and output file paths
		        File inputFile = new File(filepath); // File to convert

		        try {
		            // Read file to byte array
		            byte[] fileBytes = readFileToByteArray(inputFile);

		            // convert the array to Base64
		            String base64Encoded = encodeToBase64(fileBytes);

		          return base64Encoded;

		        } catch (IOException e) {
		            e.printStackTrace();
		            return null;
		        }
		    }
		 
		//method to read all bytes in a file;
		@WebMethod
		  public byte[] readFileToByteArray(File file) throws IOException {
		        //to read the file
		        return Files.readAllBytes(file.toPath());
		    }
		  
		//method to convert from bytes to base64
		@WebMethod
		  public String encodeToBase64(byte[] data) {
		        // convert from bytes to Base64
		        return Base64.getEncoder().encodeToString(data);
		    }
			
	    //Password encryption
	    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	    private static final String STATIC_IV_HEX =  "C:/Users/User2/Desktop/info.properties";

	    // Convert a hexadecimal string to a byte array
	    public static byte[] hexStringToByteArray(String s) {
	        int len = s.length();
	        byte[] data = new byte[len / 2];
	        for (int i = 0; i < len; i += 2) {
	            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                                  + Character.digit(s.charAt(i + 1), 16));
	        }
	        return data;
	    }

	    // Convert a byte array to a hexadecimal string
	    public static String byteArrayToHexString(byte[] bytes) {
	        StringBuilder sb = new StringBuilder();
	        for (byte b : bytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }

	    // Encrypt the password using AES-256 with CBC mode and static IV
	    public static String encryptPassword(String password, String keyHex) throws GeneralSecurityException, IOException {
	        // Convert the hexadecimal key to a byte array
	        byte[] keyBytes = hexStringToByteArray(keyHex);
	        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

	        // Convert the static IV to a byte array
	        String iv=loadivFromProperties();
	        byte[] ivBytes = hexStringToByteArray(iv);
	        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

	        // Initialize the cipher for encryption
	        Cipher cipher = Cipher.getInstance(ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

	        // Encrypt the password
	        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
	        byte[] encryptedBytes = cipher.doFinal(passwordBytes);

	        // Convert the encrypted byte array to a hexadecimal string
	        return byteArrayToHexString(encryptedBytes);
	    }

	    // Load the AES key from the properties file
	    public static String loadKeyFromProperties(String propertyfile) throws IOException {
	        Properties properties = new Properties();
	        try (FileInputStream input = new FileInputStream(propertyfile)) {
	            // Load properties file
	            properties.load(input);
	            // Get the key value
	            return properties.getProperty("aes.key");
	        }
	    }
	    
	    //Load the IV key from the properties file
	    public static String loadivFromProperties() throws IOException {
	        Properties properties = new Properties();
	        try (FileInputStream input = new FileInputStream(STATIC_IV_HEX)) {
	            // Load properties file
	            properties.load(input);
	            // Get the key value
	            return properties.getProperty("iv.key");
	        }
	    }

		
	//Soap request operation
		@WebMethod(operationName= "sendFilePath")
		public String sendFilePath(FileInfoList fileInfoList, String command, String printerName, String outputPath,
				String flag,String fileRetrieve,String message, String userName, String password){
			  try {
		            // Create a SOAP message and fill it with the file path
		            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		           
		            MessageFactory messageFactory = MessageFactory.newInstance();
		            SOAPMessage soapMessage = messageFactory.createMessage();

		           SOAPPart soapPart = soapMessage.getSOAPPart();
		           SOAPEnvelope envelope = soapPart.getEnvelope();
		           envelope.addNamespaceDeclaration("ns", "http://encode/");
		            
		           SOAPBody soapBody = envelope.getBody();
		            SOAPBodyElement bodyElement = (SOAPBodyElement) soapBody.addChildElement("sendFilePath", "ns", "http://encode/");
		           
		            //converted Array into Xml String
		            String fileInfosXml = XmlUtils.convertFileInfoListToXml(fileInfoList);
		            bodyElement.addChildElement("arg0").addTextNode(fileInfosXml);
		            
		            //Adding the other String elements to the message
		            bodyElement.addChildElement("arg1").addTextNode(command);
		            bodyElement.addChildElement("arg2").addTextNode(printerName);
		            bodyElement.addChildElement("arg3").addTextNode(outputPath);
		            bodyElement.addChildElement("arg4").addTextNode(flag);
		            bodyElement.addChildElement("arg5").addTextNode(fileRetrieve);
		            bodyElement.addChildElement("arg6").addTextNode(message);
		            bodyElement.addChildElement("arg7").addTextNode(userName);
		            bodyElement.addChildElement("arg8").addTextNode(password);

		            

		         
		        soapMessage.saveChanges();

		        // Request SOAP message
		        System.out.println("Request SOAP Message:");
		        soapMessage.writeTo(System.out);
		        System.out.println();

		        // Endpoint of the other server
		        URL endpointURL = new URL("http://192.168.0.109:8080/CommandProject/services/commandhandler");
		        // Send request and receive the base64 string of the content
		        SOAPMessage response = soapConnection.call(soapMessage, endpointURL);
		            
		        // Access the SOAP Body
		        SOAPBody body = response.getSOAPBody();
		            
		    // Retrieve the return tag's value
		SOAPElement sendFilePathResponse = (SOAPElement) body.getChildElements(new QName("http://encode/", "sendFilePathResponse")).next();
		SOAPElement returnElement = (SOAPElement) sendFilePathResponse.getChildElements(new QName("return")).next();
		   String returnValue = returnElement.getValue();

		       // Print the value of the 64base string
		        System.out.println("Return Value: " + returnValue);		            		            
		          		            
		            return returnValue;

		        } catch (Exception e) {
		            e.printStackTrace();
		            return "Error occurred: " + e.getMessage();
		        }
		           
		}
}
