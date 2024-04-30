package controller;

import org.mindrot.jbcrypt.BCrypt;

import dao.AccountDAO;
import entity.Account;
import entity.Employee;

public class Ctrl_LoginForm {

	private AccountDAO accountDAO;

	public Ctrl_LoginForm() {
		accountDAO = new AccountDAO();
	}

	public boolean checkCredentials(String username, String password) {
		Account account = accountDAO.getAccountByUsername(username);
		if (account == null || !BCrypt.checkpw(password, account.getPassword())) {
			return false;
		}
		return true;
	}

	public Employee getEmployeeByAccount(String username, String password) {
		return accountDAO.getEmployeeByUsername(username, checkCredentials(username, password));
	}

}