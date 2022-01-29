### E-R SCHEMA 
- 추천 장소 속성 추가
- 모임 로그에 대한 스케마 설계(elastic search)
### Learning Test
- Elasticsearch 학습 테스트
- Elasticsearch geo-distance query 학습 테스트
 
### 목표
- 비즈니스 로직 코드과는 별개로, DB를 SQL과 NOSQL을 상호 전환할 수 있는지 확인.
- 유효기간 지난 모임을 제거하는 로직 ==> batch, 멀티 모듈 적용해보자.
- 모임 로그 출력방식에 웹 소켓 적용할 수 있는지 확인. 모임의 변동사항은 모임 로그를 통해 확인할 수 있는데, 실시간으로 변동되는 내용을 바로 알 수 있게 만들어보자.


### 기타
- 경로 탐색을 위한 외부 api의 사용법 정리.
- 테스트 코드 작성에 사용할 모임, 유저, 주소 등 데이터 수집.
- 모든 api에 대한 테스트 코드 작성.

- 모임 로그는 입장, 퇴장, 변경, 제안 등으로 나뉜다. 유형에 따라 알맞은 key:value로 매핑해서 응답하기 위해선 다형성 관계를 지닌 객체들을 바인딩하고 response body로 어떻게 구성하는지 찾아봐야한다.
   - @ResponseBody abstract
   - jackson abstract class deserialize