<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.Mod" %>
<%
  @SuppressWarnings("unchecked")
  List<Mod> pendingMods = (List<Mod>) request.getAttribute("pendingMods");
  if (pendingMods == null) {
    pendingMods = java.util.Collections.emptyList();
  }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Administration — Mods en attente</title>
  <style>
    body { font-family: Arial, sans-serif; background: #1A1A2E; color: #fff; margin: 0; }
    .container { max-width: 1000px; margin: 40px auto; padding: 0 20px; }
    h1 { color: #4A90D9; }
    table { width: 100%; border-collapse: collapse; background: #fff; color: #333; border-radius: 8px; overflow: hidden; }
    th { background: #1E3A5F; color: white; padding: 12px; text-align: left; }
    td { padding: 10px 12px; border-bottom: 1px solid #ddd; vertical-align: middle; }
    tr:nth-child(even) { background: #EEF4FB; }
    .btn { padding: 8px 14px; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; margin-right: 8px; }
    .btn-ok { background: #2e7d32; color: #fff; }
    .btn-no { background: #c62828; color: #fff; }
    .nav { margin-top: 24px; }
    .nav a { color: #4A90D9; }
    .empty { color: #aaa; padding: 24px 0; }
  </style>
</head>
<body>
  <div class="container">
    <h1>Mods en attente de validation</h1>

    <% if (pendingMods.isEmpty()) { %>
      <p class="empty">Aucun mod en attente.</p>
    <% } else { %>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Titre</th>
            <th>Catégorie</th>
            <th>Auteur</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Mod mod : pendingMods) { %>
            <tr>
              <td><%= mod.getId() %></td>
              <td><%= mod.getTitle() == null ? "" : mod.getTitle() %></td>
              <td><%= mod.getCategory() == null ? "" : mod.getCategory() %></td>
              <td><%= mod.getAuthor() == null ? "" : mod.getAuthor() %></td>
              <td>
                <form action="<%= request.getContextPath() %>/AdminController" method="post" style="display:inline;">
                  <input type="hidden" name="modId" value="<%= mod.getId() %>">
                  <input type="hidden" name="action" value="approve">
                  <button type="submit" class="btn btn-ok">Approuver</button>
                </form>
                <form action="<%= request.getContextPath() %>/AdminController" method="post" style="display:inline;">
                  <input type="hidden" name="modId" value="<%= mod.getId() %>">
                  <input type="hidden" name="action" value="reject">
                  <button type="submit" class="btn btn-no">Rejeter</button>
                </form>
              </td>
            </tr>
          <% } %>
        </tbody>
      </table>
    <% } %>

    <p class="nav">
      <a href="<%= request.getContextPath() %>/mods">Liste publique des mods</a>
      ·
      <a href="<%= request.getContextPath() %>/home.html">Accueil</a>
    </p>
  </div>
</body>
</html>
