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
개발일지는 [해당 링크](https://github.com/chabak-map/backend/blob/master/%EA%B0%9C%EB%B0%9C%EC%9D%BC%EC%A7%80%5Bhjun-park%5D.md) 에서 확인하실 수 있습니다. 

