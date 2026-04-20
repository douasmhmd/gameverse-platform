package ma.ac.esi.gameverseacademy.service;

import java.util.List;

import ma.ac.esi.gameverseacademy.model.Client;
import ma.ac.esi.gameverseacademy.repository.ClientRepository;

/**
 * Logique métier pour la gestion des clients (CRUD — TP5).
 */
public class ClientService {

	private final ClientRepository clientRepository;

	public ClientService() {
		this.clientRepository = new ClientRepository();
	}

	public List<Client> getAllClients() {
		return clientRepository.getAllClients();
	}

	public Client getClientById(int id) {
		return clientRepository.getClientById(id);
	}

	public boolean addClient(Client client) {
		if (client == null) {
			return false;
		}
		if (isBlank(client.getFirstName()) || isBlank(client.getLastName()) || isBlank(client.getEmail())) {
			return false;
		}
		if (isBlank(client.getSubscriptionType())) {
			client.setSubscriptionType("FREE");
		}
		return clientRepository.insertClient(client);
	}

	public boolean updateClient(Client client) {
		if (client == null || client.getId() <= 0) {
			return false;
		}
		if (isBlank(client.getFirstName()) || isBlank(client.getLastName()) || isBlank(client.getEmail())) {
			return false;
		}
		return clientRepository.updateClient(client);
	}

	public boolean deleteClient(int id) {
		if (id <= 0) {
			return false;
		}
		return clientRepository.deleteClient(id);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}