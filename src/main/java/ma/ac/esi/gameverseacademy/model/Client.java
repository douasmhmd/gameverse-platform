package ma.ac.esi.gameverseacademy.model;

import java.sql.Timestamp;

public class Client {
	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String city;
	/** FREE, PREMIUM, VIP — type d'abonnement */
	private String subscriptionType;
	private Timestamp createdAt;

	public Client(int id, String firstName, String lastName, String email, String phone,
			String city, String subscriptionType, Timestamp createdAt) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.city = city;
		this.subscriptionType = subscriptionType;
		this.createdAt = createdAt;
	}

	public Client() {
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getCity() {
		return city;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Client{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName
				+ "', email='" + email + "', subscriptionType='" + subscriptionType + "'}";
	}
}