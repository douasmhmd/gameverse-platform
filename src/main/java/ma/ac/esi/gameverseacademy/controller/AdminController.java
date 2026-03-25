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

/**
 * Modération : réservé au rôle ADMIN (TP4).
 */
@WebServlet("/AdminController")
public class AdminController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final ModService modService = new ModService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}
		List<Mod> pending = modService.getPendingMods();
		request.setAttribute("pendingMods", pending);
		request.getRequestDispatcher("/WEB-INF/views/adminMods.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}

		String action = request.getParameter("action");
		String modIdStr = request.getParameter("modId");

		if (modIdStr != null && action != null) {
			try {
				int modId = Integer.parseInt(modIdStr);
				if ("approve".equals(action)) {
					modService.approveMod(modId);
				} else if ("reject".equals(action)) {
					modService.rejectMod(modId);
				}
			} catch (NumberFormatException ignored) {
				// noop
			}
		}

		response.sendRedirect(request.getContextPath() + "/AdminController");
	}
}
