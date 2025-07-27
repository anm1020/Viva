# Viva - AI 면접 채팅 애플리케이션

## 개요
AI 면접관과 대화할 수 있는 채팅 애플리케이션입니다.

## 설정 방법

### 1. 데이터베이스 설정
MySQL 데이터베이스를 설치하고 `viva` 데이터베이스를 생성하세요.

```sql
CREATE DATABASE viva CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. API 키 설정
`src/main/resources/application-secret.properties` 파일을 수정하여 실제 OpenAI API 키를 설정하세요:

```properties
openai.api.key=your-actual-openai-api-key-here
spring.datasource.username=your-database-username
spring.datasource.password=your-database-password
```

### 3. 데이터베이스 연결 정보 수정
`src/main/resources/application.properties`에서 데이터베이스 연결 정보를 수정하세요:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/viva?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=your-username
spring.datasource.password=your-password
```

## 실행 방법

### 1. Gradle을 사용한 실행
```bash
./gradlew bootRun
```

### 2. JAR 파일로 실행
```bash
./gradlew build
java -jar build/libs/viva-0.0.1-SNAPSHOT.jar
```

## 접속 방법
브라우저에서 `http://localhost:8080`으로 접속하세요.

## 주요 기능
- AI 면접관과 실시간 채팅
- 대화 기록 저장 및 불러오기
- 세션별 대화 관리

## 문제 해결

### 404 오류
- 데이터베이스 연결을 확인하세요
- API 키가 올바르게 설정되었는지 확인하세요

### 500 오류
- 서버 로그를 확인하여 구체적인 오류 메시지를 확인하세요
- OpenAI API 키가 유효한지 확인하세요

### 데이터베이스 연결 오류
- MySQL 서버가 실행 중인지 확인하세요
- 데이터베이스 사용자명과 비밀번호가 올바른지 확인하세요 