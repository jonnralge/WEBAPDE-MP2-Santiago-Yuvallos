package mp2.Servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mp2.Control.UserHandler;
import mp2.Model.User;

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
    /*
     * Format
     * 		UserHandler?user=___
     * 
     * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String user = (String)request.getParameter("user");
		UserHandler uh = new UserHandler();
		User u = uh.getUser(user);
		response.setContentType("text/plain");
		response.getWriter().write(u.toJSON());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/*
	 * 
	 * Format
	 * 		UserServlet?req=(reg/login/logout/profile/photos)
	 * 
	 * */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String req = (String) request.getParameter("req").split("&")[0];
		UserHandler us = new UserHandler();
		String redirect = "index.html";
		if(req == null){
			System.out.println("how did we even get a null request");
		}
		if(req.compareToIgnoreCase("reg") == 0){
			String user = (String) request.getParameter("user");
			String pass = (String) request.getParameter("pass");
			String desc = (String) request.getParameter("desc");
			boolean success = us.register(user, pass, desc);
			//checks if register was a success
			if(!success){
				request.setAttribute("errorMessage", "reg");
			}
		}else if(req.compareToIgnoreCase("login") == 0){
			//check if user is already logged in
			Cookie cook = getCookie("username", request);
			if(cook != null){
				redirect = "UserProfile.html";
				cook.setMaxAge(cook.getMaxAge() + 60 * 60 * 24 * 21);
			}else{
				String user = (String) request.getParameter("user");
				String pass = (String) request.getParameter("pass");
				
				//checkbox is null if unchecked, and not null if checked for some reason
				boolean neverForget = request.getParameter("remember")!= null;
				
				boolean success = us.login(user, pass);
				//if login fails
				if(!success){
					request.setAttribute("errorMessage", "login");
				}else{
					redirect = "UserProfile.html";
					Cookie cookie = new Cookie("username", user);
					if(neverForget){
						cookie.setMaxAge(60 * 60 * 24 * 21);
					}else{
						cookie.setMaxAge(-1);
					}
					
					response.addCookie(cookie);
				}
			}
		}else if(req.compareToIgnoreCase("logout") == 0){
			Cookie cookie = getCookie("username", request);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            
		}else if(req.compareToIgnoreCase("photos") == 0){
			Cookie cookie = getCookie("username", request);
			if(cookie == null){
				redirect = "index.html";
			}else
				redirect = "UserPage.html";
		}else if(req.compareToIgnoreCase("profile") == 0){
			Cookie cookie = getCookie("username", request);
			String id = (String) request.getParameter("userId");
			if(cookie == null){
				redirect = "index.html";
			}else{
				if(id == null)
					redirect = "UserProfile.html?id="+id;
				else redirect = "UserProfile.html";
			}
				
		}
		request.getRequestDispatcher(redirect).forward(request, response);
	}
	
	private Cookie getCookie(String cookieName, HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies!= null){
			for(int i = 0 ; i < cookies.length ; i++){
				if(cookies[i].getName().compareToIgnoreCase(cookieName) == 0){
					return cookies[i];
				}
			}
		}
		
		return null;
	}
}
