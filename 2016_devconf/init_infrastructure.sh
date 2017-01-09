#!/bin/bash

set -e -x

oc create -f ./scripts/infrastructure.yml
mvn clean fabric8:deploy -pl user-data-creator
mvn clean fabric8:deploy -pl transaction-creator