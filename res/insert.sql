--Data

--box data
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (1,TRUE,'Stroh',FALSE,10.0,'A', 10.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (2,FALSE,'Pellets',FALSE,5.2,'C', 5.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (3,FALSE,'Pellets',FALSE,8.3,'B', 7.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (4,FALSE,'Stroh',TRUE,7.5,'B', 3.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (5,TRUE,'Stroh',FALSE,4.3,'A', 4.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (6,FALSE,'Pellets',TRUE,8.9,'B', 2.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (7,TRUE,'Stroh',FALSE,12.2,'C', 25.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (8,FALSE,'Pellets',TRUE,7.2,'C', 15.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (9,FALSE,'Stroh',FALSE,2.2,'C', 6.00, FALSE);
INSERT INTO box (box_id,window, litter, inside, box_size,area, daily_rate, isDeleted)VALUES (10,FALSE,'Stroh',TRUE,7.7,'A', 9.00, FALSE);

--reservation data
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (1, 'Thomas', '2016-02-28', '2016-03-30', FALSE); --32days Mon:5 Tue:5 Wed:5 Thu:4 Fri:4 Sat:4 Sun:5
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (2, 'Dominik', '2015-03-12', '2015-04-20', FALSE); --40days Mon:6 Tue:5 Wed:5 Thu:6 Fri:6 Sat:6 Sun:6
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (3, 'Aleks', '2016-04-30', '2016-05-02', FALSE); --3days Mon:1 Tue:0 Wed:0 Thu:0 Fri:0 Sat:1 Sun:1
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (4, 'Martin', '2015-07-28', '2015-09-30', FALSE); --65days Mon:9 Tue:10 Wed:10 Thu:9 Fri:9 Sat9: Sun:9
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (5, 'Andreas', '2017-04-29', '2017-05-10', FALSE); --12days Mon:2 Tue:2 Wed:2 Thu:1 Fri:1 Sat:2 Sun:2
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (6, 'Hanna', '2017-11-29', '2017-12-05', FALSE); --7days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:1 Sun:1
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (7, 'Teresa', '2018-02-02', '2018-02-08', FALSE); --7days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:1 Sun:1
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (8, 'Johanna', '2018-03-03', '2018-03-10', FALSE); --8days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:2 Sun:1
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (9, 'Lukas', '2018-04-04', '2018-04-05', FALSE); --2days Mon:0 Tue:0 Wed:1 Thu:1 Fri:0 Sat:0 Sun:0
INSERT INTO reservation(res_id, customer, res_from, res_to, res_isDeleted) VALUES (10, 'Kata', '2018-12-01', '2018-12-03', FALSE); --3days Mon:1 Tue:0 Wed:0 Thu:0 Fri:0 Sat:1 Sun:1


--box_res data
INSERT INTO box_res (box_id, res_id, price, horse) VALUES (1,1, 320.0, 'Lisa');   --32days Mon:5 Tue:5 Wed:5 Thu:4 Fri:4 Sat:4 Sun:5
INSERT INTO box_res (box_id, res_id, price, horse) VALUES (2,1, 160.0, 'Hannah'); --32days Mon:5 Tue:5 Wed:5 Thu:4 Fri:4 Sat:4 Sun:5
INSERT INTO box_res (box_id, res_id, price, horse) VALUES (6,1, 64.0, 'Ida');     --32days Mon:5 Tue:5 Wed:5 Thu:4 Fri:4 Sat:4 Sun:5

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (3,2, 280,'Nina');      --40days Mon:6 Tue:5 Wed:5 Thu:6 Fri:6 Sat:6 Sun:6

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (1,3, 30.0,'Chris');    --3days Mon:1 Tue:0 Wed:0 Thu:0 Fri:0 Sat:1 Sun:1

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (9,4, 390.0,'Schmidi'); --65days Mon:9 Tue:10 Wed:10 Thu:9 Fri:9 Sat9: Sun:9
INSERT INTO box_res (box_id, res_id, price, horse) VALUES (10,4, 585.0,'Luki');   --65days Mon:9 Tue:10 Wed:10 Thu:9 Fri:9 Sat9: Sun:9

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (5,5, 48.0,'Marty');    --12days Mon:2 Tue:2 Wed:2 Thu:1 Fri:1 Sat:2 Sun:2

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (4,6, 21.0,'Felix');    --7days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:1 Sun:1

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (6,7, 14.0,'Benno');    --7days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:1 Sun:1

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (7,8, 200.0,'Rocky');   --8days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:2 Sun:1
INSERT INTO box_res (box_id, res_id, price, horse) VALUES (9,8, 48.0,'Minka');    --8days Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:2 Sun:1

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (1,9, 20.0,'Ina');      --2days Mon:0 Tue:0 Wed:1 Thu:1 Fri:0 Sat:0 Sun:0

INSERT INTO box_res (box_id, res_id, price, horse) VALUES (1,10, 30.0, 'Luna');   --3days Mon:1 Tue:0 Wed:0 Thu:0 Fri:0 Sat:1 Sun:1

--Box1: Sum:40 Mon:7 Tue:5 Wed:6 Thu:5 Fri:4 Sat:6 Sun:7
--Box2: Sum:32 Mon:5 Tue:5 Wed:5 Thu:4 Fri:4 Sat:4 Sun:5
--Box3: Sum:40 Mon:6 Tue:5 Wed:5 Thu:6 Fri:6 Sat:6 Sun:6
--Box4: Sum:7 Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:1 Sun:1
--Box5: Sum:12 Mon:2 Tue:2 Wed:2 Thu:1 Fri:1 Sat:2 Sun:2
--Box6: Sum:39 Mon:6 Tue:6 Wed:6 Thu:5 Fri:5 Sat:5 Sun:6
--Box7: Sum:8 Mon:1 Tue:1 Wed:1 Thu:1 Fri:1 Sat:2 Sun:1
--Box8: Sum:0 Mon:0 Tue:0 Wed:0 Thu:0 Fri:0 Sat:0 Sun:0
--Box9: Sum:73 Mon:10 Tue:11 Wed:11 Thu:10 Fri:10 Sat:11 Sun:10
--Box10: Sum:65 Mon:9 Tue:10 Wed:10 Thu:9 Fri:9 Sat9: Sun:9
--Sum: 316


