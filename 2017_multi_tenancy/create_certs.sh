#!/bin/bash

echo "==== Removing old files ===="
find . -name "*.jks" -exec rm -rf {} \;

echo "==== Generating private keys ===="
keytool -genkey -noprompt -trustcacerts -keyalg RSA -alias "client1" -dname "CN=HotRod-1, OU=Infinispan, O=JBoss, L=Red Hat, ST=World, C=WW" -keypass "secret" -storepass "secret" -keystore "client-1-server-keystore.jks"
keytool -genkey -noprompt -trustcacerts -keyalg RSA -alias "client2" -dname "CN=HotRod-1, OU=Infinispan, O=JBoss, L=Red Hat, ST=World, C=WW" -keypass "secret" -storepass "secret" -keystore "client-2-server-keystore.jks"

echo "==== Generating certificates ===="
keytool -export -keyalg RSA -alias "client1" -storepass "secret" -file "client-1.cer" -keystore "client-1-server-keystore.jks"
keytool -export -keyalg RSA -alias "client2" -storepass "secret" -file "client-2.cer" -keystore "client-2-server-keystore.jks"

echo "==== Importing certificates ===="
keytool -import -noprompt -v -trustcacerts -keyalg RSA -alias "client1" -file "client-1.cer" -keypass "secret" -storepass "secret" -keystore "client-1-truststore.jks"
keytool -import -noprompt -v -trustcacerts -keyalg RSA -alias "client2" -file "client-2.cer" -keypass "secret" -storepass "secret" -keystore "client-2-truststore.jks"

echo "==== Moving certs to proper directories ===="
mv client-1-server-keystore.jks scripts/client-1-server-keystore.jks
mv client-2-server-keystore.jks scripts/client-2-server-keystore.jks

mkdir -p cache-checker/src/main/resources
mv client-1-truststore.jks cache-checker/src/main/resources/client-1-truststore.jks
mv client-2-truststore.jks cache-checker/src/main/resources/client-2-truststore.jks

echo "==== Removing cert files (all you need is truststore.jks) ===="
rm "client-1.cer"
rm "client-2.cer"



