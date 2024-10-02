/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reuven.websocket.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reuven.websocket.dto.Message;
import com.reuven.websocket.dto.MessageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.socket.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MessageHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private static final String DELAY_SERVICE_URI = "http://localhost:8081/api/delay/{%s}";

    private final ObjectMapper objectMapper;
    private final WsConnMng wsConnMng;
    private final RestClient restClient;

    public MessageHandler(ObjectMapper objectMapper, WsConnMng wsConnMng, RestClient restClient) {
        this.objectMapper = objectMapper;
        this.wsConnMng = wsConnMng;
        this.restClient = restClient;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();
        MessageRequest req = readValue(payload);
        logger.info("row data: {}", req);

        session.sendMessage(new TextMessage(toString(new Message(UUID.randomUUID(), req.message(), LocalDateTime.now()))));

//        // Call the delay service (blocking)
//        String responseData = restClient.get()
//                .uri(DELAY_SERVICE_URI, session.getId())
//                .retrieve()
//                .body(String.class);
//
//        logger.info("Response from delay service: {}", responseData);
//
//        // Send the response back to the client (blocking)
//        session.sendMessage(new TextMessage("Response from delay service: " + responseData));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        wsConnMng.addSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("Error occurred: {}", exception.getMessage());
        wsConnMng.removeSession(session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        wsConnMng.removeSession(session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private <T> String toString(T msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageRequest readValue(String text) {
        try {
            return objectMapper.readValue(text, MessageRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
