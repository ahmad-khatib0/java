package se.magnus.microservices.composite.product;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

// define the semantics of the security schema security_auth: 
// a. The security schema will be based on OAuth 2.0.
// b. The authorization code grant flow will be used.
// c. The required URLs for acquiring an authorization code and access tokens will be supplied
//    by the configuration using the parameters springdoc.oAuthFlow.authorizationUrl
//    and springdoc.oAuthFlow.tokenUrl.
// d. A list of scopes (product:read and product:write) that Swagger UI 
//    will require to be able to call the APIs.
@SecurityScheme(
    name = "security_auth", 
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(authorizationCode = @OAuthFlow(authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", 
    tokenUrl = "${springdoc.oAuthFlow.tokenUrl}", 
    scopes = {
    @OAuthScope(name = "product:read", description = "read scope"),
    @OAuthScope(name = "product:write", description = "write scope")
})))

public class OpenApiConfig {
}
