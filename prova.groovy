pipeline {
    agent any  //nessuna specifica in particolare

    environment {
		//da modificare nel moento in cui si vuole fare il deployment
        OPENSHIFT_URL = "https://c115-e.eu-de.containers.cloud.ibm.com:31458"
        CREDENTIALS_ID = "OCP-API-KEY"
        TOKEN=""
        POD_NAME=""
    }

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
                            sh "oc login ${OPENSHIFT_URL} -u apikey -p ${env.TOKEN}"
                        }

                       
                }
            }
        }

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

                    
                    if(flag == "true"){
                        sh "oc new-app --template=${TEMPLATE} --name=${Nome}"
                        sh "sleep 180" //aggiunto per dare il tempo al deployment di creare il pod associato
                    }else{
                        //importante dare sempre lo stesso formato di nome al file yaml
                        sh "oc create -f deployment/${Nome}-deployment.yaml -n ${OPENSHIFT_PROJECT}" 
                        sh "sleep 180" //aggiunto per dare il tempo al deployment di creare il pod associato
                    }
                }
            }
        }

        stage('Tagging'){
            steps{
                script{

                    /*sh "oc label deploymentconfig/${Nome} ambiente=${Ambiente}"
                    sh "oc label deploymentconfig/${Nome} ruolo=${Ruolo}"
                    sh "oc label deploymentconfig/${Nome} servizio=${Servizio}"
                    sh "oc label deploymentconfig/${Nome} proprietario=${Proprietario}"
                    sh "oc label deploymentconfig/${Nome} risorsa=${Risorsa}"
                    sh "oc label deploymentconfig/${Nome} tecnologia=${Tecnologia}"
                    sh "oc label deploymentconfig/${Nome} stato=${Stato}"*/

                    //label dei pod con script bash

                    //sh "oc get pods > pod.txt"
                    def pod=sh (script: "oc get pods | egrep -e \"${Nome}\" | egrep -e \"Running\" | cut -d \" \" -f 1", returnStdout: true).trim()
                    sh "oc label pod/${pod} ambiente=${Ambiente}"
                    sh "oc label pod/${pod} ruolo=${Ruolo}"
                    sh "oc label pod/${pod} servizio=${Servizio}"
                    sh "oc label pod/${pod} proprietario=${Proprietario}"
                    sh "oc label pod/${pod} risorsa=${Risorsa}"
                    sh "oc label pod/${pod} tecnologia=${Tecnologia}"
                    sh "oc label pod/${pod} stato=${Stato}"
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
