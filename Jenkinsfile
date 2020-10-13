pipeline {
    agent any
    environment {
        PATH = "/var/jenkins_home/tools/hudson.tasks.Maven_MavenInstallation/Maven_3.6.0/bin:$PATH"
    }
    stages {
        stage('Build (models2020)') {
            when {
                expression { env.TAG_NAME == "models2020" }
            }
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Deploy (models2020)') {
            when {
                expression { env.TAG_NAME == "models2020" }
            }
            steps {
                sh 'rm -rf /var/site/models2020/typhon-dl/*'
                sh 'cp -a typhondl-update-site/target/. /var/site/models2020/typhon-dl/'
            }
        }
    }
}
