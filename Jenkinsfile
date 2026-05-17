#!/usr/bin/env groovy

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║          Jenkinsfile — Pipeline CI/CD Java/Maven             ║
 * ║          Artefacts : Nexus Repository Manager                ║
 * ║          Notifications : Email (JavaMail)                    ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Ce Jenkinsfile décrit le pipeline CI/CD complet :
 *   1. Checkout du dépôt
 *   2. Compilation (mvn compile)
 *   3. Exécution des tests (mvn test + verify)
 *   4. Packaging (mvn package)
 *   5. Analyse qualité (optionnel)
 *   6. Déploiement vers Nexus (mvn deploy)
 *   7. Notifications email en cas d'échec ou de succès
 *
 * Scrutation automatique du dépôt : configurée dans Jenkins
 * via "Poll SCM" (ex: toutes les 15 min → cron H/15 * * * *)
 */

pipeline {

    // ── Agent ────────────────────────────────────────────────────
    agent any

    // ── Outils Maven/JDK (configurés dans Jenkins > Global Tools) ─
    tools {
        maven 'Maven-3.9'
        jdk   'JDK-17'
    }

    // ── Scrutation automatique du dépôt ──────────────────────────
    triggers {
        // Poll SCM toutes les 15 minutes
        pollSCM('H/15 * * * *')
        // Ou déclenchement webhook GitHub (recommandé en production)
        // githubPush()
    }

    // ── Variables d'environnement ─────────────────────────────────
    environment {
        // Credentials Nexus définis dans Jenkins Credentials Store
        NEXUS_CREDENTIALS   = credentials('nexus-credentials')
        NEXUS_URL           = 'http://your-nexus-server:8081'
        NEXUS_REPO_RELEASES  = 'maven-releases'
        NEXUS_REPO_SNAPSHOTS = 'maven-snapshots'

        // Email destinataire des notifications
        MAIL_RECIPIENT = 'equipe-dev@example.com'

        // Infos du build
        APP_VERSION = ''
        APP_NAME    = ''
    }

    // ── Options globales du pipeline ──────────────────────────────
    options {
        // Conserver les 10 derniers builds
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Timeout global du pipeline : 30 minutes
        timeout(time: 30, unit: 'MINUTES')
        // Horodater chaque ligne de log
        timestamps()
        // Ne pas lancer deux builds en parallèle sur la même branche
        disableConcurrentBuilds()
    }

    // ════════════════════════════════════════════════════════════
    //  STAGES
    // ════════════════════════════════════════════════════════════
    stages {

        // ── Stage 1 : Checkout ───────────────────────────────────
        stage('📥 Checkout') {
            steps {
                echo '==> Récupération du code source depuis GitHub...'
                checkout scm
                script {
                    // Extraire la version et l'artifactId depuis pom.xml
                    APP_VERSION = sh(
                        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    ).trim()
                    APP_NAME = sh(
                        script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout",
                        returnStdout: true
                    ).trim()
                    echo "Projet : ${APP_NAME} | Version : ${APP_VERSION}"

                    // Afficher les informations du commit
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def commitMsg  = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    echo "Commit : ${commitHash} — ${commitMsg}"
                }
            }
        }

        // ── Stage 2 : Compilation ────────────────────────────────
        stage('🔨 Compilation') {
            steps {
                echo '==> Compilation du projet Maven...'
                sh 'mvn clean compile -B --no-transfer-progress'
            }
            post {
                failure {
                    echo '❌ La compilation a échoué.'
                }
                success {
                    echo '✅ Compilation réussie.'
                }
            }
        }

        // ── Stage 3 : Tests unitaires ────────────────────────────
        stage('🧪 Tests Unitaires') {
            steps {
                echo '==> Exécution des tests unitaires (JUnit/Mockito)...'
                sh 'mvn test -B --no-transfer-progress'
            }
            post {
                always {
                    // Publier le rapport JUnit dans Jenkins
                    junit testResults: 'target/surefire-reports/*.xml',
                          allowEmptyResults: false
                }
                failure {
                    echo '❌ Des tests unitaires ont échoué.'
                }
            }
        }

        // ── Stage 4 : Tests d'intégration ───────────────────────
        stage('🔗 Tests d\'Intégration') {
            steps {
                echo '==> Exécution des tests d\'intégration (Failsafe)...'
                sh 'mvn verify -B --no-transfer-progress -Pintegration-tests'
            }
            post {
                always {
                    junit testResults: 'target/failsafe-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        // ── Stage 5 : Packaging ──────────────────────────────────
        stage('📦 Packaging') {
            steps {
                echo '==> Génération du JAR/WAR...'
                sh 'mvn package -DskipTests -B --no-transfer-progress'
            }
            post {
                success {
                    // Archiver le JAR dans Jenkins
                    archiveArtifacts artifacts: 'target/*.jar,target/*.war',
                                     fingerprint: true,
                                     allowEmptyArchive: false
                    echo "✅ Artefact ${APP_NAME}-${APP_VERSION} packagé et archivé."
                }
            }
        }

        // ── Stage 6 : Déploiement Nexus ──────────────────────────
        stage('🚀 Déploiement Nexus') {
            // Déploiement uniquement depuis main ou develop
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                }
            }
            steps {
                echo '==> Déploiement de l\'artefact vers Nexus Repository...'
                script {
                    def nexusRepo
                    def nexusRepoUrl

                    if (APP_VERSION.endsWith('-SNAPSHOT')) {
                        nexusRepo    = NEXUS_REPO_SNAPSHOTS
                        nexusRepoUrl = "${NEXUS_URL}/repository/${NEXUS_REPO_SNAPSHOTS}/"
                        echo "📌 Version SNAPSHOT → ${nexusRepo}"
                    } else {
                        nexusRepo    = NEXUS_REPO_RELEASES
                        nexusRepoUrl = "${NEXUS_URL}/repository/${NEXUS_REPO_RELEASES}/"
                        echo "📌 Version RELEASE → ${nexusRepo}"
                    }

                    // Configurer les credentials Nexus dans settings.xml
                    configFileProvider([
                        configFile(fileId: 'maven-settings-nexus', variable: 'MAVEN_SETTINGS')
                    ]) {
                        sh """
                            mvn deploy -DskipTests -B --no-transfer-progress \\
                                -s ${MAVEN_SETTINGS} \\
                                -DaltDeploymentRepository=${nexusRepo}::default::${nexusRepoUrl}
                        """
                    }
                    echo "✅ Artefact déployé avec succès sur Nexus : ${nexusRepoUrl}"
                }
            }
        }

    } // end stages

    // ════════════════════════════════════════════════════════════
    //  POST-BUILD ACTIONS & NOTIFICATIONS EMAIL
    // ════════════════════════════════════════════════════════════
    post {

        // Exécuté toujours (nettoyage)
        always {
            echo "Pipeline terminé avec le statut : ${currentBuild.currentResult}"
            cleanWs() // Nettoyage du workspace Jenkins
        }

        // ── Notification : SUCCÈS ────────────────────────────────
        success {
            echo '✅ Pipeline CI/CD exécuté avec succès !'
            emailext(
                subject: "✅ [CI SUCCESS] ${APP_NAME} v${APP_VERSION} — Build #${BUILD_NUMBER} réussi",
                to: "${MAIL_RECIPIENT}",
                replyTo: 'no-reply@example.com',
                body: """
                    <html><body style="font-family: Arial, sans-serif; color: #333;">
                    <h2 style="color: #2e7d32;">✅ Déploiement réussi</h2>
                    <table style="border-collapse:collapse; width:100%;">
                      <tr><td style="padding:6px; font-weight:bold;">Projet</td>
                          <td style="padding:6px;">${APP_NAME}</td></tr>
                      <tr style="background:#f5f5f5;">
                          <td style="padding:6px; font-weight:bold;">Version</td>
                          <td style="padding:6px;">${APP_VERSION}</td></tr>
                      <tr><td style="padding:6px; font-weight:bold;">Branche</td>
                          <td style="padding:6px;">${env.BRANCH_NAME}</td></tr>
                      <tr style="background:#f5f5f5;">
                          <td style="padding:6px; font-weight:bold;">Build #</td>
                          <td style="padding:6px;">${BUILD_NUMBER}</td></tr>
                      <tr><td style="padding:6px; font-weight:bold;">Durée</td>
                          <td style="padding:6px;">${currentBuild.durationString}</td></tr>
                    </table>
                    <p>L'artefact a été déployé avec succès sur Nexus Repository.</p>
                    <p><a href="${BUILD_URL}">🔗 Voir les logs complets</a></p>
                    </body></html>
                """,
                mimeType: 'text/html'
            )
        }

        // ── Notification : ÉCHEC ─────────────────────────────────
        failure {
            echo '❌ Le pipeline a échoué — envoi de la notification email...'
            emailext(
                subject: "❌ [CI FAILURE] ${APP_NAME} — Build #${BUILD_NUMBER} ÉCHOUÉ sur ${env.BRANCH_NAME}",
                to: "${MAIL_RECIPIENT}",
                replyTo: 'no-reply@example.com',
                attachLog: true, // Attacher les logs complets au mail
                compressLog: true,
                body: """
                    <html><body style="font-family: Arial, sans-serif; color: #333;">
                    <h2 style="color: #c62828;">❌ Échec du Pipeline CI/CD</h2>
                    <table style="border-collapse:collapse; width:100%;">
                      <tr><td style="padding:6px; font-weight:bold;">Projet</td>
                          <td style="padding:6px;">${APP_NAME}</td></tr>
                      <tr style="background:#f5f5f5;">
                          <td style="padding:6px; font-weight:bold;">Version</td>
                          <td style="padding:6px;">${APP_VERSION}</td></tr>
                      <tr><td style="padding:6px; font-weight:bold;">Branche</td>
                          <td style="padding:6px;">${env.BRANCH_NAME}</td></tr>
                      <tr style="background:#f5f5f5;">
                          <td style="padding:6px; font-weight:bold;">Build #</td>
                          <td style="padding:6px;">${BUILD_NUMBER}</td></tr>
                      <tr><td style="padding:6px; font-weight:bold;">Stage en échec</td>
                          <td style="padding:6px; color:#c62828;">${currentBuild.description ?: 'Voir les logs'}</td></tr>
                    </table>
                    <p>⚠️ Les logs complets sont attachés à cet email.</p>
                    <p><a href="${BUILD_URL}">🔗 Voir les logs Jenkins</a></p>
                    <p><a href="${BUILD_URL}console">🔗 Console Output</a></p>
                    </body></html>
                """,
                mimeType: 'text/html'
            )
        }

        // ── Notification : INSTABLE (tests partiellement échoués) ─
        unstable {
            echo '⚠️ Pipeline instable (tests échoués).'
            emailext(
                subject: "⚠️ [CI UNSTABLE] ${APP_NAME} — Build #${BUILD_NUMBER} instable",
                to: "${MAIL_RECIPIENT}",
                body: """
                    ⚠️ Le build est instable — certains tests ont échoué.

                    Projet  : ${APP_NAME} v${APP_VERSION}
                    Branche : ${env.BRANCH_NAME}
                    Build # : ${BUILD_NUMBER}

                    🔗 ${BUILD_URL}
                """,
                attachLog: false
            )
        }

        // ── Notification : RETOUR EN SUCCÈS après échec ──────────
        fixed {
            echo '🟢 Le pipeline est de nouveau vert !'
            emailext(
                subject: "🟢 [CI FIXED] ${APP_NAME} — Build #${BUILD_NUMBER} de nouveau réussi !",
                to: "${MAIL_RECIPIENT}",
                body: """
                    🟢 Le pipeline est de nouveau stable.

                    Projet  : ${APP_NAME} v${APP_VERSION}
                    Branche : ${env.BRANCH_NAME}
                    Build # : ${BUILD_NUMBER}

                    🔗 ${BUILD_URL}
                """
            )
        }

    } // end post

} // end pipeline
