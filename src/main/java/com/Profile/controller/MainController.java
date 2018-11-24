package com.Profile.controller;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
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
	

	ArrayList<Block> alBlock = new ArrayList<Block>();
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
	
	@PostMapping("/getUserDetail")
	public String getUserStatus(@RequestBody Block uBlock) {
		
		String bankStatus ="";
		String insuranceStatus ="";
		String financialStatus ="";
		String syariahStatus ="";
		String sekuritasStatus ="";
		try {
			db.openDB();
			
			db.executeQuery("select bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas from mstemp"); 
			while(rs.next()) {
				if(rs.getString(1)=="0") {bankStatus="not Registered";}else {bankStatus="Pending";}
				if(rs.getString(2)=="0") {insuranceStatus="not Registered";}else {insuranceStatus="Pending";}
				if(rs.getString(3)=="0") {syariahStatus="not Registered";}else {syariahStatus="Pending";}
				if(rs.getString(4)=="0") {financialStatus="not Registered";}else {financialStatus="Pending";}
				if(rs.getString(5)=="0") {sekuritasStatus="not Registered";}else {sekuritasStatus="Pending";}
			}
			
			db.executeQuery("select bcabank,bcainsurance,bcasyariah,bcafinancial,bcasekuritas from msdata order by id desc limit 1");
			while(rs.next()) {
				if(rs.getString(1)=="0") {bankStatus="not Registered";}
				else if(rs.getString(1)=="1"){bankStatus="Accepted";}
				else if(rs.getString(1)=="2"){bankStatus="Blacklist";}
				
				if(rs.getString(2)=="0") {insuranceStatus="not Registered";}
				else if(rs.getString(2)=="1") {insuranceStatus="Accepted";}
				else if(rs.getString(2)=="2"){insuranceStatus="Blacklist";}
				
				if(rs.getString(3)=="0") {syariahStatus="not Registered";}
				else if(rs.getString(3)=="1") {syariahStatus="Accepted";}
				else if(rs.getString(3)=="2"){syariahStatus="Blacklist";}
				
				if(rs.getString(4)=="0") {financialStatus="not Registered";}
				else if(rs.getString(4)=="1") {financialStatus="Accepted";}
				else if(rs.getString(4)=="2"){financialStatus="Blacklist";}
				
				if(rs.getString(5)=="0") {sekuritasStatus="not Registered";}
				else if(rs.getString(5)=="1") {sekuritasStatus="Accepted";}
				else if(rs.getString(5)=="2"){sekuritasStatus="Blacklist";}
				
			}
			
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("bcabank",bankStatus);
            postdata.put("bcasyariah",syariahStatus);
            postdata.put("bcasekuritas",sekuritasStatus);
            postdata.put("bcainsurance",insuranceStatus);
            postdata.put("bcafinancial",financialStatus);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        
        String requestJson = postdata.toString();
		return requestJson;
	}
	
	@PostMapping("/tempBlock")
	public Block tempBlock(@RequestBody Block xBlock) {
		try {
			db.openDB();
			Thread.sleep(100);
			db.executeUpdate("insert into mstemp(firstname,lastname,ktp,email,dob,address,nationality,accountnum,photo,verified,bcabank,bcainsurance,bcasyariah,bcafinancial	,bcasekuritas) values "
					+ "('"+xBlock.getFirstname()+"','"+xBlock.getLastname()+"','"+xBlock.getKtp()+"','"+xBlock.getEmail()+"','"+xBlock.getDob()+"','"+xBlock.getAddress()+"','"+xBlock.getNationality()+"','"+xBlock.getAccountnum()+"','"+xBlock.getPhoto()+"','"+xBlock.getVerified()+"','"+xBlock.getBcabank()+"','"+xBlock.getBcainsurance()+"','"+xBlock.getBcasyariah()+"','"+xBlock.getBcafinancial()+"','"+xBlock.getBcasekuritas()+"')");
			
			db.closeDB();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		
		return xBlock;
	}
	
	
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
		
		if(mBlock.getBcabank().equals("1")) {
			RestTemplate restTemplate = new RestTemplate();
	         String url = "http://192.168.43.171:8090/bankBlock";
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
		
		
		try {
			db.openDB();
			db.executeUpdate("delete from mstemp where ktp ='"+mBlock.getKtp()+"'");
			db.closeDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return mBlock;
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
	
	//Haha
	public java.util.List<Block> getBlock() {
//		try {
//			db.openDB();
//			rs= db.executeQuery("select firstname from msdata");
//			alBlock = new ArrayList<Block>();
//			while(rs.next()) {
//				if(alBlock.isEmpty()) {
//					alBlock.add(new Block(rs.getString(1),"0"));
//					alBlock.get(alBlock.size()-1).mineBlock(difficulty);
//					Thread.sleep(100);	
//				}
//				else {
//					alBlock.add(new Block(rs.getString(1),alBlock.get(alBlock.size()-1).getHash()));
//					alBlock.get(alBlock.size()-1).mineBlock(difficulty);
//					Thread.sleep(100);
//				}
//			}
//			db.closeDB();
//		} catch (Exception	 e) {
//			e.printStackTrace();
//		}
//		System.out.println(alBlock.get(0));
		
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
