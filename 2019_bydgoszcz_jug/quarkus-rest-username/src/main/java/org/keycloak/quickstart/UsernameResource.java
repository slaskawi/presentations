package org.keycloak.quickstart;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonString;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/username")
public class UsernameResource {

    @Inject
    @Claim(standard = Claims.preferred_username)
    String preferred_username;

    @Inject
    @Claim("address")
    String address;

    @Inject
    @RestClient
    CapsService capsService;

    @GET
    @RolesAllowed({"user"})
    public String getUsername() {
        System.out.println(address);

        return capsService.toCaps(preferred_username);
    }
}