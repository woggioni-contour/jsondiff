pipeline {
    agent any
    stages {
        stage("Build") {
            steps {
                sh "./gradlew clean build"
                junit testResults: "build/test-results/test/*.xml"
                javadoc javadocDir: "build/docs/javadoc", keepAll: true
                archiveArtifacts artifacts: 'build/distributions/*.jar',
                                 allowEmptyArchive: true,
                                 fingerprint: true,
                                 onlyIfSuccessful: true
            }
        }
        stage("Publish") {
            steps {
                sh "./gradlew publish"
            }
        }
    }
}