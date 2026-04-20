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
  <title>GameVerse Academy — Clients</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <style>
    body { font-family: Arial, sans-serif; background: #1A1A2E; color: #fff; margin: 0; }
    .topbar {
      display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;
      padding: 14px 20px; background: #16213e; border-bottom: 1px solid #2a3f5f;
    }
    .user-info { display: flex; align-items: center; gap: 10px; color: #cdd6f4; font-size: 15px; }
    .user-info i { font-size: 1.5rem; color: #4A90D9; }
    .logout-btn {
      display: inline-flex; align-items: center; gap: 8px;
      padding: 8px 16px; background: #e94560; color: #fff; border: none; border-radius: 8px;
      cursor: pointer; font-size: 14px; font-family: inherit;
    }
    .logout-btn:hover { filter: brightness(1.08); }
    .container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
    h1 { color: #4A90D9; }
    .meta { color: #aaa; font-size: 0.95rem; margin-bottom: 24px; }
    table { width: 100%; border-collapse: collapse; background: #fff; color: #333; border-radius: 8px; overflow: hidden; }
    th { background: #1E3A5F; color: white; padding: 12px; text-align: left; }
    td { padding: 10px 12px; border-bottom: 1px solid #ddd; vertical-align: middle; }
    tr:nth-child(even) { background: #EEF4FB; }
    tr:hover { background: #D6E8F7; }
    .nav { margin-top: 24px; }
    .nav a { color: #4A90D9; }
    .empty { color: #ccc; padding: 20px 0; }
    .role-badge { font-size: 12px; color: #889; margin-left: 6px; }
    .btn {
      display: inline-flex; align-items: center; gap: 6px;
      padding: 6px 12px; border-radius: 6px; border: none; cursor: pointer;
      font-size: 13px; font-family: inherit; text-decoration: none;
    }
    .btn-primary { background: #4A90D9; color: #fff; }
    .btn-primary:hover { filter: brightness(1.08); }
    .btn-edit { background: #f0ad4e; color: #fff; }
    .btn-edit:hover { filter: brightness(1.08); }
    .btn-delete { background: #e94560; color: #fff; }
    .btn-delete:hover { filter: brightness(1.08); }
    .actions { display: flex; gap: 6px; }
    .add-bar { margin-bottom: 16px; }
    .badge {
      display: inline-block; padding: 3px 8px; border-radius: 10px; font-size: 12px; font-weight: bold;
    }
    .badge-FREE    { background: #e0e0e0; color: #333; }
    .badge-PREMIUM { background: #4A90D9; color: #fff; }
    .badge-VIP     { background: #f0ad4e; color: #fff; }
  </style>
</head>
<body>

  <div class="topbar">
    <div class="user-info">
      <i class="bi bi-person-circle" aria-hidden="true"></i>
      <span>
        <%= currentUser != null ? currentUser.getEmail() : "Visiteur" %>
        <% if (currentUser != null) { %>
          <span class="role-badge">(<%= currentUser.getRole() %>)</span>
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
      <p class="empty">Aucun client à afficher pour le moment.</p>
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