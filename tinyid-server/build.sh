#!/bin/bash
#export JAVA_HOME=xxx
#export PATH=$JAVA_HOME/bin:$PATH
#
#export MAVEN_HOME=xxxx
#export PATH=$MAVEN_HOME/bin:$PATH
#

module=tinyid-server

env=$1

if [ -z "$env" ]; then
    env='online'
fi

echo "build $env begin"

mvn clean package -P$env -Dmaven.test.skip=true -f ../pom.xml
ret=$?
if [ $ret -ne 0 ];then
    echo "===== maven build failure ====="
    exit $ret
else
    echo -n "===== maven build successfully! ====="
fi

