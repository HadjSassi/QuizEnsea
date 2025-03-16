-- init.sql: MySQL initialization script

-- Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS QuizENSEA;

-- Create the user if it doesn't already exist
CREATE USER IF NOT EXISTS 'quiz_user'@'localhost' IDENTIFIED BY 'quiz_password';

-- Grant all privileges on the new database to the user
GRANT ALL PRIVILEGES ON QuizENSEA.* TO 'quiz_user'@'localhost';

-- Apply the changes immediately
FLUSH PRIVILEGES;