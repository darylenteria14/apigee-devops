// @Library('mylibrary@develop') _
//def google = new org.foo.bar()

library identifier: 'sharedlibrary@develop',
    // 'mylibraryname' is just an identifier, it can be anything you like
    // 'master' refers to a valid git ref (branch)
    retriever: modernSCM([
      $class: 'GitSCMSource',
      credentialsId: 'github', // remove this if it's public!
      remote: 'git@github.com:hsandique/shared-library.git'
])

properties([
    parameters([

        choice(choices: ['', 'acn-apigee-poc'],
        description: 'Select the Organization to Build',
        name: 'APIGEE_ORG'),
        choice(choices: ['', 'RuntimeValidationTest', 'UnDeployProxy', 'UnDeploySharedflow', 'DeleteProxy', 'DeleteSharedflow', 'DeleteTarget', 'DeleteCache', 'DeleteProxyRevision', 'DeleteSharedFlowRevision', 'RemoveEnvFromEnvGroup', 'DeleteEnvironment'],
        description: 'Select the Action',
        name: 'ACTION'),
        string(defaultValue: 'dev1', name: 'APIGEE_ENV'),
        string(defaultValue: '', name: 'INPUT_FOR_ACTION')
    ])
])

pipeline {
    agent any 
    environment{
            organization = "${params.APIGEE_ORG}"
            environment = "${params.APIGEE_ENV}"
            action = "${params.ACTION}"
            input="${params.INPUT_FOR_ACTION}"
    }
    stages {
        stage('SharedLibray Test') {
            steps {
              script {
                echo 'Testing Shared Library'
                display("Hello World")
              }
            }
        }
        stage('Configure GCP Environment') {
            agent {
                docker {
                    image 'google/cloud-sdk' //docker image to use
                    //label "<jenkins_agent_label>"
                    // args '-u root:root'
                    // reuseNode true
                }
            }
            steps {
              script {
                activatesa(repoName:"myrepo",serviceAccount:"apigee-non-prod",projectID:"nth-rarity-376505")
              }
            }
        }
    }
}
