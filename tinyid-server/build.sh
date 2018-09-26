#!/bin/bash
#如需使用其他版本的jdk请联系scm，打开并修改下面的变量
#export JAVA_HOME=xxx   (如使用系统默认的不需要设置，系统默认版本1.7.0)
#export PATH=$JAVA_HOME/bin:$PATH
#
#export JAVA_HOME=/usr/local/jdk1.8.0_65  #(使用jdk8请设置)
#export PATH=$JAVA_HOME/bin:$PATH
#
#如需使用其他版本的maven请联系scm，打开并修改下面的变量
#export MAVEN_HOME=xxxx    (如使用系统默认的不需要设置，默认maven-3.3.3)
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
rm -rf output
mkdir output
mv target/${module}-*.jar output
cd output
