pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/youssefjlassi1/DevOps.git'
        BRANCH = 'youssef-jlassi'
        MODULE_PATH = 'tp-foyer'
        SONAR_PROJECT_KEY = 'ESPRIT-youssef1'
        SONAR_HOST_URL = 'http://localhost:9000'
        DOCKER_IMAGE_NAME = 'yousse201/tp-foyer-devops'
        DOCKER_IMAGE_TAG = '5.0.0'
        NEXUS_URL = 'http://192.168.33.10:8081/repository/maven-releases/'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
        PROMETHEUS_URL = 'http://192.168.33.10:9090'
        GRAFANA_URL = 'http://192.168.33.10:3000'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Recup Code de Git : '
                git(
                    branch: "${BRANCH}",
                    url: "${GIT_REPO}",
                    credentialsId: 'git-cred'
                )
            }
        }

        stage('Verify Directory') {
            steps {
                echo 'Listing files to verify directory structure...'
                sh 'ls -R'
            }
        }

        stage('Maven Build') {
            steps {
                echo 'Starting Maven build...'
                dir(MODULE_PATH) {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Maven Test') {
            when { expression { fileExists("${MODULE_PATH}/pom.xml") } }
            steps {
                echo 'Running Maven tests...'
                dir(MODULE_PATH) {
                    sh 'mvn test'
                }
            }
        }

        stage('SonarQube Analysis') {
            when { expression { fileExists("${MODULE_PATH}/pom.xml") } }
            steps {
                echo 'Starting SonarQube analysis...'
                dir(MODULE_PATH) {
                   withSonarQubeEnv(installationName: 'sq1') {
                        sh ' mvn sonar:sonar '


                    }
                }
            }
        }


  stage ("Quality Gate"){
  steps{
  timeout(time: 2, unit: 'MINUTES'){
  waitForQualityGate abortPipeline: true
  }
   }
      }

        stage('Run Unit Tests') {
            steps {
                echo 'Running Unit Tests: '
                dir('tp-foyer') {
                    sh 'mvn test -X'
                }
            }
        }

        stage('Publish Test Results') {
            steps {
                echo 'Publishing Test Results: '
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo 'Building Docker image...'
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f ${MODULE_PATH}/Dockerfile ${MODULE_PATH}"
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir(MODULE_PATH) {
                    sh 'mvn deploy -Dmaven.test.skip=true'
                }
            }
        }

        stage('Deploy Image to DockerHub') {
            steps {
                script {
                    echo 'Deploying Docker image to DockerHub...'
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Run Docker Compose') {
            steps {
                script {
                    echo 'Running Docker Compose...'
                    sh "docker compose -f ${MODULE_PATH}/docker-compose.yml up -d"
                }
            }
        }

        stage('Prometheus Monitoring') {
            steps {
                script {
                    echo 'Starting Prometheus Monitoring...'
                    echo "curl -X GET ${PROMETHEUS_URL}/targets" // Check if Prometheus targets are active
                }
            }
        }

        stage('Grafana Dashboard Access') {
            steps {
                script {
                    echo 'Accessing Grafana Dashboard...'
                    echo 'http://192.168.33.10:3000/d/X034JGT7Gz/springboot-apm-dashboard'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the error logs above for more details.'
        }
    }
}
