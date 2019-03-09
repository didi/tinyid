## Tinyid
[![license](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/didi/tinyid/blob/master/LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/didi/tinyid/pulls)

Tinyid is a ID Generator Service. It provides a REST API and a java client for getting ids. Over 10 million QPS per single instance when using the java client.
Support jdk version 1.7+

# Getting started

[中文wiki](https://github.com/didi/tinyid/wiki)

## Clone code
git clone https://github.com/didi/tinyid.git

## Create table
cd tinyid/tinyid-server/ && create table with db.sql (mysql)

## Config db

cd tinyid-server/src/main/resources/offline  
vi application.properties
```properties
datasource.tinyid.names=primary

datasource.tinyid.primary.driver-class-name=com.mysql.jdbc.Driver
datasource.tinyid.primary.url=jdbc:mysql://ip:port/databaseName?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8
datasource.tinyid.primary.username=root
datasource.tinyid.primary.password=123456
```
## Start tinyid-server
```xml
cd tinyid-server/
sh build.sh offline
java -jar output/tinyid-server-xxx.jar
```
## REST API 
```properties
nextId:
curl 'http://localhost:9999/tinyid/id/nextId?bizType=test&token=0f673adf80504e2eaa552f5d791b644c'
response:{"data":[2],"code":200,"message":""}

nextId Simple:
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test&token=0f673adf80504e2eaa552f5d791b644c'
response: 3

with batchSize:
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test&token=0f673adf80504e2eaa552f5d791b644c&batchSize=10'
response: 4,5,6,7,8,9,10,11,12,13

Get nextId like 1,3,5,7,9...
bizType=test_odd : delta is 2 and remainder is 1
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test_odd&batchSize=10&token=0f673adf80504e2eaa552f5d791b644c'
response: 3,5,7,9,11,13,15,17,19,21
```
## Java client  (Recommended)

### Maven dependency
```xml
<dependency>
    <groupId>com.xiaoju.uemc.tinyid</groupId>
    <artifactId>tinyid-client</artifactId>
    <version>${tinyid.version}</version>
</dependency>
```

### Create tinyid_client.properties in your classpath

tinyid_client.properties:
```properties
tinyid.server=localhost:9999
tinyid.token=0f673adf80504e2eaa552f5d791b644c

#(tinyid.server=localhost:9999/gateway,ip2:port2/prefix,...)
```
### Java Code
```java
Long id = TinyId.nextId("test");
List<Long> ids = TinyId.nextId("test", 10);
```

# Communication
<img src="doc/qqqun.JPG" alt="Tinyid Community" width="200"/>

# Contributing

Welcome to contribute by creating issues or sending pull requests. See [Contributing Guide](CONTRIBUTING.md) for guidelines.

# License

Tinyid is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file.

# Note

This is not an official Didi product (experimental or otherwise), it is just code that happens to be owned by Didi.
