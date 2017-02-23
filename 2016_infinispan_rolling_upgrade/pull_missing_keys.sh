#!/bin/bash

set -x

for pod in `oc get pods -o name --selector=cluster=new` ; do
    POD=${pod:4}
    oc exec ${pod:4} -- '/opt/jboss/infinispan-server/bin/ispn-cli.sh' '-c' '--controller=$(hostname -i):9990' '/subsystem=datagrid-infinispan/cache-container=clustered/distributed-cache=transactions:synchronize-data(migrator-name=hotrod)'
    oc exec ${pod:4} -- '/opt/jboss/infinispan-server/bin/ispn-cli.sh' '-c' '--controller=$(hostname -i):9990' '/subsystem=datagrid-infinispan/cache-container=clustered/distributed-cache=transactions:disconnect-source(migrator-name=hotrod)'
done