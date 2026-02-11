# --- Stage 1: Build Tailwind CSS + download keycloak-js ---
FROM node:20-alpine AS build
WORKDIR /build

# Install Tailwind CSS CLI and keycloak-js
RUN npm install tailwindcss@3 keycloak-js@26

# Copy source files
COPY tailwind.config.js ./
COPY src/ ./src/

# Generate minified Tailwind CSS (tree-shaken to only used classes)
RUN npx tailwindcss -i ./src/input.css -o ./dist/tailwind.css --minify

# Copy keycloak-js bundle to dist
RUN cp ./node_modules/keycloak-js/lib/keycloak.js ./dist/keycloak.min.js

# --- Stage 2: Runtime ---
FROM nginx:alpine

# Remove default config
RUN rm /etc/nginx/conf.d/default.conf

# Copy nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy static files
COPY src/index.html /usr/share/nginx/html/

# Copy built assets from stage 1
COPY --from=build /build/dist/tailwind.css /usr/share/nginx/html/
COPY --from=build /build/dist/keycloak.min.js /usr/share/nginx/html/

# Configure nginx to run as non-root on port 8080
RUN sed -i '/user  nginx;/d' /etc/nginx/nginx.conf \
    && sed -i 's|/var/run/nginx.pid|/tmp/nginx.pid|' /etc/nginx/nginx.conf

EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
