#!/bin/bash

set -e -x

oc policy add-role-to-user view system:serviceaccount:myproject:default -n myproject || true
oc create configmap transactions-configuration --from-file=./scripts/transactions.xml  || true
oc create -f ./scripts/infrastructure.yml || true
mvn clean fabric8:deploy -pl user-data-creator
mvn clean fabric8:deploy -pl transaction-creator
