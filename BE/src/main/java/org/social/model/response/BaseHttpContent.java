package org.social.model.response;

import javax.ws.rs.core.Response;

public class BaseHttpContent {
    public Response fetched(Object data) {
        var content = new HttpSuccessContent();
        content.setStatusCode(200);
        content.setMessage("Fetched");
        content.setData(data);
        return Response.ok(content).build();
    }

    public Response created(Object data) {
        var content = new HttpSuccessContent();
        content.setStatusCode(201);
        content.setMessage("Created");
        content.setData(data);
        return Response.ok(content).build();
    }

    public Response notFound(String errorMsg) {
        var content = new HttpErrorContent();
        content.setStatusCode(404);
        content.setMessage("Not Found");
        content.setErrorMsg(errorMsg);
        return Response.status(404).entity(content).build();
    }

    public Response conflict(String errorMsg) {
        var content = new HttpErrorContent();
        content.setStatusCode(409);
        content.setMessage("Conflict Error");
        content.setErrorMsg(errorMsg);
        return Response.status(409).entity(content).build();
    }

    public Response internal(String errorMsg) {
        var content = new HttpErrorContent();
        content.setStatusCode(500);
        content.setMessage("Internal Server Error");
        content.setErrorMsg(errorMsg);
        return Response.status(500).entity(content).build();
    }

    public Response unAuthorized(String errorMsg) {
        var content = new HttpErrorContent();
        content.setStatusCode(401);
        content.setMessage("Unauthorized");
        content.setErrorMsg(errorMsg);
        return Response.status(401).entity(content).build();
    }
}
