#!/bin/bash -e

# Configuration
KEYCLOAK_VERSION="26.5.3"
NAMESPACE="keycloak"
CLUSTER_NAME="keycloak"
KEYCLOAK_NAME="keycloak"

# K3d exposes ports to localhost
INGRESS_PORT="443"

echo "-----------------------------------------------------------------------------"
echo "1. Ensuring Minikube is running"
echo "-----------------------------------------------------------------------------"

if minikube status >/dev/null 2>&1; then
  echo "Minikube is already running."
else
  minikube start
  minikube addons enable ingress
  if [[ "$(uname -s)" == "Darwin" ]]; then
    echo "Checking minikube tunnel..."
    if ! pgrep -f "minikube tunnel" >/dev/null; then
      echo "Minikube tunnel is not running. Starting it..."
      nohup sudo minikube tunnel -c > ./minikube-tunnel.log 2>&1 &
      sleep 5
    else
      echo "Minikube tunnel is running."
    fi
  fi
fi

HOST_IP="127.0.0.1"

echo "-----------------------------------------------------------------------------"
echo "2. Installing Keycloak Operator"
echo "-----------------------------------------------------------------------------"

# Create namespace if it doesn't exist
kubectl get namespace $NAMESPACE >/dev/null 2>&1 || kubectl create namespace $NAMESPACE

# Install CRDs
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/$KEYCLOAK_VERSION/kubernetes/keycloaks.k8s.keycloak.org-v1.yml
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/$KEYCLOAK_VERSION/kubernetes/keycloakrealmimports.k8s.keycloak.org-v1.yml

# Install Operator
kubectl apply -f https://raw.githubusercontent.com/keycloak/keycloak-k8s-resources/$KEYCLOAK_VERSION/kubernetes/kubernetes.yml -n $NAMESPACE

echo "Waiting for Keycloak Operator to be ready..."
kubectl rollout status deployment/keycloak-operator -n $NAMESPACE --timeout=60s

echo "-----------------------------------------------------------------------------"
echo "3. Creating Keycloak Installation"
echo "-----------------------------------------------------------------------------"

# Create TLS Secret
kubectl delete secret example-tls-secret -n $NAMESPACE --ignore-not-found=true
kubectl delete secret keycloak-admin-credentials.yaml -n $NAMESPACE --ignore-not-found=true
kubectl apply -n keycloak -f example-tls-secret.yaml
kubectl apply -n keycloak -f keycloak-admin-credentials.yaml

# Apply Keycloak CR (create or update)
cat <<EOF | kubectl apply -f -
apiVersion: k8s.keycloak.org/v2alpha1
kind: Keycloak
metadata:
  name: $KEYCLOAK_NAME
  namespace: $NAMESPACE
spec:
  instances: 1
  db:
    vendor: dev-file
  bootstrapAdmin:
    user:
      secret: keycloak-admin-credentials
  hostname:
    hostname: keycloak.${HOST_IP}.nip.io
  ingress:
    enabled: false
    className: nginx
  http:
    httpEnabled: true
    tlsSecret: example-tls-secret
  proxy:
    headers: xforwarded
  env:
    - name: KC_BOOTSTRAP_ADMIN_USERNAME
      value: "admin"
    - name: KC_BOOTSTRAP_ADMIN_PASSWORD
      value: "admin"
  additionalOptions:
    - name: features
      value: client-auth-federated,kubernetes-service-accounts
    - name: http-relative-path
      value: /
    - name: hostname-strict
      value: "false"
EOF

echo "Waiting for Keycloak instance to be ready..."
# Wait for the StatefulSet to be created by the operator
MAX_WAIT=30
COUNT=0
while ! kubectl get statefulset $KEYCLOAK_NAME -n $NAMESPACE >/dev/null 2>&1 && [ "$COUNT" -lt "$MAX_WAIT" ]; do
  echo -n "."
  sleep 2
  ((COUNT++))
done
echo ""

if [ "$COUNT" -ge "$MAX_WAIT" ] && ! kubectl get statefulset $KEYCLOAK_NAME -n $NAMESPACE >/dev/null 2>&1; then
  echo "Timeout waiting for Keycloak StatefulSet to be created."
  exit 1
fi

kubectl rollout status statefulset/$KEYCLOAK_NAME -n $NAMESPACE --timeout=120s

echo "-----------------------------------------------------------------------------"
echo "4. Ingress reconfiguration for Keycloak"
echo "-----------------------------------------------------------------------------"

KEYCLOAK_HOST="keycloak.${HOST_IP}.nip.io"
KEYCLOAK_URL="https://$KEYCLOAK_HOST"

# The operator creates a service named <keycloak-name>-service
echo "Waiting for Keycloak Service..."
SERVICE_NAME=""
count=0
while [ -z "$SERVICE_NAME" ] && [ $count -lt 30 ]; do
  # Try finding by label
  SERVICE_NAME=$(kubectl get svc -n $NAMESPACE -l app.kubernetes.io/name=keycloak -o jsonpath='{.items[0].metadata.name}' 2>/dev/null || true)

  # Try finding by fallback name
  if [ -z "$SERVICE_NAME" ] && kubectl get svc keycloak-service -n $NAMESPACE >/dev/null 2>&1; then
     SERVICE_NAME="keycloak-service"
  fi

  if [ -z "$SERVICE_NAME" ]; then
    echo -n "."
    sleep 2
    count=$((count + 1))
  fi
done
echo ""

if [ -z "$SERVICE_NAME" ]; then
  echo "Error: Could not find Keycloak service."
  kubectl get svc -n $NAMESPACE
  exit 1
fi

INGRESS_NAME="$KEYCLOAK_NAME-ingress"

# Create Ingress resource
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1

kind: Ingress
metadata:
  name: $INGRESS_NAME
  namespace: $NAMESPACE
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
spec:
  rules:
  - host: $KEYCLOAK_HOST
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: $SERVICE_NAME
            port:
              number: 8080
EOF

echo "-----------------------------------------------------------------------------"
echo "5. Setting Keycloak up"
echo "-----------------------------------------------------------------------------"

echo "Login kcadm to Keycloak"

KC_POD="${KEYCLOAK_NAME}-0"
KCADMIN="kubectl exec "$KC_POD" -n "${NAMESPACE}" -- /opt/keycloak/bin/kcadm.sh"

$KCADMIN config credentials --server http://localhost:8080 --realm master --user admin --password admin

if ! $KCADMIN get realms/kubernetes >/dev/null 2>&1; then
  echo "Create demo kubernetes realm"
  $KCADMIN create realms -s realm=kubernetes -s enabled=true

  echo "Create Kubernetes Identity Provider config"
  $KCADMIN create identity-provider/instances -r kubernetes -s alias=kubernetes -s providerId=kubernetes -s config='{"issuer": "https://kubernetes.default.svc.cluster.local"}'

  echo "Create client authenticating with Kubernetes service account"
  $KCADMIN create clients -r kubernetes -s clientId=myclient -s serviceAccountsEnabled=true -s clientAuthenticatorType=federated-jwt -s attributes='{ "jwt.credential.issuer": "kubernetes", "jwt.credential.sub": "system:serviceaccount:keycloak:test-serviceaccount" }'
else
  echo "Realm kubernetes already exists"
fi

echo "-----------------------------------------------------------------------------"
echo "5. Creating test-pod"
echo "-----------------------------------------------------------------------------"

cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ServiceAccount
metadata:
  name: test-serviceaccount
  namespace: $NAMESPACE
---
apiVersion: v1
kind: Pod
metadata:
  name: test-pod
  namespace: $NAMESPACE
spec:
  serviceAccountName: test-serviceaccount
  containers:
  - name: nginx
    image: nginx:latest
    ports:
    - containerPort: 80
    volumeMounts:
    - mountPath: /var/run/secrets/tokens
      name: vault-token
  volumes:
  - name: vault-token
    projected:
      sources:
      - serviceAccountToken:
          path: kctoken
          expirationSeconds: 600
          audience: $KEYCLOAK_URL/realms/kubernetes
EOF


echo "-----------------------------------------------------------------------------"
echo "5. Validation"
echo "-----------------------------------------------------------------------------"

echo "Checking if cluster is running..."
minikube status $CLUSTER_NAME

echo "Checking if Keycloak Pod is running..."
kubectl get pods -n $NAMESPACE -l app=keycloak

echo "Verifying if Keycloak instance is reachable via curl..."
echo "Target URL: $KEYCLOAK_URL"

# Wait for Ingress to be ready
HTTP_CODE=0
COUNT=0
MAX_RETRIES=30
until [ "$HTTP_CODE" -eq 200 ] || [ "$COUNT" -eq $MAX_RETRIES ]; do
  sleep 2
  HTTP_CODE=$(curl -L -s --insecure -o /dev/null $KEYCLOAK_URL -w "%{http_code}")
  echo -n "."
  ((COUNT++))
done
echo ""

if [ "$HTTP_CODE" -eq 200 ]; then
  echo "Keycloak is reachable!"
else
  echo "Keycloak is NOT reachable (HTTP $HTTP_CODE). You might need to check ingress logs."
fi

echo "-----------------------------------------------------------------------------"
echo "5. Checking client credentials grant with Kubernetes service account token"
echo "-----------------------------------------------------------------------------"

TOKEN="$(kubectl exec test-pod -n $NAMESPACE -- cat /var/run/secrets/tokens/kctoken)"

KC_TOKEN="$(curl -s --insecure -X POST \
  -d grant_type=client_credentials \
  -d client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer \
  -d client_assertion="$TOKEN" \
  $KEYCLOAK_URL/realms/kubernetes/protocol/openid-connect/token)"

if [ -n "$KC_TOKEN" ]; then
  echo "Signed JWTs authentication successful"
else
  echo "Can't use the Signed JWTs authentication with Kubernetes service account token"
fi

echo ""
echo "Keycloak available on $KEYCLOAK_URL"
echo "Admin credentials are admin/admin"
