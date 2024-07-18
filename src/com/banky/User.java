package com.banky;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	public static final String RED = "\u001B[31m";
	public static final String RESET = "\u001B[0m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String Cyan = "\u001B[36m";

	private Connection connection;
	private Scanner scanner;

	public User(Connection connection, Scanner scanner) {
		this.connection = connection;
		this.scanner = scanner;
	}

	public void register() {
		scanner.nextLine();
		System.out.print("Full Name: ");
		String full_name = scanner.nextLine();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		if (user_exist(email)) {
			System.out.println(RED + "User Already Exists for this Email Address!!" + RESET);
			return;
		}
		String register_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(register_query);
			preparedStatement.setString(1, full_name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				System.out.println(GREEN + "Registration Successfull!\n" + RESET);
			} else {
				System.out.println(RED + "Registration Failed!" + RESET);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String login() {
		scanner.nextLine();
		System.out.print("Email: ");
		String email = scanner.nextLine();
		System.out.print("Password: ");
		String password = scanner.nextLine();
		String login_query = "SELECT * FROM User WHERE email = ? AND password = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(login_query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return email;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean user_exist(String email) {
		String query = "SELECT * FROM user WHERE email = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
