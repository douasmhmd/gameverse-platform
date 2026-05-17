package ma.ac.esi.gameverseacademy.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ma.ac.esi.gameverseacademy.model.User;
import ma.ac.esi.gameverseacademy.util.DBUtil;

public class UserRepository {

	/**
	 * Authentification : retourne l'utilisateur avec son rôle si identifiants corrects.
	 * Table users : email, password, role (défaut USER si colonne absente — exécuter sql/alter_users_role.sql).
	 */
	public User findUserByCredentials(String login, String password) throws SQLException {
		String sql = "SELECT email, password, COALESCE(user_role, 'USER') AS user_role "
				+ "FROM users WHERE email = ? AND password = ?";
		Connection connection = DBUtil.getConnection();
		if (connection == null) {
			throw new SQLException("Connexion à la base de données indisponible.");
		}
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, login);
			statement.setString(2, password);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return new User(resultSet.getString("email"), resultSet.getString("password"),
							resultSet.getString("user_role"));
				}
			}
		} finally {
			connection.close();
		}
		return null;
	}

	/**
	 * Vérifie si un email existe déjà en base (pour éviter les doublons lors du signup).
	 */
	public boolean existsByEmail(String email) throws SQLException {
		String sql = "SELECT 1 FROM users WHERE email = ?";
		Connection connection = DBUtil.getConnection();
		if (connection == null) {
			throw new SQLException("Connexion à la base de données indisponible.");
		}
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, email);
			try (ResultSet resultSet = statement.executeQuery()) {
				return resultSet.next();
			}
		} finally {
			connection.close();
		}
	}

	/**
	 * Insère un nouvel utilisateur dans la base (signup — rôle USER par défaut).
	 */
	public boolean insertUser(User user) throws SQLException {
		String sql = "INSERT INTO users (email, password, user_role) VALUES (?, ?, ?)";
		Connection connection = DBUtil.getConnection();
		if (connection == null) {
			throw new SQLException("Connexion à la base de données indisponible.");
		}
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, user.getEmail());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getRole() != null ? user.getRole() : "USER");
			return statement.executeUpdate() > 0;
		} finally {
			connection.close();
		}
	}
}