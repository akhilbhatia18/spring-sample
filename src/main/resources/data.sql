MERGE INTO students (id, name, email, course) KEY (id) VALUES
    (1, 'Alex Student', 'alex.student@example.test', 'Computer Science'),
    (2, 'Jordan Student', 'jordan.student@example.test', 'Mathematics'),
    (3, 'Taylor Student', 'taylor.student@example.test', 'Physics');

MERGE INTO users (username, password, enabled) KEY (username) VALUES
    ('student', '$2a$10$YBjsU.DqQqUmfKFm1WaI7uLo8UBt00RvtZigxi9Z.lXec3XwoBIvO', TRUE);

MERGE INTO authorities (username, authority) KEY (username, authority) VALUES
    ('student', 'ROLE_USER');
