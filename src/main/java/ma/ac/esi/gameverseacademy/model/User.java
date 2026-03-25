package ma.ac.esi.gameverseacademy.model;

/**
 * Utilisateur applicatif. Le mot de passe ne doit pas rester stocké en session après connexion.
 */
public class User {

	private String email;
	private String password;
	/** Ex. USER, ADMIN */
	private String role;

	public User(String email, String password, String role) {
		this.email = email;
		this.password = password;
		this.role = role != null ? role : "USER";
	}

	public User(String email, String password) {
		this(email, password, "USER");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isAdmin() {
		return role != null && "ADMIN".equalsIgnoreCase(role.trim());
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", role=" + role + "]";
	}
}
