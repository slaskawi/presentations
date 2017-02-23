#!/bin/bash

set -e -x

oc delete configmap transactions-configuration-new
oc create configmap transactions-configuration-new --from-file=./scripts/transactions-new.xml
oc deploy transactions-repository-new --latest -n myproject