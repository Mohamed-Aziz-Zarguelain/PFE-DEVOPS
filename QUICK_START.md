# 🎯 Guide Rapide - Configuration Jenkins

## ✅ Fichiers créés et poussés sur GitHub

1. ✅ **Jenkinsfile** - Pipeline complet avec 10 étapes
2. ✅ **sonar-project.properties** - Configuration SonarQube
3. ✅ **setup-jenkins.sh** - Script d'aide à la configuration
4. ✅ **JENKINS_PIPELINE_README.md** - Documentation complète

## 🚀 Étapes pour terminer le pipeline

### 1. Configurer les outils dans Jenkins

Accédez à Jenkins: **http://localhost:8080**

#### A. Configurer JDK
1. Allez à: **Manage Jenkins** → **Tools** (ou **Global Tool Configuration**)
2. Scrollez jusqu'à **JDK installations**
3. Cliquez sur **Add JDK**
4. Configuration:
   - ✅ Nom: `JDK17`
   - ❌ Décochez **"Install automatically"**
   - ✅ JAVA_HOME: `/usr/lib/jvm/java-17-openjdk-amd64`
5. Cliquez sur **Save**

#### B. Configurer Maven
1. Dans la même page **Tools**
2. Scrollez jusqu'à **Maven installations**
3. Cliquez sur **Add Maven**
4. Configuration:
   - ✅ Nom: `Maven`
   - Option 1 (Recommandé): Cochez **"Install automatically"** et choisissez version 3.9.x
   - Option 2: Décochez et mettez MAVEN_HOME: `/usr/share/maven`
5. Cliquez sur **Save**

### 2. Créer/Mettre à jour le Job Pipeline

#### Si le job "devops" existe déjà:
1. Allez dans le job **devops**
2. Cliquez sur **Configure**
3. Dans la section **Pipeline**:
   - **Definition**: Pipeline script from SCM
   - **SCM**: Git
   - **Repository URL**: `https://github.com/Mohamed-Aziz-Zarguelain/PFE-DEVOPS.git`
   - **Branch Specifier**: `*/azizz`
   - **Script Path**: `Jenkinsfile`
4. Cliquez sur **Save**

#### Si le job n'existe pas:
1. Cliquez sur **New Item**
2. Nom: `devops` (ou `BeeTrack-Pipeline`)
3. Type: **Pipeline**
4. Cliquez sur **OK**
5. Suivez les mêmes configurations que ci-dessus
6. Cliquez sur **Save**

### 3. Vérifier les permissions Docker

```bash
# Dans WSL
wsl sudo usermod -aG docker jenkins
wsl sudo systemctl restart jenkins
```

Attendez 30 secondes que Jenkins redémarre.

### 4. Lancer le pipeline

1. Retournez sur le job **devops**
2. Cliquez sur **Build Now**
3. Cliquez sur le numéro du build (ex: #15)
4. Cliquez sur **Console Output** pour suivre l'exécution

## 🎨 Voir la vue des stages

1. Dans votre job, cliquez sur un build
2. Vous verrez la **Stage View** avec les 10 étapes
3. Cliquez sur chaque étape pour voir les logs détaillés

## 📊 Ce que le pipeline fait maintenant

### ✅ Corrections apportées:

1. **SonarQube Analysis** - Gère gracieusement l'absence de SonarQube
2. **ArtifactArk** - Archive correctement les JARs et génère JaCoCo
3. **Building our image** - Build Docker sans erreurs
4. **Deploy our image** - Vérifie l'image avant déploiement
5. **Cleaning up** - Nettoie les anciennes ressources Docker
6. **docker-compose** - Déploie l'application correctement
7. **Grafana/Prometheus** - Vérifie les endpoints de métriques
8. **Post Actions** - Notifications et rapports

## 🔍 Résolution des problèmes courants

### Problème: "Failed to mount Z:\"
- ⚠️ Avertissement bénin, ignore-le

### Problème: SonarQube échoue
- ✅ Normal si SonarQube n'est pas installé
- Le pipeline continue quand même

### Problème: Docker permission denied
```bash
wsl sudo usermod -aG docker jenkins
wsl sudo systemctl restart jenkins
```

### Problème: "Tool type JDK not found"
- Retournez dans **Manage Jenkins → Tools**
- Vérifiez que le nom est exactement `JDK17`
- Vérifiez que le nom Maven est exactement `Maven`

## 📈 Résultats attendus

Après un build réussi:
- ✅ Code compilé
- ✅ Tests exécutés (rapports JUnit disponibles)
- ✅ Couverture de code JaCoCo générée
- ✅ Image Docker construite
- ✅ Application déployée sur http://localhost:8087
- ✅ Base de données MySQL sur port 3307

## 🌐 URLs importantes

- **Jenkins**: http://localhost:8080
- **Application**: http://localhost:8087
- **Health Check**: http://localhost:8087/actuator/health
- **Metrics**: http://localhost:8087/actuator/prometheus

## 🎯 Commandes utiles

```bash
# Voir le statut de Jenkins
wsl sudo systemctl status jenkins

# Voir le statut des conteneurs
wsl sudo docker ps

# Voir les logs de l'application
wsl sudo docker logs devops2-bee-track-1 -f

# Redémarrer Jenkins si nécessaire
wsl sudo systemctl restart jenkins
```

## ✅ Checklist finale

- [ ] Jenkins accessible sur http://localhost:8080
- [ ] JDK17 configuré dans Jenkins Tools
- [ ] Maven configuré dans Jenkins Tools
- [ ] Job créé ou mis à jour avec le Jenkinsfile
- [ ] Permissions Docker configurées pour jenkins
- [ ] Build lancé avec "Build Now"
- [ ] Stage View montre 10 étapes en vert
- [ ] Application accessible sur http://localhost:8087

---

**Prochaine étape**: Cliquez sur "Build Now" et observez votre pipeline s'exécuter ! 🚀
