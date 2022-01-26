   - Method :
   - Path :
   - Header :
   - Response :
1. 모임 조회   
   - Method : GET
   - Path : /meeting/{meetingId}
   - Header :
   - Response : 모임 정보(참가자 목록 필수 포함)
2. 모임 참가
   - Method : PUT
   - Path : /meeting/{meetingId}/entrance
   - Header : 인가된 유저의 토큰
   - Response : 모임 정보
3. 참가 모임 조회
   - Method : GET
   - Path : /meeting
   - Header : 인증된 유저의 토큰()
   - Response : 모임 정보
4. 모임 생성
   - Method : POST
   - Path : /meeting
   - Header : 인증된 유저 토큰(로그인한 유저만 모임을 생성할 수 있다.)
   - Request Body : {모임 이름, 최소인원}
   - Response : 모임 정보
5. 계정 생성
   - Method : POST
   - Path : /user
   - Header :
   - Request Body : {계정id, 비밀번호,이름, 주소}
   - Response : {토큰}
6. 참석자 출발지 목록
   - Method : GET
   - Path : /meeting/{meetingId}/departures
   - Header : 유저 토큰
   - Response : {참가자들의 출발지에 대한 배열}
7. 모임 로그 조회 
   - Method : GET
   - Path : /meeting/{meetingID}/log
   - Header : 유저 토큰
   - Response : {입장, 퇴장, 수정, 제안 로그}
8. 비회원 모임 참가
   - Method : PUT
   - Path : /meeting/{meetingId}/entrance/nonmember
   - Header : 
   - Request Body : {비밀번호, 이름, 주소}
   - Response : 
9. 장소 제안
   - Method : POST
   - Path : /meeting/{meetingId}/proposal
   - Header : 인가된 유저 토큰
   - Request Body : {도착 장소, 참가자별 경로}
   - Response : 성공 여부, 제안 
