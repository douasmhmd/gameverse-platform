<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.Mod" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.User" %>
<%
  @SuppressWarnings("unchecked")
  List<Mod> mods = (List<Mod>) request.getAttribute("mods");
  if (mods == null) {
    mods = java.util.Collections.emptyList();
  }
  String filterCategory = (String) request.getAttribute("category");
  User currentUser = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GameVerse Academy — Mods</title>
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
    <h1>Mods</h1>

    <% if (currentUser != null) { %>
      <p class="meta">
        <a href="<%= request.getContextPath() %>/ModSubmitController">
          <i class="bi bi-cloud-upload"></i> Soumettre un mod
        </a>
        <% if (currentUser.isAdmin()) { %>
          &nbsp;·&nbsp;
          <a href="<%= request.getContextPath() %>/AdminController">
            <i class="bi bi-shield-check"></i> Modération (admin)
          </a>
        <% } %>
      </p>
    <% } %>

    <% if (filterCategory != null && !filterCategory.isBlank()) { %>
      <p class="meta">
        Filtre catégorie : <strong style="color:var(--neon-yellow);"><%= filterCategory %></strong>
        &nbsp;·&nbsp;
        <a href="<%= request.getContextPath() %>/mods">Tout afficher</a>
      </p>
    <% } %>

    <% if (mods.isEmpty()) { %>
      <div class="empty">Aucun mod à afficher pour le moment.</div>
    <% } else { %>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Titre</th>
            <th>Catégorie</th>
            <th>Auteur</th>
            <th>Téléchargements</th>
          </tr>
        </thead>
        <tbody>
          <% for (Mod mod : mods) { %>
            <tr>
              <td><%= mod.getId() %></td>
              <td><%= mod.getTitle() == null ? "" : mod.getTitle() %></td>
              <td><%= mod.getCategory() == null ? "" : mod.getCategory() %></td>
              <td><%= mod.getAuthor() == null ? "" : mod.getAuthor() %></td>
              <td><%= mod.getDownloads() %></td>
            </tr>
          <% } %>
        </tbody>
      </table>
    <% } %>

    <p class="nav"><a href="<%= request.getContextPath() %>/home.html">← Retour à l'accueil</a></p>
  </div>
</body>
</html>
