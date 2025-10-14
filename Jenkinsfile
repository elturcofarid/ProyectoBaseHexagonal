pipeline {
    agent any

    environment {
        // Nombre del artefacto final (ajústalo según tu proyecto)
        APP_NAME = "hexagonalbase-app"
        IMAGE_NAME = "hexagonalbase-app"
        CONTAINER_NAME = "hexagonalbase-container"

        // Configura tus credenciales y URLs de SonarQube
        SONARQUBE_SERVER = 'SonarQubeServer'   // nombre definido en Jenkins (Manage Jenkins > Configure System)
        SONARQUBE_TOKEN = credentials('jenkins-token') // ID del token en Jenkins credentials
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Clonando repositorio con cambios......'
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                echo 'Ejecutando pruebas unitarias...'
                sh './mvnw clean test'
            }
        }

        stage('Architecture Tests') {
            steps {
                echo 'Ejecutando pruebas de arquitectura...'
                // ArchUnit suele correr con mvn test, pero si está separado:
                sh './mvnw test -Dtest=*ArchitectureTest'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_SCANNER_OPTS = "-Xmx1024m"
            }
            steps {
                echo 'Analizando calidad de código con SonarQube...'
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh """
                        ./mvnw sonar:sonar \
                        -Dsonar.projectKey=${APP_NAME} \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONARQUBE_TOKEN}
                    """
                }
            }
        }

        stage('Build JAR') {
            steps {
                echo 'Compilando y empaquetando aplicación...'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Construyendo imagen Docker...'
                sh """
                    docker build -t ${IMAGE_NAME}:latest .
                """
            }
        }

        stage('Deploy Container') {
            steps {
                echo 'Desplegando nuevo contenedor...'
                sh """
                    docker rm -f ${CONTAINER_NAME} || true
                    docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${IMAGE_NAME}:latest
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline completado con éxito. Aplicación desplegada correctamente."
        }
        failure {
            echo "❌ Error en la pipeline. Revisa los logs en Jenkins."
        }
    }
}