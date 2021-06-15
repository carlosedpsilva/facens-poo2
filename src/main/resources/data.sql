--TB_BASEUSER
INSERT INTO tb_base_user (name, email)
VALUES
  ('Person A', 'example1@email.com'),
  ('Person B', 'example2@email.com'),
  ('Person C', 'example3@email.com'),
  ('Person D', 'example4@email.com'),
  ('Person E', 'example5@email.com'),
  ('Person F', 'example6@email.com');

--TB_ADMIN
INSERT INTO tb_admin (phone_number, base_user_id)
VALUES
  ('(99) 99999-9999', 1),
  ('(99) 99999-9999', 2),
  ('(99) 99999-9999', 3);

--TB_ATTENDEE
Insert INTO tb_attendee (balance, base_user_id)
VALUES
  (0.0, 4),
  (0.0, 5),
  (0.0, 6);

--TB_PLACE
INSERT INTO tb_place (name, address)
VALUES
    ('SomePlace',    'Address A'),
    ('AnotherPlace', 'Address B'),
    ('ThirdPlace',   'Address C');

--TB_EVENT
INSERT INTO tb_event (admin_id, name, description, start_date, end_date,
    start_time, end_time, email, amount_free_tickets_available, amount_paid_tickets_available, ticket_price)
VALUES
  (1, 'Event A', 'OptionalDescription', '2022-03-11', '2022-03-15', '11:00', '15:00', 'example@email.com', 3, 10, 200),
  (2, 'Event B', 'OptionalDescription', '2022-03-10', '2022-03-12', '11:00', '15:00', 'example@email.com', 3,  5, 300),
  (3, 'Event C', 'OptionalDescription', '2022-03-14', '2022-03-16', '11:00', '15:00', 'example@email.com', 3,  7, 500);
