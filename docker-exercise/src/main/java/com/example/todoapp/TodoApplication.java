package com.example.todoapp;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.stream.Collectors;

public class TodoApplication {
    private static final int PORT = 8080;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final List<Todo> todos = new ArrayList<>();
    private static int nextId = 1;

    public static void main(String[] args) throws IOException {
        // Initialize with some sample data
        todos.add(new Todo(nextId++, "Learn Docker", false));
        todos.add(new Todo(nextId++, "Build a Java image", false));
        todos.add(new Todo(nextId++, "Deploy to production", false));

        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Define endpoints
        server.createContext("/", new RootHandler());
        server.createContext("/todos", new TodosHandler());
        server.createContext("/health", new HealthHandler());
        
        // Set executor
        server.setExecutor(Executors.newCachedThreadPool());
        
        // Start server
        server.start();
        
        System.out.println("🚀 Java Todo API Server is running on http://localhost:" + PORT);
        System.out.println("📋 Environment: " + System.getProperty("app.env", "development"));
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            ApiInfo info = new ApiInfo();
            info.message = "Welcome to Java Todo API!";
            info.version = "1.0.0";
            info.endpoints.put("GET /", "API information");
            info.endpoints.put("GET /todos", "Get all todos");
            info.endpoints.put("POST /todos", "Create a new todo");
            info.endpoints.put("GET /health", "Health check");

            String response = gson.toJson(info);
            sendJsonResponse(exchange, 200, response);
        }
    }

    static class TodosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            
            if ("GET".equals(method)) {
                handleGetTodos(exchange);
            } else if ("POST".equals(method)) {
                handleCreateTodo(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleGetTodos(HttpExchange exchange) throws IOException {
            String response = gson.toJson(todos);
            sendJsonResponse(exchange, 200, response);
        }

        private void handleCreateTodo(HttpExchange exchange) throws IOException {
            // Read request body
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
            BufferedReader br = new BufferedReader(isr);
            String body = br.lines().collect(Collectors.joining());
            
            try {
                CreateTodoRequest request = gson.fromJson(body, CreateTodoRequest.class);
                
                if (request.task == null || request.task.trim().isEmpty()) {
                    sendJsonResponse(exchange, 400, "{\"error\": \"Task is required\"}");
                    return;
                }
                
                Todo newTodo = new Todo(nextId++, request.task, false);
                todos.add(newTodo);
                
                String response = gson.toJson(newTodo);
                sendJsonResponse(exchange, 201, response);
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid request body\"}");
            }
        }
    }

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            HealthStatus status = new HealthStatus();
            status.status = "healthy";
            status.timestamp = java.time.Instant.now().toString();
            status.uptime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime() + "ms";

            String response = gson.toJson(status);
            sendJsonResponse(exchange, 200, response);
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, statusCode, response);
    }

    // Data classes
    static class Todo {
        int id;
        String task;
        boolean completed;

        Todo(int id, String task, boolean completed) {
            this.id = id;
            this.task = task;
            this.completed = completed;
        }
    }

    static class CreateTodoRequest {
        String task;
    }

    static class ApiInfo {
        String message;
        String version;
        java.util.Map<String, String> endpoints = new java.util.HashMap<>();
    }

    static class HealthStatus {
        String status;
        String timestamp;
        String uptime;
    }
}