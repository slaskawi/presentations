#!/bin/bash

set -e -x

oc delete configmap transactions-configuration || true
oc delete secret default-server-keystore || true
oc delete secret sni-server-keystore || true
oc delete all --all || true
./create_certs.sh
oc policy add-role-to-user view system:serviceaccount:myproject:default -n myproject
oc create configmap transactions-configuration --from-file=./scripts/transactions.xml
oc create secret generic default-server-keystore --from-file=./scripts/default_server_keystore.jks
oc create secret generic sni-server-keystore --from-file=./scripts/sni_server_keystore.jks
oc create -f ./scripts/infrastructure.yml
watch oc get pods