FROM ubuntu:14.04

RUN apt-get update && apt-get -y install curl && apt-get -y install git && apt-get -y install maven

# JAVA
ARG JAVA_MAJOR_VERSION=8
ARG JAVA_UPDATE_VERSION=131
ARG JAVA_BUILD_NUMBER=11
ENV JAVA_HOME /usr/jdk1.${JAVA_MAJOR_VERSION}.0_${JAVA_UPDATE_VERSION}

ENV PATH $PATH:$JAVA_HOME/bin
RUN curl -sL --retry 3 --insecure \
  --header "Cookie: oraclelicense=accept-securebackup-cookie;" \
  "http://download.oracle.com/otn-pub/java/jdk/${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-b${JAVA_BUILD_NUMBER}/d54c1d3a095b4ff2b6607d096fa80163/server-jre-${JAVA_MAJOR_VERSION}u${JAVA_UPDATE_VERSION}-linux-x64.tar.gz" \
  | gunzip \
  | tar x -C /usr/ \
  && ln -s $JAVA_HOME /usr/java \
  && rm -rf $JAVA_HOME/man

# SPARK
ARG SPARK_ARCHIVE=http://d3kbcqa49mib13.cloudfront.net/spark-2.1.0-bin-hadoop2.7.tgz
RUN curl -s $SPARK_ARCHIVE | tar -xz -C /usr/local/

ENV SPARK_HOME /usr/local/spark-2.1.0-bin-hadoop2.7
ENV PATH $PATH:$SPARK_HOME/bin

# SCALA APP
ARG APP_HOME=ucu-distributed-algs
ARG APP_REPO=https://github.com/bolshanetsky/ucu-distributed-algs.git
RUN mkdir /app
#RUN ls
WORKDIR /app
RUN git clone  --progress --verbose $APP_REPO /app
RUN cd /app

# BUILD APP
RUN mvn package

# RUN APP just for the DEMO purpose!
RUN spark-submit --master local target/ucu-distributed-algs-1.0-SNAPSHOT-jar-with-dependencies.jar data/followers.txt