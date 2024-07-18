package com.banky;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
public class AccountManager {
	public static final String RED = "\u001B[31m";
	public static final String RESET = "\u001B[0m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String Cyan = "\u001B[36m";

	private Connection connection;
	private Scanner scanner;

	AccountManager(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void credit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();

		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
					PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					int rowsAffected = preparedStatement1.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println(GREEN + "Rs." + YELLOW + amount + GREEN + " credited Successfully" + RESET);
						connection.commit();
						connection.setAutoCommit(true);
						return;
					} else {
						System.out.println(RED + "Transaction Failed!" + RESET);
						connection.rollback();
						connection.setAutoCommit(true);
					}
				} else {
					System.out.println(RED + "Invalid Security Pin!" + RESET);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and security_pin = ? ");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int rowsAffected = preparedStatement1.executeUpdate();
						if (rowsAffected > 0) {
							System.out
									.println(GREEN + "Rs." + YELLOW + amount + GREEN + " debited Successfully" + RESET);
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println(RED + "Transaction Failed!" + RESET);
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println(RED + "Insufficient Balance!" + RESET);
					}
				} else {
					System.out.println(RED + "Invalid Pin!" + RESET);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws SQLException {
		scanner.nextLine();
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = scanner.nextLong();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if (sender_account_number != 0 && receiver_account_number != 0) {
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ? ");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount <= current_balance) {

						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
						String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";

						PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
						PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);

						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setLong(2, receiver_account_number);
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setLong(2, sender_account_number);
						int rowsAffected1 = debitPreparedStatement.executeUpdate();
						int rowsAffected2 = creditPreparedStatement.executeUpdate();
						if (rowsAffected1 > 0 && rowsAffected2 > 0) {
							System.out.println(GREEN + "Transaction Successful!" + RESET);
							System.out.println(
									GREEN + "Rs." + YELLOW + amount + GREEN + " Transferred Successfully" + RESET);
							connection.commit();
							connection.setAutoCommit(true);
							return;
						} else {
							System.out.println(RED + "Transaction Failed" + RESET);
							connection.rollback();
							connection.setAutoCommit(true);
						}
					} else {
						System.out.println(RED + "Insufficient Balance!" + RESET);
					}
				} else {
					System.out.println(RED + "Invalid Security Pin!" + RESET);
				}
			} else {
				System.out.println(RED + "Invalid account number" + RESET);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
	}

	public void getBalance(long account_number) {
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println(GREEN + "Balance: " + YELLOW + balance + RESET);
			} else {
				System.out.println(RED + "Invalid Pin!" + RESET);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
