package ma.ac.esi.gameverseacademy.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ma.ac.esi.gameverseacademy.model.Client;
import ma.ac.esi.gameverseacademy.util.DBUtil;

public class ClientRepository {

	private static final String SELECT_BASE = "SELECT id, first_name, last_name, email, phone, "
			+ "       city, subscription_type, created_at "
			+ "FROM clients ";

	private static final String SELECT_ALL = SELECT_BASE + "ORDER BY id ASC";

	private Client mapRow(ResultSet rs) throws SQLException {
		return new Client(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
				rs.getString("email"), rs.getString("phone"), rs.getString("city"),
				rs.getString("subscription_type"), rs.getTimestamp("created_at"));
	}

	public List<Client> getAllClients() {
		List<Client> clients = new ArrayList<>();
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return clients;
		}
		try (Connection conn = raw;
				PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				clients.add(mapRow(rs));
			}
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans getAllClients() : " + e.getMessage());
			e.printStackTrace();
		}
		return clients;
	}

	public Client getClientById(int id) {
		final String sql = SELECT_BASE + "WHERE id = ?";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return null;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans getClientById() : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertClient(Client client) {
		String sql = "INSERT INTO clients (first_name, last_name, email, phone, city, subscription_type) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return false;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, client.getFirstName());
			stmt.setString(2, client.getLastName());
			stmt.setString(3, client.getEmail());
			stmt.setString(4, client.getPhone());
			stmt.setString(5, client.getCity());
			stmt.setString(6, client.getSubscriptionType());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans insertClient() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateClient(Client client) {
		String sql = "UPDATE clients SET first_name = ?, last_name = ?, email = ?, phone = ?, "
				+ "       city = ?, subscription_type = ? "
				+ "WHERE id = ?";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return false;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, client.getFirstName());
			stmt.setString(2, client.getLastName());
			stmt.setString(3, client.getEmail());
			stmt.setString(4, client.getPhone());
			stmt.setString(5, client.getCity());
			stmt.setString(6, client.getSubscriptionType());
			stmt.setInt(7, client.getId());
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans updateClient() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteClient(int id) {
		String sql = "DELETE FROM clients WHERE id = ?";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return false;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans deleteClient() : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}