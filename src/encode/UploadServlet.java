package encode;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.ArrayList;
import java.util.Base64;

@MultipartConfig
public class UploadServlet extends HttpServlet {
	
	//instances of classes initialization
	Extract ext = new Extract();
	EncodeFile enc = new EncodeFile();
	
	private static final Logger logger = Logger.getLogger(UploadServlet.class.getName());
    static {
        try {
            // Create a file handler that writes log messages to a file
        	String propertyPath = "C:/Users/User2/Desktop/info.properties";
        	String logFilePath = Extract.getLogPath(propertyPath);
            FileHandler fileHandler = new FileHandler(logFilePath,10*1024*1024,5, true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter()); // Use a simple text format
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	//retrieving data from the interface
    	 String folderPath = request.getParameter("folderPath");
    	 List<FileInfo> fileInfos = new ArrayList<>();
    	 
    	 Part filePart = request.getPart("file");
    	  String command = request.getParameter("action");
          String printerName = request.getParameter("printerName");
          String message= "";
          String outputPath = request.getParameter("outputPath");
          String flag = request.getParameter("flag");
          String fileRetrieve="";
          String userName = request.getParameter("userName");
          String nonencryptedpassword = request.getParameter("password");
          String PROPERTIES_FILE =  "C:/Users/User2/Desktop/info.properties";
          String key= EncodeFile.loadKeyFromProperties(PROPERTIES_FILE);
    	    	 
          //check if the folder path is empty, so we are working with a file instead of a folder
    	 if ( filePart== null || filePart.getSize() == 0) {
    		 try {
            	 //System.out.println(folderPath);
                 ArrayList<Path> filePaths = ext.listFilesInDirectory(folderPath);
                 //System.out.println(filePaths.get(0));

                 for (Path filePath : filePaths) {
                	 
                     String fileName = ext.extractFileName(filePath);
                     
                     FileInfo fileInfo = new FileInfo();
                     fileInfo.setFileBase64(filePath.toString());
                     fileInfo.setFileName(fileName);
                     
                     fileInfos.add(fileInfo);                   
                 }                 
                 	int i=0;
                 for (FileInfo fileInfo : fileInfos) {
                     String base64EncodedFile = ext.saveFileAndEncode(fileInfo.getFileBase64());
                     FileInfo fileInf=new FileInfo();
                     fileInf.setFileName(fileInfo.getFileName());
                     fileInf.setFileBase64(base64EncodedFile);
                     fileInfos.set(i,fileInf);             
                     i=i+1;
                     logger.info(command+" "+flag+" "+fileInfo.getFileName());

                 }
                 //if we are transferring the files, then i will send it to a specific folder
                 if ("transfer".equals(command)) {
                	 for (Path filePath : filePaths){
                		 ext.moveFile(filePath.toString());
                	 }
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
                
    		 //else we are working with a folder
         }else{          	 

try{     	 
        		 String oneFilePath=ext.saveFileAndGetPath(filePart,folderPath);
        		 Path path= Paths.get(oneFilePath);
        		   ArrayList<Path> filePaths = new ArrayList<Path>();
        		   filePaths.add(path);

                   for (Path filePath : filePaths) {
                  	 
                       String fileName = ext.extractFileName(filePath);
                       
                       FileInfo fileInfo = new FileInfo();
                       fileInfo.setFileBase64(filePath.toString());
                       fileInfo.setFileName(fileName);
                       
                       fileInfos.add(fileInfo);                      
                   }
                   System.out.println(filePaths.size());
                   int i=0;
                   for (FileInfo fileInfo : fileInfos) {
                       String base64EncodedFile = ext.saveFileAndEncode(fileInfo.getFileBase64());
                       FileInfo fileInf=new FileInfo();
                       fileInf.setFileName(fileInfo.getFileName());
                       fileInf.setFileBase64(base64EncodedFile);
                       fileInfos.set(i,fileInf);             
                       i=i+1;
                       logger.info(command+" "+flag+" "+fileInfo.getFileName());

                   }               
               } catch (IOException e) {
                   e.printStackTrace();
               }        
         }
    	 
        String password="";
        FileInfoList fileInfoList = new FileInfoList();
        fileInfoList.setFileInfos(fileInfos);
		try {
			password = EncodeFile.encryptPassword(nonencryptedpassword, key);
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		               
        if ("retrieve".equals(command)) {
        	
        	 if (command.equals("retrieve") && (outputPath == null || outputPath.trim().isEmpty())) {
  			    // Handle the error case where folderPath is required
     			 response.sendRedirect("uploadForm.jsp?action=retrieve&error3=true");
  			    return; // Exit the method to prevent further processing
  			}
        	
        	 fileRetrieve=request.getParameter("fileRetrieve");

           String fileInfosXml = enc.sendFilePath(fileInfoList, command, printerName,outputPath, flag,fileRetrieve,message, userName, password);
          
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><head>");
            out.println("<link rel='stylesheet' type='text/css' href='resultstyle.css'>");
            out.println("</head><body>");
            out.println("<div class='container'>");                      
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            Document doc = null;
			try {
				doc = builder.parse(new InputSource(new StringReader(fileInfosXml)));
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            // Get all <fileInfos> elements
            NodeList fileInfosList = doc.getElementsByTagName("fileInfos");
            //System.out.println(fileInfosList.getLength());
                       
            try{
            	
            	 for (int i = 1; i < fileInfosList.getLength(); i++) {
                     Element fileInfo = (Element) fileInfosList.item(i);
                     String fileBase64 = fileInfo.getElementsByTagName("fileBase64").item(0).getTextContent();
                     String fileName = fileInfo.getElementsByTagName("fileName").item(0).getTextContent();
                    // System.out.println("fileBase64: " + fileBase64);
                       logger.info(command+" "+flag+" "+fileName);   
                           	
    	    byte[] bytes= Base64.getDecoder().decode(fileBase64);
 	
    	    File file = new File("C:/Users/User2/Desktop/RetrievedFile/"+fileName);
    	    try (FileOutputStream foutput = new FileOutputStream(file)) {
    	        foutput.write(bytes);
    	        out.println("<h2>Success: File" + fileName +" created successfully at " + file.getAbsolutePath() + ".</h2>");
    	        logger.info("File retrieved");
    	    } catch (IOException e) {
    	    	 out.println("<h2>Error: Failed to write file. " + e.getMessage() + "</h2>");
    	    }catch (IllegalArgumentException e){
    	    	 out.println("<h2>Error: Failed to decode Base64 string. " + e.getMessage() + "</h2>");
    	    }             	        	 
        }
            	  out.println("<input type='button' value='Return' onclick='window.location.href=\"uploadForm.jsp?action=retrieve\"'> ");
            	  out.println("</div>");
                  out.println("</body></html>");
                  
            }finally{}
        }
        else if(command.equals("message")){
        		message=request.getParameter("message");       	
   		 if (command.equals("message") && (message == null || message.trim().isEmpty())) {
			    // Handle the error case where folderPath is required
   			 response.sendRedirect("uploadForm.jsp?action=message&empty=true");
			    return; // Exit the method to prevent further processing
			}
   		String msgresult = enc.sendFilePath(fileInfoList, command, printerName,outputPath, flag, fileRetrieve,message, userName, password);
   		logger.info(msgresult);
   		
   	 response.setContentType("text/html");
     response.getWriter().println("<html><head>");
     response.getWriter().println("<link rel='stylesheet' type='text/css' href='resultstyle.css'>");
     response.getWriter().println("</head><body>");
     response.getWriter().println("<div class='container'>");
     response.getWriter().println("<h2> "+ msgresult +"</h2>");
     response.getWriter().println("<input type='button' value='Return' onclick='window.location.href=\"uploadForm.jsp?action=message\"'> ");
     response.getWriter().println("</div>");
     response.getWriter().println("</body></html>"); 		 
       	}     
            	 else{
            		 if (command.equals("print") && (folderPath == null || folderPath.trim().isEmpty())) {
         			    // Handle the error case where folderPath is required
            			 response.sendRedirect("uploadForm.jsp?error=true");
         			    return; // Exit the method to prevent further processing
         			}
            		 if (command.equals("transfer") && (folderPath == null || folderPath.trim().isEmpty())) {
          			    // Handle the error case where folderPath is required
             			 response.sendRedirect("uploadForm.jsp?action=transfer&error2=true");
          			    return; // Exit the method to prevent further processing
          			}
        String result = enc.sendFilePath(fileInfoList, command, printerName,outputPath, flag, fileRetrieve,message, userName, password);
        logger.info(result);      	
        // Send response
        response.setContentType("text/html");
        response.getWriter().println("<html><head>");
        response.getWriter().println("<link rel='stylesheet' type='text/css' href='resultstyle.css'>");
        response.getWriter().println("</head><body>");
        response.getWriter().println("<div class='container'>");
        response.getWriter().println("<h2>Result: " + result + "</h2>");
        if (command.equals("transfer")){
        response.getWriter().println("<input type='button' value='Return' onclick='window.location.href=\"uploadForm.jsp?action=transfer\"'> ");
        }
        else{
        	response.getWriter().println("<input type='button' value='Return' onclick='window.location.href=\"uploadForm.jsp\"'> ");
        }
        response.getWriter().println("</div>");
        response.getWriter().println("</body></html>");
    }
        
}
}
