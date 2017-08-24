#!/bin/bash

set -e -x

oc delete configmap transactions-configuration || true
oc delete secret client-1-server-keystore || true
oc delete secret client-2-server-keystore || true
oc delete all --all || true
./create_certs.sh
oc policy add-role-to-user view system:serviceaccount:myproject:default -n myproject
oc create configmap transactions-configuration --from-file=./scripts/transactions.xml
oc create secret generic client-1-server-keystore --from-file=./scripts/client-1-server-keystore.jks
oc create secret generic client-2-server-keystore --from-file=./scripts/client-2-server-keystore.jks
oc create -f ./scripts/infrastructure.yml
watch oc get pods
