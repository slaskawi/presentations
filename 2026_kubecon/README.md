<p align="center">
  <img src="https://www.keycloak.org/resources/images/logo.svg" alt="Keycloak" width="300">
</p>

<h1 align="center">Sovereign Identities for Your Cloud Native Architecture with Keycloak</h1>

<p align="center">
  <strong>KubeCon + CloudNativeCon Europe 2026 | London</strong><br>
  Maintainer Track &mdash; Keycloak
</p>

<p align="center">
  <a href="https://kccnceu2026.sched.com/event/2EF6o/sovereign-identities-for-your-cloud-native-architecture-with-keycloak-alexander-schwartz-ibm-sebastian-laskawiec-defense-unicorns?iframe=no">
    <img src="https://img.shields.io/badge/KubeCon_EU_2026-Session_Link-326CE5?style=for-the-badge&logo=kubernetes&logoColor=white" alt="KubeCon EU 2026">
  </a>
</p>

---

## About the Talk

> **Wednesday, March 25, 2026 | 11:45 - 12:15 CET | Room G102-103**

When building and evolving your sovereign cloud-native architecture, identities bring together your applications, data, and infrastructure and keep them secure.

Keycloak is well-known for managing human and non-human identities with OpenID Connect and SAML. You can use it with your applications and infrastructure, and also to broker with other external identity providers across organizations. With its built-in OpenTelemetry capabilities, it provides deep insights to trace down root causes for failed requests and slowdowns.

This talk demonstrates how to use **strong authentication** and leverage **trust relationships across organizations** &mdash; including our latest features on how to use **automatically rotating Kubernetes service account tokens as client secrets**, eliminating the need to store secrets in container images.

### Speakers

| | Name | Affiliation |
|---|---|---|
| | **Alexander Schwartz** | IBM |
| | **Sebastian Laskawiec** | Defense Unicorns |

---

## What's in This Repo

This repository contains the **live demo code** presented during the talk. It showcases three OAuth 2.0 / OpenID Connect authentication flows running on Kubernetes with Keycloak, progressively moving from traditional secrets to fully secretless, cryptographic identity:

### Flow 1 &mdash; Client ID + Client Secret (Traditional)

The application reads a client secret from a Kubernetes Secret mounted as a volume. A user authenticates via Keycloak, and the app exchanges an authorization code + client secret for an access token. This is the classic approach &mdash; functional, but requires a secret stored in the cluster.

### Flow 2 &mdash; Signed JWTs with User Authentication

The application uses a **Kubernetes Service Account projected token** (signed by the K8s API server) as a `client_assertion` instead of a client secret. The user still authenticates interactively, but the application proves its own identity cryptographically &mdash; no shared secrets needed.

### Flow 3 &mdash; Client Credentials with Signed JWTs

A fully non-interactive flow. The application authenticates directly using its Kubernetes Service Account JWT via the `client_credentials` grant. **No user interaction, no secrets** &mdash; purely cryptographic proof of workload identity.

---

## Architecture

```
                    +-----------------+
                    |    Keycloak     |
                    | (Identity Mgmt)|
                    +--------+--------+
                             |
              OIDC / Token   |   Trust via JWKS
              Exchange       |   (K8s API Server)
                             |
                    +--------+--------+
                    |   Demo Webapp   |
                    | (Nginx + SPA)   |
                    +--------+--------+
                             |
                    Projected SA Token
                    (auto-rotating, 10min TTL)
                             |
                    +--------+--------+
                    |   Kubernetes    |
                    | Service Account |
                    +-----------------+
```

## Tech Stack

| Component | Technology |
|---|---|
| Cluster | Minikube |
| Identity Provider | Keycloak 26.x + Keycloak Operator |
| Web Application | Single-page app with Tailwind CSS, Mermaid diagrams, PrismJS |
| Runtime | Nginx Alpine |
| Ingress | NGINX Ingress Controller |
| TLS | Self-signed certificates via Kubernetes Secrets |

---

## Quick Start

### Prerequisites

- [Minikube](https://minikube.sigs.k8s.io/) installed
- `kubectl` configured
- Docker (for building the webapp image)
- macOS or Linux

### Install Everything

```bash
./install.sh
```

This single script will:

1. Start a Minikube cluster with the ingress addon
2. Set up a Minikube tunnel (macOS) or use the Minikube IP (Linux)
3. Install the Keycloak Operator and deploy a Keycloak instance
4. Configure the `kubernetes` realm, identity provider, and clients
5. Build and deploy the demo web application
6. Run a smoke test with the client credentials flow

### Access the Demo

Once installed, the demo is available at:

| Service | URL |
|---|---|
| Demo Webapp | `https://demo.<HOST_IP>.nip.io` |
| Keycloak Admin | `https://keycloak.<HOST_IP>.nip.io` |

Default credentials: `admin` / `admin`

### Teardown

```bash
./uninstall.sh
```

---

## Repository Structure

```
.
├── install.sh                        # One-command setup
├── uninstall.sh                      # Clean teardown
├── webapp.sh                         # Build & deploy the webapp
├── keycloak-playground/
│   ├── create-keycloak.sh            # Deploy Keycloak to K8s
│   ├── configure-keycloak.sh         # Configure realm, IdP & clients
│   ├── create-mypod.sh              # Create test pod with projected token
│   ├── client-credential-grant.sh   # Test client credentials flow
│   ├── keycloak.yaml                # Keycloak CR manifest
│   ├── keycloak-ingress.yaml        # Ingress for Keycloak
│   └── mypod.yaml                   # Pod with projected SA token
└── webapp/
    ├── Dockerfile                    # Multi-stage build
    ├── nginx.conf                    # API proxy for tokens
    ├── src/
    │   └── index.html               # Interactive SPA with 3 auth flows
    └── k8s/
        ├── deployment.yaml           # Webapp deployment
        ├── service.yaml              # Webapp service
        ├── ingress.yaml              # Webapp ingress
        └── serviceaccount.yaml       # SA with projected token
```

---

## License

This demo code is provided as-is for educational and demonstration purposes.
