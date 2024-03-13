pipeline {
    agent any  //nessuna specifica in particolare

    environment {
		//da modificare nel moento in cui si vuole fare il deployment
        OPENSHIFT_URL = "https://c100-e.eu-de.containers.cloud.ibm.com:31696"
        CREDENTIALS_ID = "OCP-API-KEY"
        TOKEN=""
        POD_NAME=""
    }

    /*parameters {
        string(name: 'c', defaultValue: 'ciao') //definire solo i paramentri che potrebbero servire e non sono stati passati da configurazione
        string(name: 'Ambiente', defaultValue: 'none')
        string(name: 'Ruolo', defaultValue: 'none')
    }*/

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: "https://github.com/r-anguilletti/Cost-Management-Openshift-Red-Hat"
            }
        }

        stage('Login to OCP'){
            steps{
                script{
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: "TOKEN")]){
                            sh "oc login -u apikey -p ${env.TOKEN}"
                        }

                       
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

        /*stage('BuildConfig'){
            steps{
                script{
                    sh "oc import-image ubi9/podman:9.3-12 --from=registry.access.redhat.com/ubi9/podman:9.3-12 --confirm"
                    sh "oc new-build --name=${APPLICATION_NAME} --image=ubi9/podman:9.3-12"
                }
            }
        }

        stage('Start Build'){
            steps{
                script{
                    sh "oc start-build ${APPLICATION_NAME} --follow"
                }
            }
        }*/

        stage('Deploy to OpenShift') {
            steps {
                script {
                    sh "oc project ${OPENSHIFT_PROJECT}"

                    //sh "oc new-app --name=${APPLICATION_NAME} ${DOCKER_IMAGE_NAME_TAG}"

                    //sh "oc create deployment ${APPLICATION_NAME}  --image=${DOCKER_IMAGE_NAME_TAG} --port=8080"

                    //sh "oc apply -f path/to/tuo/file-di-configurazione.yaml"

                    //sh "oc set image deployment ${OPENSHIFT_APP_NAME} ${OPENSHIFT_APP_NAME}=${APP_IMAGE_NAME}:${APP_IMAGE_TAG}"

					//sh "oc expose deployment ${APPLICATION_NAME}"

                    //oc new-app https://github.com/openshift/ruby-hello-world !!

                    //sh "oc import-image ubi9/podman:9.3-12 --from=registry.access.redhat.com/ubi9/podman:9.3-12 --confirm"


                    //sh "oc create -f <file, path> -n <openshift_project>" 
                    if(Nome){
                        sh "oc new-app --template=${TEMPLATE} --name=${Nome}"
                    }else{
                        //sh "oc create deployment ${APPLICATION_NAME}  --image=ubi9/podman:9.3-12 --port=8080"

                    }

                }
            }
        }

        stage('Tagging'){
            steps{
                script{

                    //POD_NAME = sh(script: "oc get all | egrep -e \"pod/${Nome}\" | egrep -v \"build\" | tr -s " " | cut -d " " -f 1")

                    //sh "oc label dc/<name> key=value"  //tag del servizio

                    //flag=sh (script: "cat temp.txt | egrep -e \"deploymentconfig\"")
                    
                    //sh "echo ${flag}"

                    sh "oc label deploymentconfig/${Nome} ambiente=${Ambiente}"
                    sh "oc label deploymentconfig/${Nome} ruolo=${Ruolo}"
                    sh "oc label deploymentconfig/${Nome} servizio=${Servizio}"
                    sh "oc label deploymentconfig/${Nome} proprietario=${Proprietario}"
                    sh "oc label deploymentconfig/${Nome} risorsa=${Risorsa}"
                    sh "oc label deploymentconfig/${Nome} tecnologia=${Tecnologia}"
                    sh "oc label deploymentconfig/${Nome} stato=${Stato}"

                    //label dei pod con script bash

                    /*

                    sh "oc get pods > temp.txt"
                    def pod=sh (script: "cat temp.txt | egrep -e \"${Nome}\" | egrep -e \"Running\" | tr -s \" \" | cut -d \" \" -f 1", returnStdout: true)
                    sh "oc label pod/${pod} ambiente=${Ambiente}"
                    sh "oc label pod/${pod} ruolo=${Ruolo}"
                    sh "oc label pod/${pod} servizio=${Servizio}"
                    sh "oc label pod/${pod} proprietario=${Proprietario}"
                    sh "oc label pod/${pod} risorsa=${Risorsa}"
                    sh "oc label pod/${pod} tecnologia=${Tecnologia}"
                    sh "oc label pod/${pod} stato=${Stato}"

                    */

                    
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
