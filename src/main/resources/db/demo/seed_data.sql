INSERT INTO users(id, first_name, last_name, birth_date, email,street_line_1, city, country, postcode, created_date, updated_date)
VALUES
('9a306cb4-0c4f-4ce6-8a49-043fc10bc210','John', 'Doe',DATE '2000-12-17', 'john.doe@gmail.com', 'Powstańców', 'Kraków', 'PL', '21234', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP ),
('9a306cb4-0c4f-4ce6-8a49-043fc10bc211','Mark', 'Whisky',DATE '1990-12-17', 'mark.whisky@gmail.com','Generałów', 'Kraków', 'PL', '12345',  CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP),
('9a306cb4-0c4f-4ce6-8a49-043fc10bc212','Smith', 'Simpson',DATE '1960-12-17','smit.simpson@gmail.com', 'Pływaków', 'Warszawa', 'PL', '11223',  CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP);


INSERT INTO public.user_cars(id, user_id, plate, car_size, created_date, updated_date)
VALUES
('9a3b820f-9d46-4d72-bd4a-bf5d234991e1','9a306cb4-0c4f-4ce6-8a49-043fc10bc210','KNS5115C','LARGE', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('9a3b820f-9d46-4d72-bd4a-bf5d234991e2','9a306cb4-0c4f-4ce6-8a49-043fc10bc210','KNS5114C', 'COMPACT', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('9a3b820f-9d46-4d72-bd4a-bf5d234991e3','9a306cb4-0c4f-4ce6-8a49-043fc10bc211','KNS5116C', 'MEDIUM', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('9a3b820f-9d46-4d72-bd4a-bf5d234991e4','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','KK60672', 'COMPACT', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('9a3b820f-9d46-4d72-bd4a-bf5d234991e5','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','KR23311', 'COMPACT', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('9a3b820f-9d46-4d72-bd4a-bf5d234991e6','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','P1RETI', 'COMPACT', CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP );

INSERT INTO parkings(id,name,type, street_line_1, street_line_2, city, country, postcode, created_date,updated_date)
VALUES
('3288c23c-5077-40e1-9d54-c11263c813e1','parking outdoor', 'OUTDOOR','Paniczna','21/2','Kraków','PL', '33112',CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP ),
('3288c23c-5077-40e1-9d54-c11263c813e2','parking indoor', 'INDOOR','Spawalnicza','22/4b','Warszawa','PL', '41231',CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP );

INSERT INTO parking_slots(id, parking_id, size, availability, created_date, updated_date)
VALUES
('002f7a20-3bd4-4633-9388-ca6b091b0c70','3288c23c-5077-40e1-9d54-c11263c813e1','SMALL','NOT_AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c71','3288c23c-5077-40e1-9d54-c11263c813e1','LARGE','AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c72','3288c23c-5077-40e1-9d54-c11263c813e2','SMALL','AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c73','3288c23c-5077-40e1-9d54-c11263c813e2','MEDIUM','AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c74','3288c23c-5077-40e1-9d54-c11263c813e2','LARGE','NOT_AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c75','3288c23c-5077-40e1-9d54-c11263c813e2','MEDIUM','NOT_AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c76','3288c23c-5077-40e1-9d54-c11263c813e2','MEDIUM','NOT_AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('002f7a20-3bd4-4633-9388-ca6b091b0c77','3288c23c-5077-40e1-9d54-c11263c813e2','MEDIUM','NOT_AVAILABLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO reservations(id,user_id, parking_slot_id, user_car_id,state, started_date, ended_date, created_date,updated_date)
VALUES
    ('94c57267-05f0-4d6e-8597-e09e82de1060','9a306cb4-0c4f-4ce6-8a49-043fc10bc210','002f7a20-3bd4-4633-9388-ca6b091b0c74','9a3b820f-9d46-4d72-bd4a-bf5d234991e1', 'RESERVED', CURRENT_TIMESTAMP + interval '10 minutes', CURRENT_TIMESTAMP + interval '3 hours', CURRENT_TIMESTAMP - interval '3 hours' ,CURRENT_TIMESTAMP - interval '3 hours'),
    ('94c57267-05f0-4d6e-8597-e09e82de1061','9a306cb4-0c4f-4ce6-8a49-043fc10bc210','002f7a20-3bd4-4633-9388-ca6b091b0c73','9a3b820f-9d46-4d72-bd4a-bf5d234991e2', 'CANCELED', CURRENT_TIMESTAMP - interval '8 hours', CURRENT_TIMESTAMP - interval '6 hours', CURRENT_TIMESTAMP - interval '9 hours',CURRENT_TIMESTAMP - interval '8 hours'),
    ('94c57267-05f0-4d6e-8597-e09e82de1062','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','002f7a20-3bd4-4633-9388-ca6b091b0c75','9a3b820f-9d46-4d72-bd4a-bf5d234991e4', 'RESERVED', CURRENT_TIMESTAMP - interval '5 hours', CURRENT_TIMESTAMP + interval '6 hours', CURRENT_TIMESTAMP - interval '9 hour',CURRENT_TIMESTAMP - interval '8 hours'),
    ('94c57267-05f0-4d6e-8597-e09e82de1063','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','002f7a20-3bd4-4633-9388-ca6b091b0c76','9a3b820f-9d46-4d72-bd4a-bf5d234991e5', 'IN_USE', CURRENT_TIMESTAMP - interval '8 hours', CURRENT_TIMESTAMP +interval '10 minutes', CURRENT_TIMESTAMP - interval '9 hours',CURRENT_TIMESTAMP - interval '8 hours'),
    ('94c57267-05f0-4d6e-8597-e09e82de1064','9a306cb4-0c4f-4ce6-8a49-043fc10bc212','002f7a20-3bd4-4633-9388-ca6b091b0c77','9a3b820f-9d46-4d72-bd4a-bf5d234991e6', 'IN_USE', CURRENT_TIMESTAMP - interval '8 hours', CURRENT_TIMESTAMP - interval '3 hours', CURRENT_TIMESTAMP - interval '9 hours',CURRENT_TIMESTAMP - interval '8 hours');

INSERT INTO outbox(id,reservation_id, event_type, processed, created_date)
VALUES
('00402b8b-15ee-43ff-8ec8-f17f269ae8d0','30289328-c957-4865-9365-18daa8b4d21d','RESERVATION_START_REMINDER','TRUE', CURRENT_TIMESTAMP),
('00402b8b-15ee-43ff-8ec8-f17f269ae8d1','30289328-c957-4865-9365-18daa8b4d21d','RESERVATION_END_REMINDER','FALSE', CURRENT_TIMESTAMP),
('00402b8b-15ee-43ff-8ec8-f17f269ae8d2','30289328-c957-4865-9365-18daa8b4d21a','RESERVATION_OCCUPIED','FALSE', CURRENT_TIMESTAMP),
('00402b8b-15ee-43ff-8ec8-f17f269ae8d3','30289328-c957-4865-9365-18daa8b4d21a','RESERVATION_START_REMINDER','FALSE', CURRENT_TIMESTAMP);
