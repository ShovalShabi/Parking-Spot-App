start transaction;

drop schema if exists `parkingspot`;
create schema `parkingspot`;
use `parkingspot`;

drop table if exists `carTable`;
create table `carTable`
(`cid` int not null,
`manufacturer` varchar(50),
`model` varchar(50),
`color` varchar(50),
primary key (`cid`)) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

lock tables `carTable` write;
insert into `carTable` values 
(56327895,'BMW','M5','#e6804d'),
(54127895,'Mercedes','S500','#e6704d'),
(09327855,'Mazda','MX-5','#a6847d'),
(26237865,'Ford','Bronko','#c6724f'),
(31283895,'Kia','Niro','#a2304i'),
(46324756,'Skoda','Octavia','#f5674d'),
(17427895,'Renault','Megan','#e0104n'),
(58732895,'Fiat','500','#e5809c'),
(98027895,'Ferrari','458 Spider','#r8800p'),
(10227895,'Tesla','Model S','#e7904d');
unlock tables;

drop table if exists `addressTable`;
create table `addressTable`
(`address_id` int not null auto_increment,
`longtitude` double,
`latitude` double,
`country` varchar(50),
`city` varchar(50),
`street` varchar(50),
`num_house` varchar(50),
primary key(`address_id`)) engine=InnoDB auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

lock tables `addressTable` write;
insert into `addressTable` values 
(null,34.79676,32.121783000000015,'Israel','Tel Aviv','Rabin','5'),
(null,34.827928150000005,32.07874710155564,'Israel','Bnei Brak','Amiel','3'),
(null,34.798632000000005,32.120993999999996,'Israel','Tel Aviv',' Rav Ashi','9'),
(null,35.289207349999984,32.60998335687581,'Israel','Afula','HaNassi Weizmann','10'),
(null,34.8152335,32.07659050000001,'Israel','Givatayim','Katznelson','120'),
(null,34.84838580000003,32.074194156998495,'Israel','Ramat Gan','Jabotinsky','7'),
(null,34.918132900000025,32.172317952408335,'Israel','Kfar Sava','Homa UMigdal','9'),
(null,34.90823530000001,32.441455300000015,'Israel','Hadera','HaZeitim','2'),
(null,34.55827755000001,31.65590600325084,'Israel','Ashkelon','HaHistadrut','7'),
(null,34.95295789791272,29.55176429948199,'Israel','Eilat','Tsofit','2'),
(null,34.93056254228578,30.984940741434876,'Israel','Yeroham','HaRambam','5'),
(null,34.77051394999997,32.005971850857705,'Israel','Holon','Naomi Shemer','10'),
(null,35.544800497591496,32.96573756879783,'Israel','Rosh Pina','HaShalom','8'),
(null,35.197535399999985,31.789639699999988,'Israel','Jerusalem','Reines','4'),
(null,34.97162015000001,32.085393802396226,'Israel', 'Rosh HaAyin','Golda Meir','6');
unlock tables;

drop table if exists `parkingSpotTable`;
CREATE TABLE `parkingSpotTable` (
  `pid` int not null auto_increment,
  `address_id` int not null,
  primary key (`pid`),
  constraint `fk_address_id_ParkingSpotTable` foreign key (`address_id`) references `addressTable`(`address_id`)) engine=InnoDB auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

lock tables `parkingSpotTable` write;
insert into `parkingSpotTable` values 
(null,1000),(null,1001),
(null,1002),(null,1003),
(null,1004),(null,1005),
(null,1006),(null,1007),
(null,1008),(null,1009),
(null,1010),(null,1011),
(null,1012),(null,1013),
(null,1014);
unlock tables;

drop table if exists `userTable`;
create table `userTable`
(`user_id` int not null auto_increment,
`userName` varchar(50),
`email` varchar(50),
`password` varchar(50),
`cid` int,
`pid` int,
`totalLikes` int not null,
primary key (`user_id`),
constraint `fk_cid_userTable` foreign key (`cid`) references `carTable`(`cid`),
constraint `fk_address_id_userTable` foreign key (`pid`) references `parkingSpotTable`(`pid`)) engine=InnoDB auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

lock tables `userTable` write;
insert into `userTable` values 
(null,'Shoval','Shoval@gmail.com',0123456,56327895,1000,0),
(null,'Roi','Roi@gmail.com',0123456,54127895,1001,0),
(null,'tal','Tal@gmail.com',0123456,09327855,1002,0),
(null,'Dganit','Dganit@gmail.com',0123456,26237865,1003,0),
(null,'Efrat','Efrat@gmail.com',0123456,31283895,1004,0),
(null,'Effi','Effi@gmail.com',0123456,46324756,1005,0),
(null,'Matan','Matan@gmail.com',0123456,17427895,1006,0),
(null,'Daniella','Daniella@gmail.com',0123456,58732895,1007,0),
(null,'Yuval','Yuval@gmail.com',0123456,98027895,1008,0),
(null,'Dor','Dor@gmail.com',0123456,10227895,1009,0);
unlock tables;


drop table if exists `reportTable`;
create table `reportTable`
(`report_id` int not null auto_increment,
`info` varchar(1000),
`num_likes` int,
`user_id` int,
primary key(`report_id`),
constraint `fk_user_id_reportTable` foreign key (`user_id`) references `userTable`(`user_id`)) engine=InnoDB auto_increment=1000 default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

drop table if exists `address_user_parkingSpotTable`;
create table `address_user_parkingSpotTable`
(`address_id` int,
`user_id` int,
`pid` int,
constraint `fk_address_id_AUP` foreign key (`address_id`) references `addressTable`(`address_id`),
constraint `fk_user_id_AUP` foreign key (`user_id`) references `userTable`(`user_id`),
constraint `fk_pid_AUP` foreign key (`pid`) references `parkingSpotTable`(`pid`)) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

lock tables `address_user_parkingSpotTable` write;
insert into `address_user_parkingSpotTable` values 
(1000,1000,1000),
(1001,1001,1001),
(1002,1002,1002),
(1003,1003,1003),
(1004,1004,1004),
(1005,1005,1005),
(1006,1006,1006),
(1007,1007,1007),
(1008,1008,1008),
(1009,1009,1009);
unlock tables;

drop table if exists `report_user_parkingSpotTable`;
create table `report_user_parkingSpotTable`
(`report_id` int,
`user_id` int,
`pid` int,
constraint `fk_report_id_RUP` foreign key (`report_id`) references `reportTable`(`report_id`),
constraint `fk_user_id_RUP` foreign key (`user_id`) references `userTable`(`user_id`),
constraint `fk_pid_RUP` foreign key (`pid`) references `parkingSpotTable`(`pid`)) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;

commit;
