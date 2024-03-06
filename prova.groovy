pipeline {
    agent any  //nessuna specifica in particolare

    environment {
		//da modificare nel moento in cui si vuole fare il deployment
        OPENSHIFT_URL = "https://c100-e.eu-de.containers.cloud.ibm.com:31696"
        OPENSHIFT_PROJECT = "hello-world"
        OPENSHIFT_NAME_APP = ""
        DOCKER_IMAGE_NAME_TAG = "docker.io/ranguilletti/jenkins"
        APPLICATION_NAME = "jk"
        CREDENTIALS_ID = "OCP-API-KEY"
        TOKEN=""
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
        }*/

        /*stage('Docker login (RH repository)'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: CREDENTIALS_DOCKER, passwordVariable: "PASSWORD", usernameVariable: "USERNAME")]){
                        sh "docker login -u ${USERNAME} -p ${PASSWORD} ${DOCKER_URL}"
                    }
                }
            }
        }

        stage('Download docker image') {
            steps {
                script {
                    sh "docker pull ${DOCKER_IMAGE_NAME_TAG}"
                }
            }
        }*/

        stage('Login to OCP'){
            steps{
                script{
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: "TOKEN")]){
                            TOKEN = env.TOKEN //il token va aggiornato a mano nelle configurazioni della pipe
                        }

                        sh "oc login -u apikey -p ${TOKEN}"
                }
            }
        }

        /*stage('Access to parameters'){
            steps{
                script{
                    def prova=params.c
                    echo "${prova}"
                    echo "${c}" //posso usare anche solo il nome del parametro nella definizione
                }
            }
        }*/

        stage('Deploy to OpenShift') {
            steps {
                script {
                    sh "oc project ${OPENSHIFT_PROJECT}"

                    //sh "oc new-app --name=${APPLICATION_NAME} ${DOCKER_IMAGE_NAME_TAG}"

                    sh "oc create deployment ${APPLICATION_NAME}  --image=${DOCKER_IMAGE_NAME_TAG} --port=8080"

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
