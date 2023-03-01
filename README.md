# simple-board-project
간단 게시판 구현 프로젝트

# 구성
- spring boot 2.7.5
- build tool : gradle
- jdk 17
- DB (h2, postgresql)
- thymeleaf
- spring security
- JPA
- querydsl

# 데모 페이지
- [간단 게시판](https://port-0-simple-board-project-1b5xkk2fldh9dtng.gksl2.cloudtype.app/)

## 실행 간 필요 환경 변수
- `SIMPLE_BOARD_DB_URL` : postgres DB URL
- `SIMPLE_BOARD_DB_USERNAME` : DB UserName
- `SIMPLE_BOARD_DB_PASSWORD` : DB Password
- `KAKAO_OAUTH2_LOGIN_KEY` : kakao 로그인 인증 관련 key
- `KAKAO_OAUTH2_LOGIN_SECRET` : kakao 로그인 인증 관련 secret

# git branch 전략
- git hub 전략 사용 (main, feature) 만 사용하는 것
-- 단, git flow 생성 간 임시로 dev 브랜치 생성

# 요구사항

- 사용자가 로그인하여 게시판 페이지를 조회할 수 있다
- 게시판 내 게시글에 대한 생성/조회/수정/삭제가 가능하다
- 게시판 내 특정 게시글 검색이 가능하다
- 게시판 내 댓글에 대한 생성/조회/수정/삭제가 가능하다
- 게시글에 해시태그를 넣을 수 있다

# 관련 노션 링크
- https://trapezoidal-curiosity-d38.notion.site/Simple-Board-Project-67af6d60db854a2786edefe35d0a4c3d