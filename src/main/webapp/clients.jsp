<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.Client" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.User" %>
<%
  @SuppressWarnings("unchecked")
  List<Client> clients = (List<Client>) request.getAttribute("clients");
  if (clients == null) {
    clients = java.util.Collections.emptyList();
  }
  User currentUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GameVerse Academy — Clients</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/gameverse-neon.css">
</head>
<body>

  <div class="topbar">
    <div class="user-info">
      <i class="bi bi-person-circle" aria-hidden="true"></i>
      <span>
        <%= currentUser != null ? currentUser.getEmail() : "Visiteur" %>
        <% if (currentUser != null) { %>
          <span class="role-badge"><%= currentUser.getRole() %></span>
        <% } %>
      </span>
    </div>
    <form action="<%= request.getContextPath() %>/LogoutController" method="post">
      <button type="submit" class="logout-btn">
        <i class="bi bi-box-arrow-right" aria-hidden="true"></i> Déconnexion
      </button>
    </form>
  </div>

  <div class="container">
    <h1>Gestion des clients</h1>
    <p class="meta">Liste des clients inscrits à GameVerse Academy.</p>

    <% if (currentUser != null && currentUser.isAdmin()) { %>
      <div class="add-bar">
        <a href="<%= request.getContextPath() %>/ClientAddController" class="btn btn-primary">
          <i class="bi bi-plus-circle"></i> Ajouter un client
        </a>
      </div>
    <% } %>

    <% if (clients.isEmpty()) { %>
      <div class="empty">Aucun client à afficher pour le moment.</div>
    <% } else { %>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Prénom</th>
            <th>Nom</th>
            <th>Email</th>
            <th>Téléphone</th>
            <th>Ville</th>
            <th>Abonnement</th>
            <% if (currentUser != null && currentUser.isAdmin()) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% for (Client c : clients) {
               String sub = c.getSubscriptionType() == null ? "FREE" : c.getSubscriptionType();
          %>
            <tr>
              <td><%= c.getId() %></td>
              <td><%= c.getFirstName() == null ? "" : c.getFirstName() %></td>
              <td><%= c.getLastName()  == null ? "" : c.getLastName()  %></td>
              <td><%= c.getEmail()     == null ? "" : c.getEmail()     %></td>
              <td><%= c.getPhone()     == null ? "" : c.getPhone()     %></td>
              <td><%= c.getCity()      == null ? "" : c.getCity()      %></td>
              <td><span class="badge badge-<%= sub %>"><%= sub %></span></td>
              <% if (currentUser != null && currentUser.isAdmin()) { %>
                <td>
                  <div class="actions">
                    <a href="<%= request.getContextPath() %>/ClientEditController?id=<%= c.getId() %>" class="btn btn-edit">
                      <i class="bi bi-pencil"></i> Modifier
                    </a>
                    <form action="<%= request.getContextPath() %>/ClientDeleteController" method="post"
                          onsubmit="return confirm('Supprimer le client <%= c.getFirstName() %> <%= c.getLastName() %> ?');"
                          style="display:inline;">
                      <input type="hidden" name="id" value="<%= c.getId() %>">
                      <button type="submit" class="btn btn-delete">
                        <i class="bi bi-trash"></i> Supprimer
                      </button>
                    </form>
                  </div>
                </td>
              <% } %>
            </tr>
          <% } %>
        </tbody>
      </table>
    <% } %>

    <p class="nav"><a href="<%= request.getContextPath() %>/home.html">← Retour à l'accueil</a></p>
  </div>
</body>
</html>