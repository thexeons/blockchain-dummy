package com.Profile.controller;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.Profile.model.Block;
import com.Profile.model.SendBlock;
import com.Profile.model.User;
import com.Profile.model.Person;
import com.Profile.service.BlockService;
import com.Profile.service.ConnectDB;
import com.Profile.service.PersonService;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "*")
public class MainController {	
	
	ResultSet rs;
	ConnectDB db = new ConnectDB();
	RestTemplate rt = new RestTemplate();
	
	public static final String  master1 = "192.168.43.219";
	public static final String  master2 = "192.168.43.171";
	public static final String  master3 = "192.168.43.219";
	public static final String  master4 = "192.168.43.219";
	public static final String  master5 = "192.168.43.219";
	
	public static final String  unit1 = "192.168.43.219";
	public static final String  unit2 = "192.168.43.171";
	public static final String  unit3 = "192.168.43.219";
	public static final String  unit4 = "192.168.43.219";
	public static final String  unit5 = "192.168.43.219";
		
	ArrayList<Block> alBlock = new ArrayList<Block>();
	ArrayList<SendBlock> sendBlock = new ArrayList<SendBlock>();
	public static int difficulty = 2;
	
	@Autowired
	private PersonService mPersonService;
	private BlockService mBlockService;
	
	public MainController(PersonService mPersonService) {
		this.mPersonService = mPersonService;
		this.mBlockService = mBlockService;
	}
	

//	@GetMapping("/persons")
//	public java.util.List<Person> getAll() {
//		return mPersonService.getAll();
//	}
	
	@GetMapping("/blocks")
	public java.util.List<Block> getAll() {
		System.out.println("Controller: " + getBlock());
		return getBlock();
	}
	
	@PostMapping("/receiveMissingBlocks")
	public String receiveBlock(@RequestBody SendBlock mBlock) {
		try {
			db.openDB();
			db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
					+ "('"+mBlock.getId()+"','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");
			
			db.executeUpdate("insert into mshash(id,hash,previoushash) values('"+mBlock.getId()+"','"+mBlock.getHash()+"','"+mBlock.getPreviousHash()+"')");
			
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Haha";
	}
	
	@PostMapping("/requestMissingBlocks")
	public String requestBlock(@RequestBody Block uBlock){
		
		int getId = Integer.parseInt(uBlock.getId())+1;
		String sendId = getId+"";
		String currId = "";
		
		try {
			db.openDB();
			
			rs = db.executeQuery("select id from msdata order by id desc limit 1");
			while(rs.next()) {
				currId = rs.getString(1);
			}
			db.closeDB();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		sendBlock = new ArrayList<SendBlock>();
		for(int i = Integer.parseInt(sendId);i<=Integer.parseInt(currId);i++){
			SendBlock sb = null;
			try {
				db.openDB();
				rs=db.executeQuery("select "
						+ "msdata.id,"
						+ "msdata.firstname,"
						+ "msdata.lastname,"
						+ "msdata.ktp,"
						+ "msdata.email,"
						+ "msdata.dob,"
						+ "msdata.address,"
						+ "msdata.nationality,"
						+ "msdata.accountnum,"
						+ "msdata.photo,"
						+ "msdata.verified,"
						+ "msdata.timestamp,"
						+ "msdata.nonce,"
						+ "msdata.bcabank,"
						+ "msdata.bcainsurance,"
						+ "msdata.bcasyariah,"
						+ "msdata.bcafinancial,"
						+ "msdata.bcasekuritas,"
						+ "mshash.hash,"
						+ "mshash.previoushash"
						+ " from msdata join mshash on msdata.id = mshash.id where msdata.id ='"+i+"'");
				while(rs.next()) {
					sb = new SendBlock(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),rs.getString(20));
				}
				db.closeDB();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			RestTemplate restTemplate = new RestTemplate();
	        String url = "http://192.168.43.171:8095/receiveMissingBlocks";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        JSONObject postdata = new JSONObject();
	        try {

				System.out.println(sb.getId());
	        	postdata.put("id",sb.getId());
	            postdata.put("firstname",sb.getFirstname());
	            postdata.put("lastname",sb.getLastname());
	            postdata.put("ktp",sb.getKtp());
	            postdata.put("email",sb.getEmail());
	            postdata.put("dob",sb.getDob());
	            postdata.put("address",sb.getAddress());
	            postdata.put("nationality",sb.getNationality());
	            postdata.put("accountnum",sb.getAccountnum());
	            postdata.put("photo",sb.getPhoto());
	            postdata.put("verified",sb.getVerified());
	            postdata.put("timestamp",sb.getTimeStamp());
	            postdata.put("nonce",sb.getNonce());
	            postdata.put("bcabank",sb.getBcabank());
	            postdata.put("bcasyariah",sb.getBcasyariah());
	            postdata.put("bcafinancial",sb.getBcafinancial());
	            postdata.put("bcasekuritas",sb.getBcasekuritas());
	            postdata.put("bcainsurance",sb.getBcainsurance());
	            postdata.put("hash",sb.getHash());
	            postdata.put("previoushash",sb.getPreviousHash());
	        }
	        catch (JSONException e)
	        {
	            e.printStackTrace();
	        }
	        String requestJson = postdata.toString();
	        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	        String answer = restTemplate.postForObject(url, entity, String.class);
	        System.out.println(answer);
			
		}
		
		return "Haha";
	}
	
	@PostMapping("/getLatest")
	public String getLatest(@RequestBody SendBlock mBlock) {
		int thisMasterId = 0;
		try {
			db.openDB();
			rs = db.executeQuery("select id from msdata order by id desc limit 1");
			while(rs.next()) {
				thisMasterId = Integer.parseInt(rs.getString(1));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int getSyncId = Integer.parseInt(mBlock.getId());
		
		if(getSyncId-thisMasterId ==1) {
			try {
				db.openDB();
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('"+mBlock.getId()+"','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('"+mBlock.getId()+"','"+mBlock.getHash()+"','"+mBlock.getPreviousHash()+"')");
				
				db.closeDB();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		else {
			RestTemplate restTemplate = new RestTemplate();
	        String url = "http://localhost:8090/requestMissingBlocks";
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        JSONObject postdata = new JSONObject();
	        try {
	            postdata.put("id",thisMasterId);
	        }
	        catch (JSONException e)
	        {
	            e.printStackTrace();
	        }
	        String requestJson = postdata.toString();
	        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	        String answer = restTemplate.postForObject(url, entity, String.class);
	        System.out.println(answer);
			
		}
		
		return "x";
	}
	
	
	@GetMapping("/persons/{id}")
	public Optional<Person> getPerson(@PathVariable("id") Long personId) {	
		return mPersonService.getPerson(personId);
	}
	
	@PostMapping("/personz")
	public Person newPerson(@RequestBody Person mPerson) {
		//throw IllegalArgumentException
		Assert.hasLength(mPerson.getFirstName(), "First name field is needed");
		Assert.hasLength(mPerson.getLastName(), "Last name field is needed");
		
		//throw MethodArgumentNotValidException
		Assert.isTrue(mPerson.getFirstName().length()<20, "First name max 20 characters");
		Assert.isTrue(mPerson.getLastName().length()<20, "Last name max 20 characters");
		Assert.isTrue(mPerson.getAge()>0, "Minimum age is 0");
		Person person = mPersonService.newPerson(mPerson);
//		Assert.isTrue(person.getFirstName().equals("Maria"), "Test Mock Service");
		return person;
	}
	
	
//	@PostMapping("/newBlock")
//	public Block newBlock(@RequestBody Block mBlock) {
//		mBlock.getFirstname();
//		return mBlock;
//	}
	
	//Unit to master + notify
	@PostMapping("/newUpdateBlock")
	public Block newUpdateBlock(@RequestBody Block mBlock) {
		String currId = "";
		String prevHash ="";
		String currHash ="";

		String notifBank ="0";
		String notifFinancial ="0";
		String notifSekuritas ="0";
		String notifSyariah ="0";
		String notifInsurance ="0";
		
		try {
			db.openDB();
			rs = db.executeQuery("select msdata.id,previoushash,`hash` from msdata join mshash on msdata.id = mshash.id order by id desc limit 1");
			String thisId="";
			while(rs.next()) {
				currId = rs.getString(1);
				int nowId = Integer.parseInt(currId.toString())+1;
				thisId = nowId+"";
				currHash = rs.getString(3);
				System.out.println("This id Id :"+thisId);
				
			}
			alBlock.add(new Block(thisId,mBlock.getFirstname(),mBlock.getLastname(),mBlock.getKtp(),mBlock.getEmail(),mBlock.getDob(),mBlock.getAddress(),mBlock.getNationality(),mBlock.getAccountnum(),mBlock.getPhoto(),mBlock.getVerified(),currHash,mBlock.getBcabank(),mBlock.getBcainsurance(),mBlock.getBcasyariah(),mBlock.getBcafinancial(),mBlock.getBcasekuritas()));
			mBlock.setHash(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[0]);
			mBlock.setNonce(Integer.parseInt(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[1]));
			Thread.sleep(100);
			db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
					+ "('"+thisId+"','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");
			
			db.executeUpdate("insert into mshash(id,hash,previoushash) values('"+thisId+"','"+mBlock.getHash()+"','"+currHash+"')");
			

			//get current register where
			rs = db.executeQuery("select bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas from msdata where ktp ='"+mBlock.getKtp()+"'");
			while(rs.next()) {
				if(rs.getString(1).equals("1")) {notifBank="1";}
				if(rs.getString(2).equals("1")) {notifInsurance="1";}
				if(rs.getString(3).equals("1")) {notifSyariah="1";}
				if(rs.getString(4).equals("1")) {notifFinancial="1";}
				if(rs.getString(5).equals("1")) {notifSekuritas="1";}
			}
			
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//notify Bank
		if(notifBank.equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/updateNotification";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		
		//Notify Syariah
		if(notifSyariah.equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/updateNotification";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		
		//Notify Sekuritas
		if(notifSekuritas.equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/updateNotification";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		//Notify Financial
		if(notifFinancial.equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/updateNotification";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		
		//Notify Insurance
		if(notifInsurance.equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/updateNotification";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		//delete temp
		try {
			db.openDB();
			db.executeUpdate("delete from mstemp where ktp ='"+mBlock.getKtp()+"'");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		//Sync data to master 2 3 4 5
		SendBlock sdBx = null;
		try {
			db.openDB();
			rs=db.executeQuery("select "
					+ "msdata.id,"
					+ "msdata.firstname,"
					+ "msdata.lastname,"
					+ "msdata.ktp,"
					+ "msdata.email,"
					+ "msdata.dob,"
					+ "msdata.address,"
					+ "msdata.nationality,"
					+ "msdata.accountnum,"
					+ "msdata.photo,"
					+ "msdata.verified,"
					+ "msdata.timestamp,"
					+ "msdata.nonce,"
					+ "msdata.bcabank,"
					+ "msdata.bcainsurance,"
					+ "msdata.bcasyariah,"
					+ "msdata.bcafinancial,"
					+ "msdata.bcasekuritas,"
					+ "mshash.hash,"
					+ "mshash.previoushash"
					+ " from msdata join mshash on msdata.id = mshash.id where msdata.id ='"+currId+"'");
			while(rs.next()) {
				sdBx = new SendBlock(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),rs.getString(20));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8090/getLatest";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postdata = new JSONObject();
        try {
			postdata.put("id",sdBx.getId());
            postdata.put("firstname",sdBx.getFirstname());
            postdata.put("lastname",sdBx.getLastname());
            postdata.put("ktp",sdBx.getKtp());
            postdata.put("email",sdBx.getEmail());
            postdata.put("dob",sdBx.getDob());
            postdata.put("address",sdBx.getAddress());
            postdata.put("nationality",sdBx.getNationality());
            postdata.put("accountnum",sdBx.getAccountnum());
            postdata.put("photo",sdBx.getPhoto());
            postdata.put("verified",sdBx.getVerified());
            postdata.put("timestamp",sdBx.getTimeStamp());
            postdata.put("nonce",sdBx.getNonce());
            postdata.put("bcabank",sdBx.getBcabank());
            postdata.put("bcasyariah",sdBx.getBcasyariah());
            postdata.put("bcafinancial",sdBx.getBcafinancial());
            postdata.put("bcasekuritas",sdBx.getBcasekuritas());
            postdata.put("bcainsurance",sdBx.getBcainsurance());
            postdata.put("hash",sdBx.getHash());
            postdata.put("previoushash",sdBx.getPreviousHash());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String requestJson = postdata.toString();
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);
		
		
		
		return mBlock;
	}
	
	//new block to blockchain
	@PostMapping("/newBlock")
	public Block newBlock(@RequestBody Block mBlock) {
		System.out.println(mBlock.getFirstname());
		alBlock = new ArrayList<Block>();
		
		String currId = "";
		String prevHash ="";
		String currHash ="";
		
		try {
			db.openDB();
			rs = db.executeQuery("select * from msdata order by id desc limit 1");
			if(!rs.next()) {
				alBlock.add(new Block("1",mBlock.getFirstname(),mBlock.getLastname(),mBlock.getKtp(),mBlock.getEmail(),mBlock.getDob(),mBlock.getAddress(),mBlock.getNationality(),mBlock.getAccountnum(),mBlock.getPhoto(),mBlock.getVerified(),"0",mBlock.getBcabank(),mBlock.getBcainsurance(),mBlock.getBcasyariah(),mBlock.getBcafinancial(),mBlock.getBcasekuritas()));
				mBlock.setHash(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[0]);
				mBlock.setPreviousHash("0");
				mBlock.setNonce(Integer.parseInt(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[1]));
				
				Thread.sleep(100);
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('1','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");                               
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('1','"+mBlock.getHash()+"','"+mBlock.getPreviousHash()+"')");
			}
			else {
				rs = db.executeQuery("select msdata.id,previoushash,`hash` from msdata join mshash on msdata.id = mshash.id order by id desc limit 1");
				String thisId="";
				while(rs.next()) {
					currId = rs.getString(1);
					int nowId = Integer.parseInt(currId.toString())+1;
					thisId = nowId+"";
					currHash = rs.getString(3);
					System.out.println("This id Id :"+thisId);
					
				}
				alBlock.add(new Block(thisId,mBlock.getFirstname(),mBlock.getLastname(),mBlock.getKtp(),mBlock.getEmail(),mBlock.getDob(),mBlock.getAddress(),mBlock.getNationality(),mBlock.getAccountnum(),mBlock.getPhoto(),mBlock.getVerified(),currHash,mBlock.getBcabank(),mBlock.getBcainsurance(),mBlock.getBcasyariah(),mBlock.getBcafinancial(),mBlock.getBcasekuritas()));
				mBlock.setHash(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[0]);
				mBlock.setNonce(Integer.parseInt(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[1]));
				Thread.sleep(100);
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('"+thisId+"','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('"+thisId+"','"+mBlock.getHash()+"','"+currHash+"')");
				
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*
		if(mBlock.getBcabank().equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/bankBlock";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",mBlock.getFirstname());
	             postdata.put("lastname",mBlock.getLastname());
	             postdata.put("ktp",mBlock.getKtp());
	             postdata.put("email",mBlock.getEmail());
	             postdata.put("dob",mBlock.getDob());
	             postdata.put("address",mBlock.getAddress());
	             postdata.put("nationality",mBlock.getNationality());
	             postdata.put("accountnum",mBlock.getAccountnum());
	             postdata.put("photo",mBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		*/
		
		try {
			db.openDB();
			db.executeUpdate("delete from mstemp where ktp ='"+mBlock.getKtp()+"'");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Sync data to master 2 3 4 5
				SendBlock sdBx = null;
				try {
					db.openDB();
					rs=db.executeQuery("select "
							+ "msdata.id,"
							+ "msdata.firstname,"
							+ "msdata.lastname,"
							+ "msdata.ktp,"
							+ "msdata.email,"
							+ "msdata.dob,"
							+ "msdata.address,"
							+ "msdata.nationality,"
							+ "msdata.accountnum,"
							+ "msdata.photo,"
							+ "msdata.verified,"
							+ "msdata.timestamp,"
							+ "msdata.nonce,"
							+ "msdata.bcabank,"
							+ "msdata.bcainsurance,"
							+ "msdata.bcasyariah,"
							+ "msdata.bcafinancial,"
							+ "msdata.bcasekuritas,"
							+ "mshash.hash,"
							+ "mshash.previoushash"
							+ " from msdata join mshash on msdata.id = mshash.id where msdata.id ='"+currId+"'");
					while(rs.next()) {
						sdBx = new SendBlock(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16),rs.getString(17),rs.getString(18),rs.getString(19),rs.getString(20));
					}
					db.closeDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				RestTemplate restTemplate = new RestTemplate();
		        String url = "http://192.168.43.171:8095/getLatest";
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        JSONObject postdata = new JSONObject();
		        try {
					postdata.put("id",sdBx.getId());
		            postdata.put("firstname",sdBx.getFirstname());
		            postdata.put("lastname",sdBx.getLastname());
		            postdata.put("ktp",sdBx.getKtp());
		            postdata.put("email",sdBx.getEmail());
		            postdata.put("dob",sdBx.getDob());
		            postdata.put("address",sdBx.getAddress());
		            postdata.put("nationality",sdBx.getNationality());
		            postdata.put("accountnum",sdBx.getAccountnum());
		            postdata.put("photo",sdBx.getPhoto());
		            postdata.put("verified",sdBx.getVerified());
		            postdata.put("timestamp",sdBx.getTimeStamp());
		            postdata.put("nonce",sdBx.getNonce());
		            postdata.put("bcabank",sdBx.getBcabank());
		            postdata.put("bcasyariah",sdBx.getBcasyariah());
		            postdata.put("bcafinancial",sdBx.getBcafinancial());
		            postdata.put("bcasekuritas",sdBx.getBcasekuritas());
		            postdata.put("bcainsurance",sdBx.getBcainsurance());
		            postdata.put("hash",sdBx.getHash());
		            postdata.put("previoushash",sdBx.getPreviousHash());
		        }
		        catch (JSONException e)
		        {
		            e.printStackTrace();
		        }
		        String requestJson = postdata.toString();
		        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
		        String answer = restTemplate.postForObject(url, entity, String.class);
		        System.out.println(answer);
				
		
		return mBlock;
	}
		
	
	//Fetch user current data
	@PostMapping("/fetchData")
	public String fetchData(@RequestBody User xUser) {
		
		String ktp ="";
		
		String sendFirstName="";
		String sendLastName="";
		String sendAddress="";
		String sendDob="";
		String sendAccountNum="";
		String sendEmail="";
		String sendPhoto="";
		String sendNationality="";
		
		try {
			db.openDB();
			rs = db.executeQuery("select ktp from msuser where username ='"+xUser.getUsername()+"'");
			while(rs.next()) {
				ktp=rs.getString(1);
			}
			rs = db.executeQuery("select firstname,lastname,address,dob,accountnum,email,nationality,photo from msdata where ktp ='"+ktp+"' order by id desc limit 1");
			while(rs.next()) {
				sendFirstName = rs.getString(1);
				sendLastName = rs.getString(2);
				sendAddress = rs.getString(3);
				sendDob = rs.getString(4);
				sendAccountNum = rs.getString(5);
				sendEmail = rs.getString(6);
				sendNationality = rs.getString(7);
				sendPhoto= rs.getString(8);
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(sendFirstName);
		System.out.println(sendLastName);
		System.out.println(ktp);
		System.out.println(sendAddress);
		System.out.println(sendEmail);
		System.out.println(sendDob);
		System.out.println(sendAccountNum);
		System.out.println(sendNationality);
		System.out.println(sendPhoto);
		
		JSONObject postdata = new JSONObject();
        try {
            postdata.put("firstname",sendFirstName);
            postdata.put("lastname",sendLastName);
            postdata.put("ktp",ktp);
            postdata.put("address",sendAddress);
            postdata.put("email",sendEmail);
            postdata.put("dob",sendDob);
            postdata.put("accountnum",sendAccountNum);
            postdata.put("nationality",sendNationality);
            postdata.put("photo",sendPhoto);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        
        String requestJson = postdata.toString();
		return requestJson;
	}
	
	
	
	
	//send update block for verification
	@PostMapping("/updateBlock")
	public Block updateBlock(@RequestBody Block uBlock) {
		try {
			db.openDB();
			db.executeUpdate("insert into mstemp(firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas) values "
					+ "('"+uBlock.getFirstname()+"','"+uBlock.getLastname()+"','"+uBlock.getKtp()+"','"+uBlock.getEmail()+"','"+uBlock.getDob()+"','"+uBlock.getAddress()+"','"+uBlock.getNationality()+"','"+uBlock.getAccountnum()+"','"+uBlock.getPhoto()+"','"+uBlock.getVerified()+"','"+uBlock.getBcabank()+"','"+uBlock.getBcainsurance()+"','"+uBlock.getBcasyariah()+"','"+uBlock.getBcafinancial()+"','"+uBlock.getBcasekuritas()+"')");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(uBlock.getBcabank().equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/getUpdate";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         try {
	             postdata.put("firstname",uBlock.getFirstname());
	             postdata.put("lastname",uBlock.getLastname());
	             postdata.put("ktp",uBlock.getKtp());
	             postdata.put("email",uBlock.getEmail());
	             postdata.put("dob",uBlock.getDob());
	             postdata.put("address",uBlock.getAddress());
	             postdata.put("nationality",uBlock.getNationality());
	             postdata.put("accountnum",uBlock.getAccountnum());
	             postdata.put("photo",uBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		return uBlock;	
	}
	
	//Try Login
	@PostMapping("/verifyLogin")
	public String verifyLogin(@RequestBody User xUser) {
		 String tempUsername = xUser.getUsername();
		 String tempPassword = xUser.getPassword();
		 ArrayList<String> alTryLogin = new ArrayList<String>();
		 
		 System.out.println(tempUsername);
		 System.out.println(tempPassword);
		 
		 try {
			db.openDB();
			rs = db.executeQuery("select * from msuser where username like '"+tempUsername+"' and password like '"+tempPassword+"'");
			while(rs.next()) {
				alTryLogin.add(rs.getString(1));
				alTryLogin.add(rs.getString(2));
				alTryLogin.add(rs.getString(3));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 if(alTryLogin.size()==0) {
			 return "0";
		 }
		 else {
			 System.out.println(tempUsername+" has logged in!");
			 return "1";
		 } 
		 
		 
	}
	
	//Try Register
	@PostMapping("/verifyRegister")
	public String verifyRegister(@RequestBody User xUser) {
		String tempUsername = xUser.getUsername();
		String tempPassword = xUser.getPassword();
		String tempKtp = xUser.getKtp();
		String warning = "-1";
		
		String flagUser = "-1";
		String flagKtp = "-1";
		
		System.out.println(xUser.getPassword());
		System.out.println(xUser.getUsername());
		System.out.println(xUser.getKtp());
		
		ArrayList<String> alTryRegister = new ArrayList<String>();
		//1 = ktp exist
		//2 = username exist
		//3 = both exist
		//4 = sukses
		//Check KTP
		try {
			db.openDB();
			rs = db.executeQuery("select * from msuser where ktp ='"+tempKtp+"'");
			while(rs.next()) {
				alTryRegister.add(rs.getString(1));
				alTryRegister.add(rs.getString(2));
				alTryRegister.add(rs.getString(3));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(alTryRegister.size()!=0) {
			flagKtp	="1";
		}

		alTryRegister = new ArrayList<String>();
		//Check KTP
		try {
			db.openDB();
			rs = db.executeQuery("select * from msuser where username ='"+tempUsername+"'");
			while(rs.next()) {
				alTryRegister.add(rs.getString(1));
				alTryRegister.add(rs.getString(2));
				alTryRegister.add(rs.getString(3));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(alTryRegister.size()!=0) {
			flagUser="1";
		}
		
		
		if(flagKtp.equals("1")) {
			warning ="1";
		}
		if(flagUser.equals("1")) {
			warning ="2";
		}
		if(flagUser.equals("1") && flagKtp.equals("1")) {
			warning ="3";
		}
		if(flagUser.equals("-1") && flagKtp.equals("-1")) {
			warning ="4";
		}

		return warning;
	}
	
	@PostMapping("/submitUser")
	public String submitUser(@RequestBody User xUser) {
		try {
			db.openDB();
			db.executeUpdate("insert into msuser (ktp,username,password) values ('"+xUser.getKtp()+"','"+xUser.getUsername()+"','"+xUser.getPassword()+"')");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(xUser.getUsername()+" has registered.");
		return "x";
	}
	
	
	@PostMapping("/masterRejectBlock")
	public String returnRejectResponse(@RequestBody Block mBlock) {
		try {
			db.openDB();
			db.executeUpdate("delete from mstemp where ktp ='"+mBlock.getKtp()+"'");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Done";
	}
	
	//Get user detail about register , bl / pending
	@PostMapping("/getUserDetail")
	public String getUserStatus(@RequestBody Block uBlock) {
		
		String bankStatus ="";
		String insuranceStatus ="";
		String financialStatus ="";
		String syariahStatus ="";
		String sekuritasStatus ="";
		System.out.println(uBlock.getKtp());
		try {
			db.openDB();
			
			rs=db.executeQuery("select bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas from mstemp where ktp ='"+uBlock.getKtp()+"'"); 
			while(rs.next()) {
				if(rs.getString(1).equals("0")) {bankStatus="not Registered";}else {bankStatus="Pending";}
				if(rs.getString(2).equals("0")) {insuranceStatus="not Registered";}else {insuranceStatus="Pending";}
				if(rs.getString(3).equals("0")) {syariahStatus="not Registered";}else {syariahStatus="Pending";}
				if(rs.getString(4).equals("0")) {financialStatus="not Registered";}else {financialStatus="Pending";}
				if(rs.getString(5).equals("0")) {sekuritasStatus="not Registered";}else {sekuritasStatus="Pending";}
			}
			
			rs=db.executeQuery("select bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas from msdata where ktp='"+uBlock.getKtp()+"'");
			while(rs.next()) {
				if(rs.getString(1).equals("0")) {bankStatus="not Registered";}
				else if(rs.getString(1).equals("1")){bankStatus="Accepted";}
				else if(rs.getString(1).equals("2")){bankStatus="Blacklist";}
				
				if(rs.getString(2).equals("0")) {insuranceStatus="not Registered";}
				else if(rs.getString(2).equals("1")) {insuranceStatus="Accepted";}
				else if(rs.getString(2).equals("2")){insuranceStatus="Blacklist";}
				
				if(rs.getString(3).equals("0")) {syariahStatus="not Registered";}
				else if(rs.getString(3).equals("1")) {syariahStatus="Accepted";}
				else if(rs.getString(3).equals("2")){syariahStatus="Blacklist";}
				
				if(rs.getString(4).equals("0")) {financialStatus="not Registered";}
				else if(rs.getString(4).equals("1")) {financialStatus="Accepted";}
				else if(rs.getString(4).equals("2")){financialStatus="Blacklist";}
				
				if(rs.getString(5).equals("0")) {sekuritasStatus="not Registered";}
				else if(rs.getString(5).equals("1")) {sekuritasStatus="Accepted";}
				else if(rs.getString(5).equals("2")){sekuritasStatus="Blacklist";}
				
			}
			
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(bankStatus);
		System.out.println(syariahStatus);
		System.out.println(sekuritasStatus);
		System.out.println(insuranceStatus);
		System.out.println(financialStatus);
		
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("bankStatus",bankStatus);
            postdata.put("syariahStatus",syariahStatus);
            postdata.put("sekuritasStatus",sekuritasStatus);
            postdata.put("insuranceStatus",insuranceStatus);
            postdata.put("financeStatus",financialStatus);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        
        String requestJson = postdata.toString();
		return requestJson;
	}
	
	//first data input set to temp
	@PostMapping("/tempBlock")
	public Block tempBlock(@RequestBody Block xBlock) {
		try {
			db.openDB();
			Thread.sleep(100);
			db.executeUpdate("insert into mstemp(firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas) values "
					+ "('"+xBlock.getFirstname()+"','"+xBlock.getLastname()+"','"+xBlock.getKtp()+"','"+xBlock.getEmail()+"','"+xBlock.getDob()+"','"+xBlock.getAddress()+"','"+xBlock.getNationality()+"','"+xBlock.getAccountnum()+"','"+xBlock.getPhoto()+"','"+xBlock.getVerified()+"','"+xBlock.getBcabank()+"','"+xBlock.getBcainsurance()+"','"+xBlock.getBcasyariah()+"','"+xBlock.getBcafinancial()+"','"+xBlock.getBcasekuritas()+"')");
			
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(xBlock.getBcabank().equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://localhost:8090/bankBlock";
	         HttpHeaders headers = new HttpHeaders();
	         headers.setContentType(MediaType.APPLICATION_JSON);
	         JSONObject postdata = new JSONObject();
	         //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
	         try {
	             postdata.put("firstname",xBlock.getFirstname());
	             postdata.put("lastname",xBlock.getLastname());
	             postdata.put("ktp",xBlock.getKtp());
	             postdata.put("email",xBlock.getEmail());
	             postdata.put("dob",xBlock.getDob());
	             postdata.put("address",xBlock.getAddress());
	             postdata.put("nationality",xBlock.getNationality());
	             postdata.put("accountnum",xBlock.getAccountnum());
	             postdata.put("photo",xBlock.getPhoto());
	         }
	         catch (JSONException e)
	         {
	             e.printStackTrace();
	         }
	         String requestJson = postdata.toString();
	         HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
	         String answer = restTemplate.postForObject(url, entity, String.class);
	         System.out.println(answer);
		}
		
		System.out.println(xBlock.getFirstname()+" "+xBlock.getLastname()+" "+" has registered.");
		
		return xBlock;
	}
	
	
	
	
	
	/*
	@PostMapping("/verifyBlock")
	public String verifyBlock(@RequestBody Block mBlock){

		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		int counterTrue = 0;
		
		String needVerify = mBlock.getFirstname()+mBlock.getLastname()+mBlock.getKtp()+mBlock.getEmail()+mBlock.getDob()+mBlock.getAddress()+mBlock.getNationality()+mBlock.getAccountnum()+mBlock.getPhoto()+mBlock.getVerified()+mBlock.getBcabank()+mBlock.getBcainsurance()+mBlock.getBcafinancial()+mBlock.getBcasyariah()+mBlock.getBcasekuritas();
		
		RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.43.171:8090/returnResponse";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postdata = new JSONObject();
        //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
        
        //Converting String to Private Key
        
        try {
	        byte[] aPrivate = Base64.getDecoder().decode(base64privateKey1.getBytes("UTF-8"));			
			PKCS8EncodedKeySpec keySpecx = new PKCS8EncodedKeySpec(aPrivate);
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA" , "BC");
			privatekey = keyFactory.generatePrivate(keySpecx);
        }
        catch(Exception e) {
        	throw new RuntimeException(e);
        }
		//Converting signature Byte to String
		byte[] byteSig = BlockService.applyECDSASig(privatekey, needVerify);
		String encoded = Base64.getEncoder().encodeToString(byteSig);
		
		try {
            postdata.put("signature",encoded);
            postdata.put("data",needVerify);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String requestJson = postdata.toString();
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        
        if(answer.equals("True")){
        	counterTrue++;
        }
        
        //if >3
        if(counterTrue==1) {
    		String currId = "";
    		String prevHash ="";
    		String currHash ="";
        	try {
				db.openDB();
				rs = db.executeQuery("select msdata.id,previoushash,`hash` from msdata join mshash on msdata.id = mshash.id order by id desc limit 1");
				String thisId="";
				while(rs.next()) {
					currId = rs.getString(1);
					int nowId = Integer.parseInt(currId.toString())+1;
					thisId = nowId+"";
					currHash = rs.getString(3);
					System.out.println("This id Id :"+thisId);
					
				}
				alBlock.add(new Block(thisId,mBlock.getFirstname(),mBlock.getLastname(),mBlock.getKtp(),mBlock.getEmail(),mBlock.getDob(),mBlock.getAddress(),mBlock.getNationality(),mBlock.getAccountnum(),mBlock.getPhoto(),mBlock.getVerified(),currHash,mBlock.getBcabank(),mBlock.getBcainsurance(),mBlock.getBcasyariah(),mBlock.getBcafinancial(),mBlock.getBcasekuritas()));
				mBlock.setHash(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[0]);
				mBlock.setNonce(Integer.parseInt(alBlock.get(alBlock.size()-1).mineBlock(difficulty)[1]));
				Thread.sleep(100);
				db.executeUpdate("insert into msdata(id,firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,timestamp,nonce,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
						+ "('"+thisId+"','"+mBlock.getFirstname()+"','"+mBlock.getLastname()+"','"+mBlock.getKtp()+"','"+mBlock.getEmail()+"','"+mBlock.getDob()+"','"+mBlock.getAddress()+"','"+mBlock.getNationality()+"','"+mBlock.getAccountnum()+"','"+mBlock.getPhoto()+"','"+mBlock.getVerified()+"','"+mBlock.getTimeStamp()+"','"+mBlock.getNonce()+"','"+mBlock.getBcabank()+"','"+mBlock.getBcainsurance()+"','"+mBlock.getBcasyariah()+"','"+mBlock.getBcafinancial()+"','"+mBlock.getBcasekuritas()+"')");
				
				db.executeUpdate("insert into mshash(id,hash,previoushash) values('"+thisId+"','"+mBlock.getHash()+"','"+currHash+"')");
				
				db.closeDB();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.out.println(counterTrue);
        	return "True";
        }
        
        //if<3
        else{
            System.out.println(counterTrue);
        	return "False";
        }
	}
	*/
	@PostMapping("/test")
	public Block test(@RequestBody Block mBlock) {
		System.out.println(mBlock.getFirstname());
		RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.43.171:8090/getTest";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postdata = new JSONObject();
        //Block blok = new Block(mBlock.getFirstname(),mBlock.getLastname(),mBlock.getDob(), mBlock.getAddress(), mBlock.getEmail(), mBlock.getKtp(), mBlock.getNationality(), mBlock.getPhoto(), mBlock.getAccountnum());
        try {
            postdata.put("firstname",mBlock.getFirstname());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String requestJson = postdata.toString();
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);		
        return mBlock;
	}
	
	@DeleteMapping("/persons/{id}")
	public void deletePerson(@PathVariable Long id) {
		mPersonService.deletePerson(id);
	}
	
	@PutMapping("/persons/{id}")
	public Person replacePerson(@RequestBody Person newPerson, @PathVariable Long id) {
		//throw IllegalArgumentException
		Assert.hasLength(newPerson.getFirstName(), "First name field is needed");
		Assert.hasLength(newPerson.getLastName(), "Last name field is needed");
		
		//throw MethodArgumentNotValidException
		Assert.isTrue(newPerson.getFirstName().length()<20, "First name max 20 characters");
		Assert.isTrue(newPerson.getLastName().length()<20, "Last name max 20 characters");
		Assert.isTrue(newPerson.getAge()>0, "Minimum age is 0");		
		
		newPerson.setPersonId(id);
		return mPersonService.replacePerson(newPerson, id);
	}
	
	//View Block
	public java.util.List<Block> getBlock() {
		alBlock = new ArrayList<Block>();
		try {
			db.openDB();
			rs=db.executeQuery("select * from msdata join mshash on msdata.id = mshash.id");
			while(rs.next()) {
				alBlock.add(new Block(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(21),rs.getString(20), Integer.parseInt(rs.getString(13)),rs.getString(14),rs.getString(15),rs.getString(16), rs.getString(17),rs.getString(18)));
			}
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alBlock;
	}
}
