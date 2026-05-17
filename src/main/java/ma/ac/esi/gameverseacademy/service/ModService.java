package ma.ac.esi.gameverseacademy.service;

import java.util.ArrayList;
import java.util.List;

import ma.ac.esi.gameverseacademy.model.Mod;
import ma.ac.esi.gameverseacademy.repository.ModRepository;

public class ModService {

	private final ModRepository modRepository = new ModRepository();

	public List<Mod> getAllMods() {
		return modRepository.getAllMods();
	}

	/** Liste affichée sur la page publique des mods */
	public List<Mod> getApprovedMods() {
		return modRepository.getApprovedMods();
	}

	public Mod getModById(int id) {
		return modRepository.getModById(id);
	}

	public List<Mod> getModsByCategory(String category) {
		List<Mod> approved = modRepository.getApprovedMods();
		List<Mod> filtered = new ArrayList<>();
		for (Mod mod : approved) {
			if (mod.getCategory() != null && mod.getCategory().equalsIgnoreCase(category)) {
				filtered.add(mod);
			}
		}
		return filtered;
	}

	public List<Mod> getPendingMods() {
		return modRepository.getPendingMods();
	}

	public boolean submitMod(Mod mod) {
		if (mod.getTitle() == null || mod.getTitle().trim().isEmpty()) {
			return false;
		}
		return modRepository.insertMod(mod);
	}

	public boolean approveMod(int modId) {
		return modRepository.updateModStatus(modId, "APPROVED");
	}

	public boolean rejectMod(int modId) {
		return modRepository.updateModStatus(modId, "REJECTED");
	}
}
