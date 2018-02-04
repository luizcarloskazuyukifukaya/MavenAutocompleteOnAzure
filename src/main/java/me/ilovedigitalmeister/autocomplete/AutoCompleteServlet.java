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

public class AutoCompleteServlet extends HttpServlet {

    private ServletContext context;
    private static final Logger logger = Logger.getLogger(AutoCompleteServlet.class.getName());

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

    private void doAutoCompleteGet(String toLowerCase, HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
