package org.social.controller;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import org.social.helper.UserHelper;
import org.social.model.user.User;
import org.social.model.user.UserVO;
import org.social.service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/users")
public class UserController extends BaseController<UserVO> {
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
    public Uni<Response> register(UserVO vo) {
        return resCreate(vo);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> login(UserVO vo) {
        return Uni.createFrom().item(vo)
                .onItem().invoke(data -> userService.handleLogin(vo.getUserName(), vo.getPassword()))
                .onItem().transform(this::fetched)
                .onFailure(UnauthorizedException.class).recoverWithItem(ex -> unAuthorized(ex.getMessage()))
                .onFailure().recoverWithItem(ex -> internal(ex.getMessage()));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(UserVO vo) {
        return resUpdate(vo);
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
