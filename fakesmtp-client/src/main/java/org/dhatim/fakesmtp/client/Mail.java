package org.dhatim.fakesmtp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Mail {
    
    private final HashMap<String, List<String>> headers;
    private final String body;
    
    @JsonCreator
    public Mail(@JsonProperty("headers") Map<String, List<String>> headers, @JsonProperty("body") String body) {
        this.headers = new HashMap<>(headers);
        this.body = body;
    }
    
    @JsonProperty
    public String getBody() {
        return body;
    }
    
    @JsonProperty
    public HashMap<String, List<String>> getHeaders() {
        return headers;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(headers, body);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Mail) {
            Mail other = (Mail) obj;
            return Objects.equals(headers, other.headers) && Objects.equals(body, other.body);
        }
        return false;
    }
    
}
