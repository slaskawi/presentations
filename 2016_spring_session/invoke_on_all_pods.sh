for pod in `oc get pods -o name` ; do
    POD=${pod:4}
    echo "POD: $POD"
    oc exec ${pod:4} $1
done
