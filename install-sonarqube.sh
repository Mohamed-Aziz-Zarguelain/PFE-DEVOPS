#!/bin/bash

# Script d'installation et configuration de SonarQube pour Jenkins
# Projet BeeTrack

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

echo "================================================="
echo "   INSTALLATION DE SONARQUBE POUR BEETRACK"
echo "================================================="
echo ""

# Étape 1: Vérifier Docker
print_step "1/6 - Vérification de Docker..."
if ! systemctl is-active --quiet docker; then
    print_warning "Docker n'est pas actif. Démarrage..."
    sudo systemctl start docker
    sleep 3
fi
print_info "✅ Docker est actif"

# Étape 2: Augmenter les limites système pour SonarQube
print_step "2/6 - Configuration des limites système..."
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536
print_info "✅ Limites système configurées"

# Étape 3: Arrêter et nettoyer les anciens conteneurs SonarQube (si existants)
print_step "3/6 - Nettoyage des anciens conteneurs..."
if [ "$(docker ps -aq -f name=sonarqube)" ]; then
    print_info "Arrêt des anciens conteneurs SonarQube..."
    docker-compose -f docker-compose-sonar.yml down || true
    docker rm -f sonarqube sonarqube-db 2>/dev/null || true
fi
print_info "✅ Nettoyage terminé"

# Étape 4: Démarrer SonarQube avec Docker Compose
print_step "4/6 - Démarrage de SonarQube..."
docker-compose -f docker-compose-sonar.yml up -d
print_info "✅ SonarQube démarré"

# Étape 5: Attendre que SonarQube soit prêt
print_step "5/6 - Attente du démarrage de SonarQube (peut prendre 2-3 minutes)..."
echo "Cela peut prendre un moment, soyez patient..."

max_attempts=60
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:9000/api/system/status | grep -q '"status":"UP"'; then
        print_info "✅ SonarQube est prêt!"
        break
    fi
    attempt=$((attempt + 1))
    echo -n "."
    sleep 5
done
echo ""

if [ $attempt -eq $max_attempts ]; then
    print_error "SonarQube n'a pas démarré dans le temps imparti"
    print_info "Vérifiez les logs avec: docker logs sonarqube"
    exit 1
fi

# Étape 6: Configuration initiale
print_step "6/6 - Configuration de SonarQube..."

echo ""
echo "================================================="
print_info "SonarQube installé avec succès!"
echo "================================================="
echo ""
echo "📍 URL SonarQube: http://localhost:9000"
echo "👤 Login par défaut:"
echo "   Username: admin"
echo "   Password: admin"
echo ""
echo "⚠️  IMPORTANT: Au premier login, vous devrez changer le mot de passe!"
echo ""
echo "================================================="
echo "PROCHAINES ÉTAPES:"
echo "================================================="
echo ""
echo "1. Accédez à http://localhost:9000"
echo "2. Connectez-vous avec admin/admin"
echo "3. Changez le mot de passe (ex: admin123)"
echo "4. Créez un token pour Jenkins:"
echo "   - Cliquez sur votre profil → My Account → Security"
echo "   - Generate Token"
echo "   - Nom: jenkins-token"
echo "   - Type: Global Analysis Token"
echo "   - Copiez le token généré"
echo ""
echo "5. Dans Jenkins:"
echo "   - Manage Jenkins → Credentials → System → Global credentials"
echo "   - Add Credentials"
echo "   - Kind: Secret text"
echo "   - Secret: [Collez le token SonarQube]"
echo "   - ID: sonarqube-token"
echo ""
echo "6. Configurez SonarQube Server dans Jenkins:"
echo "   - Manage Jenkins → Configure System"
echo "   - Section 'SonarQube servers'"
echo "   - Add SonarQube"
echo "   - Name: SonarQube"
echo "   - Server URL: http://localhost:9000"
echo "   - Server authentication token: [Sélectionnez sonarqube-token]"
echo ""
echo "================================================="
echo ""
print_info "Services en cours d'exécution:"
docker-compose -f docker-compose-sonar.yml ps
echo ""
print_info "Pour voir les logs SonarQube:"
echo "docker logs -f sonarqube"
echo ""
