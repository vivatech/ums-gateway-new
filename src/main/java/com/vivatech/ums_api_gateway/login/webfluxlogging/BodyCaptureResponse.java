package com.vivatech.ums_api_gateway.login.webfluxlogging;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class BodyCaptureResponse extends ServerHttpResponseDecorator {

    private final StringBuilder body = new StringBuilder();

    public BodyCaptureResponse(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> buffer = Flux.from(body);
        return super.writeWith(buffer.doOnNext(this::capture));
    }

    private void capture(DataBuffer buffer) {
        String contentType = getDelegate().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);

        // Check if the content is text-based (like JSON, HTML, etc.)
        if (contentType != null && contentType.contains("application/json")) {
            this.body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
        } else if (contentType != null && contentType.contains("text")) {
            // For other text content types like text/plain or text/html
            this.body.append(StandardCharsets.UTF_8.decode(buffer.asByteBuffer()).toString());
        } else {
            // Log or handle binary data differently, e.g., store as base64 or simply log as a byte array
            // Example: You could log the first few bytes or store it as base64
            byte[] data = new byte[buffer.readableByteCount()];
            buffer.read(data);
            String base64Data = java.util.Base64.getEncoder().encodeToString(data);
            this.body.append("[Binary Data: " + base64Data + "]");
        }
    }

    // Method to log the captured response body
    private void logCapturedBody() {
        // Log the full response body (for debugging purposes)
        if (this.body.length() > 0) {
            // You can print it out, log it, or take some action based on the content type.
            log.info("Captured Response Body: {}", this.body.toString());
        }
    }

    // Return the captured body (can be used for further processing if needed)
    public String getFullBody() {
        return this.body.toString();
    }
}
