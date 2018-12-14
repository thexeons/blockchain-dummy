package com.bca.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bca.master.model.Block;
import com.bca.master.service.ConnectDB;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class MasterApplication {
	public static int difficulty = 1;
	ConnectDB db = new ConnectDB();
	ResultSet rs;
	
	public static long timestampglobal = 0;

	public MasterApplication() {
		ArrayList<Block> alBlock = new ArrayList<Block>();
		Block mBlock = new Block("1", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "0", "0", "Genesis", "Genesis", "Genesis", "Genesis", "Genesis", "null", "null");
		try {
			db.openDB();
			rs = db.executeQuery("select * from msdata order by id desc limit 1");
			if(!rs.next()) {
				alBlock.add(new Block(mBlock.getId(),mBlock.getFirstname(),mBlock.getLastname(),mBlock.getKtp(),mBlock.getEmail(),mBlock.getDob(),mBlock.getAddress(),mBlock.getNationality(),mBlock.getAccountnum(),mBlock.getPhoto(),mBlock.getVerified(),"0",mBlock.getBcabank(),mBlock.getBcainsurance(),mBlock.getBcasyariah(),mBlock.getBcafinancial(),mBlock.getBcasekuritas()));
				mBlock.setHash(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[0]);
				mBlock.setPreviousHash("0");
				mBlock.setNonce(Integer.parseInt(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[1]));
				
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('1','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+Block.timestampglobal+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");                               
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('1','"+mBlock.getHash()+"','"+mBlock.getPreviousHash()+"')");

				System.out.println("Genesis Created");
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		new MasterApplication();
		
		SpringApplication.run(MasterApplication.class, args);
	}
}
