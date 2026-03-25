package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.esi.gameverseacademy.model.Mod;
import ma.ac.esi.gameverseacademy.service.ModService;
import ma.ac.esi.gameverseacademy.util.AuthUtil;

@WebServlet("/ModSubmitController")
public class ModSubmitController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final ModService modService = new ModService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		request.getRequestDispatcher("/WEB-INF/views/submitMod.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}

		String title = request.getParameter("title");
		String category = request.getParameter("category");
		String author = request.getParameter("author");
		String description = request.getParameter("description");

		Mod mod = new Mod();
		mod.setTitle(title);
		mod.setCategory(category);
		mod.setAuthor(author);
		mod.setDescription(description);

		boolean success = modService.submitMod(mod);

		if (success) {
			request.setAttribute("message", "Votre mod a été soumis avec succès !");
		} else {
			request.setAttribute("error", "Erreur lors de la soumission. Vérifiez les champs.");
		}
		request.getRequestDispatcher("/WEB-INF/views/submitMod.jsp").forward(request, response);
	}
}
