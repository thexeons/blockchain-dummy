package com.Profile.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Block {
	public String hash;
	public String previousHash;
	private String id;
	private String firstname;
	private String lastname;
	private String ktp;
	private String email;
	private String dob;
	private String address;
	private String nationality;
	private String accountnum;
	private String photo;
	private String verified;
	private long timeStamp;
	private int nonce;
	
	//Constructor.

	@JsonCreator
	public Block(String id,String firstname, String lastname, String ktp, String email, String dob, String address, String nationality, String accountnum,String photo,String verified, String previousHash){
		
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.ktp = ktp;
		this.email = email;
		this.dob = dob;
		this.address = address;
		this.nationality = nationality;
		this.accountnum = accountnum;
		this.photo = photo;
		this.verified = verified;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public Block(String id,String firstname, String lastname, String ktp, String email, String dob, String address, String nationality, String accountnum,String photo,String verified, String previousHash,String hash,int nonce){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.ktp = ktp;
		this.email = email;
		this.dob = dob;
		this.address = address;
		this.nationality = nationality;
		this.accountnum = accountnum;
		this.photo = photo;
		this.verified = verified;
		this.previousHash = previousHash;
		this.timeStamp= new Date().getTime();
		this.hash = hash;
		this.nonce=nonce;
	}
	
	public Block(String firstname) {
		this.firstname = firstname;
	}
	
	public String calculateHash(){
		//String calculatedhash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + firstname+lastname+ktp+email+dob+address+nationality+accountnum);
		String calculatedhash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + firstname+lastname+ktp+email+dob+address+nationality+accountnum);
		
		return calculatedhash;
	}
	
	public String[] mineBlock(int difficulty){
		String target = new String(new char[difficulty]).replace('\0' , '0');
		while(!hash.substring(0 , difficulty).equals(target)){
			nonce++;
			hash = calculateHash();
		}
		String[] arrayx = {"",""};
		arrayx[0]=hash+"";
		arrayx[1]=nonce+"";
		System.out.println("Successfully mined block " + hash);
		return arrayx;
	}

	
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getNonce() {
		return nonce;
	}

	public void setNonce(int nonce) {
		this.nonce = nonce;
	}	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getKtp() {
		return ktp;
	}

	public void setKtp(String ktp) {
		this.ktp = ktp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	@Override
	public String toString() {
		return "Block [hash=" + hash + ", previousHash=" + previousHash + ", id=" + id + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", ktp=" + ktp + ", email=" + email + ", dob=" + dob + ", address="
				+ address + ", nationality=" + nationality + ", accountnum=" + accountnum + ", photo=" + photo
				+ ", verified=" + verified + ", timeStamp=" + timeStamp + ", nonce=" + nonce + "]";
	}	
}
