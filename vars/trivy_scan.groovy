def call (){
    sh "trivy fs . > trivy_report.txt"
}