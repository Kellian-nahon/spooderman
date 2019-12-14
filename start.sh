#!/bin/sh

VERSION="1.0-SNAPSHOT"
SUFFIX="$VERSION-jar-with-dependencies.jar"

command="$1"

shift

case $command in
  "crawl
  er" )
    java -cp ./crawler/target/crawler-$SUFFIX com.epita.crawler.MainKt $@;;
  "broker" )
    java -cp ./broker/target/broker-$SUFFIX com.epita.broker.api.server.MainKt $@ ;;
  "orchestrator" )
    java -cp ./orchestrator/target/orchestrator-$SUFFIX com.epita.spooderman.orchestrator.MainKt $@ ;;
  "url-validator" )
    java -cp ./urlvalidator/target/urlvalidator-$SUFFIX com.epita.urlvalidator.MainKt $@ ;;
  "index" )
    java -cp ./indexer/target/indexer-$SUFFIX com.epita.indexer.core.IndexMainKt $@ ;;
  "indexer" )
    java -cp ./indexer/target/indexer-$SUFFIX com.epita.indexer.DocumentizerMainKt $@ ;;
esac
