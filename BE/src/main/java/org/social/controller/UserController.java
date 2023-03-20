package org.social.controller;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import org.social.helper.UserHelper;
import org.social.model.exception.ConflictException;
import org.social.model.exception.NotFoundException;
import org.social.model.user.User;
import org.social.model.user.UserVO;
import org.social.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/api/users")
public class UserController extends BaseController<UserVO> {
    @Inject
    UserService userService;

    @GET
    @RolesAllowed("user")
    public Uni<Response> getAll(@Context SecurityContext ctx) {
        return resFindAll(ctx);
    }

    @GET
    @RolesAllowed("user")
    @Path("/search")
    public Uni<Response> search(@Context SecurityContext ctx, @QueryParam("username") String userName, @QueryParam("id") Long id) {
        if (id != null) {
            return resFindBy(ctx, "id", id);
        }
        return resFindBy(ctx, "userName", userName);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> register(UserVO vo) {
        return Uni.createFrom().item(vo)
                .onItem().transform(this::handleCreate)
                .onItem().transform(this::created)
                .onFailure(ConflictException.class).recoverWithItem(ex -> conflict(ex.getMessage()))
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(UserVO vo) {
        return Uni.createFrom().item(vo)
                .onItem().transform(data -> userService.handleLogin(vo.getUserName(), vo.getPassword()))
                .onItem().transform(UserHelper::toVOIncludeToken)
                .onItem().transform(this::fetched)
                .onFailure(UnauthorizedException.class).recoverWithItem(ex -> unAuthorized(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @PUT
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Uni<Response> logout(@Context SecurityContext ctx) {
        return Uni.createFrom().item(ctx)
                .onItem().invoke(userService::handleLogout)
                .onItem().transform(data -> fetched("Logout successfully"))
                .onFailure(UnauthorizedException.class).recoverWithItem(ex -> unAuthorized(ex.getMessage()))
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @GET
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("refresh")
    public Uni<Response> refreshToken(@Context SecurityContext ctx) {
        return Uni.createFrom().item(ctx)
                .onItem().transform(userService::refreshToken)
                .onItem().transform(this::fetched)
                .onFailure(UnauthorizedException.class).recoverWithItem(ex -> unAuthorized(ex.getMessage()))
                .onFailure(NotFoundException.class).recoverWithItem(ex -> notFound(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @PUT
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(@Context SecurityContext ctx, UserVO vo) {
        return resUpdate(ctx, vo);
    }

    @DELETE
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@Context SecurityContext ctx, @QueryParam("username") String userName, @QueryParam("id") String id) {
        if (id != null) {
            return resDeleteId(ctx, id);
        }
        return resDelete(ctx, userName);
    }

    @Override
    protected List<UserVO> handleFindAll() {
        return userService.findAll().stream().map(UserHelper::toVO).toList();
    }

    @Override
    protected UserVO handleFindBy(String query, Object object) {
        User user;
        if (query.equals("id")) {
            user = userService.findById(String.valueOf(object));
        } else {
            user = userService.findByUser(String.valueOf(object));
        }
        return UserHelper.toVO(user);
    }

    @Override
    protected UserVO handleCreate(UserVO user) {
        return UserHelper.toVO(userService.register(user));
    }

    @Override
    protected UserVO handleUpdate(UserVO user) {
        return UserHelper.toVO(userService.update(user));
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
