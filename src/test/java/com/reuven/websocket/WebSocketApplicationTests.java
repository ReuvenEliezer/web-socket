//package com.reuven.websocket;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.reuven.websocket.dto.Message;
//import com.reuven.websocket.dto.MessageRequest;
//import jakarta.annotation.PostConstruct;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.StopWatch;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.handler.AbstractWebSocketHandler;
//
//
//import java.net.URI;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.IntStream;
//
////@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = WebSocketApplication.class)
//class WebSocketApplicationTests {
//
//    private static final Logger logger = LoggerFactory.getLogger(WebSocketApplicationTests.class);
//
//    private final WebSocketClient client = new StandardWebSocketClient();
//    private static final String WEB_SOCKET_URI_STR = "ws://localhost:%s/ws/messages";
//    private static URI WEB_SOCKET_URI;
//
//    @Value("${server.port}")
//    private int port;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private WebSocketHandler webSocketHandler;
//
//    @PostConstruct
//    public void init() {
//        WEB_SOCKET_URI = URI.create(String.format(WEB_SOCKET_URI_STR, port));
//    }
//
//
//    @Test
//    public void testReactiveWebSocketPerformance() throws Exception {
//        int totalConnections = 3;
//        int totalMessagesForEachConnection = 2;
//
////        PerformanceMonitor performanceMonitor = new PerformanceMonitor();
////        performanceMonitor.startMonitoring(20);
//        CountDownLatch latch = new CountDownLatch(totalConnections * totalMessagesForEachConnection);
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//
//        IntStream.range(0, totalConnections)
//                .forEach(connNum -> {
//                    CompletableFuture<WebSocketSession> webSocketSessionFuture = client.execute(webSocketHandler, null, WEB_SOCKET_URI);
//                    try {
//                        WebSocketSession webSocketSession = webSocketSessionFuture.get();
//                        latch.countDown();
//                        for (int msgNum = 0; msgNum < totalMessagesForEachConnection; msgNum++) {
//                            String msg = toString(new MessageRequest("Test Message " + msgNum + " for connection: " + connNum));
//                            webSocketHandler.handleMessage(webSocketSession, new TextMessage(msg));
//                        }
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//        latch.await(60, TimeUnit.SECONDS);
//
//        stopWatch.stop();
//
//        logger.info("Reactive WebSocket took: {}, shortSummary: {}", stopWatch.prettyPrint(), stopWatch.shortSummary());
//    }
//
//
//    @Test
//    public void testReactiveWebSocketPerformance1() throws InterruptedException {
//        int totalConnections = 3;
//        CountDownLatch latch = new CountDownLatch(totalConnections); // להמתין לסיום כל החיבורים
//        IntStream.range(0, totalConnections)
//                .forEach(connNum -> {
//                    client.execute(webSocketHandler, null, WEB_SOCKET_URI)
//                            .thenAccept(webSocketSession -> {
//                                try {
//                                    String msg = toString(new MessageRequest("Test Message for connection: " + connNum));
//                                    logger.info("Sending message: {}", msg);
//                                    webSocketHandler.handleMessage(webSocketSession, new TextMessage(msg));
//                                } catch (Exception e) {
//                                    logger.error("Error sending message: {}", e.getMessage());
//                                } finally {
//                                    latch.countDown(); // לסמן שסיימנו עם החיבור הנוכחי
//                                }
//                            }).exceptionally(ex -> {
//                                logger.error("Error during WebSocket connection: {}", ex.getMessage());
//                                latch.countDown(); // לסמן שסיימנו גם במקרה של שגיאה
//                                return null;
//                            });
//                });
//        latch.await(60, TimeUnit.SECONDS);
//
//    }
//
//    @Test
//    public void testReactiveWebSocketPerformance2() throws InterruptedException, ExecutionException {
//        int totalConnections = 3;
//        CountDownLatch latch = new CountDownLatch(totalConnections);
//        CompletableFuture<WebSocketSession> webSocketSessionFuture = client.execute(webSocketHandler, null, WEB_SOCKET_URI);
//        WebSocketSession webSocketSession = webSocketSessionFuture.get();
////        webSocketSessionFuture.thenAccept(
////                webSocketSession
//            try {
//                String msg = toString(new MessageRequest("Test Message "));
//                webSocketSession.sendMessage(new TextMessage(msg));
//                Thread.sleep(100); // המתן בין שליחת ההודעות
//            } catch (InterruptedException e) {
//                logger.error("Thread was interrupted: {}", e.getMessage());
//            } catch (Exception e) {
//                logger.error("Error sending message: {}", e.getMessage());
//            } finally {
//                latch.countDown(); // לסמן שסיימנו עם החיבור הנוכחי
//            }
////        });
//
//        // להמתין שכל החיבורים יסתיימו
//        if (!latch.await(30, TimeUnit.SECONDS)) {
//            logger.error("Not all connections completed in time");
//        }
//    }
//
//    private URI buildUri() {
//        return URI.create("ws://localhost:" + port + "/ws/messages");
//    }
//
//
//    private String toString(MessageRequest msg) {
//        try {
//            return objectMapper.writeValueAsString(msg);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
