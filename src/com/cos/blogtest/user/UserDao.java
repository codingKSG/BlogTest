package com.cos.blogtest.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cos.blogtest.config.DB;
import com.cos.blogtest.user.dto.DeleteReqDto;
import com.cos.blogtest.user.dto.UserJoinDto;
import com.cos.blogtest.user.dto.UserLoginDto;

public class UserDao {
	
	public int save(UserJoinDto dto) {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO user(username, password, email, role) ");
		sb.append("VALUE (?, ? ,? , \"user\")");
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getEmail());
			
			int result = pstmt.executeUpdate();
			
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt);
		}
		return -1;
	}
	
	public User findByUnAndPsw(UserLoginDto dto) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, username, password, email, role ");
		sb.append("FROM user WHERE username = ? AND password = ?");
		
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getUsername());
			pstmt.setString(2, dto.getPassword());
			
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.role(rs.getString("role"))
						.build();
				
				return user;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public List<User> findAll(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, username, password, email, role ");
		sb.append("FROM user");
		
		String sql = sb.toString();
		
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<User> users = new ArrayList<>();
		User user;
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.role(rs.getString("role"))
						.build();
				
				users.add(user);
			}
			
			return users;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt, rs);
		}
		return null;
	}
	
	public int deleteById(DeleteReqDto dto) {
		
		String sql = "DELETE FROM user WHERE id = ?";
		Connection conn = DB.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getId());
			
			int result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(conn, pstmt);
		}
		return -1;
	}
}
