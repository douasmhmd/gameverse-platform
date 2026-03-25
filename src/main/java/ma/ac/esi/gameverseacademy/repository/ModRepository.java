package ma.ac.esi.gameverseacademy.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ma.ac.esi.gameverseacademy.model.Mod;
import ma.ac.esi.gameverseacademy.util.DBUtil;

public class ModRepository {

	private static final String SELECT_BASE = "SELECT id, title, category, author, description, "
			+ "       downloads, created_at, developer, publisher, "
			+ "       platform, release_date, metacritic, status   "
			+ "FROM mods ";

	private static final String SELECT_ALL = SELECT_BASE + "ORDER BY id ASC";

	private static final String SELECT_APPROVED = SELECT_BASE + "WHERE status = 'APPROVED' ORDER BY id ASC";

	private static final String SELECT_PENDING = SELECT_BASE + "WHERE status = 'PENDING' ORDER BY id ASC";

	private Mod mapRow(ResultSet rs) throws SQLException {
		return new Mod(rs.getInt("id"), rs.getString("title"), rs.getString("category"), rs.getString("author"),
				rs.getString("description"), rs.getInt("downloads"), rs.getTimestamp("created_at"),
				rs.getString("developer"), rs.getString("publisher"), rs.getString("platform"),
				rs.getString("release_date"), rs.getInt("metacritic"), rs.getString("status"));
	}

	public List<Mod> getAllMods() {
		return queryMods(SELECT_ALL);
	}

	/** Liste publique : uniquement les mods validés */
	public List<Mod> getApprovedMods() {
		return queryMods(SELECT_APPROVED);
	}

	public List<Mod> getPendingMods() {
		return queryMods(SELECT_PENDING);
	}

	private List<Mod> queryMods(String sql) {
		List<Mod> mods = new ArrayList<>();
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return mods;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				mods.add(mapRow(rs));
			}
		} catch (SQLException e) {
			System.err.println("Erreur SQL dans queryMods : " + e.getMessage());
			e.printStackTrace();
		}
		return mods;
	}

	public Mod getModById(int id) {
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
			System.err.println("Erreur SQL dans getModById() : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertMod(Mod mod) {
		String sql = "INSERT INTO mods (title, category, author, description, status) " + "VALUES (?, ?, ?, ?, 'PENDING')";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return false;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, mod.getTitle());
			stmt.setString(2, mod.getCategory());
			stmt.setString(3, mod.getAuthor());
			stmt.setString(4, mod.getDescription());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateModStatus(int modId, String status) {
		String sql = "UPDATE mods SET status = ? WHERE id = ?";
		Connection raw = DBUtil.getConnection();
		if (raw == null) {
			return false;
		}
		try (Connection conn = raw; PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, status);
			stmt.setInt(2, modId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
