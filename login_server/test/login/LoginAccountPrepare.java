package login;

import login.logic.LoginAccountManager;
import game.db.DataSourceManager;

public class LoginAccountPrepare {

	public static void main(String[] args) {
		DataSourceManager.getInstance();
		LoginAccountManager.getInstance().insert1KAccount();
	}

}
