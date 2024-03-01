pipeline {
    agent any  //nessuna specifica in particolare

    environment {
		//da modificare nel moento in cui si vuole fare il deployment
        OPENSHIFT_URL = "https://<indirizzo-ocp>"
        OPENSHIFT_PROJECT = "<nome-progetto-ocp>"
        APPLICATION_NAME = "<nome-applicazione>"
        DOCKER_IMAGE = "<immagine-docker>"
    }

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

        stage('Deploy to OpenShift') {
            steps {
                script {
                    sh 'oc login --token=sha256~jvtAJzi7ld6w6FoeJkzY3VOY3vsO6xmDaIHEANmRzbA --server=https://c100-e.eu-de.containers.cloud.ibm.com:31696'

                    sh 'oc projects'

                    //sh 'oc project ${OPENSHIFT_PROJECT}'

                    //sh 'oc new-app ${DOCKER_IMAGE} --name=${APPLICATION_NAME}'

					//sh 'oc expose service ${APPLICATION_NAME}'
                }
            }
        }
    }
}