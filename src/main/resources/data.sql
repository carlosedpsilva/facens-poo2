--TB_BASEUSER
INSERT INTO tb_base_user (name, email)
VALUES
  ('Person A', 'example@email.com'),
  ('Person B', 'example@email.com'),
  ('Person C', 'example@email.com'),
  ('Person D', 'example@email.com'),
  ('Person E', 'example@email.com'),
  ('Person F', 'example@email.com');

--TB_ADMIN
INSERT INTO tb_admin (phone_number, base_user_id)
VALUES
  ('(99)999999999', 1),
  ('(99)999999999', 2),
  ('(99)999999999', 3);

--TB_ATTENDEE
Insert INTO tb_attendee (balance, base_user_id)
VALUES
  (500.0, 4),
  (500.0, 5),
  (500.0, 6);

--TB_PLACE
INSERT INTO tb_place (name, address)
VALUES
    ('SomePlace', 'Address A'),
    ('AnotherPlace', 'Address B'),
    ('ThirdPlace', 'Address C');

--TB_EVENT
INSERT INTO tb_event (admin_base_user_id, name, description, start_date, end_date, start_time, end_time, email_contact, amount_free_tickets, amount_payed_tickets, price_ticket)
VALUES
  (1, 'Event A', '', '2022-03-11', '2022-03-15', '11:00', '15:00', 'example@email.com', 0, 10000, 200),
  (2, 'Event B', '', '2022-03-10', '2022-03-12', '11:00', '15:00', 'example@email.com', 0, 5000, 300),
  (3, 'Event C', '', '2022-03-14', '2022-03-16', '11:00', '15:00', 'example@email.com', 0, 7000, 500);
