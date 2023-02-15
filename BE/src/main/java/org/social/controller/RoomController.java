package org.social.controller;

import io.smallrye.mutiny.Uni;
import org.social.helper.RoomHelper;
import org.social.model.room.Room;
import org.social.model.room.RoomVO;
import org.social.service.RoomService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rooms")
public class RoomController extends BaseController<RoomVO> {
    @Inject
    RoomService roomService;

    @GET
    public Uni<Response> getAll() {
        return resFindAll();
    }

    @GET
    @Path("/search")
    public Uni<Response> search(@QueryParam("name") String name, @QueryParam("id") Long id) {
        if (id != null) {
            return resFindBy("id", id);
        }
        return resFindBy("name", name);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> create(RoomVO vo) {
        return resCreate(vo);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(RoomVO vo) {
        return resUpdate(vo);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@QueryParam("name") String name, @QueryParam("id") String id) {
        if (id != null) {
            return resDeleteId(id);
        }
        return resDelete(name);
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
