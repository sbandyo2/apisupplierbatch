package com.ibm.gdc.batch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.gdc.batch.dbUtils.DBConstants;


public class GDBatchUtils {

	private static final String CONFIG_FILE = "batch_config.properties";
	private  Map<String, String> xmlMap = null;
	
	private  Map<String, Map<String, String>> connMap = null;

	
	/**
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public static String getConnectionConfig(String key) throws IOException {
		Properties properties = null;
		String value = "";
		properties = new Properties();
		InputStream inputStream = null;

		inputStream = GDBatchUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

		if (inputStream != null)
			properties.load(inputStream);
		if (properties != null)
			value = properties.getProperty(key);

		return value;
	}
	
	/**
	 * @param obj
	 * @return
	 */
	public static String getString(Object obj) {
		String str = "";
		
		if (obj != null) {
			str = String.valueOf(obj);
		}
		return str;
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(Timestamp tmstamp) {
		if (tmstamp == null) {
			return true;
		}

		return false;
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String[] str) {
		if (str == null) {
			return true;
		} else if (str.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static String getValue(String key) throws IOException {
		Properties properties = null;
		InputStream inputStream = null;

		// load the properties file
		properties = new Properties();
		inputStream = GDBatchUtils.class.getClassLoader().getResourceAsStream(
				CONFIG_FILE);

		if (inputStream != null)
			properties.load(inputStream);

		inputStream.close();
		return properties.getProperty(key);
	}

	/**
	 * @param directoryName
	 */
	public static List<File> listFiles(String directoryName) {
		List<File> files = null;
		File directory = null;
		File[] fList = null;

		// get all the files from a directory
		directory = new File(directoryName);
		fList = directory.listFiles();

		files = new ArrayList<File>();

		for (File file : fList) {

			if (file.isFile()) {
				files.add(file);
			}

		}

		return files;
	}

	/**
	 * @param ts
	 * @param delimeter
	 * @return
	 */
	public static String formatTimeStamp(String ts, String delimeter) {
		ts = ts.replace(" ", delimeter);
		ts = ts.replace(".", delimeter);
		ts = ts.replace(":", delimeter);
		ts = ts.replace("-", delimeter);
		return ts;
	}

	/**
	 * @return
	 */
	public static String getCurrentTimestampToString() {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

	/**
	 * @param source
	 * @param destination
	 * @throws BatchException
	 */
	public static void copyAndDeleteFile(String source, String destination)
			throws BatchException {
		InputStream inStream = null;
		OutputStream outStream = null;
		File sourceFile = null;
		File targetFile = null;

		try {
			sourceFile = new File(source);

			// stop the processing if source file does not exist
			if (!sourceFile.exists())
				return;

			targetFile = new File(destination);

			inStream = new FileInputStream(sourceFile);
			outStream = new FileOutputStream(targetFile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			// delete the original file
			sourceFile.delete();

		} catch (IOException e) {
			throw new BatchException(e.getMessage());
		}
	}
	
	/**
	 * @param deleteFile
	 */
	public static void deleteFile(String deleteFile) {
		File file = null;

		file = new File(deleteFile);

		if (file.exists())
			file.delete();
	}

	/**
	 * @param directory
	 */
	public static void makeDir(String directory) {
		File dir = null;
		dir = new File(directory);

		if (!dir.exists())
			dir.mkdir();
	}
	
	
	/**
	 * Get the resource bundle for the selected role and language code. 
	 * The resource bundle is either cached or retrieved from property file
	 * @return
	 * @throws BatchException 
	 */
	public  String getConnectionPropertiesValue(String key, String appType) throws BatchException {
		String value = null;
		String propertyFileName = null;
		String dbEncrypted = null;
		StringEncrypter encrypter = null;
		boolean firstTimeEncryption = false;
		String nodeName = null;
		
		try {
			
			propertyFileName = appType + BatchConstants.connection_properties+ ".xml";
			
			FileInputStream in = new FileInputStream(GDBatchUtils.getValue(BatchConstants.PROP_DIR)+propertyFileName);
			
			if(connMap==null){
				connMap = new HashMap<String, Map<String,String>>();
				xmlMap = new HashMap<String, String>();
			}else{
				xmlMap = connMap.get(propertyFileName);
			}
		
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.parse(in);
			
			encrypter = new StringEncrypter();
			
			Node rootNode = doc.getElementsByTagName(DBConstants.DB_CONN).item(0);
			NodeList childNodes = rootNode.getChildNodes();
			for(int i=0;i<childNodes.getLength();i++){
				Node childNode =  childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						//get the value
						value = childNode.getTextContent();
						nodeName = childNode.getNodeName();
						
						if(!xmlMap.containsKey(nodeName))
							xmlMap.put(nodeName, value);
				}
			}
			
			if(!connMap.containsKey(propertyFileName))
				connMap.put(propertyFileName, xmlMap);
			
			//handle password encryption
			if(DBConstants.DB_PWD.equalsIgnoreCase(key)){
				dbEncrypted = xmlMap.get(DBConstants.DB_PWD_ENCRYPTED);
				if(DBConstants.DB_PWD_ENCRYPTED_NO.equalsIgnoreCase(dbEncrypted)){
					
					//get the plain text and encryption
					String unEncryptVal =  xmlMap.get(DBConstants.DB_PWD);
					String encryptedVal = encrypter.encrypt(unEncryptVal);
					
					//se
					for(int nodeCount=0;nodeCount<childNodes.getLength();nodeCount++){
						Node childNode =  childNodes.item(nodeCount);
						if (childNode.getNodeType() == Node.ELEMENT_NODE) {
							
							//set the password encrypted param to yes
							if(childNode.getNodeName().equalsIgnoreCase(DBConstants.DB_PWD_ENCRYPTED))
								childNode.setTextContent(DBConstants.DB_PWD_ENCRYPTED_YES);
							
							// set the pwd encrypted in the properties file for
							// the first time
							if(childNode.getNodeName().equalsIgnoreCase(DBConstants.DB_PWD))
								childNode.setTextContent(encryptedVal);
						}
					}
					//update the xml
					TransformerFactory factory = TransformerFactory.newInstance();
					Transformer transformer = factory.newTransformer();
					GDBatchUtils.class.getClassLoader().getResourceAsStream(propertyFileName);
					
					StringWriter sw = new StringWriter();
					StreamResult result = new StreamResult(sw);
					DOMSource source = new DOMSource(doc);
					transformer.transform(source, result);
					
					firstTimeEncryption = true;
				}
				if(!firstTimeEncryption){
					//decrypt the value
					value =xmlMap.get(DBConstants.DB_PWD);
					return encrypter.decrypt(value);
				}
			}
			
		} catch (IOException e) {
			throw new BatchException(e);
		} catch (TransformerConfigurationException e) {
			throw new BatchException(e);
		} catch (TransformerException e) {
			throw new BatchException(e);
		} catch (ParserConfigurationException e) {
			throw new BatchException(e);
		} catch (SAXException e) {
			throw new BatchException(e);
		}

		return xmlMap.get(key);
	}
	
	
	/**
	 * @param processFiles
	 * @throws BatchException
	 */
	public static void archiveAndDelete(List<File> processFiles) throws BatchException {

        ZipOutputStream zipOutputStream = null;       
        String fName = null;
        String name = null;
        String extension = null;
        String doneDir = null;
        try {            
        	
			extension = GDBatchUtils.getValue(BatchConstants.FILE_EXT);
			doneDir = GDBatchUtils.getValue(BatchConstants.DONE_DIR);
        	//make done directory
        	makeDir(doneDir);
        	
            // Iterating the list of file(s) to zip/compress
            for (File file : processFiles) {
            	
            	fName = file.getName();
				name = fName.substring(	0,fName.indexOf(BatchConstants.DOT+ extension));
            	
            	// Creating the zipOutputStream instance
                zipOutputStream = new ZipOutputStream(new FileOutputStream(doneDir+BatchConstants.DOUBLE_BACKSLASH+name+BatchConstants.ZIP));
                
                // Adding the file(s) to the zip
				ZipEntry zipEntry = new ZipEntry(name + BatchConstants.DOT+ extension);
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file);
                
                int length;
                byte[] buffer = new byte[1024];
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }                
                // Closing the fileInputStream instance
                fileInputStream.close();
                // De-allocating the memory by assigning the null value
                fileInputStream = null;
                
                //closing the streams
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                
                //delete the file after archival
                deleteFile(file.getPath());
                
            }
        } catch (IOException iOException) {
            throw new BatchException(iOException);
        } finally {
            // Validating if zipOutputStream instance in not null
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                } catch (IOException iOException) {
                }
            }
        }
    }
	
	
	/**
	 * @param processFiles
	 * @throws BatchException
	 */
	public static void archiveAndDeleteForError(List<File> processFiles) throws BatchException {

        ZipOutputStream zipOutputStream = null;       
        String fName = null;
        String name = null;
        String extension = null;
        String invalidDir = null;
        try {            
        	
			extension = GDBatchUtils.getValue(BatchConstants.FILE_EXT);
			invalidDir = GDBatchUtils.getValue(BatchConstants.INVALID_DIR);
        	//make done directory
        	makeDir(invalidDir);
        	
            // Iterating the list of file(s) to zip/compress
            for (File file : processFiles) {
            	
            	fName = file.getName();
				name = fName.substring(	0,fName.indexOf(BatchConstants.DOT+ extension));
            	
            	// Creating the zipOutputStream instance
                zipOutputStream = new ZipOutputStream(new FileOutputStream(invalidDir+BatchConstants.DOUBLE_BACKSLASH+name+BatchConstants.ZIP));
                
                // Adding the file(s) to the zip
				ZipEntry zipEntry = new ZipEntry(name + BatchConstants.DOT+ extension);
                zipOutputStream.putNextEntry(zipEntry);
                FileInputStream fileInputStream = new FileInputStream(file);
                
                int length;
                byte[] buffer = new byte[1024];
                while ((length = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }                
                // Closing the fileInputStream instance
                fileInputStream.close();
                // De-allocating the memory by assigning the null value
                fileInputStream = null;
                
                //closing the streams
                zipOutputStream.closeEntry();
                zipOutputStream.close();
                
                //delete the file after archival
                deleteFile(file.getPath());
                
            }
        } catch (IOException iOException) {
            throw new BatchException(iOException);
        } finally {
            // Validating if zipOutputStream instance in not null
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                } catch (IOException iOException) {
                }
            }
        }
    }

}
