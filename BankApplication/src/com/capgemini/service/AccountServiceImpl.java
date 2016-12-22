package com.capgemini.service;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialBalanceException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;

public class AccountServiceImpl implements AccountService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.capgemini.service.AccountService#createAccount(int, int)
	 */

	AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Account createAccount(int accountNumber, int amount) throws InsufficientInitialBalanceException {
		if (amount < 500) {
			throw new InsufficientInitialBalanceException();
		}

		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setAmount(amount);

		if (accountRepository.save(account)) {
			return account;
		}

		return null;

	}

	@Override
	public int showBalance(int accountNumber) throws InvalidAccountNumberException {

		Account account = accountRepository.searchAccount(accountNumber);
		if (account == null) {
			throw new InvalidAccountNumberException();
		} else {
			return account.getAmount();
		}
	}

	@Override
	public int withDrawAmount(int accountNumber, int amount)
			throws InsufficientBalanceException, InvalidAccountNumberException {
		Account account = accountRepository.searchAccount(accountNumber);
		if (account == null) {
			throw new InvalidAccountNumberException();
		} else if (account.getAmount() < amount) {
			throw new InsufficientBalanceException();
		} else {
			return account.getAmount() - amount;
		}

	}

}
