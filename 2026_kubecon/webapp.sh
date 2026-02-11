#!/bin/bash -e

NAMESPACE="keycloak"
IMAGE_NAME="webapp"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo "--- Building webapp image inside Minikube Docker ---"
eval $(minikube docker-env)
docker build -t ${IMAGE_NAME}:latest "${SCRIPT_DIR}/webapp"

echo "--- Applying Kubernetes manifests ---"
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/serviceaccount.yaml"
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/deployment.yaml"
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/service.yaml"
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/ingress.yaml"

echo "--- Restarting deployment to pick up new image ---"
kubectl rollout restart deployment/webapp -n ${NAMESPACE}
kubectl rollout status deployment/webapp -n ${NAMESPACE} --timeout=60s

echo ""
echo "--- Webapp deployed! ---"
