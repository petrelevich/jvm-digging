package ru.demo.webserver;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.ByteBuffer;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.datasrc.ServiceRedis;

public class ApiHandler extends Handler.Abstract.NonBlocking {
    private static final Logger log = LoggerFactory.getLogger(ApiHandler.class);

    private final ServiceRedis serviceRedis;

    public ApiHandler(ServiceRedis serviceRedis) {
        this.serviceRedis = serviceRedis;
        __nonBlocking.set(true);
    }

    @Override
    public InvocationType getInvocationType() {
        return InvocationType.NON_BLOCKING;
    }

    @Override
    public boolean handle(Request request, Response response, Callback callback) {
        var id = getParamId(request);
        log.info("find id:{}", id);
        response.setStatus(HttpStatus.OK_200);
        makeResponseContent(id, response, callback);
        return true;
    }

    private long getParamId(Request request) {
        var valAsString =
                request.getHttpURI().getPath().replace(request.getContext().getContextPath() + "/", "");
        return Long.parseLong(valAsString);
    }

    private void makeResponseContent(long id, Response response, Callback callback) {
        serviceRedis.action(id, respContent -> {
            log.info("find id:{}, response:{}", id, respContent);
            write(response, callback, respContent);
        });
    }

    private void write(Response response, Callback callback, String responseContent) {
        byte[] bytes = responseContent.getBytes(UTF_8);
        long contentLength = bytes.length;

        HttpFields.Mutable responseHeaders = response.getHeaders();
        responseHeaders.put(HttpHeader.CONTENT_TYPE, "application/json; charset=UTF-8");
        responseHeaders.put(HttpHeader.CONTENT_LENGTH, contentLength);
        response.write(true, ByteBuffer.wrap(bytes), callback);
    }
}
