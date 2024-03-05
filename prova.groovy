pipeline {
    agent any  //nessuna specifica in particolare

    environment {
		//da modificare nel moento in cui si vuole fare il deployment
        OPENSHIFT_URL = "https://c100-e.eu-de.containers.cloud.ibm.com:31696"
        OPENSHIFT_PROJECT = "<nome-progetto-ocp>"
        APPLICATION_NAME = "<nome-applicazione>"
        DOCKER_IMAGE = "<immagine-docker>"
        CREDENTIALS_ID = "OCP-token"
        TOKEN=""
        DOCKER_REGISTRY_CREDENTIALS_ID = 'NomeCredenzialiDockerRegistry'
        APP_IMAGE_NAME = 'nome-immagine-pre-build'
        APP_IMAGE_TAG = 'tag-immagine-pre-build'
    }

    /*parameters {
        string(name: 'c', defaultValue: 'ciao') //definire solo i paramentri che potrebbero servire e non sono stati passati da configurazione
        string(name: 'Ambiente', defaultValue: 'none')
        string(name: 'Ruolo', defaultValue: 'none')
    }*/

    stages {
        /*stage('Clone Repository') {
            steps {
                git 'https://github.com/r-anguilletti/Cost-Management-Openshift-Red-Hat'
            }
        }

        stage('Build and Push Docker Image') {
            //non ho idea di come farlo
            steps {
                script {
                    sh 'll'
                }
            }
        }*/

        stage('Login to OCP'){
            steps{
                script{
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: "TOKEN")]){
                            TOKEN = env.TOKEN //il token va aggiornato a mano nelle configurazioni della pipe
                        }

                        sh "oc login --token=${TOKEN} --server=${OPENSHIFT_URL}"
                }
            }
        }

         /*stage('Login al Docker Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_REGISTRY_CREDENTIALS_ID, passwordVariable: 'DOCKER_REGISTRY_TOKEN', usernameVariable: 'DOCKER_REGISTRY_USERNAME')]) {
                        sh "docker login -u ${DOCKER_REGISTRY_USERNAME} -p ${DOCKER_REGISTRY_TOKEN} registry.example.com"
                    }
                }
            }
        }*/

        stage('Access to parameters'){
            steps{
                script{
                    def prova=params.c
                    echo "${prova}"
                    echo "${c}" //posso usare anche solo il nome del parametro nella definizione
                }
            }
        }

        stage('Deploy to OpenShift') {
            steps {
                script {

                    sh "oc project ${OPENSHIFT_PROJECT}"

                    //sh "oc new-app ${DOCKER_IMAGE} --name=${APPLICATION_NAME}"

                    //sh "oc apply -f path/to/tuo/file-di-configurazione.yaml"

                    //sh "oc set image deployment ${OPENSHIFT_APP_NAME} ${OPENSHIFT_APP_NAME}=${APP_IMAGE_NAME}:${APP_IMAGE_TAG}"

					//sh "oc expose service ${APPLICATION_NAME}"
                }
            }
        }
    }

    /*post {
        always{
            sh "oc logout"
        }
    }*/
}