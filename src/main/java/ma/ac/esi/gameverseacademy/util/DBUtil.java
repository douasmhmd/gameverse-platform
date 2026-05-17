package ma.ac.esi.gameverseacademy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connexion JDBC : définir les variables d'environnement (ex. Render, machine locale) :
 * {@code DB_URL}, {@code DB_USER}, {@code DB_PASSWORD}.
 */
public class DBUtil {

	private static final String URL = env("DB_URL",
			"jdbc:postgresql://localhost:5432/gameverseacademy");
	private static final String USER = env("DB_USER", "postgres");
	private static final String PASSWORD = env("DB_PASSWORD", "");

	private static String env(String key, String defaultValue) {
		String v = System.getenv(key);
		return (v != null && !v.isBlank()) ? v.trim() : defaultValue;
	}

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
		}
	}

	/**
	 * Retourne une connexion JDBC vers la base PostgreSQL, ou null en cas d'échec.
	 */
	public static Connection getConnection() {
		if (PASSWORD.isEmpty()) {
			System.err.println(
					"DB_PASSWORD non défini : exportez DB_URL, DB_USER, DB_PASSWORD (ou variables Render).");
			return null;
		}
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.err.println("Erreur lors de la connexion à la base de données PostgreSQL !");
			e.printStackTrace();
		}
		return null;
	}
}
