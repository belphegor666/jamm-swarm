#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')

// lets allow the VERSION_PREFIX to be specified as a parameter to the build
// but if not lets just default to 1.0
def versionPrefix = ""
try {
  versionPrefix = VERSION_PREFIX
} catch (Throwable e) {
  versionPrefix = "1.0"
}

def canaryVersion = "${versionPrefix}.${env.BUILD_NUMBER}"
def label = "buildpod.${env.JOB_NAME}.${env.BUILD_NUMBER}".replace('-', '_').replace('/', '_')

podTemplate(label: label, serviceAccount: 'jenkins', containers: [
        [name: 'maven', image: 'fabric8/maven-builder', command: 'cat', ttyEnabled: true, envVars: [
                [key: 'MAVEN_OPTS', value:'-Duser.home=/home/jenkins/'],
                [key: 'DOCKER_CONFIG', value:'/home/jenkins/.docker/'],
                [key: 'KUBERNETES_MASTER', value:  'kubernetes.default'] ]],
        [name: 'jnlp', image: 'iocanel/jenkins-jnlp-client:latest', command:'/usr/local/bin/start.sh', args: '${computer.jnlpmac} ${computer.name}', ttyEnabled: false,
                envVars: [[key: 'DOCKER_HOST', value: 'unix:/var/run/docker.sock']]]],
        volumes: [
                [$class: 'PersistentVolumeClaim', mountPath: '/home/jenkins/.mvnrepository', claimName: 'jenkins-mvn-local-repo'],
                [$class: 'SecretVolume', mountPath: '/home/jenkins/.m2/', secretName: 'jenkins-maven-settings'],
                [$class: 'SecretVolume', mountPath: '/home/jenkins/.docker', secretName: 'jenkins-docker-cfg'],
                [$class: 'HostPathVolume', mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock']
        ]) {

  node(label) {
    git GIT_URL

    echo 'NOTE: running pipelines for the first time will take longer as build and base docker images are pulled onto the node'
    container(name: 'maven') {

      stage 'Build Release'
      mavenCanaryRelease {
        version = canaryVersion
      }
    }
  }
}
