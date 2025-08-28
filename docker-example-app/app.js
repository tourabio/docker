const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.json());

// In-memory storage for demo
let todos = [
    { id: 1, task: 'Learn Docker', completed: false },
    { id: 2, task: 'Build an image', completed: false }
];

// Routes
app.get('/', (req, res) => {
    res.json({ 
        message: 'Welcome to Todo API!',
        version: '1.0.0',
        endpoints: {
            'GET /todos': 'Get all todos',
            'POST /todos': 'Create a new todo',
            'GET /health': 'Health check'
        }
    });
});

app.get('/todos', (req, res) => {
    res.json(todos);
});

app.post('/todos', (req, res) => {
    const { task } = req.body;
    if (!task) {
        return res.status(400).json({ error: 'Task is required' });
    }
    
    const newTodo = {
        id: todos.length + 1,
        task,
        completed: false
    };
    
    todos.push(newTodo);
    res.status(201).json(newTodo);
});

app.get('/health', (req, res) => {
    res.json({ status: 'healthy', timestamp: new Date().toISOString() });
});

// Start server
app.listen(PORT, () => {
    console.log(`🚀 Server is running on http://localhost:${PORT}`);
    console.log(`📋 Environment: ${process.env.NODE_ENV || 'development'}`);
});