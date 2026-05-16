# 🎮 GameVerse Academy

> Plateforme web JEE de distribution et de gestion de mods de jeux vidéo, avec gestion des clients et système de modération.

![Java](https://img.shields.io/badge/Java-21-orange)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791)
![Maven](https://img.shields.io/badge/Maven-build-red)
![Tomcat](https://img.shields.io/badge/Tomcat-Embedded%2010.1-yellow)

---

## 📋 Table des matières

- [Description](#-description)
- [Fonctionnalités](#-fonctionnalités)
- [Stack technique](#-stack-technique)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Lancement](#-lancement)
- [Comptes de test](#-comptes-de-test)
- [Structure du projet](#-structure-du-projet)
- [Auteur](#-auteur)

---

## 📖 Description

**GameVerse Academy** est une application web développée en **Jakarta EE** permettant :
- La publication et la modération de mods de jeux vidéo
- La gestion complète des clients de la plateforme (CRUD)
- L'authentification des utilisateurs avec gestion de rôles

Le projet suit une **architecture n-tiers** et se déploie de manière autonome grâce à un serveur **Tomcat embarqué**.

---

## ✨ Fonctionnalités

### 🔐 Authentification
- Connexion / Déconnexion par session
- Inscription de nouveaux utilisateurs
- Gestion de rôles : `USER` et `ADMIN`

### 📋 Gestion des clients (CRUD)
- **C**réer un client
- **L**ire / afficher la liste des clients
- **M**odifier un client
- **S**upprimer un client
- Accès réservé aux administrateurs

### 🎮 Gestion des mods
- Soumission de mods par les utilisateurs
- Workflow de modération : `PENDING` → `APPROVED` / `REJECTED`
- Consultation publique des mods approuvés
- Filtrage par catégorie

### 🔒 Sécurité
- Contrôle d'accès basé sur les rôles (RBAC)
- Requêtes paramétrées (protection contre les injections SQL)
- Validation des données côté serveur

---

## 🛠 Stack technique

| Couche | Technologie |
|--------|-------------|
| Langage | Java 21 |
| Framework web | Jakarta Servlet 6.0, JSP |
| Serveur | Apache Tomcat Embedded 10.1.18 |
| Base de données | PostgreSQL 16 |
| Accès données | JDBC (PostgreSQL Driver 42.7.3) |
| Build | Apache Maven |
| Packaging | Fat JAR/WAR exécutable (maven-shade-plugin) |

---

## 🏗 Architecture

Le projet suit une **architecture n-tiers** en couches :

```
┌─────────────────────────────────────────┐
│  Présentation  →  JSP / HTML             │
├─────────────────────────────────────────┤
│  Contrôle      →  Servlets (Controllers) │
├─────────────────────────────────────────┤
│  Métier        →  Services               │
├─────────────────────────────────────────┤
│  Données       →  Repositories (JDBC)    │
├─────────────────────────────────────────┤
│  Base          →  PostgreSQL             │
└─────────────────────────────────────────┘
```

---

## ⚙️ Prérequis

- **Java JDK 21** ou supérieur
- **Apache Maven** 3.8+
- **PostgreSQL 16**
- **Git**

---

## 🚀 Installation

### 1. Cloner le dépôt

```bash
git clone https://github.com/douasmhmd/gameverse-platform.git
cd gameverse-platform
```

### 2. Créer la base de données

```bash
psql -U postgres -c "CREATE DATABASE gameverseacademy;"
```

### 3. Exécuter les scripts SQL

```bash
psql -U postgres -d gameverseacademy -f sql/create_users_table.sql
psql -U postgres -d gameverseacademy -f sql/create_mods_table.sql
psql -U postgres -d gameverseacademy -f sql/alter_mods_add_status.sql
psql -U postgres -d gameverseacademy -f sql/alter_users_role.sql
psql -U postgres -d gameverseacademy -f sql/create_clients_table.sql
```

### 4. Configurer la connexion à la base

Définir les variables d'environnement (ou les renseigner dans `run-embedded.cmd`) :

```bash
DB_URL=jdbc:postgresql://localhost:5432/gameverseacademy
DB_USER=postgres
DB_PASSWORD=votre_mot_de_passe
```

---

## ▶️ Lancement

### Option 1 — Script (Windows)

```bash
run-embedded.cmd
```

### Option 2 — Maven

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="ma.ac.esi.gameverseacademy.Main"
```

### Option 3 — JAR exécutable autonome

```bash
mvn clean package
java -jar target/GameVerseAcademy-0.0.1-SNAPSHOT.war
```

L'application est ensuite accessible à l'adresse :

👉 **http://localhost:6060/gameverseacademy/**

---

## 👤 Comptes de test

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Administrateur | `admin@gameverseacademy.ma` | `admin123` |
| Utilisateur | `user@gameverseacademy.ma` | `user123` |

---

## 📁 Structure du projet

```
gameverse-platform/
├── sql/                          # Scripts SQL
│   ├── create_users_table.sql
│   ├── create_mods_table.sql
│   ├── create_clients_table.sql
│   └── ...
├── src/main/
│   ├── java/ma/ac/esi/gameverseacademy/
│   │   ├── Main.java             # Point d'entrée (Tomcat embarqué)
│   │   ├── controller/           # Servlets
│   │   ├── model/                # Entités (POJOs)
│   │   ├── repository/           # Accès aux données (JDBC)
│   │   ├── service/              # Logique métier
│   │   └── util/                 # Utilitaires (DB, Auth)
│   └── webapp/
│       ├── assets/css/           # Styles (thème Gaming Neon)
│       ├── WEB-INF/
│       ├── index.html            # Page de connexion
│       ├── signup.html           # Page d'inscription
│       ├── home.html             # Page d'accueil
│       ├── clients.jsp           # Liste des clients
│       ├── client-form.jsp       # Formulaire client
│       └── mods.jsp              # Liste des mods
├── pom.xml                       # Configuration Maven
├── run-embedded.cmd              # Script de lancement
└── README.md
```

---

## 🌐 Endpoints principaux

| URL | Méthode | Description | Accès |
|-----|---------|-------------|-------|
| `/index.html` | GET | Page de connexion | Public |
| `/signup.html` | GET | Page d'inscription | Public |
| `/LoginController` | POST | Authentification | Public |
| `/SignupController` | POST | Inscription | Public |
| `/LogoutController` | POST | Déconnexion | Authentifié |
| `/clients` | GET | Liste des clients | Authentifié |
| `/ClientAddController` | GET/POST | Ajouter un client | Admin |
| `/ClientEditController` | GET/POST | Modifier un client | Admin |
| `/ClientDeleteController` | POST | Supprimer un client | Admin |
| `/mods` | GET | Liste des mods | Authentifié |
| `/ModSubmitController` | GET/POST | Soumettre un mod | Authentifié |
| `/AdminController` | GET/POST | Modération des mods | Admin |

---

## 👨‍💻 Auteur

**DOUAS Mohamed**
Module : Applications N-Tiers en Java
2025–2026

---

## 📄 Licence

Projet réalisé dans un cadre pédagogique.
