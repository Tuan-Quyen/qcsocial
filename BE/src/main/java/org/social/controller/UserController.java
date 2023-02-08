package org.social.controller;

import io.smallrye.mutiny.Uni;
import org.social.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserController {
    @Inject
    UserService userService;

    @GET
    public Uni<Response> getAll() {
        return Uni.createFrom().item(userService.findAll()).onItem().transform(data -> Response.ok(data).build());
    }
}
