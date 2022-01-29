   - Description : 모임 ID로 단일 모임 조회한다.
   ```
   POST /meeting
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Request:
   {
      "": ,
      "": 
   }
   Response:
   {  
      "": ,
      "": 
   }
   ```
   
1. 참가 모임 조회
   - Description : 사용자가 참가한 전체 모임 목록을 조회한다.  
   ```
   GET /meeting
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Response:
   {  
      "nick": 사용자 이름,
      "tot": 참가 모임 수,
      "meetings":[
         {
            "id": 모임 id,
            "name": 모임 이름,
            "host": 관리자 이름,
            "atLeast": 최소인원,
            "participantsCount": 현재참석인원수
         },
         ...
      ]
   }
   ```
2. 모임 생성
   - Description : 모임을 생성한다.
   ```
   POST /meeting
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Request:
   {
      "name": 모임 이름,
      "atLeast": 최소 인원수
   }
   Response:
   {  
      "id": 모임 id,
      "name": 모임 이름,
      "host": 관리자 이름,
      "atLeast": 최소인원,
      "participantsCount": 현재참석인원수
   }
   ```
3. 모임 조회 
   - Description : 모임 ID로 단일 모임 조회한다.
   ```
   GET /meeting/{meetingId}
   ```
   ```(json)
   Response:
   {  
       "id": 모임 id,
      "name": 모임 이름,
      "host": 관리자 이름,
      "atLeast": 최소인원,
      "participantsCount": 현재참석인원수,
      "participants":[
         참가자 이름, ...
      ]
   }
   ```
3. 모임 수정
   - Description : {meetingID}} 모임의 정보를 수정한다.
   ```
   PUT /meeting/{meetingId}
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Request:
   {
      "name": 수정한 모임 이름,
      "atLeast": 수정한 최소 인원
   }
   Response:
   {  
      "id": 모임 id,
      "name": 모임 이름,
      "host": 관리자 이름,
      "atLeast": 최소인원,
      "participantsCount": 현재참석인원수
   }
   ```   
4. 모임 참가
   - Description : 인증된 사용자가 모임에 참가한다.
   ```
   PUT /meeting/{meetingId}/entrance
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Response:
   {  
      "id": 모임 id,
      "name": 모임 이름,
      "host": 관리자 이름,
      "atLeast": 최소인원,
      "participantsCount": 현재참석인원수 
   }
   ```   
5. 참석자 출발지 목록
   - Description : 참가자들의 출발지 목록을 조회한다.
   ```
   GET /meeting/{meetingId}/departures
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Response:
   {  
      "participants":[
         {
            "nick": 사용자 이름,
            "departure":{
               "id": 장소 id,
               "place_name": 장소명,
               "lon": 경도,
               "lat": 위도,
               "address_name": 지번 주소,
               "road_address_name": 도로명 주소,
               "category_group_code": 코드,
               "category_group_name": 코드 설명
            }
         },
         ...
      ]
   }
   ```
6. 모임 로그 조회 
   - Description : 모임의 전체 로그를 시간순으로 조회.
   ```
   GET /meeting/{meetingID}/log
   Authorization: Bearer {JWT}
   ```
   ```(json)
   Response:
   {  
      "logs":[
         {
            "log_type": 입장 or 퇴장 or 변경 or 제안,
            "log_detail": { // 로그 타입에 따라 다름
               "var1": aaa,
               "var2": bbb,
               "var3": ccc,
               "message": var1~3이 필요한 로그 메시지 포맷
            },
            "timestamp": 로그 생성 시간 

         },
         ...
      ]
   }
   ```
7. 장소 제안
   - Method : POST
   - Path : /meeting/{meetingId}/proposal
   - Header : 인가된 유저 토큰
   - Request Body : {도착 장소, 참가자별 경로}
   - Response : 성공 여부, 제안
   - 인증 + 인가
8. 계정 생성
   - Description : 새로운 계정을 생성한다.
   ```
   POST /user
   ```
   ```(json)
   Request:
   {
      "username": 사용자 계정 아이디,
      "password": 비밀번호,
      "nick": 이름,
      "departure":{
         "id": 장소 id,
         "place_name": 장소명,
         "lon": 경도,
         "lat": 위도,
         "address_name": 지번 주소,
         "road_address_name": 도로명 주소,
         "category_group_code": 코드,
         "category_group_name": 코드 설명
      }
   }
   Response:
   {  
      "token": jwt 토큰 
   }
   ```
9. 로그인
   - Description : 로그인한다.
   ```
   POST /login
   ```
   ```(json)
   Request:
   {
      "username": 사용자 계정 아이디,
      "password": 비밀번호
   }
   Response header:
      - "token": jwt 토큰
   ```
---
## API Depth Table
| depth 1 |      2      |      3     |        methods        |
|:-------:|:-----------:|:----------:|:---------------------:|
| meeting |             |            | GET(인증), POST(인증) |
|         | {meetingId} |            | GET, PUT(인가)                   |
|         |             | entrance   | PUT(인증)             |
|         |             | departures | GET(인가)             |
|         |             | log        | GET(인가)             |
|         |             | proposal   | POST(인가)            |
| user    |             |            | POST                  |

---

## Category_group_code

|Name|	Description|
|:-:|:-:|
|MT1|	대형마트          |
|CS2|	편의점            |
|PS3|	어린이집, 유치원  |
|SC4|	학교              |
|AC5|	학원              |
|PK6|	주차장|
|OL7|	주유소, 충전소|
|SW8|	지하철역|
|BK9|	은행|
|CT1|	문화시설|
|AG2|	중개업소|
|PO3|	공공기관|
|AT4|	관광명소|
|AD5|	숙박|
|FD6|	음식점|
|CE7|	카페|
|HP8|	병원|
|PM9|	약국|
* kakao local api의 카테고리 그룹 코드를 참조함.