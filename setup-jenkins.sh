#!/bin/bash

# Script de configuration Jenkins pour le projet BeeTrack
# Ce script aide à configurer les outils nécessaires dans Jenkins

echo "========================================="
echo "Configuration Jenkins - Projet BeeTrack"
echo "========================================="

# Couleurs pour l'affichage
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Fonction pour afficher les messages
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Vérifier que Jenkins est actif
print_info "Vérification du statut de Jenkins..."
if systemctl is-active --quiet jenkins; then
    print_info "✅ Jenkins est actif"
else
    print_warning "Jenkins n'est pas actif. Démarrage..."
    sudo systemctl start jenkins
    sleep 10
fi

# Vérifier Docker
print_info "Vérification de Docker..."
if systemctl is-active --quiet docker; then
    print_info "✅ Docker est actif"
else
    print_warning "Docker n'est pas actif. Démarrage..."
    sudo systemctl start docker
fi

# Ajouter l'utilisateur jenkins au groupe docker
print_info "Configuration des permissions Docker pour Jenkins..."
sudo usermod -aG docker jenkins
sudo usermod -aG docker $USER

# Afficher les informations de connexion
echo ""
echo "========================================="
print_info "Informations de connexion Jenkins"
echo "========================================="
echo "URL Jenkins: http://localhost:8080"
echo ""

# Obtenir le mot de passe initial si c'est la première installation
if [ -f /var/lib/jenkins/secrets/initialAdminPassword ]; then
    print_info "Mot de passe administrateur initial Jenkins:"
    sudo cat /var/lib/jenkins/secrets/initialAdminPassword
    echo ""
fi

# Afficher les chemins importants pour la configuration
echo "========================================="
print_info "Configuration des outils dans Jenkins"
echo "========================================="
echo ""
echo "📍 Chemin Java (JDK 17):"
JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
echo "   $JAVA_HOME"
echo ""
echo "📍 Chemin Maven:"
MVN_HOME=$(dirname $(dirname $(which mvn)))
echo "   $MVN_HOME"
echo ""
echo "📍 Chemin Docker:"
which docker
echo ""

# Instructions de configuration
echo "========================================="
print_info "Étapes de configuration dans Jenkins UI"
echo "========================================="
echo ""
echo "1. Accédez à: Manage Jenkins → Tools"
echo ""
echo "2. Configurez JDK:"
echo "   - Nom: JDK17"
echo "   - Décochez 'Install automatically'"
echo "   - JAVA_HOME: $JAVA_HOME"
echo ""
echo "3. Configurez Maven:"
echo "   - Nom: Maven"
echo "   - Décochez 'Install automatically'"
echo "   - MAVEN_HOME: $MVN_HOME"
echo ""
echo "4. Installez les plugins nécessaires:"
echo "   - Docker Pipeline"
echo "   - Pipeline"
echo "   - Git plugin"
echo "   - JaCoCo plugin"
echo "   - JUnit plugin"
echo "   - Email Extension Plugin (optionnel)"
echo ""
echo "5. Créez un nouveau job de type 'Pipeline'"
echo "   - Source: SCM → Git"
echo "   - Repository: https://github.com/Mohamed-Aziz-Zarguelain/PFE-DEVOPS.git"
echo "   - Branch: */azizz"
echo "   - Script Path: Jenkinsfile"
echo ""
echo "========================================="
print_info "Configuration terminée!"
echo "========================================="
echo ""
print_warning "N'oubliez pas de redémarrer Jenkins après avoir ajouté l'utilisateur au groupe docker:"
echo "sudo systemctl restart jenkins"
echo ""
