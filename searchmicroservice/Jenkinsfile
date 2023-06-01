pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    options {
        gitLabConnection('jenkins-hello')
    }

    stages {
        stage('Build') {
            steps {
                updateGitlabCommitStatus name: 'build', state: 'pending'
                sh 'mvn clean verify'
            }
        }
    }
    post {
            success {
                updateGitlabCommitStatus name: 'build', state: 'success'
            }
            failure {
                updateGitlabCommitStatus name: 'build', state: 'failed'
            }
        }

}

