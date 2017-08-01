package mp2.Servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import mp2.Control.ImageHandler;
import mp2.Model.Photo;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    //do get retrieves things from db;
    /*
     * Format:
     * 		ImageServlet?req=(tagname/userid/imageId/all/shareId)&val=___
     * 
     * 
     * */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//gets a single image
		String action = (String)request.getParameter("req");
		String val = (String)request.getParameter("val").split("&")[0];
		ImageHandler ih = new ImageHandler();
		Photo[] photos = null;
		System.out.println(val);
		if(action.compareToIgnoreCase("tagname") == 0){
			photos = ih.getPhotoByTag(val);
		}else if(action.compareToIgnoreCase("shareId") == 0){
			photos = ih.getPhotoByShare(val);
		}else if(action.compareToIgnoreCase("imageId") == 0){
			photos = new Photo[1];
			photos[0] = ih.getPhotoByPhotoId(val);
		}else if(action.compareToIgnoreCase("all") == 0){
			photos = ih.allPublic();
		}else if(action.compareToIgnoreCase("userid") == 0){
			photos = ih.getPhotoByUserId(val);
		}
		String encoded = "";
		if(photos == null){
			//encoded = "null";
		}else{
			for(int i = 0 ; i < photos.length ; i++){
				encoded += photos[i].toJSON();
				encoded += "&";
			}
		}
		response.setContentType("text/plain");
		response.getWriter().write(encoded);
	}
	/*
	private void getImage(HttpServletRequest request, HttpServletResponse response, Photo photo){
		try {
			InputStream in = photo.getImage().getBinaryStream();
			OutputStream out= response.getOutputStream();
			IOUtils.copy(in,out);
			response.setContentType("image/gif");
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getTitle(HttpServletRequest request, HttpServletResponse response, Photo photo){
		try {
			response.getWriter().write(photo.getTitle());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDesc(HttpServletRequest request, HttpServletResponse response, Photo photo){
		try {
			response.getWriter().write(photo.getDescription());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getTags(HttpServletRequest request, HttpServletResponse response, Photo photo){
		
	}
	
	private void getUploader(HttpServletRequest request, HttpServletResponse response, Photo photo){
		try {
			response.getWriter().write(photo.getUploader());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	//do post will add thigns to db
	/*
	 * Formats:
	 * add tag:
	 * 		'"/WEBAPDEMP2/ImageServlet?tag=____&imageId=____&req=tag'
	 * 
	 * upload photo:
	 * 		'ImageServlet?req=upload'
	 * 
	 * share photo:
	 * 		'ImageServlet?user=____&imageId=____&req=share'
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = (String) request.getParameter("req").split("&")[0];
		String dest = (String) request.getParameter("htmlLink");
		ImageHandler ih = new ImageHandler();
		//add tag
		
		if(action.compareToIgnoreCase("tag") == 0){
			String tagname = (String) request.getParameter("tag").split("&")[0];
			String imageId = (String) request.getParameter("imageId").split("&")[0];
			ih.addTag(tagname, imageId);
		}else if(action.compareToIgnoreCase("upload") == 0){//add photo
			String path = "C:\\Users\\amiel\\Desktop\\Internet Explorer\\eclipse projects\\WEBAPDEMP2\\images\\" + request.getParameter("file");
			String desc = request.getParameter("desc");
			String upload = request.getParameter("username");
			if(upload == null){
				Cookie cookie = getCookie("username", request);
				if(cookie!=null){
					upload = cookie.getValue();
				}
			}
			String title = request.getParameter("title");
			String[] tags = request.getParameter("tags").split(",");
 			boolean notPublic = request.getParameter("public") != null;
 			if(notPublic){
 				String[] users = request.getParameter("share").split(",");
 				ih.addPhoto(upload, desc, !notPublic, path, title, tags, users);
 			}else{
 				System.out.println(upload);
 				ih.addPhoto(upload, desc, !notPublic, path, title, tags);
 			}
 			
			
		} else if(action.compareToIgnoreCase("share") == 0){
			String photoId = (String) request.getParameter("imageId").split("&")[0];
			String username = (String) request.getParameter("user").split("&")[0];
			ih.sharePhoto(photoId, username);
		}
		request.getRequestDispatcher("UserProfile.html").forward(request, response);
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
