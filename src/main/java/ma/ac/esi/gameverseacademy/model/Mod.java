package ma.ac.esi.gameverseacademy.model;

import java.sql.Timestamp;

public class Mod {

	private int id;
	private String title;
	private String category;
	private String author;
	private String description;
	private int downloads;
	private Timestamp createdAt;
	private String developer;
	private String publisher;
	private String platform;
	private String releaseDate;
	private int metacritic;
	private String status;

	public Mod(int id, String title, String category, String author, String description, int downloads,
			Timestamp createdAt, String developer, String publisher, String platform, String releaseDate,
			int metacritic, String status) {
		this.id = id;
		this.title = title;
		this.category = category;
		this.author = author;
		this.description = description;
		this.downloads = downloads;
		this.createdAt = createdAt;
		this.developer = developer;
		this.publisher = publisher;
		this.platform = platform;
		this.releaseDate = releaseDate;
		this.metacritic = metacritic;
		this.status = status;
	}

	public Mod() {
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getCategory() {
		return category;
	}

	public String getAuthor() {
		return author;
	}

	public String getDescription() {
		return description;
	}

	public int getDownloads() {
		return downloads;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public String getDeveloper() {
		return developer;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getPlatform() {
		return platform;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public int getMetacritic() {
		return metacritic;
	}

	public String getStatus() {
		return status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public void setMetacritic(int metacritic) {
		this.metacritic = metacritic;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Mod{id=" + id + ", title='" + title + "', developer='" + developer + "', status='" + status + "'}";
	}
}
