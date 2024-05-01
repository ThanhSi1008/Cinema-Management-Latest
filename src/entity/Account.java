package entity;

public class Account {
	private String accountID;
	private String username;
	private String password;
	private Employee emp;
	
	public Account(String accountID, String username, String password, Employee emp) {
		super();
		this.accountID = accountID;
		this.username = username;
		this.password = password;
		this.emp = emp;
	}

	public Account(String username, String password, Employee emp) {
		super();
		this.username = username;
		this.password = password;
		this.emp = emp;
	}

	public Account() {
		super();
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getAccountID() {
		return accountID;
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

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	@Override
	public String toString() {
		return "Account [accountID=" + accountID + ", username=" + username + ", password=" + password + ", emp=" + emp
				+ "]";
	}

}
