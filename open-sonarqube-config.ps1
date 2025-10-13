# Script PowerShell pour ouvrir les URLs de configuration

Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "   ACTIVATION DE SONARQUBE POUR JENKINS" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""

# Vérifier que SonarQube est actif
Write-Host "[1/3] Vérification de SonarQube..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:9000/api/system/status" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ SonarQube est actif!" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ SonarQube n'est pas accessible sur le port 9000" -ForegroundColor Red
    Write-Host "Démarrez SonarQube avec: wsl sudo docker-compose -f docker-compose-sonar.yml up -d" -ForegroundColor Yellow
    exit 1
}

# Vérifier que Jenkins est actif
Write-Host "[2/3] Vérification de Jenkins..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080" -UseBasicParsing -TimeoutSec 5
    if ($response.StatusCode -eq 200) {
        Write-Host "✅ Jenkins est actif!" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Jenkins n'est pas accessible sur le port 8080" -ForegroundColor Red
    Write-Host "Démarrez Jenkins avec: wsl sudo systemctl start jenkins" -ForegroundColor Yellow
    exit 1
}

Write-Host "[3/3] Ouverture des URLs de configuration..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

# Ouvrir SonarQube
Write-Host "Ouverture de SonarQube..." -ForegroundColor Cyan
Start-Process "http://localhost:9000"
Start-Sleep -Seconds 3

# Ouvrir Jenkins
Write-Host "Ouverture de Jenkins..." -ForegroundColor Cyan
Start-Process "http://localhost:8080"

Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "   URLs OUVERTES" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "🔍 SonarQube: http://localhost:9000" -ForegroundColor Green
Write-Host "   Login: admin / admin (changez le mot de passe!)" -ForegroundColor White
Write-Host ""
Write-Host "🔧 Jenkins: http://localhost:8080" -ForegroundColor Green
Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "   PROCHAINES ÉTAPES" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Dans SonarQube:" -ForegroundColor Yellow
Write-Host "   - Changez le mot de passe admin" -ForegroundColor White
Write-Host "   - My Account → Security → Generate Token" -ForegroundColor White
Write-Host "   - Nom: jenkins-token" -ForegroundColor White
Write-Host "   - COPIEZ le token généré" -ForegroundColor White
Write-Host ""
Write-Host "2. Dans Jenkins:" -ForegroundColor Yellow
Write-Host "   - Manage Jenkins → Plugins → Install 'SonarQube Scanner'" -ForegroundColor White
Write-Host "   - Manage Jenkins → Credentials → Add le token SonarQube" -ForegroundColor White
Write-Host "   - Manage Jenkins → System → Configure SonarQube server" -ForegroundColor White
Write-Host "   - Manage Jenkins → Tools → Configure SonarQube Scanner" -ForegroundColor White
Write-Host ""
Write-Host "📖 Guide détaillé: SONARQUBE_QUICK_CONFIG.md" -ForegroundColor Cyan
Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
