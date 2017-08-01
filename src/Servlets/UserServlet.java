package Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = (String) request.getParameter("user").split("&")[0];
		String pass = (String) request.getParameter("pass").split("&")[0];
		boolean remember = Boolean.parseBoolean((String)request.getParameter("remembered").split("&")[0]);
		boolean b = false;
		if(user.compareToIgnoreCase("jonnski") == 0 && pass.compareToIgnoreCase("lmao") == 0){
			b = true;
			Cookie cookie = new Cookie("username", "jonnski");
			if(remember){
				cookie.setMaxAge(60*60*24*21);
			}
			response.addCookie(cookie);
		}
		response.getWriter().write(String.valueOf(b));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cookie cookie = request.getCookies()[0];
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
}
