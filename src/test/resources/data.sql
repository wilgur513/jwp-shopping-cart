set foreign_key_checks = 0;

truncate table orders_detail;

truncate table orders;

truncate table cart_item;

truncate table product;

truncate table accounts;

set foreign_key_checks = 1;

insert into accounts (username)
values ('puterism'),
       ('tanney-102'),
       ('jho2301'),
       ('365kim'),
       ('dudtjr913'),
       ('jum0'),
       ('hyuuunjukim'),
       ('zereight'),
       ('devhyun637'),
       ('swon3210'),
       ('bigsaigon333'),
       ('yungo1846'),
       ('zigsong'),
       ('iborymagic'),
       ('0307kwon'),
       ('gwangyeol-im'),
       ('shinsehantan'),
       ('ddongule'),
       ('seojihwan'),
       ('0imbean0'),
       ('sunyoungkwon'),
       ('hchayan'),
       ('2sooy'),
       ('yujo11'),
       ('sunhpark42')
;