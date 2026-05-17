# 🔄 Rapport d'Intégration Continue — CI/CD Java/Maven

> Pipeline CI/CD complet avec **GitHub Actions**, **Nexus Repository Manager** et notifications **Email**.

---

## 📁 Structure des fichiers

```
.
├── .github/
│   └── workflows/
│       └── ci-cd.yml        ← Pipeline GitHub Actions (principal)
├── Jenkinsfile              ← Script Groovy pipeline (Jenkins)
├── settings.xml             ← Template Maven pour Nexus
└── README-CI.md             ← Ce fichier
```

---

## 🏗️ Architecture du Pipeline

```
┌─────────────────────────────────────────────────────────────────┐
│                     DÉCLENCHEURS (Triggers)                     │
│  push/PR → main, develop, feature/**, hotfix/**                 │
│  Scrutation automatique : cron H/15 * * * * (toutes 15 min)    │
│  Déclenchement manuel (workflow_dispatch)                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
          ┌──────────────▼──────────────┐
          │     Job 1 : 🔨 BUILD        │
          │  mvn clean compile          │
          └──────────────┬──────────────┘
                         │ ✅
          ┌──────────────▼──────────────┐
          │     Job 2 : 🧪 TESTS        │
          │  mvn test + mvn verify      │
          │  → Rapport JUnit publié     │
          └──────────────┬──────────────┘
                         │ ✅          ❌→ 📧 Email ÉCHEC
          ┌──────────────▼──────────────┐
          │    Job 3 : 📦 PACKAGE       │
          │  mvn package → .jar/.war    │
          │  → Archivage de l'artefact  │
          └──────────────┬──────────────┘
                         │ ✅ (si branch main/develop)
          ┌──────────────▼──────────────┐
          │  Job 4 : 🚀 DEPLOY NEXUS    │
          │  SNAPSHOT → nexus-snapshots │
          │  RELEASE  → nexus-releases  │
          └──────────────┬──────────────┘
                         │
              ┌──────────┴──────────┐
              │ ✅ 📧 Email SUCCÈS  │ ❌ 📧 Email ÉCHEC
              └─────────────────────┘
```

---

## ⚙️ Configuration des Secrets GitHub

Aller dans **Settings → Secrets and variables → Actions** et ajouter :

| Secret | Description | Exemple |
|--------|-------------|---------|
| `NEXUS_URL` | URL de votre Nexus | `http://192.168.1.10:8081` |
| `NEXUS_USERNAME` | Utilisateur Nexus CI | `ci-user` |
| `NEXUS_PASSWORD` | Mot de passe Nexus | `••••••••` |
| `MAIL_SMTP_HOST` | Serveur SMTP | `smtp.gmail.com` |
| `MAIL_SMTP_PORT` | Port SMTP | `587` |
| `MAIL_USERNAME` | Email expéditeur | `ci@example.com` |
| `MAIL_PASSWORD` | Mot de passe / App Password | `••••••••` |
| `MAIL_RECIPIENT` | Email destinataire | `equipe@example.com` |

> 💡 Pour Gmail, activez **"App Password"** dans la sécurité du compte Google et utilisez-le comme `MAIL_PASSWORD`.

---

## 🔧 Configuration du pom.xml

Ajouter dans votre `pom.xml` la section `distributionManagement` pour le déploiement Nexus :

```xml
<distributionManagement>
  <repository>
    <id>nexus-releases</id>
    <url>http://your-nexus:8081/repository/maven-releases/</url>
  </repository>
  <snapshotRepository>
    <id>nexus-snapshots</id>
    <url>http://your-nexus:8081/repository/maven-snapshots/</url>
  </snapshotRepository>
</distributionManagement>
```

---

## 📋 Scrutation automatique du dépôt

### GitHub Actions
La scrutation est configurée via le trigger `schedule` dans `ci-cd.yml` :
```yaml
schedule:
  - cron: '*/15 * * * *'   # Toutes les 15 minutes
```

### Jenkins (Jenkinsfile)
Dans le Jenkinsfile, le trigger `pollSCM` est configuré :
```groovy
triggers {
    pollSCM('H/15 * * * *')   # Toutes les 15 minutes
}
```
Le `H` (Hash) évite que tous les jobs se déclenchent en même temps.

---

## 📧 Notifications Email

| Événement | Email envoyé | Contenu |
|-----------|-------------|---------|
| Échec des tests | ✅ Oui | Branche, commit, auteur, lien vers les logs |
| Échec du build | ✅ Oui | Idem + stage en échec |
| Échec du déploiement | ✅ Oui | Idem |
| Succès du déploiement | ✅ Oui | Version, artefact, URL Nexus |
| Retour en succès (Jenkins) | ✅ Oui | Notification "pipeline fixed" |

---

## 🏭 Gestion des Artefacts Nexus

| Version pom.xml | Dépôt Nexus cible |
|-----------------|-------------------|
| `1.0.0-SNAPSHOT` | `maven-snapshots` (écrasable) |
| `1.0.0` (release) | `maven-releases` (immuable) |

Le pipeline détecte automatiquement si la version est SNAPSHOT ou RELEASE et choisit le bon dépôt.

---

## 🚀 Déploiement conditionnel

Le déploiement vers Nexus ne s'exécute que sur les branches `main` et `develop` :
- `feature/**` → build + tests uniquement
- `hotfix/**` → build + tests uniquement  
- `develop` → build + tests + package + **déploiement SNAPSHOT**
- `main` → build + tests + package + **déploiement RELEASE**

---

## 🛠️ Configuration Jenkins

### Plugins requis
- **Pipeline** (Jenkinsfile support)
- **Git** (SCM)
- **Maven Integration**
- **Email Extension Plugin** (emailext)
- **Config File Provider** (settings.xml)
- **JUnit** (publication des rapports de tests)
- **Timestamper** (horodatage des logs)

### Configuration Maven dans Jenkins
`Manage Jenkins → Global Tool Configuration → Maven` :
- Nom : `Maven-3.9`
- Version : `3.9.x`

### Credentials Nexus dans Jenkins
`Manage Jenkins → Credentials → Add` :
- Type : **Username with password**
- ID : `nexus-credentials`

---

## 📊 Versionnement du pipeline

Ce pipeline est entièrement versionné dans le dépôt GitHub :
- `Jenkinsfile` — script Groovy du pipeline Jenkins
- `.github/workflows/ci-cd.yml` — workflow GitHub Actions

Tout changement au pipeline passe par une Pull Request et suit le même processus CI/CD.
