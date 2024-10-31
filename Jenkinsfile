@Library('Shared') _
pipeline{
    agent{label 'node1' }

    environment{
        SCANNER_HOME=tool 'sonar-scanner'  //If my SonarQube Scanner name is sonar-scanner. Then it's working otherwise it's not working...
    }
    parameters{
        string(name: 'FRONTEND_DOCKER_TAG', defaultValue: '', description: 'Setting docker image for latest push')
        string(name: 'BACKEND_DOCKER_TAG', defaultValue: '', description: 'Setting docker image for latest push')
    }
    stages{
        stage("Validate Parameters"){
            steps{
               script{
                if (params.FRONTEND_DOCKER_TAG == '' || params.BACKEND_DOCKER_TAG == ''){
                     error("FRONTEND_DOCKER_TAG and BACKEND_DOCKER_TAG must be provided.")  
                }
               }
            }
        }
        stage('Workspace cleanup'){
            steps{
                script{
                    cleanWs()
                }
            }
        }
        stage('Git checkout'){
            steps{
                script{
                    code_checkout("https://github.com/Rabi8429/Wanderlust-Mega-Project.git", "main")
                }
            }
        }
        stage('Trivy: Filesystem scan'){
            steps{
                script{
                    trivy_scan()
                }
            }
        }
        stage('OWASP: Dependency check'){
            steps{
                script{
                    owasp_dependency()
                }
            }
        }
        stage('SonarQube: Code Analysis'){
            steps{
                script{
                    sonarqube_analysis('sonar-server',"wanderlust","wanderlust" )
                }
            }
        }
        stage('SonarQube: Code Quality Check'){
            steps{
                script{
                    sonarqube_code_quality()
                }
            }
        }
        stage('Exporting environment variables') {
            parallel{
                stage("Backend env setup"){
                    steps {
                        script{
                            dir("Automations"){
                                sh "bash updatebackendnew.sh"
                            }
                        }
                    }
                }
                
                stage("Frontend env setup"){
                    steps {
                        script{
                            dir("Automations"){
                                sh "bash updatefrontendnew.sh"
                            }
                        }
                    }
                }
            }
        }
        stage("Docker: Build Images") {
            steps{
                script{
                    dir('backend'){
                        docker_build("wanderlust-backend-beta","${params.BACKEND_DOCKER_TAG}","rabi4450")
                    }
                    dir('frontend'){
                        docker_build("wanderlust-frontend-beta","${params.FRONTEND_DOCKER_TAG}","rabi4450")
                    }
                }
            }
        }
        stage("Docker: Push to DockerHub"){
            steps{
                script{
                    docker_push("wanderlust-backend-beta","${params.BACKEND_DOCKER_TAG}","rabi4450") 
                    docker_push("wanderlust-frontend-beta","${params.FRONTEND_DOCKER_TAG}","rabi4450")
                }
            }
        }
    post{
        success{
            archiveArtifacts artifacts: '*.xml', followSymlinks: false
            build job: "Wanderlust-CD", parameters: [
                string(name: 'FRONTEND_DOCKER_TAG', value: "${params.FRONTEND_DOCKER_TAG}"),
                string(name: 'BACKEND_DOCKER_TAG', value: "${params.BACKEND_DOCKER_TAG}")
            ]
        }
      }
    }

}