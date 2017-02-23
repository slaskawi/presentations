#!/bin/bash

set -e -x

oc policy add-role-to-user view system:serviceaccount:myproject:default -n myproject || true
oc create configmap transactions-configuration-old --from-file=./scripts/transactions-old.xml  || true
oc create -f ./scripts/infrastructure-old.yml || true