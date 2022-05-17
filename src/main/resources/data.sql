-- SAMPLE BOOKS
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (1, '1000000000000', 'rig1000000000000', 'Harry Potter and Philosopher''s Stone ', 'J. K. Rowling',
        'The first novel in the Harry Potter series.', 1, 40.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (2, '1000000000001', 'rig1000000000001', 'Harry Potter and Chamber of Secrets', 'J. K. Rowling',
        'The second novel in the Harry Potter series...', 1, 30.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (3, '1000000000002', 'rig1000000000002', 'Harry Potter and Prisoner of Azkaban', 'J. K. Rowling',
        'The third novel in the Harry Potter series...', 1, 25.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (4, '1000000000003', 'rig1000000000003', 'Harry Potter and Goblet of Fire', 'J. K. Rowling',
        'The fourth novel in the Harry Potter series...', 1, 28.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (5, '1000000000004', 'rig1000000000004', 'Harry Potter and Order of the Phoenix', 'J. K. Rowling',
        'The fifth novel in the Harry Potter series...', 1, 11.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (6, '1000000000005', 'rig1000000000005', 'Harry Potter and Half-Blood Prince', 'J. K. Rowling',
        'The sixth novel in the Harry Potter series...', 1, 32.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);
insert into books (id, isbn, sku, name, author, description, stock, price, active, created_date, created_by, version)
values (7, '1000000000006', 'rig1000000000006', 'Harry Potter and Deathly Hallows', 'J. K. Rowling',
        'The seventh novel in the Harry Potter series...', 1, 32.99, true, CURRENT_TIMESTAMP(), 'init_script', 0);

-- SAMPLE USERS
insert into users (id, email, hashed_pass, first_name, last_name, active, authority, created_date, created_by)
values (1, 'user1@getir.com', '$2a$10$1UBF3v2g82jViNQ.FNHQIOikZLTmGA4JZFdXvXmnTzrFB.y/3CSr6', 'Customer1 Name',
        'Customer1 LastName', 1, 'CUSTOMER', CURRENT_TIMESTAMP(), 'init_script');
insert into users (id, email, hashed_pass, first_name, last_name, active, authority, created_date, created_by)
values (2, 'user2@getir.com', '$2a$10$OgvU.y.vWyFg6deA/RoIresz.8kDvFu9QaqdWpofTVPkHhKs6H3QK', 'BO User1 Name',
        'BO User1 LastName', 1, 'BO_USER', CURRENT_TIMESTAMP(), 'init_script');