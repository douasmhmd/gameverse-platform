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
 * Authentification : session avec l'objet {@link User} (email + rôle), sans mot de passe en session.
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/index.html");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String ctx = request.getContextPath();

		if (login == null || login.isBlank() || password == null || password.isBlank()) {
			response.sendRedirect(ctx + "/index.html?error=1");
			return;
		}

		UserService userService = new UserService();

		try {
			User user = userService.authenticate(login.trim(), password);
			if (user != null) {
				user.setPassword(null);
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect(ctx + "/mods");
			} else {
				response.sendRedirect(ctx + "/error.html");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect(ctx + "/error.html");
		}
	}
}
