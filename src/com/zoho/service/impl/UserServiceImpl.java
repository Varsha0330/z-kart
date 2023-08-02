package com.zoho.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zoho.beans.UserBean;
import com.zoho.constants.IUserConstants;
import com.zoho.service.UserService;
import com.zoho.utility.DBUtil;
import com.zoho.utility.MailMessage;

public class UserServiceImpl implements UserService {
	
	
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz"; 

	@Override
	public String registerUser(String userName, Long mobileNo, int credit, String emailId, 
			String password) {
		
	
		UserBean user=new UserBean(userName, mobileNo, credit, emailId, password);
		String status = registerUser(user);
		return status;
	}
	
	public static String Encryt(String password)
	{
		 String pass = password.toLowerCase();   
	     String encryptStr = "";   
	     for (int i = 0; i < pass.length(); i++)   
	     {
	    	 int pos = ALPHABET.indexOf(pass.charAt(i));    
	         int encryptPos = (3 + pos) % 26;   
	         char encryptChar = ALPHABET.charAt(encryptPos);   
	         encryptStr += encryptChar;   
	        }    
	     return encryptStr;  
	}

	public static String Decrypt(String password)
	{
		String pass = password.toLowerCase();      
        String decryptStr = "";    
        for (int i = 0; i < pass.length(); i++)   
        {     
            int pos = ALPHABET.indexOf(pass.charAt(i));   
            int decryptPos = (pos - 3) % 26;     
            if (decryptPos < 0){   
                decryptPos = ALPHABET.length() + decryptPos;   
            }   
            char decryptChar = ALPHABET.charAt(decryptPos);   
            decryptStr += decryptChar;   
        }   
     return decryptStr; 
}

	@Override
	public String registerUser(UserBean user) {

		String status = "User Registration Failed!";

		boolean isRegtd = isRegistered(user.getEmail());

		if (isRegtd) {
			status = "Email Id Already Registered!";
			return status;
		}
		Connection conn = DBUtil.provideConnection();
		PreparedStatement ps = null;
		if (conn != null) {
			System.out.println("Connected Successfully!");
		}

		try {

			ps = conn.prepareStatement("insert into " + IUserConstants.TABLE_USER + " values(?,?,?,?,?)");
			
			String a=Encryt(user.getPassword());

			ps.setString(1, user.getEmail());
			ps.setString(2, user.getName());
			ps.setLong(3, user.getMobile());
			ps.setInt(4, user.getCredit());
			ps.setString(5, a);

			int k = ps.executeUpdate();

			if (k > 0) {
				status = "User Registered Successfully!";
				MailMessage.registrationSuccess(user.getEmail(), user.getName().split(" ")[0]);
			}

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(ps);

		return status;
	}

	@Override
	public boolean isRegistered(String emailId) {
		boolean flag = false;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from user where email=?");

			ps.setString(1, emailId);

			rs = ps.executeQuery();

			if (rs.next())
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return flag;
	}

	@Override
	public String isValidCredential(String emailId, String password) {
		String status = "Login Denied! Incorrect Username or Password";

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			
			String p=Encryt(password);

			ps = con.prepareStatement("select * from user where email=? and password=?");

			ps.setString(1, emailId);
			ps.setString(2, p);
			
			
			rs = ps.executeQuery();

			if (rs.next())
				status = "valid";

		} catch (SQLException e) {
			status = "Error: " + e.getMessage();
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);
		return status;
	}

	@Override
	public UserBean getUserDetails(String emailId, String password) {

		UserBean user = null;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select * from user where email=? and password=?");
			String p=Encryt(password);
			ps.setString(1, emailId);
			ps.setString(2, p);
			rs = ps.executeQuery();

			if (rs.next()) {
				String d=Decrypt(rs.getString("password"));
				user = new UserBean();
				user.setName(rs.getString("name"));
				user.setMobile(rs.getLong("mobile"));
				user.setEmail(rs.getString("email"));
				user.setCredit(rs.getInt("pincode"));
				user.setPassword(d);

				return user;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		DBUtil.closeConnection(con);
		DBUtil.closeConnection(ps);
		DBUtil.closeConnection(rs);

		return user;
	}

	@Override
	public String getFName(String emailId) {
		String fname = "";

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select name from user where email=?");
			ps.setString(1, emailId);

			rs = ps.executeQuery();

			if (rs.next()) {
				fname = rs.getString(1);

				fname = fname.split(" ")[0];

			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return fname;
	}

	/**@Override
	public String getUserAddr(String userId) {
		String userAddr = "";

		Connection con = DBUtil.provideConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("select address from user where email=?");

			ps.setString(1, userId);

			rs = ps.executeQuery();

			if (rs.next())
				userAddr = rs.getString(1);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return userAddr;
	}**/

}
