package rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class CleanUrlsFilter
 */
public class CleanUrlsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public CleanUrlsFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String path = req.getRequestURI().substring(req.getContextPath().length());
		
		if (req.getMethod() == "GET") {
			if (path.equals("/maps")) {
				request.getRequestDispatcher("/WEB-INF/public_hidden/maps.html").forward(request, response);
			} else if (path.equals("/logs")) {
				request.getRequestDispatcher("/WEB-INF/public_hidden/logs.html").forward(request, response);
			} else {
				chain.doFilter(request, response);
			}
			
			return;
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
