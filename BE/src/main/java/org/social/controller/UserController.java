package org.social.controller;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import org.social.model.User;
import org.social.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserController extends BaseController<User> {
    @Inject
    UserService userService;

    @GET
    public Uni<Response> getAll() {
        return resFindAll();
    }

    @GET
    @Path("/search")
    public Uni<Response> search(@QueryParam("username") String userName, @QueryParam("id") Long id) {
        if (id != null) {
            return resFindBy("id", id);
        }
        return resFindBy("userName", userName);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> register(User user) {
        return resCreate(user);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(User user) {
        return Uni.createFrom().item(user)
                .onItem().invoke(data -> userService.handleLogin(user.getUserName(), user.getPassword()))
                .onItem().transform(data -> Response.ok(data.id).build())
                .onFailure(UnauthorizedException.class).recoverWithItem(ex -> unAuthorized(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(User user) {
        return resUpdate(user);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@QueryParam("username") String userName, @QueryParam("id") String id) {
        if (id != null) {
            return resDeleteId(id);
        }
        return resDelete(userName);
    }

    @Override
    protected List<User> handleFindAll() {
        return userService.findAll();
    }

    @Override
    protected User handleFindBy(String query, Object object) {
        if (query.equals("id")) {
            return userService.findById(String.valueOf(object));
        }
        return userService.findByUser(String.valueOf(object));
    }

    @Override
    protected User handleCreate(User user) {
        return userService.register(user);
    }

    @Override
    protected User handleUpdate(User user) {
        return userService.update(user);
    }

    @Override
    protected void handleDelete(String userName) {
        userService.deleteByName(userName);
    }

    @Override
    protected void handleDeleteId(String id) {
        userService.deleteById(id);
    }
}
