package com.Profile.service;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.Profile.model.Block;
import com.Profile.model.Person;
import com.Profile.controller.MainController;;

public class BlockService {
	ResultSet rs;
	ConnectDB db = new ConnectDB();

	ArrayList<Block> alBlock = new ArrayList<Block>();
	
//	public java.util.List<Block> getAll() {
//		try {
//			db.openDB();
//			rs= db.executeQuery("select firstname from msdata");
//			
//			while(rs.next()) {
//				alBlock.add(new Block(rs.getString(1),"0"));
//			}
//			db.closeDB();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println(alBlock.get(0));
//		
//		return alBlock;
//	}
	
}
