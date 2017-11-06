package net.gh.ghoshMyRmc.model;

import net.gh.ghoshMyRmcBackend.dto.Account;

public class AccountTransferModel {

	private Account accountTo;
	private Account accountFrom;

	public Account getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(Account accountFrom) {
		this.accountFrom = accountFrom;
	}

	public Account getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(Account accountTo) {
		this.accountTo = accountTo;
	}

}
