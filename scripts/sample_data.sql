DELETE FROM books WHERE true;
DELETE FROM reviews WHERE true;
DELETE FROM roles WHERE true;
DELETE FROM user_roles WHERE true;
DELETE FROM users WHERE true;

INSERT INTO users (email, password) VALUES ("apultyn@example.com", "$2a$12$G5kS20cTB/x/tGuY5.rAE.Q7WDG0UpYEUCblKXqj3mnDzBtnLlTTK");
INSERT INTO users (email, password) VALUES ("bpultyn@example.com", "$2a$12$G5kS20cTB/x/tGuY5.rAE.Q7WDG0UpYEUCblKXqj3mnDzBtnLlTTK");

INSERT INTO roles (name) VALUES ("USER"), ("ADMIN");

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (1, 2), (2, 2);

INSERT INTO books (author, title) VALUES
('J.K. Rowling', 'Harry Potter and the Philosopher\'s Stone'),
('George Orwell', '1984'),
('J.R.R. Tolkien', 'The Fellowship of the Ring'),
('F. Scott Fitzgerald', 'The Great Gatsby'),
('Harper Lee', 'To Kill a Mockingbird'),
('Jane Austen', 'Pride and Prejudice'),
('Mark Twain', 'Adventures of Huckleberry Finn'),
('Ernest Hemingway', 'The Sun Also Rises'),
('Leo Tolstoy', 'War and Peace'),
('Gabriel García Márquez', 'One Hundred Years of Solitude'),
('Herman Melville', 'Moby Dick'),
('Victor Hugo', 'Les Misérables'),
('Mary Shelley', 'Frankenstein'),
('Charles Dickens', 'A Christmas Carol'),
('Aldous Huxley', 'Brave New World'),
('C.S. Lewis', 'The Lion, the Witch and the Wardrobe'),
('Homer', 'The Iliad'),
('Dante Alighieri', 'The Divine Comedy'),
('Franz Kafka', 'The Metamorphosis'),
('Miguel de Cervantes','Don Quixote');

INSERT INTO reviews (comment, stars, book_id, user_id) VALUES 
  ('A gripping narrative that kept me turning pages well past midnight.', 5,  1, 1),
  ('Solid world-building but the pacing drags in the middle.',            3,  1, 2),
  ('Unexpectedly moving—finished it in a single sitting.',               5,  2, 2),
  ('Enjoyable pulp adventure; perfect for a lazy weekend.',              4,  3, 1),
  ('Concept is intriguing, but characters felt flat.',                   2,  4, 1),
  ('Masterful prose; every sentence sings.',                             5,  5, 2),
  ('Not my cup of tea, but I can see why others rave.',                  3,  6, 1),
  ('Quick read with a satisfying twist.',                                4,  7, 2),
  ('Too predictable; I guessed the ending halfway through.',             2,  8, 2),
  ('Beautiful illustrations elevate a mediocre story.',                  3,  9, 1),
  ('A dense but rewarding philosophical journey.',                       5, 10, 1),
  ('Plot holes galore—left me frustrated.',                              2, 11, 2),
  ('Heart-warming and funny in equal measure.',                          4, 12, 1),
  ('Ambitious in scope, but fails to stick the landing.',                3, 13, 2),
  ('A chilling dystopia that feels all too possible.',                   5, 14, 1);