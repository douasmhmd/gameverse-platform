package ma.ac.esi.gameverseacademy.service;

import java.sql.SQLException;

import ma.ac.esi.gameverseacademy.model.User;
import ma.ac.esi.gameverseacademy.repository.UserRepository;

public class UserService {

	private final UserRepository userRepository;

	public UserService() {
		this.userRepository = new UserRepository();
	}

	public User authenticate(String login, String password) throws SQLException {
		return userRepository.findUserByCredentials(login, password);
	}

	/**
	 * Inscription d'un nouvel utilisateur (rôle USER).
	 * Renvoie le User créé en cas de succès, ou null si échec (email existant, validation...).
	 *
	 * @throws IllegalArgumentException si la validation métier échoue
	 */
	public User register(String email, String password) throws SQLException {
		// Validation métier
		if (isBlank(email) || isBlank(password)) {
			throw new IllegalArgumentException("Email et mot de passe obligatoires.");
		}
		if (!email.contains("@") || !email.contains(".")) {
			throw new IllegalArgumentException("Format d'email invalide.");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
		}

		// Vérification d'unicité
		if (userRepository.existsByEmail(email.trim())) {
			throw new IllegalArgumentException("Cet email est déjà utilisé.");
		}

		// Création
		User user = new User(email.trim(), password, "USER");
		boolean ok = userRepository.insertUser(user);
		return ok ? user : null;
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}