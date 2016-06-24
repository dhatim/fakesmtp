package org.dhatim.fakesmtp.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Mail {
    
    private final Map<String, List<String>> headers;
    private final String body;
    
    @JsonCreator
    public Mail(@JsonProperty("headers") Map<String, List<String>> headers, @JsonProperty("body") String body) {
        this.headers = unmodifiable(headers);
        this.body = body;
    }
    
    @JsonProperty
    public String getBody() {
        return body;
    }
    
    @JsonProperty
    public Map<String, List<String>> getHeaders() {
        return headers;
    }
    
    public String getFirstHeaderValue(String name) {
        List<String> list = headers.get(name);
        return (list == null || list.isEmpty()) ? null : list.get(0);
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
    
    private static <K, V> Map<K, List<V>> unmodifiable(Map<K, List<V>> map) {
        LinkedHashMap<K, List<V>> dest = new LinkedHashMap<>();
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            dest.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(dest);
    }
    
}
