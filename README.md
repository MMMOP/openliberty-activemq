# Open Liberty - JMS & Active MQ

A sample application that uses Open Liberty to connect to ActiveMQ 's queue and topic.

## Setup

For this project you'll need to install the following requirements:

- JDK 11
- Maven
- Docker

## Running it

Install Active MQ

```sh
docker compose -f 'docker-compose.yml' up -d --build
```

Install the dependencies and start OpenLiberty:

```sh
mvn install
mvn liberty:dev
mvn liberty:deploy
```

Once the server is running, send a message to the queue:

```sh
curl -X GET -d 'msg=test mq' http://localhost:9080/send
```

Monitor Program by [JavaMelody](https://github.com/javamelody/javamelody/wiki/UserGuide)

[http://localhost:9080/monitoring](http://localhost:9080/monitoring)

Install Oracle Database ([gvenzl/oracle-free](https://hub.docker.com/r/gvenzl/oracle-free))

```sh
docker run -d -p 1521:1521 -e ORACLE_RANDOM_PASSWORD=y -e APP_USER=admin -e APP_USER_PASSWORD=admin gvenzl/oracle-free
```

