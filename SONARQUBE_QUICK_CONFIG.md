# ⚡ Configuration Rapide SonarQube + Jenkins

## ✅ État actuel

- ✅ SonarQube installé et actif (port 9000)
- ✅ PostgreSQL actif
- ✅ Configuration SonarQube ajoutée au pom.xml
- ✅ Fichiers poussés sur GitHub

---

## 🚀 ÉTAPES À SUIVRE MAINTENANT

### Étape 1: Accéder à SonarQube (2 minutes)

1. Ouvrez: **http://localhost:9000**
2. Login avec:
   - Username: `admin`
   - Password: `admin`
3. **Changez le mot de passe** (ex: `admin123`)

### Étape 2: Créer un Token (2 minutes)

1. Cliquez sur votre **profil** (A en haut à droite)
2. **My Account** → **Security** (ou Security tab)
3. **Generate Token**:
   - Name: `jenkins-token`
   - Type: `Global Analysis Token` 
   - Expires: `No expiration`
4. Cliquez sur **Generate**
5. **⚠️ COPIEZ LE TOKEN** (exemple: `squ_a1b2c3d4...`)

### Étape 3: Installer le plugin SonarQube dans Jenkins (3 minutes)

1. Ouvrez: **http://localhost:8080**
2. **Manage Jenkins** → **Plugins**
3. Onglet **Available plugins**
4. Recherchez: `SonarQube Scanner`
5. Cochez et **Install**
6. ✅ Redémarrez Jenkins si demandé

### Étape 4: Ajouter les Credentials dans Jenkins (2 minutes)

1. **Manage Jenkins** → **Credentials**
2. **System** → **Global credentials (unrestricted)**
3. **Add Credentials**:
   - Kind: `Secret text`
   - Secret: `[COLLEZ VOTRE TOKEN SONARQUBE]`
   - ID: `sonarqube-token`
   - Description: `SonarQube Token`
4. **Create**

### Étape 5: Configurer le serveur SonarQube (3 minutes)

1. **Manage Jenkins** → **System** (Configure System)
2. Scrollez jusqu'à **SonarQube servers**
3. Cochez: ✅ **Environment variables** (Enable injection...)
4. **Add SonarQube**:
   - Name: `SonarQube`
   - Server URL: `http://localhost:9000`
   - Server authentication token: Sélectionnez `sonarqube-token`
5. **Save**

### Étape 6: Configurer SonarQube Scanner dans Tools (2 minutes)

1. **Manage Jenkins** → **Tools**
2. Scrollez jusqu'à **SonarQube Scanner installations**
3. **Add SonarQube Scanner**:
   - Name: `SonarQubeScanner`
   - ✅ Install automatically
   - Version: Dernière (ex: 6.2.1.4610)
4. **Save**

### Étape 7: Tester le Pipeline (1 minute)

1. Allez dans le job **devops**
2. **Build Now** 🚀
3. Regardez le stage **SonarQube Analysis** passer en VERT ✅

---

## 📊 Voir les résultats

Après le build:

1. Retournez sur **http://localhost:9000**
2. Vous devriez voir le projet **beetrackapp**
3. Cliquez dessus pour voir:
   - 🐛 Bugs
   - 🔒 Vulnerabilities
   - 💡 Code Smells
   - 📊 Coverage
   - 📈 Quality Gate

---

## ⚡ Commandes de vérification rapide

```bash
# Vérifier SonarQube
wsl curl http://localhost:9000/api/system/status

# Voir les logs SonarQube
wsl sudo docker logs sonarqube --tail 50

# Redémarrer SonarQube si besoin
wsl sudo docker-compose -f docker-compose-sonar.yml restart

# Analyse manuelle (test)
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=beetrackapp \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=VOTRE_TOKEN
```

---

## 🎯 URLs importantes

| Service | URL |
|---------|-----|
| SonarQube | http://localhost:9000 |
| Jenkins | http://localhost:8080 |
| BeeTrack API | http://localhost:8087 |

---

## ✅ Checklist finale

- [ ] SonarQube accessible sur port 9000
- [ ] Mot de passe admin changé
- [ ] Token créé et copié
- [ ] Plugin SonarQube Scanner installé dans Jenkins
- [ ] Credentials ajoutées dans Jenkins
- [ ] Serveur SonarQube configuré dans System
- [ ] SonarQube Scanner configuré dans Tools
- [ ] Build Jenkins lancé
- [ ] Stage "SonarQube Analysis" en VERT ✅
- [ ] Résultats visibles dans SonarQube

---

## 🐛 Problèmes courants

### Token SonarQube invalide
→ Générez un nouveau token dans SonarQube → My Account → Security

### Jenkins ne peut pas se connecter
→ Vérifiez que l'URL est `http://localhost:9000` (pas https)

### Plugin SonarQube Scanner non trouvé
→ Redémarrez Jenkins après l'installation du plugin

### Erreur "Tool type 'hudson.plugins.sonar.SonarRunnerInstallation'"
→ Vérifiez que le nom est exactement `SonarQubeScanner` dans Tools

---

## 🎊 Temps total: ~15 minutes

Après configuration, chaque build Jenkins analysera automatiquement votre code! 🚀

---

**Prochaine action:** Ouvrez http://localhost:9000 et suivez les étapes!
