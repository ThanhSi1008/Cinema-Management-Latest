package controller;

import org.mindrot.jbcrypt.BCrypt;

import dao.AccountDAO;
import entity.Account;

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

	public String getRole(String username) {
		return accountDAO.getRoleByUsername(username);
	}

}