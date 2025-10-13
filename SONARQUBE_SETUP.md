# 🔍 Guide d'activation de SonarQube

## 📋 Vue d'ensemble

SonarQube va analyser la qualité de votre code et détecter:
- 🐛 Bugs
- 🔒 Vulnérabilités de sécurité
- 💡 Code smells
- 🔄 Code dupliqué
- 📊 Couverture de code

---

## 🚀 Installation rapide

### Option 1: Script automatique (Recommandé)

Dans votre terminal WSL:

```bash
cd /mnt/c/Users/MSI/Desktop/NEWW/PFE-DEV
chmod +x install-sonarqube.sh
sudo ./install-sonarqube.sh
```

Le script va:
1. ✅ Configurer les limites système
2. ✅ Démarrer SonarQube avec Docker
3. ✅ Démarrer PostgreSQL pour SonarQube
4. ✅ Attendre que SonarQube soit prêt
5. ✅ Afficher les instructions de configuration

### Option 2: Installation manuelle

```bash
cd /mnt/c/Users/MSI/Desktop/NEWW/PFE-DEV

# Configurer les limites système
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536

# Démarrer SonarQube
docker-compose -f docker-compose-sonar.yml up -d

# Attendre le démarrage (2-3 minutes)
docker logs -f sonarqube
```

---

## 🔐 Configuration initiale de SonarQube

### 1. Accéder à SonarQube

Ouvrez votre navigateur: **http://localhost:9000**

**Credentials par défaut:**
- Username: `admin`
- Password: `admin`

### 2. Changer le mot de passe

Au premier login, SonarQube vous demandera de changer le mot de passe.

**Nouveau mot de passe suggéré:** `admin123`
(ou choisissez le vôtre)

### 3. Créer un projet

1. Cliquez sur **"Create Project"** → **"Manually"**
2. Remplissez:
   - **Project key**: `beetrackapp`
   - **Display name**: `BeeTrack Backend`
3. Cliquez sur **"Set Up"**

### 4. Créer un Token d'authentification

1. Cliquez sur votre **profil** (coin supérieur droit)
2. **My Account** → **Security**
3. **Generate Token**:
   - **Name**: `jenkins-token`
   - **Type**: `Global Analysis Token`
   - **Expires in**: `No expiration`
4. Cliquez sur **"Generate"**
5. **⚠️ IMPORTANT:** Copiez le token (vous ne pourrez plus le voir!)

**Exemple de token:**
```
squ_a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8
```

---

## 🔧 Configuration de Jenkins avec SonarQube

### Étape 1: Installer le plugin SonarQube Scanner

1. Dans Jenkins: **Manage Jenkins** → **Plugins**
2. Onglet **"Available plugins"**
3. Recherchez: `SonarQube Scanner`
4. Cochez et cliquez sur **"Install"**
5. Redémarrez Jenkins si demandé

### Étape 2: Ajouter les credentials SonarQube dans Jenkins

1. **Manage Jenkins** → **Credentials**
2. Cliquez sur **"System"** → **"Global credentials (unrestricted)"**
3. Cliquez sur **"Add Credentials"**
4. Remplissez:
   - **Kind**: `Secret text`
   - **Scope**: `Global`
   - **Secret**: `[Collez votre token SonarQube]`
   - **ID**: `sonarqube-token`
   - **Description**: `SonarQube Authentication Token`
5. Cliquez sur **"Create"**

### Étape 3: Configurer le serveur SonarQube dans Jenkins

1. **Manage Jenkins** → **System** (ou **Configure System**)
2. Scrollez jusqu'à la section **"SonarQube servers"**
3. Cochez **"Environment variables"** → **"Enable injection of SonarQube server configuration..."**
4. Cliquez sur **"Add SonarQube"**
5. Remplissez:
   - **Name**: `SonarQube`
   - **Server URL**: `http://localhost:9000`
   - **Server authentication token**: Sélectionnez `sonarqube-token`
6. Cliquez sur **"Save"**

### Étape 4: Configurer SonarQube Scanner dans Tools

1. **Manage Jenkins** → **Tools** (ou **Global Tool Configuration**)
2. Scrollez jusqu'à **"SonarQube Scanner installations"**
3. Cliquez sur **"Add SonarQube Scanner"**
4. Remplissez:
   - **Name**: `SonarQubeScanner`
   - ✅ Cochez **"Install automatically"**
   - **Version**: Choisissez la dernière version (ex: 6.2.1.4610)
5. Cliquez sur **"Save"**

---

## ✅ Vérification de la configuration

### Test rapide dans SonarQube

1. Accédez à http://localhost:9000
2. Vous devriez voir votre projet **"BeeTrack Backend"**
3. Vérifiez que le statut est **"Ready"**

### Test dans Jenkins

1. Allez dans votre job **"devops"**
2. Cliquez sur **"Build Now"**
3. Le stage **"SonarQube Analysis"** devrait maintenant passer ✅
4. Après le build, vous verrez l'analyse dans SonarQube

---

## 📊 Voir les résultats d'analyse

Après un build Jenkins:

1. Accédez à http://localhost:9000
2. Cliquez sur votre projet **"BeeTrack Backend"**
3. Vous verrez:
   - 🐛 **Bugs** détectés
   - 🔒 **Vulnerabilities**
   - 💡 **Code Smells**
   - 📊 **Coverage** (couverture de code)
   - 🔄 **Duplications**
   - 📈 **Ratings** (A, B, C, D, E)

---

## 🔍 Commandes utiles

```bash
# Voir les logs SonarQube
docker logs -f sonarqube

# Redémarrer SonarQube
docker-compose -f docker-compose-sonar.yml restart

# Arrêter SonarQube
docker-compose -f docker-compose-sonar.yml down

# Voir le statut des conteneurs
docker-compose -f docker-compose-sonar.yml ps

# Vérifier que SonarQube répond
curl http://localhost:9000/api/system/status

# Analyser manuellement depuis la ligne de commande
mvn sonar:sonar \
  -Dsonar.projectKey=beetrackapp \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=VOTRE_TOKEN
```

---

## 🐛 Résolution de problèmes

### Problème: SonarQube ne démarre pas

```bash
# Vérifier les logs
docker logs sonarqube

# Augmenter les limites système (requis!)
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536

# Redémarrer
docker-compose -f docker-compose-sonar.yml restart
```

### Problème: "max virtual memory areas too low"

```bash
# Solution permanente
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf
sudo sysctl -p
```

### Problème: Port 9000 déjà utilisé

```bash
# Voir qui utilise le port
sudo lsof -i :9000

# Modifier le port dans docker-compose-sonar.yml
# Changez "9000:9000" en "9001:9000"
```

### Problème: Jenkins ne peut pas se connecter à SonarQube

1. Vérifiez que SonarQube est accessible: http://localhost:9000
2. Vérifiez le token dans Jenkins Credentials
3. Vérifiez l'URL du serveur dans Configure System
4. Testez la connexion dans Jenkins (bouton "Check connection")

---

## 📝 Configuration avancée (Optionnel)

### Quality Gates personnalisées

1. Dans SonarQube: **Quality Gates**
2. Créez une nouvelle gate ou modifiez la default
3. Ajoutez des conditions:
   - Coverage > 80%
   - Bugs = 0
   - Vulnerabilities = 0
   - Code Smells < 10

### Webhooks pour Jenkins

1. Dans SonarQube: **Administration** → **Webhooks**
2. **Create**:
   - **Name**: `Jenkins`
   - **URL**: `http://host.docker.internal:8080/sonarqube-webhook/`
3. Save

---

## 🎯 URLs et Ports

| Service | URL | Port |
|---------|-----|------|
| SonarQube Web | http://localhost:9000 | 9000 |
| Jenkins | http://localhost:8080 | 8080 |
| BeeTrack API | http://localhost:8087 | 8087 |
| PostgreSQL (SonarQube) | localhost | 5432 |

---

## ✅ Checklist de configuration

- [ ] Docker actif
- [ ] SonarQube démarré avec `install-sonarqube.sh`
- [ ] SonarQube accessible sur http://localhost:9000
- [ ] Mot de passe admin changé
- [ ] Projet "beetrackapp" créé dans SonarQube
- [ ] Token généré et copié
- [ ] Plugin SonarQube Scanner installé dans Jenkins
- [ ] Credentials ajoutées dans Jenkins
- [ ] Serveur SonarQube configuré dans Jenkins
- [ ] SonarQube Scanner configuré dans Tools
- [ ] Build Jenkins lancé avec succès
- [ ] Résultats visibles dans SonarQube

---

## 🎊 Succès!

Après configuration complète:
- ✅ SonarQube analyse votre code automatiquement
- ✅ Chaque build Jenkins envoie les résultats à SonarQube
- ✅ Vous pouvez suivre l'évolution de la qualité du code
- ✅ Les rapports sont disponibles dans SonarQube

---

**Prêt à activer SonarQube?** 🚀

Lancez: `sudo ./install-sonarqube.sh`
