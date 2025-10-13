#!/bin/bash

# Script de réinitialisation du mot de passe SonarQube
# Réinitialise le compte admin à admin/admin

echo "================================================="
echo "  RÉINITIALISATION MOT DE PASSE SONARQUBE"
echo "================================================="
echo ""

# Vérifier que le conteneur existe
if ! docker ps -a | grep -q sonarqube-db; then
    echo "❌ Le conteneur sonarqube-db n'existe pas!"
    echo "Démarrez SonarQube avec: docker-compose -f docker-compose-sonar.yml up -d"
    exit 1
fi

# Vérifier que le conteneur est actif
if ! docker ps | grep -q sonarqube-db; then
    echo "❌ Le conteneur sonarqube-db n'est pas actif!"
    echo "Démarrez-le avec: docker start sonarqube-db"
    exit 1
fi

echo "🔧 Réinitialisation du mot de passe admin..."
echo ""

# Réinitialiser le mot de passe dans la base de données
docker exec -it sonarqube-db psql -U sonar -d sonar -c "UPDATE users SET crypted_password='\$2a\$12\$uCkkXmhW5ThVK8mpBvnXOOJRLd64LJeHTeCkSuB3lfaR2N0AYBaSi', salt=null, hash_method='BCRYPT' WHERE login='admin';"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Mot de passe réinitialisé avec succès!"
    echo ""
    echo "🔄 Redémarrage de SonarQube..."
    docker restart sonarqube
    echo ""
    echo "⏳ Attente du démarrage de SonarQube (30 secondes)..."
    sleep 30
    echo ""
    echo "================================================="
    echo "  RÉINITIALISATION TERMINÉE!"
    echo "================================================="
    echo ""
    echo "🌐 URL: http://localhost:9000"
    echo "👤 Username: admin"
    echo "🔑 Password: admin"
    echo ""
    echo "⚠️  Changez le mot de passe après le premier login!"
    echo ""
    echo "================================================="
else
    echo ""
    echo "❌ Erreur lors de la réinitialisation!"
    echo "Vérifiez que PostgreSQL fonctionne correctement."
    exit 1
fi
