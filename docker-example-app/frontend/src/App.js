import React, { useState, useEffect } from 'react';
import './App.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:3000';

function App() {
  const [todos, setTodos] = useState([]);
  const [newTask, setNewTask] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editTask, setEditTask] = useState('');
  const [editDescription, setEditDescription] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch todos on component mount
  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_URL}/todos`);
      if (!response.ok) throw new Error('Failed to fetch todos');
      const data = await response.json();
      setTodos(data);
      setError(null);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching todos:', err);
    } finally {
      setLoading(false);
    }
  };

  const createTodo = async (e) => {
    e.preventDefault();
    if (!newTask.trim()) return;

    try {
      const response = await fetch(`${API_URL}/todos`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ task: newTask, description: newDescription })
      });
      
      if (!response.ok) throw new Error('Failed to create todo');
      const newTodo = await response.json();
      setTodos([...todos, newTodo]);
      setNewTask('');
      setNewDescription('');
    } catch (err) {
      setError(err.message);
      console.error('Error creating todo:', err);
    }
  };

  const updateTodo = async (id) => {
    try {
      const todo = todos.find(t => t.id === id);
      const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          task: editingId === id ? editTask : todo.task,
          description: editingId === id ? editDescription : todo.description,
          completed: editingId === id ? todo.completed : !todo.completed
        })
      });
      
      if (!response.ok) throw new Error('Failed to update todo');
      const updatedTodo = await response.json();
      setTodos(todos.map(t => t.id === id ? updatedTodo : t));
      
      if (editingId === id) {
        setEditingId(null);
        setEditTask('');
        setEditDescription('');
      }
    } catch (err) {
      setError(err.message);
      console.error('Error updating todo:', err);
    }
  };

  const deleteTodo = async (id) => {
    try {
      const response = await fetch(`${API_URL}/todos/${id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) throw new Error('Failed to delete todo');
      setTodos(todos.filter(t => t.id !== id));
    } catch (err) {
      setError(err.message);
      console.error('Error deleting todo:', err);
    }
  };

  const startEditing = (todo) => {
    setEditingId(todo.id);
    setEditTask(todo.task);
    setEditDescription(todo.description || '');
  };

  const cancelEditing = () => {
    setEditingId(null);
    setEditTask('');
    setEditDescription('');
  };

  if (loading) {
    return (
      <div className="app">
        <div className="loading">Loading todos...</div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="app-header">
        <h1>📝 Todo Application</h1>
        <p className="subtitle">Powered by Docker Compose</p>
      </header>

      {error && (
        <div className="error-message">
          ⚠️ {error}
          <button onClick={() => setError(null)}>✕</button>
        </div>
      )}

      <main className="app-main">
        <form onSubmit={createTodo} className="todo-form">
          <h2>Add New Todo</h2>
          <input
            type="text"
            placeholder="Task name..."
            value={newTask}
            onChange={(e) => setNewTask(e.target.value)}
            className="input-task"
            required
          />
          <textarea
            placeholder="Description (optional)..."
            value={newDescription}
            onChange={(e) => setNewDescription(e.target.value)}
            className="input-description"
            rows="2"
          />
          <button type="submit" className="btn-add">
            Add Todo
          </button>
        </form>

        <div className="todos-container">
          <h2>Todo List ({todos.length})</h2>
          
          {todos.length === 0 ? (
            <p className="no-todos">No todos yet. Create your first one!</p>
          ) : (
            <ul className="todo-list">
              {todos.map(todo => (
                <li key={todo.id} className={`todo-item ${todo.completed ? 'completed' : ''}`}>
                  {editingId === todo.id ? (
                    <div className="todo-edit">
                      <input
                        type="text"
                        value={editTask}
                        onChange={(e) => setEditTask(e.target.value)}
                        className="edit-input"
                      />
                      <textarea
                        value={editDescription}
                        onChange={(e) => setEditDescription(e.target.value)}
                        className="edit-description"
                        rows="2"
                      />
                      <div className="edit-actions">
                        <button 
                          onClick={() => updateTodo(todo.id)}
                          className="btn-save"
                        >
                          Save
                        </button>
                        <button 
                          onClick={cancelEditing}
                          className="btn-cancel"
                        >
                          Cancel
                        </button>
                      </div>
                    </div>
                  ) : (
                    <div className="todo-content">
                      <div className="todo-header">
                        <h3 className="todo-task">{todo.task}</h3>
                      </div>
                      {todo.description && (
                        <p className="todo-description">{todo.description}</p>
                      )}
                      <div className="todo-meta">
                        <span className="todo-date">
                          Created: {new Date(todo.created_at).toLocaleDateString()}
                        </span>
                        <div className="todo-actions">
                          <button 
                            onClick={() => startEditing(todo)}
                            className="btn-edit"
                          >
                            Edit
                          </button>
                          <button 
                            onClick={() => deleteTodo(todo.id)}
                            className="btn-delete"
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                    </div>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      </main>

      <footer className="app-footer">
        <p>🐳 Running in Docker | 🗄️ PostgreSQL | ⚛️ React | 🚀 Node.js</p>
      </footer>
    </div>
  );
}

export default App;