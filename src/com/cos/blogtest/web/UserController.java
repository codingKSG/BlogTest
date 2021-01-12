package com.cos.blogtest.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cos.blogtest.user.User;
import com.cos.blogtest.user.UserDao;
import com.cos.blogtest.user.dto.DeleteReqDto;
import com.cos.blogtest.user.dto.UserJoinDto;
import com.cos.blogtest.user.dto.UserLoginDto;
import com.cos.blogtest.util.Script;
import com.google.gson.Gson;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		UserDao userDao = new UserDao();

		if (cmd.equals("joinForm")) {
			RequestDispatcher dis = request.getRequestDispatcher("/user/join.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("loginForm")) {
			RequestDispatcher dis = request.getRequestDispatcher("/user/login.jsp");
			dis.forward(request, response);
		} else if (cmd.equals("userListForm")) {

			List<User> users = userDao.findAll();
			request.setAttribute("users", users);

			RequestDispatcher dis = request.getRequestDispatcher("/user/userList.jsp");
			dis.forward(request, response);

		} else if (cmd.equals("join")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");

			UserJoinDto userJoinDto = UserJoinDto.builder().username(username).password(password).email(email).build();
			int result = userDao.save(userJoinDto);

			if (result == 1) {
				response.sendRedirect("/blogTest/board/success.jsp");
			} else {
				Script.back(response, "회원가입이 정상적으로 처리되지 못했습니다. 다시 시도해 주세요.");
			}
		} else if (cmd.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			UserLoginDto userLoginDto = new UserLoginDto();
			userLoginDto.setUsername(username);
			userLoginDto.setPassword(password);

			User userEntity = userDao.findByUnAndPsw(userLoginDto);

			if (userEntity != null) {
				HttpSession session = request.getSession();
				session.setAttribute("principal", userEntity);
				response.sendRedirect("index.jsp");
			} else {
				Script.back(response, "아이디 또는 비밀번호가 잘못되었습니다. 확인 후 다시 시도해주십시오");
			}
		} else if (cmd.equals("delete")) {
			BufferedReader br = request.getReader();

			String data = br.readLine();
			System.out.println(data);

			Gson gson = new Gson();

			DeleteReqDto dto = gson.fromJson(data, DeleteReqDto.class);

			int result = userDao.deleteById(dto);

			
			if (dto.getRole().equals("admin")) {
				
			} else if (dto.getRole().equals("user")) {
				HttpSession session = request.getSession();
				session.invalidate();
			}
			
			PrintWriter out = response.getWriter();
			out.print(result);
			out.flush();

		} else if (cmd.equals("logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("index.jsp");
		}

	}
}
