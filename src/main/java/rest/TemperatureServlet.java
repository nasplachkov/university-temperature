package rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.Temperature;

/**
 * Servlet implementation class TemperatureServlet
 */
public class TemperatureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TemperatureServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getPathInfo();
		Temperature temperature = null;
		String msg = "{}";
		
		try {
			temperature = new Temperature();
		} catch (IOException ex) {
			ex.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		if (path == null || path.equals("/")) {
			msg = temperature.getAll();
		} else if (path.equals("/name")) {
			String name = request.getParameter("q");
			
			if (name != null) {
				msg = temperature.getByName(name);
			}
		} else if (path.equals("/type")) {
			String type = request.getParameter("q");
			
			if (type.contains("temp")) { 
				msg = temperature.getTemperatures();
			} else if (type.contains("hum")) {
				msg = temperature.getHumidities();
			}
		} else if (path.equals("/location")) {
			String lat = request.getParameter("lat");
			String lng = request.getParameter("long");
			String rad = request.getParameter("rad");
			
			if (lat == null || lng == null || rad == null) {
				response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
				return;
			}
			
			msg = temperature.getLocations(Double.parseDouble(lat), Double.parseDouble(lng), Double.parseDouble(rad));
		}
		
		response.setStatus(200);
		response.setContentLength(msg.length());
		response.setContentType("application/json");
		
		PrintWriter pw = response.getWriter();
		pw.write(msg);
		pw.close();
	}

}
