package rest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

/**
 * Servlet implementation class TwitterServlet
 */
public class TwitterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TwitterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		HttpSession session = request.getSession();
		
		if (path.endsWith("/twitter")) {
			
			Object terms = session.getAttribute("terms_accepted");
			if (terms == null) {
				terms = false;
			}
			
			if ((Boolean) terms) {
				// show app page
				request.getRequestDispatcher("/WEB-INF/public_hidden/twitter.html").forward(request, response);
			} else {
				// show terms page
				request.getRequestDispatcher("/WEB-INF/public_hidden/terms.html").forward(request, response);
			}
			
		} else {
			// Twitter callback
			Twitter twitter = (Twitter) session.getAttribute("twitter");
			RequestToken requestToken = (RequestToken) session.getAttribute("requestToken");
			try {
				twitter.getOAuthAccessToken(requestToken, request.getParameter("oauth_verifier"));
				
				session.removeAttribute("requestToken");
				
				response.sendRedirect(request.getContextPath() + "/twitter");
			} catch (TwitterException e) {
				throw new ServletException(e);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		HttpSession session = request.getSession();
		
		if (path.endsWith("/twitter")) {
			
			if (request.getParameter("update") == null) {
				String accepted = request.getParameter("terms_accept");
				
				if (accepted.equals("true")) {
					session.setAttribute("terms_accepted", true);
					
					Twitter twitter = (new TwitterFactory()).getInstance();
					try {
						String baseUrl = String.format("%s://%s:%d%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
						RequestToken rt = twitter.getOAuthRequestToken(baseUrl + "/twitter/callback");
						
						session.setAttribute("twitter", twitter);
						session.setAttribute("requestToken", rt);
						
						response.sendRedirect(rt.getAuthenticationURL());
					} catch (TwitterException e) {
						e.printStackTrace();
					}
				} else {
					response.sendError(400);
				}
			} else {
				String status = request.getParameter("status");
				
				if (status.length() > 0) {
					Twitter twitter = (Twitter) session.getAttribute("twitter");
					
					try {
						twitter.updateStatus(status);
					} catch (TwitterException e) {
						throw new ServletException(e);
					}
				}
			}
		}
	}

}
