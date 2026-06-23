#!/bin/bash
mvn clean test -Dtest="ApiUtilsTest,DataLoaderTest,ConfigReaderTest"

rm -rf /tmp/coverage-classes
mkdir -p /tmp/coverage-classes
cp target/test-classes/com/sparta/utilities/ApiUtils.class /tmp/coverage-classes/
cp target/test-classes/com/sparta/utilities/DataLoader.class /tmp/coverage-classes/
cp target/test-classes/com/sparta/utilities/ConfigReader.class /tmp/coverage-classes/

java -jar jacococli.jar report target/jacoco.exec \
  --classfiles /tmp/coverage-classes \
  --sourcefiles src/test/java \
  --html target/site/jacoco

open target/site/jacoco/index.html
