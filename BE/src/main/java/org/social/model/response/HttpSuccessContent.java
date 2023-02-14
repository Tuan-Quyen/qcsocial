package org.social.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HttpSuccessContent extends HttpResult{
    private Object data;
}
