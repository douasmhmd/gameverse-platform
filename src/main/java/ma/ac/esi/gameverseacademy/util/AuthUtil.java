package ma.ac.esi.gameverseacademy.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.ac.esi.gameverseacademy.model.User;

/**
 * Vérifications de session mutualisées (TP4).
 */
public final class AuthUtil {

	private AuthUtil() {
	}

	public static boolean isAuthenticated(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session != null && session.getAttribute("user") instanceof User;
	}

	public static User getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		Object u = session.getAttribute("user");
		return u instanceof User ? (User) u : null;
	}

	public static boolean isAdmin(HttpServletRequest request) {
		User u = getCurrentUser(request);
		return u != null && u.isAdmin();
	}
}
