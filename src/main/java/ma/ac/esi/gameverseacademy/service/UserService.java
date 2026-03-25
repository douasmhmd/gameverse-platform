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
}
