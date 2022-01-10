# ㅊㅂㅊㅂ: 나만의 차박지도

## 📑 프로젝트 개요
주변 여러 차박지를 소개하고 차박 정보를 공유하는 차박지도 서비스입니다.  

<br />

## 🌱 프로젝트 멤버
|담당|인원수|프로젝트 URL| 
|---|---|---|
|디자인|1명|[ㅊㅂㅊㅂ Figma 디자인](https://www.figma.com/file/OJhobFAs7K0uEcUL72wZat/%EC%B0%A8%EB%B0%95?node-id=0%3A1)|
|안드로이드|2명|[ㅊㅂㅊㅂ 프론트엔드](https://github.com/chabak-map/frontend)|  
|서버|1명|[ㅊㅂㅊㅂ 백엔드](https://github.com/chabak-map/backend)|

<br />

## ⚙ Backend Skills
<p>
  <img src="https://img.shields.io/badge/-SpringBoot-blue"/>&nbsp
  <img src="https://img.shields.io/badge/-AWS-yellow"/>&nbsp
  <img src="https://img.shields.io/badge/-SpringBatch-green"/>&nbsp
  <img src="https://img.shields.io/badge/-Nginx-brightgreen"/>&nbsp
  <img src="https://img.shields.io/badge/-MySQL-blue"/>&nbsp
  <img src="https://img.shields.io/badge/-Redis-red"/>&nbsp
  <img src="https://img.shields.io/badge/-Git Action-lightgrey"/>&nbsp
  <img src="https://img.shields.io/badge/-JWT-orange"/>&nbsp
  <img src="https://img.shields.io/badge/-JPA-grey"/>&nbsp
</p> 

![image](https://user-images.githubusercontent.com/70880695/142659006-a443981b-8dd2-4560-8157-75d20d0e8ef9.png)


<br />

## 개발환경
- JAVA 11 / Gradle
- Spring Boot 2.5.6

## Usage
```shell
 # cd chabak-map
 # ./gradlew clean
 # ./gradlew build --exclude-task test 
```

#### 비용 문제로 서버는 2022-01-30까지만 운영됩니다.
```
  https://univ-city.shop
```

<br />

### [📔 API 명세서](https://docs.google.com/spreadsheets/d/1CAUGx_R49infGViDuEJqjZczTnQG_Ay6hiHIZNiUPO8/edit?usp=sharing)

- - -

## ERD
![diagram](https://user-images.githubusercontent.com/70880695/148759514-4b92895b-8f6f-40a7-a694-ceb6a288b55e.png)

<br >

## 코드 컨벤션 적용 
### 1. EditorConfig
```shell
  [IntelliJ]
  1. Preferences > Plugins > Marketplace > 'EditorConfig' 플러그인 설치 
  2. root 위치에 .editorconfig 확인
  3. Preferences > Editor > Code Style > Java  :: "Settings may be overridden by EditorConfig" 메시지 확인
  4. Code Style에서 line separator 에서 '\n'으로 설정
  5. 추가적으로 Scheme는 intellij 파일로 설정
  
  [Eclipse]
  1. https://marketplace.eclipse.org/content/editorconfig-eclipse 설치
```
### 2. Autosave
```shell
  [IntelliJ]
  1. Preferences > Plugins > Marketplace > 'Save Actions' 플러그인 설치 및 재시작
  2. Preferences > Other Settings > Save Actions 이동하여 아래 내용 체크
    - Activate save actions on save
    - Optimize imoprts
    - Refomat file
    
  [Eclipse]
  1. http://docs.navercorp.com/coding-convention/java.html#eclipse
```
### 3. CheckStyle
[naver-code-convention 다운로드](https://github.com/naver/hackday-conventions-java/tree/master/rule-config)
- rules, suppressions.xml 파일은 `rules-config` 폴더에 위치해 있습니다. 
```shell
  [IntelliJ]
  1. Preferences > Plugins > Marketplace > 'CheckStyle-IDEA' 설치 및 재시작
  2. Preferences > Tools > Checkstyle 설정
    - Checkstyle versions : 8.24 이상 선택 및 Apply
    - Scan scope : All sources including tests 선택 
    - Treat Checkstyle errors as warnings 체크
    - Configure File 박스 하단의 + 버튼 클릭
      Description : naver-checkstyle-rules
      Use a Local Checkstyle File : 선택하고 [Browse] 버튼을 눌러서 naver-checkstyle-rules.xml을 저장
      suppressionFile 변수를 설정하라는 화면에서 naver-checkstyle-suppressions.xml 지정 및 완료
      naver-checkstyle-rules 사용 선택
    
  [Eclipse]
  1. http://docs.navercorp.com/coding-convention/java.html#eclipse
```

### 3-1. line separator 다를 시 에러 설정
Preferences > Editor > Inspections > "General: Inconsistent line separators" 에서 클릭 후 Severity를 Error로 설정"

### 4. git에서 파일을 불러올 시 개행문자 처리를 해 주어야 LF -> CRLF로 바뀌지 않습니다.
```shell
git clone git@github.com:chabak-map/backend.git
cd backend
git config core.eol lf
gitconfig core.autocrlf input
git checkout -t origin/feature/bookmark
git remote update
```

<br />
<br />

## 개발일지

### 2021-11-15 ~ 2021-11-19 진행상황
- - -
- 초기 프로젝트 생성 및 build.gradle 세팅
- EC2 서버 구축 및 보안 그룹 설정
- ERD 설계 완료
- RDS 생성
- 도메인 생성 후 EC2 연결
- https 인증 적용 + 리다이렉션 설정
- Jenkins CI/CD 구축
- CI/CD 방식 Jenkins -> GitAction + CodeDeploy로 변경
- docker redis 설정
- rest api 명세서 작성

### 2021-11-20 진행상황
- - -
- SMS 인증 구현
  - redis 추가 연동 필요
- 회원 관련 기능 구현
  - 로그인, 회원가입, 회원정보

### 2021-11-21 진행상황
- - -
- redis를 이용한 SMS 인증 구현
- 개발용 서버 Pull request 

### 2021-11-23 진행상황
- - -
- 회원 프로필 이미지 업로드 구현
- 닉네임, 이메일 중복 검증
- Exception 관련 변경

### 2021-11-24 진행상황
- - -
- ERD 일부 수정 (태그, 댓글 테이블 각각 2개로 분리)
- 장소 삭제 및 상세 장소 조회 API 구현
- 반경 내 장소 구현 중

### 2021-11-25 진행상황
- - - 
- 내 주변 특정 반경 조회 API 구현 
- 장소 태그, 포스팅 태그 API 구현
- 북마크 조회 API 구현

### 2021-11-26 진행상황
- - - 
- 포스팅 관련 API 구현
- 포스팅 글 업로드 테스트 중

### 2021-11-27 진행상황
- - -
- 댓글 관련 API 구현 중
- Intellij Code Convention 적용 -> README 작성 필요

### 2021-11-28 진행상황
- - -
- 댓글 관련 API 구현 완료
- 코딩 컨벤션 적용 방법 README 작성

### 2021-11-29 진행상황
- - -
- 유저 정보 관련 API 구현

### 2021-11-30 진행상황
- - -
- 북마크 API 구현

### 2021-12-01 진행상황
- - -
- issue 일부 해결

### 2021-12-02 진행상황
- - -
- DB 컬럼에 이모지를 사용할 수 없는 문제 조치
- Interceptor 계층 추가 -> 추후 개선 필요 (현재 Interceptor 2개)

### 2021-12-04 진행상황
- - -
- Interceptor PR 개선 중

### 2021-12-05 진행상황
- - -
- Interceptor PR 수정
- 포스트 작성 이미지 업로드 오류 수정 완료

### 2021-12-06 진행상황
- - -
- 패키지 구조 변경
- 생성된 이슈 처리 중

### 2021-12-08 진행상황
- - -
- 패키지 구조 변경 중
- 토큰 관련 이슈 확인 중

### 2021-12-10 진행상황
- - -
- JWT 토큰 미적용 api 적용되도록 수정

### 2021-12-11 진행상황
- - -
- 테스트코드 작성 및 테스트 진행 

### 2021-12-13 진행상황
- - -
- 소셜 로그인 구현 (Google, Kakao)

### 2021-12-14 진행상황
- - -
- 거리순 장소검색 API 구현 중

### 2021-12-16 진행상황
- - -
- 거리순 장소검색 API 구현 중

### 2021-12-17 진행상황
- - -
- 통합검색 API 구현 중

### 2021-12-20 진행상황
- - -
- 장소 API 변경

### 2021-12-21 진행상황
- - -
- 포스트 수정 api 구현

### 2022-01-03 진행상황
- - -
- 회원 신고 기능 변경 완료 및 패키지 정리
- 통합 검색 API 구현
