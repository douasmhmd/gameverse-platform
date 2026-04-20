<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Soumettre un mod — GameVerse Academy</title>
  <style>
    body { font-family: Arial, sans-serif; background: #0D1B2A; color: #CDD6F4; margin: 0; }
    .container { max-width: 600px; margin: 60px auto; background: #1E3A5F; padding: 32px; border-radius: 10px; }
    h1 { color: #4A90D9; margin-bottom: 24px; }
    label { display: block; margin-top: 16px; font-weight: bold; }
    input, select, textarea {
      width: 100%; padding: 10px; margin-top: 6px;
      background: #0D1B2A; color: #CDD6F4;
      border: 1px solid #4A90D9; border-radius: 6px; box-sizing: border-box;
    }
    textarea { height: 120px; resize: vertical; }
    button {
      margin-top: 24px; padding: 12px 28px; background: #4A90D9;
      color: white; border: none; border-radius: 6px;
      cursor: pointer; font-size: 15px;
    }
    .success { background: #1B5E20; padding: 12px; border-radius: 6px; margin-bottom: 16px; }
    .error { background: #B71C1C; padding: 12px; border-radius: 6px; margin-bottom: 16px; }
    .nav { margin-top: 20px; }
    .nav a { color: #4A90D9; }
  </style>
</head>
<body>
<div class="container">
  <h1>Soumettre un nouveau mod</h1>

  <% String message = (String) request.getAttribute("message"); %>
  <% String error = (String) request.getAttribute("error"); %>
  <% if (message != null) { %><div class="success"><%= message %></div><% } %>
  <% if (error != null) { %><div class="error"><%= error %></div><% } %>

  <form action="<%= request.getContextPath() %>/ModSubmitController" method="post">

    <label for="title">Titre du mod *</label>
    <input type="text" id="title" name="title" placeholder="Ex: Ultra HD Texture Pack" required>

    <label for="category">Catégorie</label>
    <select id="category" name="category">
      <option value="Graphique">Graphique</option>
      <option value="Gameplay">Gameplay</option>
      <option value="Son">Son</option>
      <option value="Map">Map</option>
      <option value="Script">Script</option>
      <option value="Autre">Autre</option>
    </select>

    <label for="author">Auteur</label>
    <input type="text" id="author" name="author" placeholder="Votre pseudo">

    <label for="description">Description</label>
    <textarea id="description" name="description" placeholder="Décrivez votre mod en quelques phrases..."></textarea>

    <button type="submit">Soumettre le mod</button>
  </form>

  <p class="nav"><a href="<%= request.getContextPath() %>/mods">← Liste des mods</a></p>
</div>
</body>
</html>
