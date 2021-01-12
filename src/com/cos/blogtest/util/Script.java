package com.cos.blogtest.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Script {
	public static void back(HttpServletResponse response, String msg) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("<script>");
			out.print("alert('"+msg+"');");
			out.print("history.back();");
			out.print("</script>");
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
