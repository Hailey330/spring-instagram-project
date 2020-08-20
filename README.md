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
