#!/bin/bash -e

NAMESPACE="keycloak"
IMAGE_NAME="webapp"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
HOST_IP="${HOST_IP:-127.0.0.1}"

echo "--- Building webapp image inside Minikube Docker (HOST_IP=$HOST_IP) ---"

# Prepare a temp build context with HOST_IP substituted into index.html
BUILD_DIR=$(mktemp -d)
cp -r "${SCRIPT_DIR}/webapp/"* "$BUILD_DIR/"
sed -i.bak "s|127\.0\.0\.1|${HOST_IP}|g" "$BUILD_DIR/src/index.html"
rm -f "$BUILD_DIR/src/index.html.bak"

eval $(minikube docker-env)
docker build -t ${IMAGE_NAME}:latest "$BUILD_DIR"
rm -rf "$BUILD_DIR"

echo "--- Applying Kubernetes manifests ---"
kubectl apply -n ${NAMESPACE} -f "${SCRIPT_DIR}/webapp-client-secret.yaml"
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/serviceaccount.yaml"

# Template deployment and ingress with HOST_IP
sed "s|127\.0\.0\.1|${HOST_IP}|g" "${SCRIPT_DIR}/webapp/k8s/deployment.yaml" | kubectl apply -f -
kubectl apply -f "${SCRIPT_DIR}/webapp/k8s/service.yaml"
sed "s|127\.0\.0\.1|${HOST_IP}|g" "${SCRIPT_DIR}/webapp/k8s/ingress.yaml" | kubectl apply -f -

echo "--- Restarting deployment to pick up new image ---"
kubectl rollout restart deployment/webapp -n ${NAMESPACE}
kubectl rollout status deployment/webapp -n ${NAMESPACE} --timeout=60s

echo ""
echo "--- Webapp deployed! ---"
