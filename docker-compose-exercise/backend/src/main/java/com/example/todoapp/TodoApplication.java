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
import java.sql.*;

public class TodoApplication {
    private static final int PORT = 8080;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    // Database connection properties
    private static final String DB_HOST = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
    private static final String DB_PORT = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
    private static final String DB_NAME = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "tododb";
    private static final String DB_USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "todouser";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "todopass";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static void main(String[] args) throws IOException {
        // Test database connection
        try {
            testDatabaseConnection();
            System.out.println("✅ Database connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.exit(1);
        }

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
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                handleCORS(exchange);
                return;
            }
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
            
            if ("OPTIONS".equals(method)) {
                handleCORS(exchange);
                return;
            }
            
            if ("GET".equals(method)) {
                handleGetTodos(exchange);
            } else if ("POST".equals(method)) {
                handleCreateTodo(exchange);
            } else if ("DELETE".equals(method)) {
                handleDeleteTodo(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleGetTodos(HttpExchange exchange) throws IOException {
            try {
                List<Todo> todos = getAllTodos();
                String response = gson.toJson(todos);
                sendJsonResponse(exchange, 200, response);
            } catch (SQLException e) {
                sendJsonResponse(exchange, 500, "{\"error\": \"Database error: " + e.getMessage() + "\"}");
            }
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
                
                Todo newTodo = createTodo(request.task, false);
                String response = gson.toJson(newTodo);
                sendJsonResponse(exchange, 201, response);
            } catch (SQLException e) {
                sendJsonResponse(exchange, 500, "{\"error\": \"Database error: " + e.getMessage() + "\"}");
            } catch (Exception e) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid request body\"}");
            }
        }

        private void handleDeleteTodo(HttpExchange exchange) throws IOException {
            // Extract ID from URL path /todos/123
            String path = exchange.getRequestURI().getPath();
            String[] pathSegments = path.split("/");
            
            if (pathSegments.length != 3) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid URL. Use /todos/{id}\"}");
                return;
            }
            
            try {
                int todoId = Integer.parseInt(pathSegments[2]);
                
                Todo deletedTodo = deleteTodo(todoId);
                if (deletedTodo == null) {
                    sendJsonResponse(exchange, 404, "{\"error\": \"Todo not found\"}");
                    return;
                }
                
                // Return success response with deleted todo
                DeleteResponse response = new DeleteResponse();
                response.message = "Todo deleted successfully";
                response.deletedTodo = deletedTodo;
                
                String responseJson = gson.toJson(response);
                sendJsonResponse(exchange, 200, responseJson);
                
            } catch (NumberFormatException e) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid todo ID\"}");
            } catch (SQLException e) {
                sendJsonResponse(exchange, 500, "{\"error\": \"Database error: " + e.getMessage() + "\"}");
            }
        }
    }

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                handleCORS(exchange);
                return;
            }
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

    private static void handleCORS(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        // Add CORS headers to allow frontend access
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        sendResponse(exchange, statusCode, response);
    }

    // Database Methods
    private static void testDatabaseConnection() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Database URL: " + DB_URL);
        }
    }

    private static List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT id, task, completed FROM todos ORDER BY id";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Todo todo = new Todo(
                    rs.getInt("id"),
                    rs.getString("task"),
                    rs.getBoolean("completed")
                );
                todos.add(todo);
            }
        }
        return todos;
    }

    private static Todo createTodo(String task, boolean completed) throws SQLException {
        String sql = "INSERT INTO todos (task, completed) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, task);
            stmt.setBoolean(2, completed);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating todo failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new Todo(id, task, completed);
                } else {
                    throw new SQLException("Creating todo failed, no ID obtained.");
                }
            }
        }
    }

    private static Todo deleteTodo(int id) throws SQLException {
        // First, get the todo to return it in the response
        Todo todoToDelete = getTodoById(id);
        if (todoToDelete == null) {
            return null;
        }
        
        String sql = "DELETE FROM todos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0 ? todoToDelete : null;
        }
    }

    private static Todo getTodoById(int id) throws SQLException {
        String sql = "SELECT id, task, completed FROM todos WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Todo(
                        rs.getInt("id"),
                        rs.getString("task"),
                        rs.getBoolean("completed")
                    );
                }
            }
        }
        return null;
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

    static class DeleteResponse {
        String message;
        Todo deletedTodo;
    }
}