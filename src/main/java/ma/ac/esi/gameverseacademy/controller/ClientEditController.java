package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.esi.gameverseacademy.model.Client;
import ma.ac.esi.gameverseacademy.service.ClientService;
import ma.ac.esi.gameverseacademy.util.AuthUtil;

@WebServlet("/ClientEditController")
public class ClientEditController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}

		int id = parseId(request.getParameter("id"));
		if (id <= 0) {
			response.sendRedirect(request.getContextPath() + "/clients");
			return;
		}

		ClientService clientService = new ClientService();
		Client client = clientService.getClientById(id);
		if (client == null) {
			response.sendRedirect(request.getContextPath() + "/clients");
			return;
		}

		request.setAttribute("formMode", "edit");
		request.setAttribute("client", client);
		request.getRequestDispatcher("/client-form.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}

		int id = parseId(request.getParameter("id"));
		if (id <= 0) {
			response.sendRedirect(request.getContextPath() + "/clients");
			return;
		}

		Client client = new Client();
		client.setId(id);
		client.setFirstName(trim(request.getParameter("firstName")));
		client.setLastName(trim(request.getParameter("lastName")));
		client.setEmail(trim(request.getParameter("email")));
		client.setPhone(trim(request.getParameter("phone")));
		client.setCity(trim(request.getParameter("city")));
		client.setSubscriptionType(trim(request.getParameter("subscriptionType")));

		ClientService clientService = new ClientService();
		boolean success = clientService.updateClient(client);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/clients");
		} else {
			request.setAttribute("formMode", "edit");
			request.setAttribute("client", client);
			request.setAttribute("errorMessage",
					"Échec de la modification. Vérifiez les champs obligatoires.");
			request.getRequestDispatcher("/client-form.jsp").forward(request, response);
		}
	}

	private int parseId(String raw) {
		try {
			return raw == null ? -1 : Integer.parseInt(raw.trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private String trim(String s) {
		return s == null ? null : s.trim();
	}
}