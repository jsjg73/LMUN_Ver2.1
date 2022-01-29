## 각 화면에서 호출하는 api 종류

---

1. 웹 메인 화면
   - 모임 조회 : GET /meeting/{meetingId}
   - 모임 참가 : PUT /meeting/{meetingId}/entrance
   - 참가 모임 조회 : GET /meeting
2. 모임 생성 화면
   - 모임 생성 : POST /meeting
   - 계정 생성 : POST /user
3. 모임 메인 화면
   - 모임 조회 : GET /meeting/{meetingId}
   - 참석자 출발지 목록 : GET /meeting/{meetingId}/departures
   - 모임 로그 조회 : GET /meeting/{meetingId}/log
4. 추천 지역 상세 화면
   - 모임 조회
   - 참가자 출발지 목록
   - 추천 지역 검색 서비스(외부 API)
   - 모임 장소 제안 하기 : POST /meeting/{meetingId}/proposal
   - 장소 제안 조회 : GET /meeting/{meetingId}/proposal/{propsalId}
   - 제안에 동의하기 : PUT /meeting/{meetingId}/proposal/{propsalId}