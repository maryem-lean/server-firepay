package com.example.firepay.service.lean.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
public class EventMeta {
    String type;
    String message;
    String timestamp;
    UUID eventId;
    JsonNode payload;
}
