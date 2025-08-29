const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');
const app = express();
const PORT = process.env.PORT || 3000;

// Database connection
const pool = new Pool({
    host: process.env.DB_HOST || 'localhost',
    port: process.env.DB_PORT || 5432,
    user: process.env.DB_USER || 'todouser',
    password: process.env.DB_PASSWORD || 'todopass',
    database: process.env.DB_NAME || 'tododb',
});

// Middleware
app.use(express.json());
app.use(cors());

// Routes
app.get('/', (req, res) => {
    res.json({ 
        message: 'Welcome to Todo API with PostgreSQL!',
        version: '2.0.0',
        endpoints: {
            'GET /todos': 'Get all todos',
            'GET /todos/:id': 'Get a specific todo',
            'POST /todos': 'Create a new todo',
            'PUT /todos/:id': 'Update a todo',
            'DELETE /todos/:id': 'Delete a todo',
            'GET /health': 'Health check'
        }
    });
});

// Get all todos
app.get('/todos', async (req, res) => {
    try {
        const result = await pool.query('SELECT * FROM todos ORDER BY id ASC');
        res.json(result.rows);
    } catch (error) {
        console.error('Error fetching todos:', error);
        res.status(500).json({ error: 'Failed to fetch todos' });
    }
});

// Get a specific todo
app.get('/todos/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const result = await pool.query('SELECT * FROM todos WHERE id = $1', [id]);
        
        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Todo not found' });
        }
        
        res.json(result.rows[0]);
    } catch (error) {
        console.error('Error fetching todo:', error);
        res.status(500).json({ error: 'Failed to fetch todo' });
    }
});

// Create a new todo
app.post('/todos', async (req, res) => {
    try {
        const { task, description } = req.body;
        
        if (!task) {
            return res.status(400).json({ error: 'Task is required' });
        }
        
        const result = await pool.query(
            'INSERT INTO todos (task, description, completed) VALUES ($1, $2, $3) RETURNING *',
            [task, description || '', false]
        );
        
        res.status(201).json(result.rows[0]);
    } catch (error) {
        console.error('Error creating todo:', error);
        res.status(500).json({ error: 'Failed to create todo' });
    }
});

// Update a todo
app.put('/todos/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { task, description, completed } = req.body;
        
        const result = await pool.query(
            'UPDATE todos SET task = COALESCE($1, task), description = COALESCE($2, description), completed = COALESCE($3, completed), updated_at = CURRENT_TIMESTAMP WHERE id = $4 RETURNING *',
            [task, description, completed, id]
        );
        
        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Todo not found' });
        }
        
        res.json(result.rows[0]);
    } catch (error) {
        console.error('Error updating todo:', error);
        res.status(500).json({ error: 'Failed to update todo' });
    }
});

// Delete a todo
app.delete('/todos/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const result = await pool.query('DELETE FROM todos WHERE id = $1 RETURNING *', [id]);
        
        if (result.rows.length === 0) {
            return res.status(404).json({ error: 'Todo not found' });
        }
        
        res.json({ message: 'Todo deleted successfully', todo: result.rows[0] });
    } catch (error) {
        console.error('Error deleting todo:', error);
        res.status(500).json({ error: 'Failed to delete todo' });
    }
});

// Health check
app.get('/health', async (req, res) => {
    try {
        await pool.query('SELECT 1');
        res.json({ 
            status: 'healthy', 
            timestamp: new Date().toISOString(),
            database: 'connected'
        });
    } catch (error) {
        res.status(503).json({ 
            status: 'unhealthy', 
            timestamp: new Date().toISOString(),
            database: 'disconnected',
            error: error.message
        });
    }
});

// Start server with retry logic for database connection
const startServer = async () => {
    let retries = 5;
    while (retries) {
        try {
            await pool.query('SELECT 1');
            console.log('✅ Database connected successfully');
            break;
        } catch (err) {
            console.log(`⏳ Waiting for database... (${6 - retries}/5)`);
            retries -= 1;
            await new Promise(res => setTimeout(res, 5000));
        }
    }
    
    app.listen(PORT, () => {
        console.log(`🚀 Server is running on http://localhost:${PORT}`);
        console.log(`📋 Environment: ${process.env.NODE_ENV || 'development'}`);
        console.log(`🗄️  Database: ${process.env.DB_HOST || 'localhost'}:${process.env.DB_PORT || 5432}`);
    });
};

startServer();