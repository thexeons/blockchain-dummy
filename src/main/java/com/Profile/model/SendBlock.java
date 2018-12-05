package com.Profile.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SendBlock {
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
	private String bcabank;
	private String bcainsurance;
	private String bcasyariah;
	private String bcafinancial;
	private String bcasekuritas;
	private String hash;
	private String previousHash;

	@JsonCreator
	public SendBlock(String id,String firstname, String lastname, String ktp, String email, String dob, String address, String nationality, String accountnum, String photo, String verified, String timestamp, String nonce, String bcabank, String bcainsurance, String bcasyariah, String bcafinancial, String bcasekuritas, String hash, String previoushash) {
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
		this.timeStamp = Long.parseLong(timestamp);
		this.nonce = Integer.parseInt(nonce);
		this.bcabank = bcabank;
		this.bcainsurance = bcainsurance;
		this.bcasyariah = bcasyariah;
		this.bcafinancial = bcafinancial;
		this.bcasekuritas = bcasekuritas;
		this.hash = hash;
		this.previousHash = previoushash;	
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


	public String getBcabank() {
		return bcabank;
	}


	public void setBcabank(String bcabank) {
		this.bcabank = bcabank;
	}


	public String getBcainsurance() {
		return bcainsurance;
	}


	public void setBcainsurance(String bcainsurance) {
		this.bcainsurance = bcainsurance;
	}


	public String getBcasyariah() {
		return bcasyariah;
	}


	public void setBcasyariah(String bcasyariah) {
		this.bcasyariah = bcasyariah;
	}


	public String getBcafinancial() {
		return bcafinancial;
	}


	public void setBcafinancial(String bcafinancial) {
		this.bcafinancial = bcafinancial;
	}


	public String getBcasekuritas() {
		return bcasekuritas;
	}


	public void setBcasekuritas(String bcasekuritas) {
		this.bcasekuritas = bcasekuritas;
	}


	@Override
	public String toString() {
		return "SendBlock [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", ktp=" + ktp
				+ ", email=" + email + ", dob=" + dob + ", address=" + address + ", nationality=" + nationality
				+ ", accountnum=" + accountnum + ", photo=" + photo + ", verified=" + verified + ", timeStamp="
				+ timeStamp + ", nonce=" + nonce + ", bcabank=" + bcabank + ", bcainsurance=" + bcainsurance
				+ ", bcasyariah=" + bcasyariah + ", bcafinancial=" + bcafinancial + ", bcasekuritas=" + bcasekuritas
				+ ", hash=" + hash + ", previousHash=" + previousHash + "]";
	}
	
}
