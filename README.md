# web-crawler-with-spring-kafka-redis
Implementation for Web Crawler with Spring 

### Installation
- Download Kafka Server from [here](https://kafka.apache.org/quickstart)
- Download Redis Server https://redis.io/download
```
Note: You can run $ brew install redis on macOS  
```
- Open the project in IntelliJ or any other IDE
- Install Maven dependencies

### User Guide
Start redis server
```shell script
$ redis-server
```

Start Zookeeper
```shell script
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka
```shell script
$ bin/kafka-server-start.sh config/server.properties
```