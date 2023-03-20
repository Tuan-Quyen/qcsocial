package org.social.controller;

import io.smallrye.mutiny.Uni;
import org.social.helper.RoomHelper;
import org.social.model.room.Room;
import org.social.model.room.RoomVO;
import org.social.service.RoomService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/api/rooms")
public class RoomController extends BaseController<RoomVO> {
    @Inject
    RoomService roomService;

    @GET
    @RolesAllowed("user")
    public Uni<Response> getAll(@Context SecurityContext ctx) {
        return resFindAll(ctx);
    }

    @GET
    @Path("/search")
    @RolesAllowed("user")
    public Uni<Response> search(@Context SecurityContext ctx, @QueryParam("name") String name, @QueryParam("id") Long id) {
        if (id != null) {
            return resFindBy(ctx, "id", id);
        }
        return resFindBy(ctx, "name", name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Uni<Response> create(@Context SecurityContext ctx, RoomVO vo) {
        return resCreate(ctx, vo);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Uni<Response> update(@Context SecurityContext ctx, RoomVO vo) {
        return resUpdate(ctx, vo);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Uni<Response> delete(@Context SecurityContext ctx, @QueryParam("name") String name, @QueryParam("id") String id) {
        if (id != null) {
            return resDeleteId(ctx, id);
        }
        return resDelete(ctx, name);
    }

    @Override
    protected List<RoomVO> handleFindAll() {
        return roomService.findAll();
    }

    @Override
    protected RoomVO handleFindBy(String query, Object object) {
        Room room;
        if (query.equals("id")) {
            room = roomService.findById(String.valueOf(object));
        } else {
            room = roomService.findByName(String.valueOf(object));
        }
        var vo = RoomHelper.toVO(room);
        roomService.injectUser(room.getExistUser(), vo);
        return vo;
    }

    @Override
    protected RoomVO handleCreate(RoomVO vo) {
        var room = roomService.create(vo);
        var res = RoomHelper.toVO(room);
        roomService.injectUser(room.getExistUser(), res);
        return res;
    }

    @Override
    protected RoomVO handleUpdate(RoomVO vo) {
        var room = roomService.update(vo);
        var res = RoomHelper.toVO(room);
        roomService.injectUser(room.getExistUser(), res);
        return res;
    }

    @Override
    protected void handleDelete(String name) {
        roomService.deleteByName(name);
    }

    @Override
    protected void handleDeleteId(String id) {
        roomService.deleteById(id);
    }
}
