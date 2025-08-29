-- Initialize the todos database for Java application

USE tododb;

-- Create todos table (simplified to match backend structure)
CREATE TABLE IF NOT EXISTS todos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    completed BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create index for better query performance
CREATE INDEX idx_completed ON todos(completed);

-- Insert sample data (simplified to match backend structure)
INSERT INTO todos (task, completed) VALUES
    ('Complete Docker Compose Exercise', FALSE),
    ('Test MySQL Connection', FALSE),
    ('Configure Networks', FALSE),
    ('Add Health Checks', TRUE),
    ('Deploy Application', FALSE);

-- Display confirmation
SELECT COUNT(*) as total_todos FROM todos;