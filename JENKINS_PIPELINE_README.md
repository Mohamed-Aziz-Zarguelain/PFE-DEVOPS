# 🚀 Pipeline Jenkins - BeeTrack DevOps

## 📋 Vue d'ensemble

Ce pipeline Jenkins automatise le processus de build, test, et déploiement de l'application BeeTrack.

## 🔄 Étapes du Pipeline

### 1. **git** - Clonage du repository
- Clone la branche `azizz` depuis GitHub
- Durée moyenne: ~4s

### 2. **maven build** - Compilation
- Compile le code source avec Maven
- Commande: `mvn clean compile -DskipTests`
- Durée moyenne: ~14s

### 3. **testing with mockito** - Tests unitaires
- Exécute les tests avec JUnit et Mockito
- Génère les rapports de tests
- Durée moyenne: ~4s

### 4. **SonarQube Analysis** - Analyse de code
- Analyse la qualité du code (si SonarQube est configuré)
- Détecte les bugs, vulnérabilités et code smells
- **Note**: Échoue gracieusement si SonarQube n'est pas disponible
- Durée moyenne: ~7s

### 5. **ArtifactArk** - Packaging et archivage
- Package l'application en JAR
- Archive les artifacts
- Génère le rapport de couverture JaCoCo
- Durée moyenne: ~3s

### 6. **Building our image** - Build Docker
- Construit l'image Docker de l'application
- Tag avec le numéro de build et 'latest'
- Durée moyenne: ~15s

### 7. **Deploy our image** - Vérification de l'image
- Vérifie que l'image Docker est prête
- Durée moyenne: <1s

### 8. **Cleaning up** - Nettoyage
- Supprime les anciennes images Docker
- Nettoie les conteneurs arrêtés
- Durée moyenne: <1s

### 9. **Building and deploying using docker-compose** - Déploiement
- Arrête les anciens conteneurs
- Déploie l'application avec docker-compose
- Durée moyenne: ~3s

### 10. **Grafana: Prometheus** - Monitoring
- Vérifie les endpoints de métriques Prometheus
- Configure le monitoring
- Durée moyenne: ~231ms

## 🛠️ Prérequis

### Sur WSL Ubuntu:
```bash
# Java 17
sudo apt install openjdk-17-jdk -y

# Maven
sudo apt install maven -y

# Docker
sudo apt install docker.io docker-compose -y
sudo systemctl start docker
sudo usermod -aG docker jenkins

# Jenkins
# (voir documentation d'installation Jenkins)
```

## 📦 Configuration Jenkins

### 1. Outils à configurer (Manage Jenkins → Tools):

#### JDK
- **Nom**: `JDK17`
- **JAVA_HOME**: `/usr/lib/jvm/java-17-openjdk-amd64`

#### Maven
- **Nom**: `Maven`
- **Version**: Maven 3.9.x (auto-install) ou chemin local

### 2. Plugins requis:
- ✅ Pipeline
- ✅ Git plugin
- ✅ Docker Pipeline
- ✅ JaCoCo plugin
- ✅ JUnit plugin
- ⚠️ Email Extension (optionnel)
- ⚠️ SonarQube Scanner (optionnel)

### 3. Créer un nouveau Job:
1. New Item → Pipeline
2. **Definition**: Pipeline script from SCM
3. **SCM**: Git
4. **Repository URL**: `https://github.com/Mohamed-Aziz-Zarguelain/PFE-DEVOPS.git`
5. **Branch**: `*/azizz`
6. **Script Path**: `Jenkinsfile`

## 🚀 Lancer le Pipeline

### Via Jenkins UI:
1. Accédez à votre job
2. Cliquez sur "Build Now"
3. Suivez l'exécution dans "Console Output"

### Via script:
```bash
cd /mnt/c/Users/MSI/Desktop/NEWW/PFE-DEV
./setup-jenkins.sh
```

## 📊 Résultats et Rapports

Après un build réussi, vous trouverez:

- **Artifacts**: Fichier JAR dans `target/`
- **Rapport JaCoCo**: Couverture de code
- **Rapports JUnit**: Résultats des tests
- **Image Docker**: `devops2-bee-track:latest`

## 🌐 Endpoints de l'application

Après déploiement:
- **API**: http://localhost:8087
- **Health Check**: http://localhost:8087/actuator/health
- **Metrics Prometheus**: http://localhost:8087/actuator/prometheus
- **MySQL**: localhost:3307

## 🐛 Résolution des problèmes

### Problème: "Permission denied" avec Docker
```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

### Problème: Port 8080 déjà utilisé
```bash
sudo nano /etc/default/jenkins
# Modifier HTTP_PORT=8080 vers HTTP_PORT=9090
sudo systemctl restart jenkins
```

### Problème: Tests échouent
```bash
# Vérifier les logs
mvn test
# Voir les rapports dans target/surefire-reports/
```

### Problème: Docker Compose échoue
```bash
# Vérifier les conteneurs
docker ps -a
docker-compose logs

# Redémarrer
docker-compose down
docker-compose up -d
```

## 📝 Variables d'environnement

Le pipeline utilise:
- `DOCKER_IMAGE`: Nom de l'image Docker
- `DOCKER_TAG`: Tag de l'image (numéro de build)
- `SONAR_HOST_URL`: URL de SonarQube (si configuré)

## 🔄 Workflow complet

```
Git Clone → Maven Build → Tests → Quality Analysis →
Package → Docker Build → Verify → Cleanup →
Deploy → Monitoring Setup → Notifications
```

## ✅ Checklist de vérification

- [ ] Jenkins installé et démarré
- [ ] Java 17 configuré
- [ ] Maven configuré
- [ ] Docker et Docker Compose installés
- [ ] Utilisateur jenkins dans le groupe docker
- [ ] Tous les plugins installés
- [ ] Job Pipeline créé
- [ ] Repository Git accessible
- [ ] Ports 8080, 8087, 3307 disponibles

## 📧 Notifications

Le pipeline peut envoyer des emails:
- ✅ En cas de succès
- ❌ En cas d'échec
- ⚠️ En cas d'instabilité

**Configuration**: Modifiez l'email dans le Jenkinsfile à `to: 'votre-email@example.com'`

## 🔐 Sécurité

- Les credentials Git peuvent être configurés dans Jenkins
- Les secrets doivent être stockés dans Jenkins Credentials
- Ne jamais commiter les mots de passe dans le code

## 📚 Ressources

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Auteur**: DevOps Team  
**Projet**: BeeTrack Backend  
**Version**: 1.0  
**Dernière mise à jour**: Octobre 2025
