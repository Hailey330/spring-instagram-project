# Instagram (Springboot + JPA + Security) project


## 의존성 추가

- Spring Boot DevTools
- Lombok
- Spring Data JPA
- MySQL Driver
- Spring Security
- OAuth2 Client
- Mustache
- Spring Web


## MySQL 세팅

#### 1. MySQL 한글 설정(my.ini)

```ini
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
init_connect='SET collation_connection = utf8_general_ci'
character-set-server=utf8
```

#### 2. MySQL 데이터베이스 및 사용자 생성

```sql
create user 'insta'@'%' identified by 'bitc5600';
GRANT ALL PRIVILEGES ON 별.별 TO 'insta'@'%';
create database insta;
use insta;
```

#### 3. application.yml 설정

```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/insta?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true
    username: insta
    password: bitc5600

  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: create
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      use-new-id-generator-mappings: false
    show-sql: true
    properties:
      hibernate.enable_lazy_load_no_trans: true
      hibernate.format_sql: true

  servlet:
    multipart:
      enabled: true
      max-file-size: 2MB

  security: # test용 security 로그인 아이디, 비밀번호 지정
    user:
      name: cos
      password: 1234

cos: # like 변수, 자바에서 찾기 쉽게 커스텀
  secret: 홍차

file: # 파일 저장시 경로
  path: C:/src/aouthwork/instagram/src/main/resources/upload/
```


## 맞팔 쿼리, 좋아요 카운트 쿼리

#### 1. 좋아요 수 쿼리 (스칼라 서브쿼리)

![blog](https://postfiles.pstatic.net/MjAyMDA4MjRfNzcg/MDAxNTk4MjQ1Nzk3ODUy.VgUAPShBqfpMV_AzAj5SsBrj3LuukpgnesMXans6HjUg.K5dJE464yisexWWKqdNSXu-VwzRm-YlJEahWRf1SKpYg.PNG.kid0739/image.png?type=w966)

```sql
select
img.id,
img.caption,
(select count(*) from likes where imageId = img.id) "like count"
from image img;
```

#### 2. 맞팔 유무 쿼리 (Left Outer Join & 스칼라 서브쿼리)

![blog](https://postfiles.pstatic.net/MjAyMDA4MjRfMTc5/MDAxNTk4MjQ1ODIyMzQw._VL1HFKlMUKQ9Y0V6tIQl6poO6slv2XimLZ3L_Ow31Mg.7AQp6Ai6WgM9FwyDCeLM2WlVwZdZQ7oK_DxD8H2W-9gg.PNG.kid0739/image.png?type=w966)

```sql
-- Left Outer Join
select f1.id, f1.fromUserId, f1.toUserId, f1.createDate,
if(f2.fromUserId is null, false, true) "matpal"
from follow f1 left outer join follow f2
on f1.fromUserId = f2.toUserId and f1.toUserId = f2.fromUserId
order by f1.id;

-- 스칼라 서브 쿼리
select f1.id, f1.fromUserId, f1.toUserId,
f1.createDate,
(
select 1 from follow f2
where f1.fromUserId = f2.toUserId
and f1.toUserId = f2.fromUserId
) "matpal"
from follow f1;
```
