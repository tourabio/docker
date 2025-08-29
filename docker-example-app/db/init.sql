-- Initialize the todos database schema

-- Create todos table
CREATE TABLE IF NOT EXISTS todos (
    id SERIAL PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on completed status for faster queries
CREATE INDEX idx_todos_completed ON todos(completed);

-- Insert some sample data
INSERT INTO todos (task, description, completed) VALUES
    ('Learn Docker', 'Understand containers, images, and Docker CLI', false),
    ('Master Docker Compose', 'Learn multi-container applications and orchestration', false),
    ('Database Integration', 'Connect application with PostgreSQL database', true),
    ('Build Frontend', 'Create React UI for todo management', false),
    ('Deploy Application', 'Deploy the full stack to production', false);

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger to automatically update the updated_at column
CREATE TRIGGER update_todos_updated_at
    BEFORE UPDATE ON todos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();