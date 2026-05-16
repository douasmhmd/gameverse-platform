package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.ac.esi.gameverseacademy.model.User;
import ma.ac.esi.gameverseacademy.service.UserService;

/**
 * Inscription : création de compte USER.
 * Auto-login après inscription réussie (session créée).
 */
@WebServlet("/SignupController")
public class SignupController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/signup.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String ctx = request.getContextPath();

		// Vérification de la confirmation côté serveur
		if (password == null || !password.equals(confirmPassword)) {
			response.sendRedirect(ctx + "/signup.html?error=mismatch");
			return;
		}

		UserService userService = new UserService();
		try {
			User user = userService.register(email, password);
			if (user != null) {
				// Auto-login : on crée la session et on redirige vers /mods
				user.setPassword(null);
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect(ctx + "/mods");
			} else {
				response.sendRedirect(ctx + "/signup.html?error=db");
			}
		} catch (IllegalArgumentException e) {
			// Erreur de validation métier (email invalide, déjà utilisé, mdp trop court...)
			String msg = java.net.URLEncoder.encode(e.getMessage(), "UTF-8");
			response.sendRedirect(ctx + "/signup.html?error=" + msg);
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect(ctx + "/signup.html?error=db");
		}
	}
}