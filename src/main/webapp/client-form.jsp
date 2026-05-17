<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.Client" %>
<%@ page import="ma.ac.esi.gameverseacademy.model.User" %>
<%
  String formMode = (String) request.getAttribute("formMode");
  if (formMode == null) formMode = "add";
  Client client = (Client) request.getAttribute("client");
  String errorMessage = (String) request.getAttribute("errorMessage");
  User currentUser = (User) session.getAttribute("user");

  boolean isEdit = "edit".equals(formMode);
  String title  = isEdit ? "Modifier un client" : "Ajouter un client";
  String action = isEdit
      ? request.getContextPath() + "/ClientEditController"
      : request.getContextPath() + "/ClientAddController";

  String vId        = (client != null) ? String.valueOf(client.getId()) : "";
  String vFirstName = (client != null && client.getFirstName() != null) ? client.getFirstName() : "";
  String vLastName  = (client != null && client.getLastName()  != null) ? client.getLastName()  : "";
  String vEmail     = (client != null && client.getEmail()     != null) ? client.getEmail()     : "";
  String vPhone     = (client != null && client.getPhone()     != null) ? client.getPhone()     : "";
  String vCity      = (client != null && client.getCity()      != null) ? client.getCity()      : "";
  String vSub       = (client != null && client.getSubscriptionType() != null) ? client.getSubscriptionType() : "FREE";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>GameVerse Academy — <%= title %></title>
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
    .container { max-width: 700px; margin: 40px auto; padding: 0 20px; }
    h1 { color: #4A90D9; }
    .meta { color: #aaa; font-size: 0.95rem; margin-bottom: 24px; }
    .role-badge { font-size: 12px; color: #889; margin-left: 6px; }
    .nav { margin-top: 24px; }
    .nav a { color: #4A90D9; }
    .card { background: #fff; color: #333; border-radius: 8px; padding: 24px; }
    .form-group { margin-bottom: 16px; }
    label { display: block; font-weight: bold; margin-bottom: 6px; color: #1E3A5F; }
    input[type="text"], input[type="email"], select {
      width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px;
      font-family: inherit; box-sizing: border-box;
    }
    .required { color: #e94560; }
    .btn {
      display: inline-flex; align-items: center; gap: 6px;
      padding: 10px 18px; border-radius: 6px; border: none; cursor: pointer;
      font-size: 14px; font-family: inherit; text-decoration: none;
    }
    .btn-primary { background: #4A90D9; color: #fff; }
    .btn-cancel  { background: #888;    color: #fff; margin-left: 8px; }
    .error {
      background: #fdecea; color: #a4161a; border: 1px solid #f5c2c0;
      padding: 10px 14px; border-radius: 6px; margin-bottom: 16px;
    }
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
    <h1><%= title %></h1>
    <p class="meta">Les champs marqués <span class="required">*</span> sont obligatoires.</p>

    <% if (errorMessage != null) { %>
      <div class="error"><%= errorMessage %></div>
    <% } %>

    <div class="card">
      <form action="<%= action %>" method="post">
        <% if (isEdit) { %>
          <input type="hidden" name="id" value="<%= vId %>">
        <% } %>

        <div class="form-group">
          <label>Prénom <span class="required">*</span></label>
          <input type="text" name="firstName" value="<%= vFirstName %>" required>
        </div>

        <div class="form-group">
          <label>Nom <span class="required">*</span></label>
          <input type="text" name="lastName" value="<%= vLastName %>" required>
        </div>

        <div class="form-group">
          <label>Email <span class="required">*</span></label>
          <input type="email" name="email" value="<%= vEmail %>" required>
        </div>

        <div class="form-group">
          <label>Téléphone</label>
          <input type="text" name="phone" value="<%= vPhone %>">
        </div>

        <div class="form-group">
          <label>Ville</label>
          <input type="text" name="city" value="<%= vCity %>">
        </div>

        <div class="form-group">
          <label>Type d'abonnement</label>
          <select name="subscriptionType">
            <option value="FREE"    <%= "FREE".equals(vSub)    ? "selected" : "" %>>FREE</option>
            <option value="PREMIUM" <%= "PREMIUM".equals(vSub) ? "selected" : "" %>>PREMIUM</option>
            <option value="VIP"     <%= "VIP".equals(vSub)     ? "selected" : "" %>>VIP</option>
          </select>
        </div>

        <button type="submit" class="btn btn-primary">
          <i class="bi bi-check-circle"></i> <%= isEdit ? "Enregistrer" : "Ajouter" %>
        </button>
        <a href="<%= request.getContextPath() %>/clients" class="btn btn-cancel">
          <i class="bi bi-x-circle"></i> Annuler
        </a>
      </form>
    </div>

    <p class="nav"><a href="<%= request.getContextPath() %>/clients">← Retour à la liste</a></p>
  </div>
</body>
</html>