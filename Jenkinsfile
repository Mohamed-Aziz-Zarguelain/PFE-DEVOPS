pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    
    environment {
        DOCKER_IMAGE = 'devops2-bee-track'
        DOCKER_TAG = "${BUILD_NUMBER}"
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }
    
    stages {
        stage('git') {
            steps {
                echo '========== Cloning Git Repository =========='
                git branch: 'azizz', 
                    url: 'https://github.com/Mohamed-Aziz-Zarguelain/PFE-DEVOPS.git'
                echo 'Git clone completed successfully!'
            }
        }
        
        stage('maven build') {
            steps {
                echo '========== Building with Maven =========='
                sh 'mvn clean compile -DskipTests'
                echo 'Maven build completed!'
            }
        }
        
        stage('testing with mockito') {
            steps {
                echo '========== Running Unit Tests =========='
                sh 'mvn test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo '========== Running SonarQube Analysis =========='
                script {
                    try {
                        // Vérifier si SonarQube est disponible
                        def sonarAvailable = sh(script: "curl -s -o /dev/null -w '%{http_code}' ${SONAR_HOST_URL}", returnStdout: true).trim()
                        
                        if (sonarAvailable == '200' || sonarAvailable == '401') {
                            withSonarQubeEnv('SonarQube') {
                                sh '''
                                    mvn sonar:sonar \
                                    -Dsonar.projectKey=beetrackapp \
                                    -Dsonar.projectName=BeeTrack \
                                    -Dsonar.host.url=${SONAR_HOST_URL} \
                                    -Dsonar.java.binaries=target/classes
                                '''
                            }
                        } else {
                            echo "⚠️ SonarQube n'est pas disponible, analyse ignorée"
                        }
                    } catch (Exception e) {
                        echo "⚠️ SonarQube analysis failed: ${e.message}"
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }
        
        stage('ArtifactArk') {
            steps {
                echo '========== Packaging Application =========='
                sh 'mvn package -DskipTests'
                
                echo '========== Archiving Artifacts =========='
                archiveArtifacts artifacts: '**/target/*.jar', 
                                fingerprint: true,
                                allowEmptyArchive: false
                
                // Générer le rapport JaCoCo
                sh 'mvn jacoco:report'
                
                // Publier le rapport JaCoCo
                jacoco(
                    execPattern: '**/target/jacoco.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java',
                    exclusionPattern: '**/test/**'
                )
            }
        }
        
        stage('Building our image') {
            steps {
                echo '========== Building Docker Image =========='
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                    echo "✅ Docker image built: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
        
        stage('Deploy our image') {
            steps {
                echo '========== Deploying Docker Image =========='
                script {
                    // Vérifier si l'image existe
                    def imageExists = sh(
                        script: "docker images -q ${DOCKER_IMAGE}:latest",
                        returnStdout: true
                    ).trim()
                    
                    if (imageExists) {
                        echo "✅ Image ready for deployment: ${DOCKER_IMAGE}:latest"
                    } else {
                        error "❌ Docker image not found!"
                    }
                }
            }
        }
        
        stage('Cleaning up') {
            steps {
                echo '========== Cleaning Old Docker Resources =========='
                script {
                    // Nettoyer les anciennes images (garder les 3 dernières)
                    sh '''
                        echo "Cleaning up old images..."
                        docker images ${DOCKER_IMAGE} --format "{{.ID}} {{.Tag}}" | \
                        grep -v "latest" | tail -n +4 | awk '{print $1}' | \
                        xargs -r docker rmi -f || true
                    '''
                    
                    // Nettoyer les conteneurs arrêtés
                    sh 'docker container prune -f || true'
                    
                    echo '✅ Cleanup completed!'
                }
            }
        }
        
        stage('Building and deploying using docker-compose') {
            steps {
                echo '========== Deploying with Docker Compose =========='
                script {
                    // Arrêter les anciens conteneurs
                    sh 'docker-compose down || true'
                    
                    // Attendre un peu pour s'assurer que tout est arrêté
                    sleep(time: 5, unit: 'SECONDS')
                    
                    // Démarrer avec docker-compose
                    sh 'docker-compose up -d --build'
                    
                    echo '✅ Application deployed successfully!'
                }
            }
        }
        
        stage('Grafana: Prometheus') {
            steps {
                echo '========== Monitoring Configuration =========='
                script {
                    // Vérifier que l'application expose les métriques Prometheus
                    sleep(time: 15, unit: 'SECONDS')
                    
                    def metricsAvailable = sh(
                        script: 'curl -s http://localhost:8087/actuator/prometheus | head -n 5',
                        returnStdout: true
                    ).trim()
                    
                    if (metricsAvailable) {
                        echo '✅ Prometheus metrics endpoint is available'
                        echo "Metrics preview:\n${metricsAvailable}"
                    } else {
                        echo '⚠️ Warning: Prometheus metrics endpoint not responding'
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo '========================================='
            echo '✅ Pipeline completed successfully!'
            echo '========================================='
            echo "🚀 Application URL: http://localhost:8087"
            echo "📊 Health Check: http://localhost:8087/actuator/health"
            echo "📈 Metrics: http://localhost:8087/actuator/prometheus"
            echo '========================================='
            
            // Notification par email (optionnel)
            script {
                try {
                    emailext (
                        subject: "✅ SUCCESS: Pipeline '${env.JOB_NAME}' [${env.BUILD_NUMBER}]",
                        body: """
                            <h2>Build Success</h2>
                            <p>The pipeline completed successfully!</p>
                            <p><b>Job:</b> ${env.JOB_NAME}</p>
                            <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                            <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            <p><b>Application:</b> <a href="http://localhost:8087">http://localhost:8087</a></p>
                        """,
                        to: 'your-email@example.com',
                        mimeType: 'text/html'
                    )
                } catch (Exception e) {
                    echo "Email notification skipped: ${e.message}"
                }
            }
        }
        
        failure {
            echo '========================================='
            echo '❌ Pipeline failed!'
            echo '========================================='
            
            script {
                try {
                    emailext (
                        subject: "❌ FAILED: Pipeline '${env.JOB_NAME}' [${env.BUILD_NUMBER}]",
                        body: """
                            <h2>Build Failed</h2>
                            <p>The pipeline encountered errors.</p>
                            <p><b>Job:</b> ${env.JOB_NAME}</p>
                            <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                            <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                            <p>Please check the console output for details.</p>
                        """,
                        to: 'your-email@example.com',
                        mimeType: 'text/html'
                    )
                } catch (Exception e) {
                    echo "Email notification skipped: ${e.message}"
                }
            }
        }
        
        unstable {
            echo '⚠️ Pipeline completed with warnings'
        }
        
        always {
            echo '========== Cleaning Workspace =========='
            // Ne pas nettoyer complètement pour debugging
            // cleanWs()
            echo '✅ Pipeline execution finished'
        }
    }
}
