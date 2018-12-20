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
		try {
			db.openDB();
			rs = db.executeQuery("select * from msdata order by id desc limit 1");
			if(!rs.next()) {
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('1','Genesis','Genesis','Genesis','Genesis','Genesis','Genesis','Genesis','Genesis','Genesis','Genesis','1','78','Genesis','Genesis','Genesis','Genesis','Genesis')");                               
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('1','01a6af3d25bc58ec2472bf567701568c1ccea9beb70a3ce0779f88f42ffeba03','0')");

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
