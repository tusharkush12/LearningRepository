package com.capgemini.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.exceptions.InsufficientBalanceException;
import com.capgemini.exceptions.InsufficientInitialBalanceException;
import com.capgemini.exceptions.InvalidAccountNumberException;
import com.capgemini.model.Account;
import com.capgemini.repository.AccountRepository;
import com.capgemini.service.AccountService;
import com.capgemini.service.AccountServiceImpl;

public class AccountTest {

	@Mock
	AccountRepository accountRepository;

	AccountService accountService;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		accountService = new AccountServiceImpl(accountRepository);
	}

	/*
	 * create account 1. when the amount is less than 500 system should throw
	 * exception 2. when the valid info is passed account should be created
	 * successfully
	 */

	@Test(expected = com.capgemini.exceptions.InsufficientInitialBalanceException.class)
	public void whenTheAmountIsLessThan500SystemShouldThrowException() throws InsufficientInitialBalanceException {
		accountService.createAccount(101, 400);
	}

	@Test
	public void whenTheValidInfoIsPassedAccountShouldBeCreatedSuccessfully()
			throws InsufficientInitialBalanceException {
		Account account = new Account();

		account.setAccountNumber(101);
		account.setAmount(5000);

		when(accountRepository.save(account)).thenReturn(true);

		assertEquals(account, accountService.createAccount(101, 5000));

	}

	/*
	 * 1. invalid account number passed 2. VAlid account nunmber Passed
	 */
	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void wheninValidAccountNumberIsPassed() throws InvalidAccountNumberException {

		when(accountRepository.searchAccount(101)).thenReturn(null);

		accountService.showBalance(101);

	}

	@Test
	public void whenValidAccountNumberIsPassed() throws InvalidAccountNumberException {
		Account account = new Account();
		account.setAccountNumber(101);
		account.setAmount(2000);
		when(accountRepository.searchAccount(101)).thenReturn(account);

		assertEquals(2000, accountService.showBalance(101));
		;

	}

	@Test(expected = com.capgemini.exceptions.InvalidAccountNumberException.class)
	public void whenInValidAccountNumberIsPassedForWithdrawal() throws InvalidAccountNumberException, InsufficientBalanceException {

		when(accountRepository.searchAccount(101)).thenReturn(null);
		accountService.withDrawAmount(101, 100);

	}
	@Test(expected = com.capgemini.exceptions.InsufficientBalanceException.class)
	public void whenInValidAmountsPassedForWithdrawal() throws InvalidAccountNumberException, InsufficientBalanceException {
		Account account = new Account();
		account.setAccountNumber(101);
		account.setAmount(2000);
		when(accountRepository.searchAccount(101)).thenReturn(account);
		accountService.withDrawAmount(101,3000);

	}
	@Test
	public void whenValidAmountsPassedForWithdrawal() throws InvalidAccountNumberException, InsufficientBalanceException {
		Account account = new Account();
		account.setAccountNumber(101);
		account.setAmount(2000);
		when(accountRepository.searchAccount(101)).thenReturn(account);
		assertEquals(1000, accountService.withDrawAmount(101,1000));;

	}
}
