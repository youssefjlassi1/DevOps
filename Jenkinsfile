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
                    sh "docker compose -f ${MODULE_PATH}/docker-compose.yml up --build -d"
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
           always {
               echo 'Pipeline execution completed!'
           }
           success {
               mail to: 'youssef.jlassi@esprit.tn',
                   subject: "Succès du Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                   body: """
                   Bonjour Youssef Jlassi !

                   Le build du projet '${env.JOB_NAME}' s'est terminé avec succès.

                   Détails :

   Numéro du Build : ${env.BUILD_NUMBER}
   Statut du Build : SUCCESS
   Durée du Build : ${currentBuild.durationString}

                   Vous pouvez consulter la sortie complète de la console ici :
                   ${env.BUILD_URL}console

                   Cordialement,
                   Jenkins CI/CD
                   """
           }
           failure {
               mail to: 'youssef.jlassi@esprit.tn',
                   subject: "Échec du Build: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                   body: """
                    Bonjour Youssef Jlassi !,

                   Le build du projet '${env.JOB_NAME}' s'est terminé avec le statut : FAILURE.

                   Détails :

   Numéro du Build : ${env.BUILD_NUMBER}
   Statut du Build : FAILURE
   Durée du Build : ${currentBuild.durationString}

                   Vous pouvez consulter la sortie complète de la console ici :
                   ${env.BUILD_URL}console

                   Cordialement,
                   Jenkins CI/CD
                   """
           }
           }
           }
