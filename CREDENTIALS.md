# 🔐 Identifiants et Mots de Passe - Projet BeeTrack DevOps

## 📋 Services et Credentials

### 1. SonarQube
- **URL**: http://localhost:9000
- **Username**: `admin`
- **Password par défaut**: `admin`
- **Note**: Changez le mot de passe au premier login!

### 2. Jenkins
- **URL**: http://localhost:8080
- **Username**: Configuré lors de l'installation
- **Password initial**: Visible avec `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`

### 3. BeeTrack API
- **URL**: http://localhost:8087
- **Health Check**: http://localhost:8087/actuator/health
- **Prometheus Metrics**: http://localhost:8087/actuator/prometheus

### 4. Base de données MySQL (BeeTrack)
- **Host**: localhost
- **Port**: 3307
- **Database**: beetrackdb
- **Username**: root
- **Password**: (vide)

### 5. PostgreSQL (SonarQube)
- **Host**: localhost
- **Port**: 5432 (interne au conteneur)
- **Database**: sonar
- **Username**: sonar
- **Password**: sonar
- **Note**: Utilisé uniquement par SonarQube

---

## 🔄 Réinitialisation des mots de passe

### Réinitialiser SonarQube à admin/admin

```bash
# Méthode 1: Via la base de données PostgreSQL
wsl sudo docker exec -it sonarqube-db psql -U sonar -d sonar -c "UPDATE users SET crypted_password='\$2a\$12\$uCkkXmhW5ThVK8mpBvnXOOJRLd64LJeHTeCkSuB3lfaR2N0AYBaSi', salt=null, hash_method='BCRYPT' WHERE login='admin';"

# Redémarrer SonarQube
wsl sudo docker restart sonarqube

# Attendre 20 secondes puis se connecter avec admin/admin
```

### Obtenir le mot de passe initial Jenkins

```bash
wsl sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

### Réinitialiser mot de passe Jenkins

```bash
# Arrêter Jenkins
wsl sudo systemctl stop jenkins

# Modifier le fichier de configuration
wsl sudo nano /var/lib/jenkins/config.xml
# Cherchez <useSecurity>true</useSecurity> et changez en false

# Redémarrer Jenkins
wsl sudo systemctl start jenkins

# Reconnectez-vous sans mot de passe, puis:
# Manage Jenkins → Configure Global Security → Réactivez la sécurité
```

---

## 🔑 Tokens et Credentials Jenkins

### SonarQube Token (à créer)
1. Dans SonarQube: My Account → Security → Generate Token
2. **Name**: `jenkins-token`
3. **Type**: Global Analysis Token
4. **Copiez le token** (ex: `squ_a1b2c3d4e5f6...`)
5. Dans Jenkins: Credentials → Add Credentials
   - Kind: Secret text
   - Secret: [le token]
   - ID: `sonarqube-token`

### GitHub Token (si nécessaire)
Pour les webhooks ou l'intégration GitHub:
1. GitHub → Settings → Developer settings → Personal access tokens
2. Generate new token (classic)
3. Sélectionnez les scopes: `repo`, `admin:repo_hook`
4. Copiez le token
5. Dans Jenkins: Credentials → Add Credentials

---

## 📝 Bonnes pratiques

### Sécurité des mots de passe
- ❌ Ne jamais commiter les mots de passe dans Git
- ✅ Utilisez Jenkins Credentials pour les secrets
- ✅ Changez les mots de passe par défaut
- ✅ Utilisez des tokens pour les API

### Suggestions de mots de passe
- **SonarQube**: `admin123`, `sonar2025!`, `BeeTrack@Sonar`
- **Jenkins**: `jenkins2025!`, `DevOps@2025`
- **Format recommandé**: Min 8 caractères, majuscules, chiffres, symboles

---

## 🆘 En cas de problème

### Mot de passe SonarQube oublié
→ Utilisez la commande de réinitialisation ci-dessus

### Mot de passe Jenkins oublié
→ Désactivez temporairement la sécurité dans config.xml

### Token SonarQube expiré ou perdu
→ Générez un nouveau token dans SonarQube
→ Mettez à jour les credentials dans Jenkins

### Accès à la base de données PostgreSQL
```bash
# Se connecter au conteneur
wsl sudo docker exec -it sonarqube-db psql -U sonar -d sonar

# Lister les utilisateurs
SELECT login, active FROM users;

# Quitter
\q
```

---

## 📞 Contact et Support

Pour toute question sur les identifiants:
1. Consultez ce document: `CREDENTIALS.md`
2. Vérifiez les logs: `docker logs sonarqube`
3. Documentation SonarQube: https://docs.sonarqube.org

---

## 🔒 Sécurité

**⚠️ IMPORTANT:**
- Ce fichier contient des informations sensibles
- Ne le partagez pas publiquement
- Ajoutez-le à `.gitignore` si nécessaire
- Utilisez des gestionnaires de secrets en production

---

**Dernière mise à jour**: Octobre 2025  
**Projet**: BeeTrack DevOps Pipeline
