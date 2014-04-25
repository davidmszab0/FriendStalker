package com.example.maffia;


public class Account {

	private boolean activated;
	private String userName;
	private String email;
	private String password;
	private String activationCode;

	public Account(String userName, String email, String password,
			String activationCode) {
		activated = false;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.activationCode = activationCode;
	}

	protected String userName() {
		return this.userName;
	}

	protected String email() {
		return this.email;
	}

	protected String password() {
		return this.password;
	}

	protected String activationCode() {
		return this.activationCode;
	}

	protected void activate(String activationCode) {
		if (this.activationCode == activationCode) {
			activated = true;
		}
	}
}
