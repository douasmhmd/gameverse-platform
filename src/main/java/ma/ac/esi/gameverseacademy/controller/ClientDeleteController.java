package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.esi.gameverseacademy.service.ClientService;
import ma.ac.esi.gameverseacademy.util.AuthUtil;

@WebServlet("/ClientDeleteController")
public class ClientDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}

		int id = parseId(request.getParameter("id"));
		if (id > 0) {
			ClientService clientService = new ClientService();
			clientService.deleteClient(id);
		}
		response.sendRedirect(request.getContextPath() + "/clients");
	}

	private int parseId(String raw) {
		try {
			return raw == null ? -1 : Integer.parseInt(raw.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}
}