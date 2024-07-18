package com.banky;
import java.sql.*;
import java.util.Scanner;

import java.util.Scanner;

import static java.lang.Class.forName;
public class BankingApp {
	public static final String RED = "\u001B[31m";
	public static final String RESET = "\u001B[0m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String Magenta = "\u001B[35m";
	public static final String Cyan = "\u001B[36m";

	private static final String url = "jdbc:mysql://localhost:3306/bank";
	private static final String username = "root";
	private static final String password = "root";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Scanner scanner = new Scanner(System.in);
			User user = new User(connection, scanner);
			Accounts accounts = new Accounts(connection, scanner);
			AccountManager accountManager = new AccountManager(connection, scanner);

			String email;
			long account_number;

			while (true) {
				System.out.println(Cyan + "*** WELCOME TO BANKY ***\n" + RESET);
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println(Cyan + "Enter your choice: " + RESET);
				int choice1 = scanner.nextInt();
				switch (choice1) {
				case 1:
					user.register();
					break;
				case 2:
					email = user.login();
					if (email != null) {
						System.out.println();
						System.out.println(GREEN + "User Logged In!" + RESET);
						if (!accounts.account_exist(email)) {
							System.out.println();
							System.out.println("1. Open a new Bank Account");
							System.out.println("2. Exit");
							System.out.println(Cyan + "Enter your choice: " + RESET);
							if (scanner.nextInt() == 1) {
								account_number = accounts.open_account(email);
								System.out.println(GREEN + "Account Created Successfully" + RESET);
								System.out.println("Your Account Number is: " + YELLOW + account_number + RESET);
							} else {
								break;
							}

						}
						account_number = accounts.getAccount_number(email);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. Credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Log Out");
							System.out.println(Cyan + "Enter your choice: " + RESET);
							choice2 = scanner.nextInt();
							switch (choice2) {
							case 1:
								accountManager.debit_money(account_number);
								break;
							case 2:
								accountManager.credit_money(account_number);
								break;
							case 3:
								accountManager.transfer_money(account_number);
								break;
							case 4:
								accountManager.getBalance(account_number);
								break;
							case 5:
								break;
							default:
								System.out.println(YELLOW + "Enter Valid Choice!" + RESET);
								break;
							}
						}

					} else {
						System.out.println(RED + "Incorrect Email or Password!" + RESET);
					}
				case 3:
					System.out.println(Magenta + "THANK YOU FOR USING BANKY !!!");
					System.out.println("Exiting System!" + RESET);
					return;
				default:
					System.out.println(YELLOW + "Enter Valid Choice" + RESET);
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}}

}
