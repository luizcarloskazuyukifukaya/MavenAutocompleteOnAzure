package me.ilovedigitalmeister.autocomplete;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.ilovedigitalmeister.autocomplete.data.ProductNameInfoFactory;

public class AutoCompleteServlet extends HttpServlet {

    private ServletContext context;
    private static final Logger logger = Logger.getLogger(AutoCompleteServlet.class.getName());
    private static final String FILE_EXTENSTION_NAME = ".xml";
    private static final String BUCKET_UNIQUE_NAME = "autocomplete_xml_dynamic_cache"; // gautocompletedemo

    /**
     * This is to be implemented as GAE Memcache so the Batch App which is triggered by the Task Queue can update the XML Index.
     * 
     */
    private final HashMap<String, String> _storageXMLCacheIndex;; // To be substitude with Google App Engine Memcache

    public AutoCompleteServlet() {
        this._storageXMLCacheIndex = new HashMap();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();                
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.log(Level.INFO, "** doGet **");

        String action = request.getParameter("action");
        // targetId is actuly a key that should match with the searching name in the first few letters.
        String targetId = request.getParameter("id");
        StringBuilder sb = new StringBuilder();

        if (targetId != null) {
            // doAutoCompleteGet
            doAutoCompleteGet(targetId.toLowerCase(), request, response);
        } else if(action == null ) {
            this.context.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void doAutoCompleteGet(String key, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        final String blobName = key+FILE_EXTENSTION_NAME;
        StringBuilder sb = null;
                
        logger.log(Level.INFO, "key:{0}", key);
        if( key == null ) {
            logger.log(Level.INFO, "Skipp doAutoCompleteGet since the keyword is null.");
            return;
        }
        
        // check if user sent empty string
        if (!key.equals("")) {
            
            logger.log(Level.INFO, "Pre-fetch request for {0} ....", key);
            requestPreFetch(key);

            logger.log(Level.INFO, "Searching for word starting with [{0}].", key);
            
            sb = getFromStorageCache(key);
            if (sb == null){ //eventual to call putIntoStorageCache
                String xmlStr = ProductNameInfoFactory.getProductNameInfo(key);
                // Save xml data into Google Storage
                if(xmlStr != null) {
                    if(putIntoStorageCache(key, xmlStr)) {
                        // Put entry on Storage Cache manager
                        // KEY: key, VALUE: filename ([key].xml
                        /**
                         * TODO Implement XML cache and then enable index registration
                        _storageXMLCacheIndex.put(key, blobName);
                        logger.log(Level.INFO,"XML blob [{0}] cache registered.", blobName);
                         */
                        sb = new StringBuilder(xmlStr);
                        logger.log(Level.INFO,"XML content [{0}].", sb.toString());                
                    }
                } else {
                    logger.log(Level.INFO," Entries matching {0} could not be found. Thus no XML cache was created in the Storage Cache.", key);
                }                
            } else {
                if(sb.length()>0) { //If the XML corresponding to the keyword was already stored in the storage
                    logger.log(Level.INFO, "Cache stored in the storage found for reuse. {0}", sb.toString());
                } else {
                    logger.log(Level.INFO, "Cache stored in the storage found but no entry exist. {0}", sb.toString());
                    sb = null;
                }
            }
        }
        if (sb != null) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<products>" + sb.toString() + "</products>");            
        } else {
            //nothing to show
            context.getRequestDispatcher("/error.jsp").forward(request, response);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    private void requestPreFetch(String key) {
        /**
         * TODO pre-fecth mechanism to be implemented here
         */
    }

    private StringBuilder getFromStorageCache(String key) {
        /**
         * TODO Get from xml cache
         */
        return null;
    }

    private boolean putIntoStorageCache(String key, String xmlStr) {
        /**
         * TODO Implement xml cache store
         */
        return true;
    }
}
