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
  <title>GameVerse Academy — Mods</title>
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
    .container { max-width: 1100px; margin: 40px auto; padding: 0 20px; }
    h1 { color: #4A90D9; }
    .meta { color: #aaa; font-size: 0.95rem; margin-bottom: 24px; }
    table { width: 100%; border-collapse: collapse; background: #fff; color: #333; border-radius: 8px; overflow: hidden; }
    th { background: #1E3A5F; color: white; padding: 12px; text-align: left; }
    td { padding: 10px 12px; border-bottom: 1px solid #ddd; }
    tr:nth-child(even) { background: #EEF4FB; }
    tr:hover { background: #D6E8F7; }
    .nav { margin-top: 24px; }
    .nav a { color: #4A90D9; }
    .empty { color: #ccc; padding: 20px 0; }
    .role-badge { font-size: 12px; color: #889; margin-left: 6px; }
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
    <h1>Mods</h1>
    <% if (currentUser != null) { %>
      <p class="meta">
        <a href="<%= request.getContextPath() %>/ModSubmitController">Soumettre un mod</a>
        <% if (currentUser.isAdmin()) { %>
          · <a href="<%= request.getContextPath() %>/AdminController">Modération (admin)</a>
        <% } %>
      </p>
    <% } %>
    <% if (filterCategory != null && !filterCategory.isBlank()) { %>
      <p class="meta">Filtre catégorie : <strong><%= filterCategory %></strong> · <a href="<%= request.getContextPath() %>/mods">Tout afficher</a></p>
    <% } %>

    <% if (mods.isEmpty()) { %>
      <p class="empty">Aucun mod à afficher pour le moment.</p>
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
