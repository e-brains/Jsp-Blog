package com.kye.blog.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DB {
	
	public static Connection getConnection() {
		
		try {
			
			Context initContext = new  InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			DataSource ds = (DataSource)envContext.lookup("jdbc/TestDB"); // context에 등록된 DB Resource Name
			Connection conn = ds.getConnection();			
			return conn;  // conn을 받아서 리턴한다
			
		} catch (Exception e) {
			
			System.out.println("DB 연결 실패 ");
			e.printStackTrace();
			
		}		
		
		return null;
		
	}

	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			conn.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			conn.close();
			pstmt.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
