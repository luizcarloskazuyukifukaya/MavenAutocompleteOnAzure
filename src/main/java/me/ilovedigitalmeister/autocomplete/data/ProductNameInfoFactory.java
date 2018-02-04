/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ilovedigitalmeister.autocomplete.data;

import java.util.logging.*;
import java.util.HashMap;
import javax.servlet.ServletException;
import com.google.common.html.HtmlEscapers;
import java.util.Iterator;


/**
 *
 * @author kazuyuf
 */
public class ProductNameInfoFactory {
    
    private static final HashMap<String, ProductNameInfo> productNameInfos = new HashMap<String, ProductNameInfo>();
    private static final Logger logger = Logger.getLogger(ProductNameInfoFactory.class.getName());

    public static final int MAX_NAME_LENGTH = 64;

    /**
     * ONLY DISPLY MAX_DISP_CANDIATE_NUM_ITEM
     */
    public static int MAX_DISP_CANDIDATE_NUM_ITEM = 100;
    public static int MAX_DB_LOOKUP_NUM_ITEM = 50000;
    private static boolean _debug = false;
    
    public HashMap getProducts() {
        if (_debug) {
            createDemoDataProducts();
        } else {
            try {
                if(getFromDatabase()) {

                    logger.log(Level.INFO, "Data created from database.");

                } else {
                    /** Create dummy data when instance is created **/            
                    createDemoDataProducts();
                }
            } catch (ServletException ex) {
                /** Create dummy data when instance is created **/            
                createDemoDataProducts();
                logger.log(Level.SEVERE, "SQL Database error occured. {0}", ex.toString());
            }            
        }
        return productNameInfos;
    }
    
    public ProductNameInfoFactory(boolean mode) {
        _debug = mode;        
    }
    
    private HashMap createDemoDataProducts() {

        logger.log(Level.INFO, "Switching to dummy information ... for demo facilitation purpose... ");
        
        productNameInfos.put("100", new ProductNameInfo("100","Apple iPhone"));
        productNameInfos.put("101", new ProductNameInfo("101","Apple iPhone 3G"));
        productNameInfos.put("102", new ProductNameInfo("102","Apple iPhone 3GS"));
        productNameInfos.put("103", new ProductNameInfo("103","Apple iPhone 4"));
        productNameInfos.put("104", new ProductNameInfo("104","Apple iPhone 4S"));
        productNameInfos.put("105", new ProductNameInfo("105","Apple iPhone 5"));
        productNameInfos.put("106", new ProductNameInfo("106","Apple iPhone 5s"));
        productNameInfos.put("107", new ProductNameInfo("107","Apple iPhone 5C"));
        productNameInfos.put("108", new ProductNameInfo("108","Apple iPhone 6"));
        productNameInfos.put("109", new ProductNameInfo("109","Apple iPhone 6 Plus"));
        productNameInfos.put("110", new ProductNameInfo("110","Apple iPhone 6s"));
        productNameInfos.put("111", new ProductNameInfo("111","Apple iPhone 6s Plus"));
        productNameInfos.put("112", new ProductNameInfo("112","Apple iPhone SE"));
        productNameInfos.put("113", new ProductNameInfo("113","Apple iPhone 7"));
        productNameInfos.put("114", new ProductNameInfo("114","Apple iPhone 7 Plus"));
        productNameInfos.put("115", new ProductNameInfo("115","Apple iPhone 7s"));
        productNameInfos.put("116", new ProductNameInfo("116","Apple iPhone 7s Plus"));
        productNameInfos.put("117", new ProductNameInfo("117","Apple iPhone X"));

        productNameInfos.put("200", new ProductNameInfo("200","Big Blue"));
        productNameInfos.put("300", new ProductNameInfo("300","Compaq iPaq"));
        
        productNameInfos.put("501", new ProductNameInfo("501","Google Pixel 2"));
        productNameInfos.put("502", new ProductNameInfo("502","Google Pixel 2 XL"));
        productNameInfos.put("503", new ProductNameInfo("503","Google Pixel XL"));
        productNameInfos.put("504", new ProductNameInfo("504","Google Pixel"));
        productNameInfos.put("505", new ProductNameInfo("505","Google Pixel C"));

        productNameInfos.put("1000", new ProductNameInfo("1000","HPE"));
        productNameInfos.put("2000", new ProductNameInfo("2000","Digital Equipment Corp."));
        productNameInfos.put("3000", new ProductNameInfo("3000","OpenVMS"));

        productNameInfos.put("4000", new ProductNameInfo("4000","PlayStation"));
        productNameInfos.put("5000", new ProductNameInfo("5000","Xbox"));
        productNameInfos.put("5001", new ProductNameInfo("5001","Xbox One"));
        productNameInfos.put("5002", new ProductNameInfo("5002","Xbox 360"));
        productNameInfos.put("6001", new ProductNameInfo("6001","Nintendo Will"));
        productNameInfos.put("6002", new ProductNameInfo("6002","Nintendo Switch"));
        productNameInfos.put("6003", new ProductNameInfo("6003","Nintendo 3DS"));

        return productNameInfos;
    }

    /**
     * Retrieve all product information into a memory cache
     * 
     * @return false if failed to retrieve from the database, otherwise true
     * @throws ServletException then failed with database operation
     */
    public boolean getFromDatabase() throws ServletException {
        boolean retVal = false;
        HashMap<String, ProductNameInfo> products = productNameInfos;
        // User products for reference to the HasMap (Cache data)
        
        return retVal;
    }

        /**
     * Retrieve all product information into a memory cache
     * 
     * @param key keyword to create the String cache
     * @return false if failed to retrieve from the database, otherwise true
     * @throws ServletException then failed with database operation
     */
    public static String getProductNameInfo(String key) throws ServletException {
        
        String retVal = null;
        
        if (key == null) return null;
        if (key.length() < 1) return null;
        
        StringBuilder productNameInfoStr = new StringBuilder();
        
        /**
         * TODO get products from DB, for now will use the debug option of ProductNameInfoFactory
         */
        ProductNameInfoFactory factory = new ProductNameInfoFactory(true);
        HashMap<String, ProductNameInfo> products = factory.getProducts();
        // Iterator<ProductNameInfo>
        Iterator<ProductNameInfo> val_itr = products.values().iterator();
        int cnt = 0; // found count
        // get value
        while(val_itr.hasNext()) {
            // next
            ProductNameInfo p = (ProductNameInfo)val_itr.next();
            String n = p.getName();
            String id = p.getId();
            if (n != null) {
                n = n.trim();
                if( !(n.isEmpty()) ) {
                    //compare with key parameter here
                    if(n.toLowerCase().startsWith(key.toLowerCase())) {
                        // HTML ESCAPE HERE
                        n = HtmlEscapers.htmlEscaper().escape(n);
                        // XML String created here
                        productNameInfoStr.append("<product>");
                        productNameInfoStr.append("<id>").append(id).append("</id>");
                        productNameInfoStr.append("<name>").append(n).append("</name>");
                        productNameInfoStr.append("</product>");
                        cnt++;                                                    
                    } else {
                        logger.log(Level.INFO, "No match with {0}.", n);                        
                    }
                }
            }
        }        
        logger.log(Level.INFO, "Got response from the database engine. {0} records is valid.", cnt);
        if (cnt>0) {
            // records found
            retVal = productNameInfoStr.toString();
            logger.log(Level.INFO, "XML String:{0}", retVal);
        }            
        return retVal;
    }
}
