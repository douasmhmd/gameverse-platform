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

@WebServlet("/ClientAddController")
public class ClientAddController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}
		request.setAttribute("formMode", "add");
		request.getRequestDispatcher("/client-form.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/error403.html");
			return;
		}

		Client client = new Client();
		client.setFirstName(trim(request.getParameter("firstName")));
		client.setLastName(trim(request.getParameter("lastName")));
		client.setEmail(trim(request.getParameter("email")));
		client.setPhone(trim(request.getParameter("phone")));
		client.setCity(trim(request.getParameter("city")));
		client.setSubscriptionType(trim(request.getParameter("subscriptionType")));

		ClientService clientService = new ClientService();
		boolean success = clientService.addClient(client);

		if (success) {
			response.sendRedirect(request.getContextPath() + "/clients");
		} else {
			request.setAttribute("formMode", "add");
			request.setAttribute("client", client);
			request.setAttribute("errorMessage",
					"Échec de l'ajout. Vérifiez les champs obligatoires (prénom, nom, email unique).");
			request.getRequestDispatcher("/client-form.jsp").forward(request, response);
		}
	}

	private String trim(String s) {
		return s == null ? null : s.trim();
	}
}