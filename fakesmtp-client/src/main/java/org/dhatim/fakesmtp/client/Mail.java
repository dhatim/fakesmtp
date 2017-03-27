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
    private final String decodedBody;

    @JsonCreator
    public Mail(@JsonProperty("headers") Map<String, List<String>> headers, @JsonProperty("body") String body, @JsonProperty("decodedBody") String decodedBody) {
        this.headers = unmodifiable(headers);
        this.body = body;
        this.decodedBody = decodedBody;
    }

    @JsonProperty
    public String getBody() {
        return body;
    }

    @JsonProperty
    public String getDecodedBody() {
        return decodedBody;
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
        return Objects.hash(headers, body, decodedBody);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Mail) {
            Mail other = (Mail) obj;
            return Objects.equals(headers, other.headers) && Objects.equals(body, other.body) && Objects.equals(decodedBody, other.decodedBody);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        headers.forEach((name, values) -> values.forEach(value -> msg.append(name).append(": ").append(value).append('\n')));
        msg.append('\n');
        msg.append(decodedBody == null ? body : decodedBody);
        msg.append('\n');
        return msg.toString();
    }

    private static <K, V> Map<K, List<V>> unmodifiable(Map<K, List<V>> map) {
        LinkedHashMap<K, List<V>> dest = new LinkedHashMap<>();
        map.forEach((key, value) -> dest.put(key, Collections.unmodifiableList(value)));
        return Collections.unmodifiableMap(dest);
    }

}
