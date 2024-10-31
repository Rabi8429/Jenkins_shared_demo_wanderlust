def call(String Dependency-Check){
    dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: "${Dependency-Check}"
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
}
