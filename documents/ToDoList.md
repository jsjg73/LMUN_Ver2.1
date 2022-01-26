### E-R SCHEMA 
- 추천 장소 속성 추가
- 모임 테이블 : 
   - 인원수정시간 -> 수정시간
   - 생성 시간 칼럼 추가.
- 모임 로그에 대한 스케마 설계(elastic search)
### Learning Test
- Elasticsearch 학습 테스트
- Elasticsearch geo-distance query 학습 테스트
 
### 목표
- 비즈니스 로직 코드과는 별개로, DB를 SQL과 NOSQL을 상호 전환할 수 있는지 확인.
- 유효기간 지난 모임을 제거하는 로직 ==> batch, 멀티 모듈 적용해보자.

### 기타
- spring security 적용 방안
   - 사용자가 해당 모임에 참가자인가? ( 인가 1 )
   - 참가자가 해당 모임의 관리자인가? ( 인가 2 )
- 각 api 요청의 요청과 응답 구체화
- 경로 탐색을 위한 외부 api의 사용법 정리. 동시에 어플리케이션에서 사용할 주소 체계 결정.