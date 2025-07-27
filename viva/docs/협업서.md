아래는 Spring(STS4) + Gradle + JSP + MySQL + Lombok 환경에서 Git 협업을 위한 팀 표준 운영 문서입니다.
운영체제는 Windows 3명 + macOS 2명 기준으로 작성되었으며, **팀장(최초 환경설정자)**과 팀원이 수행해야 할 작업, Git 전략, 네이밍 규칙 등을 통합한 팀 협업 가이드라인입니다.

---

# ✅ WAC 협업 운영 문서


---

## 1. ✅ 환경 구성 개요

### 항목	내용

IDE	Spring Tool Suite 4 (STS4)
빌드 도구	Gradle (Wrapper 기반, 8.7 추천)
DB	MySQL (JDBC 연결)
뷰 템플릿	JSP
ORM	MyBatis (또는 JDBC Template)
기타	Lombok 사용, Web MVC 구조 기반 개발



---

## 2. ✅ 팀장(최초 세팅자)이 해야 할 작업

### 📌 [1] Gradle Wrapper 생성 및 프로젝트 구조 세팅
```
gradle wrapper --gradle-version 8.7

생성된 다음 파일들을 Git에 반드시 포함:

gradlew, gradlew.bat

gradle/wrapper/gradle-wrapper.properties

gradle/wrapper/gradle-wrapper.jar
```

### 📌 [2] .gitignore 및 .gitattributes 설정
```
.gitignore

# === OS Specific ===
.DS_Store
Thumbs.db
ehthumbs.db
Desktop.ini

# === Build Directories ===
/build/
/bin/
/out/
/.gradle/
/target/

# === IDE Specific ===
/.classpath
/.project
/.settings/
/.idea/
/*.iml

# === Logs / temp ===
*.log
*.tmp
*.bak
*.swp

# === Others ===
*.env
*.db

.gitattributes

* text=auto eol=lf
```

### 📌 [3] README 작성 (최초 세팅 가이드 포함)

---

## 🚀 2. 프로젝트 세팅 방법
```
1. Git clone
2. `./gradlew clean build`
3. STS4에서 `Import > Existing Gradle Project`
4. Lombok 플러그인 설치 (필수)
```

---

## 3. ✅ 팀원들이 해야 할 작업

```
1. GitHub repository clone
2. gradlew 또는 ./gradlew로 빌드
3. STS4에서 Import > Existing Gradle Project
4. Lombok 플러그인 설치
5. DB 설정 로컬 환경에 맞게 수정 (application-secret.properties 등 분리 권장)
```



---

## 4. ✅ 브랜치 전략 (Git Flow 방식 기반)

브랜치명	용도

main	배포 버전 (항상 stable)
develop	개발 통합 브랜치
feature/xxx	기능 단위 개발 브랜치 (ex. feature/login)
bugfix/xxx	버그 수정 브랜치
hotfix/xxx	운영 중 긴급 수정 브랜치



---

## 5. ✅ Pull Request(PR) 전략

모든 PR은 develop 브랜치로 요청

PR 제목: [기능명] 작업 요약

PR 템플릿 예시:
```

#### 🔧 작업 내용
- 로그인 기능 구현
- 세션 처리 로직 추가

#### 🧪 테스트 방법
- `/login` 페이지에서 로그인 시도
- 잘못된 ID 입력 시 에러 메시지 확인

#### ⚠ 주의 사항
- 아직 비밀번호 암호화 미적용

Self-review 후 팀원이 코드리뷰하여 merge
```


---

## 6. ✅ 파일/코드 네이밍 컨벤션

📁 디렉토리 구조

/src
  /main
    /java
      /com/example/project
        /controller
        /service
        /mapper
        /model
        /config
    /resources
      /mapper
      /static
      /templates

🧾 Java 파일 네이밍

타입	예시	규칙

Controller	UserController.java	뒤에 Controller
Service	UserService.java	뒤에 Service
DTO	UserDto.java	Dto
Entity/Model	User.java	단수형
Mapper(XML)	user-mapper.xml	소문자-하이픈 형태 (JDBC)


🧾 변수/메서드/클래스

클래스명: PascalCase

메서드명/변수명: camelCase

상수: UPPER_SNAKE_CASE



---

## 7. ✅ 코드 작성 규칙

들여쓰기: 4칸

중괄호 {}는 항상 줄 바꿈 없이 작성

주석은 한글 가능하되, 함수 단위 설명 필수


// 사용자 로그인 여부를 세션에서 확인
public boolean isLoggedIn(HttpSession session) {
    return session.getAttribute("user") != null;
}


---

## 8. ✅ 팀 내 커밋 메시지 규칙

태그	설명

feat:	새로운 기능 추가
fix:	버그 수정
docs:	문서 수정
style:	코드 스타일 (세미콜론, 들여쓰기 등)
refactor:	코드 리팩토링
test:	테스트 코드
chore:	빌드, 설정 파일 변경 등


예시:

git commit -m "feat: 회원가입 기능 구현"
git commit -m "fix: 로그인 오류 수정"


---

## 9. ✅ 기타 팁

DB 설정은 application-secret.properties 파일로 분리 관리하고 .gitignore에 포함

로컬 DB는 Docker 또는 개별 설치로 팀원별로 설정

VSCode 등 다른 에디터 사용자도 Gradle import 후 빌드 가능


---

# 📘 네이밍 컨벤션 문서 (Naming Convention)

> 본 문서는 프로젝트 내 클래스, 변수, 메서드, 테이블명 등에 대한 명명 규칙을 정의합니다.  
> 모든 구성원은 본 문서를 기준으로 일관된 코드를 작성해야 합니다.

---

## 1. ✅ 공통 원칙

- **언어**: 영어 사용 (단축어 통일)
- **명확성**: 의미가 명확하게 전달되도록 작성
- **일관성**: 동일한 구조와 스타일 유지
- **카멜/파스칼/스네이크** 사용 규칙 구분

| 유형 | 규칙 | 예시 |
|------|------|------|
| 클래스/DTO | PascalCase | `UserController`, `PolicyDTO` |
| 변수/메서드 | camelCase | `userName`, `getPolicyList()` |
| 테이블/컬럼 | snake_case | `user_table`, `user_id` |
| 상수 | SNAKE_CASE (대문자) | `MAX_FILE_SIZE` |

---

## 2. 🧱 Java 네이밍 규칙

### 클래스 및 인터페이스

| 항목 | 규칙 | 예시 |
|------|------|------|
| Controller | PascalCase + `Controller` | `UserController` |
| Service | PascalCase + `Service` | `PolicyService` |
| DTO | PascalCase + `DTO` | `UserDTO` |
| VO | PascalCase + `VO` | `LoginVO` |
| Enum | PascalCase (복수형) | `UserRole`, `PolicyType` |

### 메서드 및 변수

| 항목 | 규칙 | 예시 |
|------|------|------|
| 일반 변수 | camelCase | `userName`, `policyList` |
| boolean 변수 | is/has/can + 명사 | `isActive`, `hasPermission` |
| 메서드 | 동사 + 목적어 | `getUserList()`, `updateInfo()` |

---

## 3. 🗃 DB 테이블 및 컬럼명 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 테이블 | 소문자 + 복수형 | `users`, `policy_data` |
| PK 컬럼 | `table명_id` 또는 `id` | `user_id`, `policy_id` |
| FK 컬럼 | 참조 테이블명 + `_id` | `user_id`, `post_id` |
| 날짜 컬럼 | `_dt`, `_date` 접미사 | `created_dt`, `deleted_at` |

---

## 4. 🌐 JavaScript 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 변수 | camelCase | `searchInput`, `selectedItems` |
| 함수 | 동사 + 목적어 | `loadPage()`, `fetchData()` |
| DOM ID/class | 소문자 + 하이픈 | `main-header`, `search-bar` |

---

## 5. 🧩 상수 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 전역 상수 | SNAKE_CASE (전부 대문자) | `DEFAULT_PAGE_SIZE`, `API_BASE_URL` |

---

## 6. 📝 약어 사용 규칙

| 의미 | 사용 약어 | 예시 |
|------|-----------|------|
| Number | `no` | `user_no`, `plcy_no` |
| Identifier | `id` | `user_id`, `login_id` |
| Information | `info` | `user_info`, `member_info` |
| Date | `dt` 또는 `date` | `created_dt`, `aply_ymd_strt` |
| Count | `cnt` | `view_cnt`, `user_cnt` |
| Code | `cd` | `gender_cd`, `status_cd` |

---

## 7. 🧪 테스트 클래스/메서드 네이밍

| 항목 | 규칙 | 예시 |
|------|------|------|
| 테스트 클래스 | `Class명 + Test` | `UserServiceTest` |
| 테스트 메서드 | `기능명_상황_기대결과` | `createUser_빈값입력_에러반환()` |

---

## 📌 기타 규칙

- **불용어 지양**: `data`, `temp`, `test`, `foo`, `bar` 등 의미 없는 명칭 사용 금지
- **약어 최소화**: 명확성을 해치지 않는 범위 내에서만 약어 사용
- **파일명과 클래스명 일치**: Java의 경우 `.java` 파일명과 클래스명은 반드시 동일해야 함

---

> ✏️ 본 문서는 프로젝트 진행 중 팀원 합의에 따라 업데이트될 수 있습니다. 변경 시 버전 히스토리를 함께 관리하세요.


---

필요 시 .md 파일로 저장해서 직접 보내드릴 수도 있으니, 원하시면 내려받기용 파일도 만들어드릴게요.


# 📘 네이밍 컨벤션 문서 (Naming Convention)

> 본 문서는 프로젝트 내 클래스, 변수, 메서드, 테이블명 등에 대한 명명 규칙을 정의합니다.  
> 모든 구성원은 본 문서를 기준으로 일관된 코드를 작성해야 합니다.

---

## 1. ✅ 공통 원칙

- **언어**: 영어 사용 (단축어 통일)
- **명확성**: 의미가 명확하게 전달되도록 작성
- **일관성**: 동일한 구조와 스타일 유지
- **카멜/파스칼/스네이크** 사용 규칙 구분

| 유형 | 규칙 | 예시 |
|------|------|------|
| 클래스/DTO | PascalCase | `UserController`, `PolicyDTO` |
| 변수/메서드 | camelCase | `userName`, `getPolicyList()` |
| 테이블/컬럼 | snake_case | `user_table`, `user_id` |
| 상수 | SNAKE_CASE (대문자) | `MAX_FILE_SIZE` |

---

## 2. 🧱 Java 네이밍 규칙

### 클래스 및 인터페이스

| 항목 | 규칙 | 예시 |
|------|------|------|
| Controller | PascalCase + `Controller` | `UserController` |
| Service | PascalCase + `Service` | `PolicyService` |
| DTO | PascalCase + `DTO` | `UserDTO` |
| VO | PascalCase + `VO` | `LoginVO` |
| Enum | PascalCase (복수형) | `UserRole`, `PolicyType` |

### 메서드 및 변수

| 항목 | 규칙 | 예시 |
|------|------|------|
| 일반 변수 | camelCase | `userName`, `policyList` |
| boolean 변수 | is/has/can + 명사 | `isActive`, `hasPermission` |
| 메서드 | 동사 + 목적어 | `getUserList()`, `updateInfo()` |

---

## 3. 🗃 DB 테이블 및 컬럼명 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 테이블 | 소문자 + 복수형 | `users`, `policy_data` |
| PK 컬럼 | `table명_id` 또는 `id` | `user_id`, `policy_id` |
| FK 컬럼 | 참조 테이블명 + `_id` | `user_id`, `post_id` |
| 날짜 컬럼 | `_dt`, `_date` 접미사 | `created_dt`, `deleted_at` |

---

## 4. 🌐 JavaScript 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 변수 | camelCase | `searchInput`, `selectedItems` |
| 함수 | 동사 + 목적어 | `loadPage()`, `fetchData()` |
| DOM ID/class | 소문자 + 하이픈 | `main-header`, `search-bar` |

---

## 5. 🧩 상수 네이밍 규칙

| 항목 | 규칙 | 예시 |
|------|------|------|
| 전역 상수 | SNAKE_CASE (전부 대문자) | `DEFAULT_PAGE_SIZE`, `API_BASE_URL` |

---

## 6. 📝 약어 사용 규칙

| 의미 | 사용 약어 | 예시 |
|------|-----------|------|
| Number | `no` | `user_no`, `plcy_no` |
| Identifier | `id` | `user_id`, `login_id` |
| Information | `info` | `user_info`, `member_info` |
| Date | `dt` 또는 `date` | `created_dt`, `aply_ymd_strt` |
| Count | `cnt` | `view_cnt`, `user_cnt` |
| Code | `cd` | `gender_cd`, `status_cd` |

---

## 7. 🧪 테스트 클래스/메서드 네이밍

| 항목 | 규칙 | 예시 |
|------|------|------|
| 테스트 클래스 | `Class명 + Test` | `UserServiceTest` |
| 테스트 메서드 | `기능명_상황_기대결과` | `createUser_빈값입력_에러반환()` |

---

## 📌 기타 규칙

- **불용어 지양**: `data`, `temp`, `test`, `foo`, `bar` 등 의미 없는 명칭 사용 금지
- **약어 최소화**: 명확성을 해치지 않는 범위 내에서만 약어 사용
- **파일명과 클래스명 일치**: Java의 경우 `.java` 파일명과 클래스명은 반드시 동일해야 함

---

> ✏️ 본 문서는 프로젝트 진행 중 팀원 합의에 따라 업데이트될 수 있습니다. 변경 시 버전 히스토리를 함께 관리하세요.


---

