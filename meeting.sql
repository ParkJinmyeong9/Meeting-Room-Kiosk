SET GLOBAL max_connections = 5000;

/*사용자 id(pk), pw, name, tel, money*/
CREATE TABLE `MEMBER` (
	`member_id` VARCHAR(100) PRIMARY KEY,
	`member_pw` VARCHAR(100) NOT NULL,
	`member_name` VARCHAR(20) NOT NULL,
	`member_tel` VARCHAR(20) NOT NULL,
	`member_money` INT NULL,
	`member_level` INT NOT NULL
);

/*음식 정보 food(pk - f01, f02, ...), 음식명, 가격, 분류*/
CREATE TABLE `FOOD`(
	`food` VARCHAR(10) PRIMARY KEY,
	`food_name` VARCHAR(100) NOT NULL,
	`food_price` INT NOT NULL
);

/*음식 정보 추가*/
ALTER TABLE `FOOD` ADD COLUMN `food_type` ENUM('카페', '음료','면류','과자','밥류','술');

/*게임 정보 game(pk - g01, g02, ...), 게임명, 가격, 대여 가능 여부(TRUE, false)*/
CREATE TABLE `GAME` (
	`game` VARCHAR(10) PRIMARY KEY,
	`game_name` VARCHAR(100) NOT NULL,
	`game_person` INT NOT NULL,
	`game_price` INT NOT NULL,
	`game_state` BOOLEAN NOT NULL
);

/*게임 정보 추가*/
ALTER TABLE `GAME` ADD COLUMN `game_type` ENUM('보드게임', '카드게임','롤플레잉','전략게임','협동게임','퍼즐게임');

/*룸 정보 room(pk - R01, R02, ...), 룸 최대 인원, 룸 가격, 인원당 추가 가격*/
CREATE TABLE `ROOM` (
	`room` VARCHAR(10) PRIMARY KEY,
	`room_person` INT NOT NULL,
	`room_price` INT NOT NULL,
	`room_add` INT NULL,
	`room_state` BOOLEAN NOT NULL
);

/*룸 매출 use(pk 순번), 룸 번호, 예약자 id, 이용 날짜, 시작 시간, 종료 시간, 총 가격*/
CREATE TABLE `USE` (
	`use` INT AUTO_INCREMENT PRIMARY KEY,
	`room_num` VARCHAR(10) NOT NULL,
	`use_id` VARCHAR(20) NOT NULL,
	`use_day` DATE NOT NULL,
	`in_time` DATETIME NOT NULL,
	`out_time` DATETIME NOT NULL,
	`food_total` INT NULL,
	`game_total` INT NULL,
	`room_total` INT NOT NULL,
	`use_total` INT NOT NULL
);

/*룸 예약 resv(pk 순번), 예약자 id, 이름, 전화번호, 이용할 자리, 예약 날짜, 이용할 시간, 인원, 총 가격, 예약자 도착 여부*/
CREATE TABLE `RESERVE` (
	`resv` INT AUTO_INCREMENT PRIMARY KEY,
	`resv_id` VARCHAR(100) NOT NULL,
	`resv_name` VARCHAR(20) NOT NULL,
	`resv_tel` VARCHAR(20) NOT NULL,
	`resv_room` VARCHAR(10) NOT NULL,
	`resv_time` DATETIME NOT NULL,
	`resv_usetime` INT NOT NULL,
	`resv_person` INT NOT NULL,
	`resv_total` INT NOT NULL,
	`resv_state` BOOLEAN DEFAULT FALSE
);

/*보드게임 매출 game_num(pk 순번), 대여한 룸, 매출 날짜, 대여한 게임, 매출 금액, 게임 대여 여부*/
CREATE TABLE `GAME_SALES` (
	`game_num` INT AUTO_INCREMENT PRIMARY KEY,
	`game_room` VARCHAR(10) NOT NULL,
	`game_id` VARCHAR(100) NOT NULL,
	`sales_day` DATETIME NOT NULL,
	`games` VARCHAR(10) NOT NULL,
	`game_count` INT NOT NULL,
	`sales_amount` INT NOT NULL,
	`game_state` BOOLEAN DEFAULT FALSE,
	
	FOREIGN KEY `GAME_SALES_FK1`(games) REFERENCES `GAME`(`game`)
);

/*음식 매출 food_num(pk 순번), 대여한 룸, 매출 날짜, 주문한 음식, 주문한 음식 수, 매출 금액, 주문 요청사항, 조리 완료 여부*/
CREATE TABLE `FOOD_SALES` (
	`food_num` INT AUTO_INCREMENT PRIMARY KEY,
	`food_room` VARCHAR(10) NOT NULL,
	`food_id` VARCHAR(100) NOT NULL,
	`sales_day` DATETIME NOT NULL,
	`foods` VARCHAR(10) NOT NULL,
	`food_count` INT NOT NULL,
	`sales_amount` INT NOT NULL,
	`food_state` BOOLEAN DEFAULT FALSE,
	
	FOREIGN KEY `FOOD_SALES_FK1`(foods) REFERENCES `FOOD`(`food`) 
);

CREATE TABLE `ORDER` (
	`order_num` INT AUTO_INCREMENT PRIMARY KEY,
	`order_id` VARCHAR(100) NOT NULL,
	`order_room` VARCHAR(10) NOT NULL,
	`order_time` DATETIME NOT NULL,
	`order_total` INT NOT NULL,
	`order_request` VARCHAR(500) NULL,
	`order_state` BOOLEAN DEFAULT FALSE
);

/*member와 admin에 임의 계정 추가*/
INSERT INTO member VALUES ('user', 'G6u4hrCP0hrYjCQaNRx84g==', '김자바', '010-1234-1234', '100000', '1');
INSERT INTO member VALUES ('admin', 'mKojPjckDN7Lmfd8jMTb4w==', '최쿼리', '011-4321-4321', NULL, '2');

/*보드 게임 정보 추가*/
INSERT INTO game VALUES ('G01', '셜록13', '2', '3000', TRUE, "보드게임");
INSERT INTO game VALUES ('G02', '피나타', '2', '3000', TRUE, "보드게임");
INSERT INTO game VALUES ('G03', '13클루', '4', '10000', TRUE, "보드게임");
INSERT INTO game VALUES ('G04', '시즌스', '2', '20000', TRUE, "보드게임");
INSERT INTO game VALUES ('G05', '부루마블', '4', '3000', TRUE, "보드게임");
INSERT INTO game VALUES ('G06', '호텔의 제왕', '2', '10000', TRUE, "보드게임");
INSERT INTO game VALUES ('G07', '언락:방탈출게임', '4', '20000', TRUE, "보드게임");
INSERT INTO game VALUES ('G08', '뱅', '4', '8000', TRUE, "카드게임");
INSERT INTO game VALUES ('G09', '도블', '8', '3000', TRUE, "카드게임");
INSERT INTO game VALUES ('G10', '바퀴벌레 포커', '6', '5000', TRUE, "카드게임");
INSERT INTO game VALUES ('G11', '불독조심(개조심)', '4', '3000', TRUE, "카드게임");
INSERT INTO game VALUES ('G12', '빙고팝', '6', '8000', TRUE, "롤플레잉");
INSERT INTO game VALUES ('G13', '블로커스', '4', '5000', TRUE, "롤플레잉");
INSERT INTO game VALUES ('G14', '블로커스 듀얼', '2', '4000', TRUE, "롤플레잉");
INSERT INTO game VALUES ('G15', '카후나', '2', '8000', TRUE, "전략게임");
INSERT INTO game VALUES ('G16', '도망자', '2', '8000', TRUE, "전략게임");
INSERT INTO game VALUES ('G17', '컨퓨전', '5', '5000', TRUE, "전략게임");
INSERT INTO game VALUES ('G18', '티키토플', '4', '5000', TRUE, "전략게임");
INSERT INTO game VALUES ('G19', '시타델(구판)', '6', '6000', TRUE, "전략게임");
INSERT INTO game VALUES ('G20', '시타델(신판)', '6', '10000', TRUE, "전략게임");
INSERT INTO game VALUES ('G21', '포비든 데저트', '5', '20000', TRUE, "전략게임");
INSERT INTO game VALUES ('G22', '사건의 재구성', '4', '10000', TRUE, "전략게임");
INSERT INTO game VALUES ('G23', '도둑잡기 게임 디럭스', '6', '5000', TRUE, "전략게임");
INSERT INTO game VALUES ('G24', '이스케이프 덱:폭풍의 실험실', '5', '5000', TRUE, "전략게임");
INSERT INTO game VALUES ('G25', '이스케이프 더 룸:닥터 그래블리 별장의 비밀', '5', '10000', TRUE, "전략게임");
INSERT INTO game VALUES ('G26', '젠가', '2', '2000', TRUE, "협동게임");
INSERT INTO game VALUES ('G27', '위자드', '6', '5000', TRUE, "협동게임");
INSERT INTO game VALUES ('G28', '에스노스', '6', '18000', TRUE, "협동게임");
INSERT INTO game VALUES ('G29', '장미전쟁', '2', '5000', TRUE, "협동게임");
INSERT INTO game VALUES ('G30', '숲속의 여우', '2', '8000', TRUE, "협동게임");
INSERT INTO game VALUES ('G31', '콘셉트', '12', '12000', TRUE, "퍼즐게임");
INSERT INTO game VALUES ('G32', '인생게임', '4', '5000', TRUE, "퍼즐게임");
INSERT INTO game VALUES ('G33', '코드777', '5', '10000', TRUE, "퍼즐게임");
INSERT INTO game VALUES ('G34', '좀비사이드', '6', '60000', TRUE, "퍼즐게임");

/*방 정보 추가*/
INSERT INTO room VALUES ('R01', '2', '20000','10000', TRUE);
INSERT INTO room VALUES ('R02', '2', '20000','10000', TRUE);
INSERT INTO room VALUES ('R03', '2', '20000','10000', TRUE);
INSERT INTO room VALUES ('R04', '2', '20000','10000', TRUE);
INSERT INTO room VALUES ('R05', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R06', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R07', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R08', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R09', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R10', '4', '40000','10000', TRUE);
INSERT INTO room VALUES ('R11', '8', '80000','10000', TRUE);
INSERT INTO room VALUES ('R12', '8', '80000','10000', TRUE);

/*음식 정보 추가*/
INSERT INTO food VALUES ('F01', '참이슬', '4500', '술'),
('F02', '대선', '4500', '술'),
('F03', '카스', '4500', '술'),
('F04', '테라', '4500', '술'),
('F05', '처음처럼', '4500', '술'),
('F06', '허브티', '3000', '카페'),
('F07', '카푸치노', '3500', '카페'),
('F08', '아이스티', '2000', '카페'),
('F09', '카페라떼', '3500', '카페'),
('F10', '아메리카노', '3000', '카페'),
('F11', '레몬에이드', '4000', '카페'),
('F12', '바닐라라떼', '2000', '카페'),
('F13', '딸기스무디', '2000', '카페'),
('F14', '망고스무디', '4500', '카페'),
('F15', '식혜', '2000', '음료'),
('F16', '생수', '1000', '음료'),
('F17', '맥콜', '2000', '음료'),
('F18', '밀키스', '2000', '음료'),
('F19', '핫식스', '2000', '음료'),
('F20', '환타파인', '2000', '음료'),
('F21', '제로펩시', '2000', '음료'),
('F22', '마운틴듀', '2000', '음료'),
('F23', '코카콜라', '2000', '음료'),
('F24', '파워에이드', '2000', '음료'),
('F25', '칠성사이다', '2000', '음료'),
('F26', '환타오렌지', '2000', '음료'),
('F27', '갈아만든배', '2000', '음료'),
('F28', '웰치스포도', '2000', '음료'),
('F29', '웰치스딸기', '2000', '음료'),
('F30', '제로코카콜라', '2000', '음료'),
('F31', '옥수수수염차', '2000', '음료'),
('F32', '몬스터에너지울트라', '3000', '음료'),
('F33', '칸쵸', '1500', '과자'),
('F34', '홈런볼', '1800', '과자'),
('F35', '꼬칼콘', '1500', '과자'),
('F36', '스윙칩', '1500', '과자'),
('F37', '오래오', '1500', '과자'),
('F38', '치토스', '1500', '과자'),
('F39', '새우깡', '1500', '과자'),
('F40', '꿀꽈배기', '1500', '과자'),
('F41', '오징어땅콩', '1500', '과자'),
('F42', '매운새우깡', '1500', '과자'),
('F43', '신라면', '3500', '면류'),
('F44', '너구리', '3500', '면류'),
('F45', '안성탕면', '3500', '면류'),
('F46', '짜파게티', '3500', '면류'),
('F47', '팔도비빔면', '3500', '면류'),
('F48', '불닭볶음면', '3500', '면류'),
('F49', '열라면', '3500', '면류'),
('F50', '간짬뽕', '3500', '면류'),
('F51', '제육덮밥', '5500', '밥류'),
('F52', '김치볶음밥', '5500', '밥류'),
('F53', '낚지볶음밥', '5500', '밥류'),
('F54', '스팸마요덮밥', '5500', '밥류'),
('F55', '치킨마요덮밥', '5500', '밥류'),
('F56', '참치마요덮밥', '5500', '밥류');