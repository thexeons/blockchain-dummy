package com.Profile.model;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;

public class User {

	public static final String key = "TeddyGembelGante";
	public static final String initVector = "GembelTeddyGante";
	
	public String ktp;
	public String username;
	public String password;
	
	@JsonCreator
	public User(String ktp, String username, String password) {
		this.ktp = ktp;
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getKtp() {
		return ktp;
	}

	public void setKtp(String ktp) {
		this.ktp = ktp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public static String AESEncrypt(String password){
		try{	
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE,sKeySpec,iv);
			
			byte[] encrypted = cipher.doFinal(password.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "User [ktp=" + ktp + ", username=" + username + ", password=" + password + "]";
	}
	
	
}
