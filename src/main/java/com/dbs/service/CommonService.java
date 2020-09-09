package com.dbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CommonService {
	@Autowired
	private Environment env;
	/**
	 * Hash string.
	 *
	 * @param body the body
	 * @return the string
	 */
	public static String getHashValue(String body){
		try {
			// Static getInstance method is called with hashing SHA
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return Base64.getEncoder().encodeToString(md.digest(body.getBytes()));
		}catch(NoSuchAlgorithmException nse){
			//LOGGER.error(nse.getMessage());
			return null;
		}
	}


	public String validateFile(String filename) {

		//java.io.File f = new java.io.File(filePath);

		// Get the file
		//String filePath=env.getProperty("file.path");
		//System.out.println("filePath-------->"+filePath);
		/*File f = new File(filePath+"filename");
		if (f.exists())
			System.out.println("Exists");
		else
			return "File does not exists";*/


		/*Path path = Paths.get("filename");
		Path file_Name = path.getFileName();*/
		if(filename.toString().length()>50){
			return "invalid length";
		}
		// Check if the specified file
		//String fileName = path.getFileName().toString().toUpperCase();
		String fName = filename.toUpperCase();
		boolean extension = fName.endsWith(".JPG") || fName.endsWith(".JPEG") ||fName.endsWith(".PDF") || fName.endsWith(".TIFF") ;
		if (!extension) {
			return "invalid extension";
		}

		return "Success";

	}


}
