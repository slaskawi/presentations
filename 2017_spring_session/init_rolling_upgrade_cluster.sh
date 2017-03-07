#!/bin/bash

set -e -x

oc create configmap transactions-configuration-new --from-file=./scripts/transactions-new.xml  || true
oc create -f ./scripts/infrastructure-new.yml || true