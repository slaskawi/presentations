_TEST_PROJECT = myproject
CURRENT_DIR = $(shell pwd)
KEYCLOAK_ID := $(shell docker ps --filter="ancestor=jboss/keycloak:7.0.0" --format={{.ID}})

turn-se-linux-off:
	sudo setenforce 0

build-native:
	mvn clean package -f quarkus-rest-caps/pom.xml -Pnative
	mvn clean package -f quarkus-rest-username/pom.xml -Pnative
	mvn clean package -f quarkus-front/pom.xml -Pnative

keycloak-start:
	docker run -d -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8080:8080 -v $(CURRENT_DIR)/quarkus-kc-quickstart.json:/config/quarkus-kc-quickstart.json -it jboss/keycloak:7.0.0 -Dkeycloak.migration.action=import -Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=/config/quarkus-kc-quickstart.json -Dkeycloak.migration.strategy=OVERWRITE_EXISTING

keycloak-stop:
	docker kill $(KEYCLOAK_ID) || true

keycloak-logs:
	docker logs -f $(KEYCLOAK_ID)

print-summary:
	@echo "Keycloak: 			http://localhost:8080"
	@echo "Front:			 	http://localhost:8081"
	@echo "Username service: 		http://localhost:8082"
	@echo "CAPS service: 			http://localhost:8083"
	@echo ""
	@echo "Examples:"
	@echo "- export token: export TOKEN="
	@echo "- username uppercase: curl -H \"Authorization: Bearer \$$TOKEN\" http://localhost:8083/caps/test"
	@echo "- credentials grant: curl -s -v --data \"client_id=quarkus-front&username=test&password=test&grant_type=password\" http://localhost:8080/auth/realms/quarkus-quickstart/protocol/openid-connect/token | jq"