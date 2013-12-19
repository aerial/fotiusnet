package com.fotius.server.impl;

import com.fotius.server.impl.hibernate.HibernateUtil;
import com.fotius.shared.model.Document;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;

/**
 * This is an example of how to use UploadAction class.
 *
 * This servlet saves all received files in a temporary folder,
 * and deletes them when the user sends a remove request.
 *
 * @author Manolo Carrasco Moñino
 *
 */
public class UploaderServlet extends UploadAction {


    private static final String uploadDir = "C:\\uploaded";
    private static final long serialVersionUID = 1L;

    Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
    /**
     * Maintain a list with received files and their content types.
     */
    Hashtable<String, File> receivedFiles = new Hashtable<String, File>();


    private HibernateUtil hibernateUtil = new HibernateUtil();
    private static Logger log = Logger.getLogger(FotiusServiceImpl.class.getName());



    /**
     * Override executeAction to save the received files in a custom place
     * and delete this items from session.
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = "";
        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {
                try {
                    /// Create a new file based on the remote file name in the client
                    // String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
                    // File file =new File("/tmp/" + saveName);

                    /// Create a temporary file placed in /tmp (only works in unix)
                    // File file = File.createTempFile("upload-", ".bin", new File("/tmp"));

                    /// Create a temporary file placed in the default system temp folder
                    File file = File.createTempFile("upload-", ".bin");
                    item.write(file);

                    /// Save a list with the received files
                    receivedFiles.put(item.getFieldName(), file);
                    receivedContentTypes.put(item.getFieldName(), item.getContentType());
                    String hashed = generateHash(file);
                    file.renameTo(new File(uploadDir + File.separatorChar + hashed));
                    /// Send a customized message to the client.
                    response += "File saved as " + file.getAbsolutePath();

                    Document document = new Document();
                    document.setName(item.getName());
//                    docuemtn

                } catch (Exception e) {
                    throw new UploadActionException(e);
                }
            }
        }

        /// Remove files from session because we have a copy of them
        removeSessionFileItems(request);

        /// Send your customized message to the client.
        return response;
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fieldName = request.getParameter(UConsts.PARAM_SHOW);
        File f = receivedFiles.get(fieldName);
        if (f != null) {
            response.setContentType(receivedContentTypes.get(fieldName));
            FileInputStream is = new FileInputStream(f);
            copyFromInputStreamToOutputStream(is, response.getOutputStream());
        } else {
            renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
        }
    }

    /**
     * Remove a file when the user sends a delete request.
     */
    @Override
    public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
        File file = receivedFiles.get(fieldName);
        receivedFiles.remove(fieldName);
        receivedContentTypes.remove(fieldName);
        if (file != null) {
            file.delete();
        }
    }


    private static String generateHash(File file) throws NoSuchAlgorithmException,
            FileNotFoundException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5"); // SHA or MD5
        String hash = "";
        byte[] data = new byte[(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data);
        fis.close();
        md.update(data);
        byte[] digest = md.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(digest[i]);
            if (hex.length() == 1)
                hex = "0" + hex;
            hex = hex.substring(hex.length() - 2);
            hash += hex;
        }
        return hash;
    }

    private Document saveDocument(Document doc) {
        Session session = hibernateUtil.getSessionFactory().getCurrentSession();
        Document savedDocument = null;
        try {
            session.beginTransaction();
            savedDocument = (Document) session.merge(doc);

        } catch (Exception e) {
            log.error("Error occured while saving document information to DB:");
            e.printStackTrace();
            return null;
        }
        return savedDocument;
    }
}