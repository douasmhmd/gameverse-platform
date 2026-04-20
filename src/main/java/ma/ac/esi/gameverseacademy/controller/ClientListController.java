package ma.ac.esi.gameverseacademy.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.esi.gameverseacademy.model.Client;
import ma.ac.esi.gameverseacademy.service.ClientService;
import ma.ac.esi.gameverseacademy.util.AuthUtil;

@WebServlet("/clients")
public class ClientListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!AuthUtil.isAuthenticated(request)) {
			response.sendRedirect(request.getContextPath() + "/index.html");
			return;
		}
		ClientService clientService = new ClientService();
		List<Client> clients = clientService.getAllClients();
		request.setAttribute("clients", clients);
		request.getRequestDispatcher("/clients.jsp").forward(request, response);
	}
}