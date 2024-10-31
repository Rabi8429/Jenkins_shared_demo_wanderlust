def call (String SonarQubeAPI){
    withSonarQubeEnv("${SonarQubeAPI}") {
    timeout(time: 10, unit: 'MINUTES') {
        sh 'sonar-scanner'
    }

  }  
}