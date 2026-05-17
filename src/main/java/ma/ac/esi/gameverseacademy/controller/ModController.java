package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.esi.gameverseacademy.model.Mod;
import ma.ac.esi.gameverseacademy.service.ModService;
import ma.ac.esi.gameverseacademy.util.AuthUtil;

@WebServlet("/mods")
public class ModController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}

		ModService modService = new ModService();

		String category = request.getParameter("category");

		List<Mod> mods;
		if (category != null && !category.trim().isEmpty()) {
			mods = modService.getModsByCategory(category.trim());
		} else {
			mods = modService.getApprovedMods();
		}

		request.setAttribute("mods", mods);
		request.setAttribute("category", category);

		request.getRequestDispatcher("/mods.jsp").forward(request, response);
	}
}
