# Open Liberty - JMS & Active MQ

A sample application that uses Open Liberty to connect to ActiveMQ 's queue.

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
