package encode;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.Part;

public class Extract {
	
	//new instance of the encoding class
	 EncodeFile enc = new EncodeFile();
	 	 	 
	 public String saveFileAndGetPath(Part filePart, String folderPath) throws IOException {
		    // Use folderPath provided by the user
		    Path uploadPath = Paths.get(folderPath);

		    // Check if the directory exists; create it if it does not
		    if (!Files.exists(uploadPath)) {
		        Files.createDirectories(uploadPath);
		    }

		    // Extract file name from Content-Disposition header
		    String fileName = extractFileNamePart(filePart);

		    // Create file path using the specified directory
		    Path filePath = uploadPath.resolve(fileName);

		    return filePath.toString();
		}
	    
	 //Method that returns the string Base64 of a file by calling the encode class method and giving it a filepath parameter
	    public String saveFileAndEncode(String filePath) throws IOException {
	    	String fileBase64 = enc.encode(filePath);
	        // deleteFile(filePath);

	         return fileBase64;
	    }
	    
	 	    // method to delete file from the filepath
	   public void deleteFile(String filePath) {
	        Path path = Paths.get(filePath);
	        System.out.println("Attempting to delete file at: " + path.toAbsolutePath());

	        try {
	            if (Files.exists(path)) {
	                // Ensure file is not open before deletion
	                if (Files.isReadable(path)) {
	                    Files.delete(path);
	                    System.out.println("File successfully deleted: " + filePath);
	                } else {
	                    System.out.println("File is not accessible for deletion: " + filePath);
	                }
	            } else {
	                System.out.println("File does not exist: " + filePath);
	            }
	        } catch (java.nio.file.NoSuchFileException e) {
	            System.out.println("File not found: " + filePath);
	        } catch (java.io.IOException e) {
	            System.out.println("IOException occurred while deleting the file: " + filePath);
	            e.printStackTrace();
	        } catch (Exception e) {
	            System.out.println("Unexpected error occurred while deleting the file: " + filePath);
	            e.printStackTrace();
	        }
	    }

	   //method to extract the filename from the browse option in the interface
	    public String extractFileNamePart(Part filePart) {
	        String contentDisposition = filePart.getHeader("Content-Disposition");
	        for (String content : contentDisposition.split(";")) {
	            if (content.trim().startsWith("filename")) {
	                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
	            }
	        }
	        return "unknown";
	    }
	    
	    //method to extract the filename from the filepath
	    public String extractFileName(Path filePath) {
	        // Use the getFileName method from Path to get the file name
	        return filePath.getFileName().toString();
	    }

	    //method that goes through a folder using the folderpath, and fills an array with all the filepaths in this folder
	    public ArrayList<Path> listFilesInDirectory(String directoryPath) throws IOException {
	        Path dirPath = Paths.get(directoryPath);
	        ArrayList<Path> filePaths = new ArrayList<>();

	        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
	            for (Path path : directoryStream) {
	                if (Files.isRegularFile(path)) {
	                    filePaths.add(path);
	                }
	            }
	        }

	        return filePaths;
	    }
	    
	    //method to move transferred files to a folder
	    public void moveFile(String sourceFilePath) throws IOException {
	        // Convert the string paths to Path objects
	        Path sourcePath = Paths.get(sourceFilePath);
	        Path destinationDirPathObj = Paths.get("C:/Users/User2/Desktop/transferred");
	        
	        // Ensure the destination directory exists
	        if (!Files.isDirectory(destinationDirPathObj)) {
	            throw new IOException("Destination directory does not exist: C:/Users/User2/Desktop/transferred");
	        }
	        
	        // Create the destination path by appending the source file name to the destination directory
	        Path destinationPath = destinationDirPathObj.resolve(sourcePath.getFileName());

	        // Move the file to the destination directory
	        Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

	        System.out.println("File moved successfully from " + sourceFilePath + " to " + destinationPath);
	    }
	    
	    
	    public static String getLogPath(String filePath) {
	        Properties properties = new Properties();
	        try (FileInputStream fis = new FileInputStream(filePath)) {
	            // Load the properties file
	            properties.load(fis);
	            
	            // Get the key from the properties file
	            String logPath = properties.getProperty("log.path");
	            // Return the key
	            return logPath;
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;  // Handle this case as needed
	        }
	    }

	    
	    
	    
	    
}
