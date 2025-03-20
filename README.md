# Wanna Be Cool? MUTT-IROOM

<img src="https://github.com/user-attachments/assets/b2d42506-101e-48f3-aa8c-d3e3c8515326" width="1200"/>

<div align="center">
    <a href="https://mutt-iroom.store/"><code>🕶️ 멋이룸 체험하기</code></a>
    <a href="https://yeim.notion.site/mushroom"><code>📜 팀노션 구경하기</code></a>
</div>

<br/>

## 목차

- [1. 프로젝트 소개](#1-프로젝트-소개)
- [2. 주요 기능](#2-주요-기능)
- [3. 트러블 슈팅 \& 성능 개선](#3-트러블-슈팅--성능-개선)
- [4. 기술적 의사 결정 과정](#4-기술적-의사-결정-과정)
- [5. 인프라 아키텍처 \& 적용 기술](#5-인프라-아키텍처--적용-기술)
  - [🏰 인프라 아키텍처](#인프라-아키텍처)
  - [💎 적용 기술](#적용-기술)
- [6. 성과 및 회고](#6-성과-및-회고)
  - [👍🏻 잘된 점](#잘된-점)
  - [👀 아쉬운 점](#아쉬운-점)
  - [📆 향후 계획](#향후-계획)
- [7. 역할 분담 및 협업 방식](#7-역할-분담-및-협업-방식)
  - [🤼‍♂️ 역할 분담](#️-역할-분담)
  - [🌱 Ground Rule](#-ground-rule)
  - [✨ Core Value](#-core-value)
- [8. 최우수 프로젝트 선정](#8-최우수-프로젝트-선정)
<br/>

## 1. 프로젝트 소개

<div align="center"><img src="https://github.com/user-attachments/assets/c3f6ba7b-3a39-41f3-a947-0e8319eaa1bc" width="1200"/></div>

**[패션 경매 e-commerce 플랫폼]**

> 멋이룸은 단순히 물건을 사고파는 곳이 아닙니다.
> 
> 트렌드를 선도하는 사람들, 희귀한 아이템을 찾는 컬렉터들, 그리고 나만의 감각을 더하고 싶은 이들이 모이는 공간입니다.
> 
> 한정판 스니커즈, 독창적인 디자이너 브랜드, 빈티지 명품까지 멋이룸에서는 누구나 원하는 아이템을 경매로 만나고, 직접 그 가치를 결정할 수 있습니다.
> 
> 우리는 **구매의 순간을 하나의 경험**으로 만듭니다.
> 치열한 경매의 긴장감, 예상치 못한 기회, 그리고 내가 원하던 아이템을 손에 넣었을 때의 짜릿함까지!
> 
> 당신이 원하는 멋을 이루는 곳, **멋이룸**과 함께 하세요!


<br/>


## 2. 주요 기능

<div align="center">
    <img src="https://github.com/user-attachments/assets/d5e52486-8d2e-43ad-b3e3-b7182bd4ec18" width="350" />
    <img src="https://github.com/user-attachments/assets/b676a8d1-78de-4016-a911-22bdfc989bfd" width="350" />
</div>
<div align="center">
    <img src="https://github.com/user-attachments/assets/4a4c2f90-02b3-4312-8328-95eb223f5889" width="350" />
    <img src="https://github.com/user-attachments/assets/dd4c29bd-3959-495c-8ce5-a2349504feae" width="350" />
</div>

멋이룸의 모든 사용자는 판매와 구매 모두 가능하며, 전체적인 흐름은 아래와 같습니다.

<details>
    <summary>경매</summary>

![Image](https://github.com/user-attachments/assets/34ebac0f-2590-4021-9c7f-e0ec25f6eccc)

- 경매 서비스
    - 유저가 판매를 원할 때는 경매 서비스에서 판매하고자 하는 아이템을 등록할 수 있습니다.
    - 흐름
        1. 판매하고자 하는 물품의 정보를 입력하고, 멋이룸 플랫폼으로 상품을 전달합니다.
        2. 멋이룸 플랫폼에서 검수를 진행한 후 경매 물품을 등록합니다. 

</details>

<details>
    <summary>입찰</summary>
    
![Image](https://github.com/user-attachments/assets/7ecb5102-3c65-4992-8dd6-c1b293cf21f0)

- 입찰하기 서비스
    - 유저는 경매에 자유롭게 참여할 수 있습니다.
    - 흐름
        1. 구매하고자 하는 물품에 입찰가를 정해 입찰 대기를 할 수 있습니다.
        2. 입찰가는 계속 수정이 가능하며, 가장 높은 입찰가로 입찰한 유저가 낙찰이 됩니다. 

</details>

<details>
    <summary>채팅</summary>

![Image](https://github.com/user-attachments/assets/a348b6f9-960f-467c-abb8-9cb7d253620b)

- 입찰자 실시간 채팅 기능
    - 입찰에 참여한 유저들끼리 실시간으로 채팅을 할 수 있습니다.
    - 입찰에 참여하지 않은 유저는 채팅에 참여할 수 없지만 실시간 채팅 내용을 확인할 수 있습니다.
    - 최대 입찰 금액이 변경되는 사항을 실시간으로 확인할 수 있습니다.
    
</details>

<details>
    <summary>좋아요 및 공지</summary>
    
![Image](https://github.com/user-attachments/assets/db35e96e-83bb-4a86-870b-9f8471b5c1de)

- 관심 갖기 기능
    - 유저는 관심 있는 경매 물품에 대해 관심 갖기를 등록하고, 관심 갖기를 한 물품의 목록을 확인할 수 있습니다.
- 경매 공지 기능
    - 관심 갖기한 경매 물품의 경매가 시작될 때 공지 알림을 받을 수 있습니다.

</details>

<details>
    <summary>결제</summary>
    
![Image](https://github.com/user-attachments/assets/d584cf82-4746-49d5-a8cc-5fe1ab41b879)

- 결제 기능
    - 유저는 낙찰받은 경매 물품을 다양한 결제 수단을 통해 안정적으로 결제할 수 있습니다.

</details>

<details>
    <summary>리뷰</summary>

![Image](https://github.com/user-attachments/assets/f158bef3-0606-4cf9-9dde-bd8ed517f8f4)

- 리뷰 기능
    - 유저는 경매 물품의 결제를 완료한 후, 해당 물품을 판매한 유저에 대한 리뷰를 남길 수 있습니다.    
    
</details>

<br/>

## 3. 트러블 슈팅 & 성능 개선

<details>
    <summary><a href="https://yeim.notion.site/AWS-EC2-19b16458a6bf808391f0c77b5f21fd22">[🎯 트러블슈팅] AWS EC2 서버 시간 불일치 이슈</a></summary>

- 문제 상황
    - 경매 자동 시작/종료 스케줄러가 정상적으로 동작하지 않는 문제가 발생했다. 로그 분석 결과 스케줄러는 실행되었지만 설정한 시간과 일치하는 경매 물품을 찾지 못하는 현상이 확인되었다.

- 문제 원인
    - LocalDateTime.now()가 JVM의 기본 타임존(UTC)을 따르고 있어, DB에 저장된 KST 시간과 불일치하여 올바른 데이터를 찾지 못했다. 
    - EC2 리전이 ap-northeast-2라 자동으로 KST가 적용될 것이라 착각하여 이 문제를 인지하지 못했다.
  
- 해결
    - JVM 실행 옵션을 활용해 KST를 적용했고 스케줄러가 정상 동작하는 것을 확인했다.
    ![Image](https://github.com/user-attachments/assets/2cb7a6ef-3afc-41bf-9df3-1a50d5c3860e)


</details>

<details><summary><a href="https://yeim.notion.site/1b316458a6bf80fa8834d56610052e0e">[🎯 트러블슈팅] 메시지 유실 이슈</a></summary>
    
- 문제 상황
    - Redis 장애 발생 시 일부 채팅 메시지가 유실되며 입찰 안내 메시지와 실제 최고 입찰가 정보가 불일치하는 문제가 발생했다.
이로 인해 사용자가 경매 진행 상황을 정확히 파악하지 못하고 서비스의 신뢰성이 저하될 위험이 있었다.

- 문제 원인
    - Redis는 인메모리 저장소로 비정상 종료 시 데이터가 유실될 위험이 있었다.
	- 기본적으로 설정된 RDB(스냅샷) 방식은 일정 주기로만 데이터를 저장했으며 마지막 스냅샷 이후에 저장된 데이터는 복구되지 않았다.
	- 이로 인해 일부 메시지는 유지되었지만 최근 메시지는 유실되며 입찰 기록과 채팅 내역이 불일치하는 문제까지 발생했다.
  
- 해결
    - RDB(스냅샷) + AOF(Append-Only File) 방식을 병행 적용하여 데이터 유실을 방지했다.
    - 이를 통해 Redis 장애 발생 시에도 메시지가 정상적으로 복구되며, 입찰 정보와 채팅 내역이 일관되게 유지되도록 개선했다.
    
</details>
<details><summary><a href="https://yeim.notion.site/19f16458a6bf808fbe9cfb9dc5a1593f">[🎯 트러블슈팅] 결제 API와 트랜잭션 충돌 제어</a></summary>
    
- **문제 상황**
  - 결제가 정상적으로 진행되었지만, 서비스에서 결제 완료 처리가 반영되지 않는 문제가 발생했다.
  - 사용자는 결제는 했으나 서비스 혜택을 받지 못하는 상황이 발생했다.

- **문제 원인**
  1. **결제 이후 비즈니스 로직에서 에러 발생**
     - 결제가 정상적으로 완료되었지만, 이후 실행되는 서비스 로직에서 에러가 발생하면서 결제 완료 상태로 변경되지 않았다.
     - 결제 후 에러가 발생하면 취소 요청을 보내야 하는데, 기존 구현에서는 이를 고려하지 않았다.

  2. **트랜잭션과 외부 API 요청의 결합 문제**
     - `@Transactional`이 적용된 메서드 내에서 결제 API 요청이 포함되어 있어, 트랜잭션이 커밋되는 시점에서 오류가 발생하면 결제 취소가 정상적으로 처리되지 않았다.
     - 이는 DB 커넥션 풀을 점유하며 병목 현상을 유발할 수 있는 문제였다.

  3. **보상 트랜잭션 중 서버 장애 가능성**
     - 결제 후 취소 요청을 보내는 방식의 보상 트랜잭션을 사용했지만, 만약 결제 취소 요청을 보내기 전에 서버가 다운되거나 결제 취소 API 요청이 실패하면 사용자의 결제가 유지될 수 있는 리스크가 존재했다.

- **해결 방법**
  1. **결제 후 로직 실패 시 즉시 취소 요청**
     - 토스 결제 취소 API를 활용하여, 서비스 로직에서 에러 발생 시 즉시 결제 취소 요청을 보내도록 구현했다.
     - 기존 로직에 예외 처리를 추가하여 결제 완료 처리가 되지 않은 경우 결제를 취소하도록 변경했다.
  2. **트랜잭션과 외부 API 요청 분리**
     - `PaymentFacade` 객체를 생성하여, 트랜잭션과 외부 API 요청을 분리했다.
     - 트랜잭션 내부에서는 데이터베이스 관련 로직만 수행하고, 외부 API 요청은 트랜잭션 외부에서 실행되도록 변경하여 안정성을 확보했다.
  3. **결제 흐름 변경 (낙관적 결제 처리 방식 적용)**
     - 기존에는 `결제 성공 → 서비스 로직 실행` 순서였으나, `서비스 로직 실행 → 결제 승인 요청` 순서로 변경했다.
     - 이를 통해 서비스 로직에서 실패가 발생하면 결제 승인 요청 자체가 이루어지지 않도록 설계했다.

- **향후 개선 방향**
  - 결제 시도부터 실패 로그를 세밀하게 남기고, 재시도 가능한 보상 트랜잭션 구조를 적용하여 안정성을 높일 계획이다.
  - 결제 모델에 따라 롤백 정책을 세분화하여, 정기 결제 등의 복잡한 결제 흐름에서도 안정적인 처리를 보장할 예정이다.
  - 결제 속도보다는 정확성을 우선으로 고려하여, 다양한 실패 시나리오를 철저히 검토하고 대비하는 방식을 지속적으로 개선할 것이다.

</details>
<details><summary><a href="https://yeim.notion.site/1a416458a6bf80b4a71cebf6e247a5f1">[🎯 트러블슈팅] 채팅 내역 중복 로드 이슈</a></summary>

- 문제 상황
    <img src="https://github.com/user-attachments/assets/f9791905-bc60-4a8a-85b7-cbc53fd4d214" width="400"/>
    - 새로운 사용자들이 채팅방에 입장할 떄마다 기존 참여 사용자들에게도 채팅 내역이 중복으로 전송되는 문제가 있었다.
    - 이로 인해 사용자는 같은 메시지를 반복해서 받게 되는 혼란을 겪고, ‘멋이룸’ 서비스에 대한 피로감을 느낄 수 있는 상황이 발생했다.

- 문제 원인
    - WebSocket의 convertAndSend()가 브로드캐스트 방식으로 동작하여 모든 사용자에게 동일한 데이터를 전송하고 있었다.
    - 같은 사용자라도 웹소켓 세션이 다르면 중복 요청이 발생하여 채팅 내역이 여러 번 전송됐다.
    
- 해결
    - 기존 STOMP 기반의 웹소켓 메시지 전송 방식 대신 REST API를 활용하여 이전 채팅 내역을 조회하도록 변경했다.
    - 이를 통해 중복 전송 문제를 해결하고 웹소켓 연결 수를 줄여 서버 부하를 감소시켰다.

</details>
<details><summary><a href="https://yeim.notion.site/03-12-1ad16458a6bf80beae0bc2e546df1421">[🎯 트러블슈팅] 스케줄러 데이터 처리 누락 이슈</a></summary>

- 문제 상황
    - 스케줄러 로직
        - 스케줄러는 cron 방식으로 매 분 시작하고, 작동한 기점의 시간( 초 제함 ) , +1 분 사이에 데이터를 추출하여 처리한다.
    - 10만 건의 더미 데이터를 처리 시, 데이터 처리 누락 문제 발생한다.
    
    - <a href="https://yeim.notion.site/1af16458a6bf80cba382fa6befa59891?pvs=4">스케줄러가 단일 스레드?로 작동 안하는 문제 - 본 스케줄러의 상황</a> 
- 문제 원인
    - 스케줄러는 싱글 스레드로 작동한다.
        - 하나의 스케줄러가 끝나면, 다음 스케줄러가 실행된다.
    - cron 시간 설정
        - fixedDelay, fixedRate 은 처리 시간이 늦어지면, 바로 다음 스케줄러가 작동한다
        - cron 은 예약과 같다. 해당 시간이 아니라면, 스케줄러는 작동하지 않는다.
- 해결
    - rabbitMQ를 통해 이를 해결한다.
        - 스케줄러가 있는 api 서버를 producer, 메세지를 받고 처리하는 consumer 서버를 두었다.
        - 동일한 10만 건의 데이터를 가지고 테스트 결과, 데이터 처리 누락 문제가 해결되었다.
            - producer에서 스케줄러의 단위 시간 내에 10만 건의 메세지 만들었다. 
            - 다음 스케줄러 정상 작동 확인했다.
                ![Image](https://github.com/user-attachments/assets/939f53c5-bbea-4837-a195-8a0136c90c3b)
                ![Image](https://github.com/user-attachments/assets/6cefb1b4-bdf0-4e0c-9b4f-4c383e7fe1d3)


</details>


<details><summary><a href="https://yeim.notion.site/1b516458a6bf8017892ddf78bcef05ae?pvs=4">[⏱️ 성능개선] 키워드 검색 기능 조회 최적화 </a></summary>

- 개선 이슈
쿼리 성능 저하 문제로, 경매 상품 검색 시 필터링 조건에 OR과 LIKE를 사용한 쿼리에서 인덱스가 제대로 활용되지 않아 풀스캔이 발생하고 있었다. ORDER BY 절에 의한 파일 정렬 문제로 성능이 저하되어 있었고, 이는 전체 쿼리 실행 시간을 크게 늘렸다.

- 개선 과정
    1. 쿼리 튜닝
    쿼리에서 auctionItem.status에 대한 OR 조건을 IN 조건으로 변경했다. 이를 통해 여러 조건을 동시에 처리하며 쿼리 성능을 최적화했다. 또한, AND/OR 조건의 우선순위를 변경하여 불필요한 중첩 조건을 줄였고, 내림차순 정렬 시 발생할 수 있는 중복 문제도 해결했다. is_deleted 조건을 BooleanBuilder로 추가하여 삭제된 항목을 제외하는 등의 개선을 진행했다.

    2. 인덱스 최적화
    쿼리에서 LIKE 조건에 대한 인덱스 적용을 제대로 활용하기 위해      **UNION ALL**을 도입했다. 이를 통해 OR 조건을 병렬로 처리하면서 인덱스를 잘 활용할 수 있도록 했다. 이렇게 함으로써 쿼리가 풀스캔을 피하고 인덱스를 통한 효율적인 데이터 조회를 할 수 있게 되었다. 또한, EXPLAIN을 통해 실행 계획을 분석하고, 필요한 인덱스를 적절히 적용했다.

- 결과
    결과적으로, 쿼리 성능이 99% 이상 향상되었으며, 검색 속도와 정확도 모두 크게 개선되었다.
    
    ![Image](https://github.com/user-attachments/assets/02c29df8-dd79-45e0-949d-1912a66626f1)
    ![Image](https://github.com/user-attachments/assets/7b6ad989-673b-40df-9cf0-d5165ff8a243)
    ![Image](https://github.com/user-attachments/assets/d66a3d6f-13f0-495a-93e1-c9caf537ae91)
    
</details>



<details><summary><a href="https://yeim.notion.site/Unique-1ac16458a6bf8016a621f70aa977ea9b?pvs=25">[⏱️ 성능개선] Unique 인덱스로 동시성 제어 및 성능개선</a></summary>

- 문제 상황
   - 실시간 입찰 시스템에서 동일한 가격이 동시에 입찰되는 문제가 발생했다.
   - 이로 인해 최고 입찰자와 최종 낙찰자가 다르게 결정되는 오류가 발생했으며, 특정 입찰자의 상태가 변경되지 않는 문제도 함께 확인되었다.

- 문제 원인
  - 동시에 입찰 요청이 들어올 경우, 서비스 로직에서 최고가를 조회한 후 업데이트하는 과정에서 경쟁 상태가 발생했다.
  - `사용자1`이 18,600원으로 입찰한 직후, `사용자2`이 최고가를 조회하면서 동일한 금액으로 입찰하는 상황이 발생했다.

- 해결
  - **낙관적 락을 응용한 유니크 제약으로 동시성 문제 해결**
    - `경매 ID`와 `직전최대가(prevMaxPrice)` 필드를 복합 유니크 인덱스로 설정하여, 동일한 최고가를 기준으로 입찰하는 경우 한 개의 입찰만 성공하도록 제한했다.
    - 이를 통해 동일 가격의 입찰이 발생하는 문제를 해결했다.

  - **쿼리 개선으로 성능 최적화**
    - 기존에는 `bidding_price` 기준으로 최고 입찰을 조회했으나, 새롭게 추가된 `prevMaxPrice` 인덱스를 활용하여 정렬 기준을 변경했다.
    - 이를 통해 정렬 시 발생하는 `filesort`를 제거하고, `Backward index scan`을 활용하여 성능을 향상시켰다.

적용 후 테스트를 진행한 결과, 동시 입찰이 발생하는 상황에서도 데이터 정합성이 유지되었으며, 최고 입찰자 조회 쿼리의 성능도 향상된 것을 확인했다.
     
</details>

<br/>


## 4. 기술적 의사 결정 과정

<details>
  <summary>
    <a href="https://yeim.notion.site/INSERT-UPDATE-1ad16458a6bf80dbaefefe3bfadff7b2?pvs>https://yeim.notion.site/INSERT-UPDATE-1ad16458a6bf80dbaefefe3bfadff7b2?pvs=4">💡 입찰을 INSERT가 아닌, UPDATE로 한 이유</a>
  </summary>

- **배경**  
  입찰 시스템을 구현할 때, 사용자의 새로운 입찰을 **새로운 레코드로 추가할지**, 아니면 **기존 레코드를 찾아 변경할지** 고민이 되었다. 각 방식의 장단점을 비교하여 서비스에 적합한 구조를 결정하고자 했다.

-  **선택지**  

	1. **새로운 레코드 추가 방식**  
	   - **단점**  
	     - 이전 입찰 내역을 모두 저장해야 하므로 **DB 저장 공간을 많이 차지**한다.  
	     - **‘본인 & 전체 최고 입찰’ 조회 성능이 저하**될 가능성이 있다.  

	2. **이전 레코드 변경 방식**  
	   - **단점**  
	     - 입찰할 때 **두 개의 쿼리(조회 + 업데이트)가 실행**되며, 일반적으로 `UPDATE` 문이 `INSERT` 문보다 성능이 약간 느리다.  
	     - 사용자가 **모든 입찰 내역을 직접 조회할 수 없다**.  


-  **의사결정 및 이유**  

	1. **‘본인 최고 입찰’과 ‘전체 최고 입찰’ 조회는 서비스 흐름에서 매우 빈번히 발생**  
	   - 따라서 **모든 입찰 내역을 저장하는 것은 비효율적**이라고 판단했다.  
	
	2. **입찰 시 실행되는 쿼리 비교**  
	   - **레코드 추가 방식**: `전체 최고 입찰 SELECT + 현재 입찰 INSERT`  
	   - **레코드 변경 방식**: `전체 최고 입찰 SELECT + 본인 최고 입찰 SELECT + 현재 입찰 UPDATE`  
	   - 하지만 입찰 데이터의 양이 100배 이상 차이 날 수 있기 때문에, **꼭 후자가 더 느릴 것이라고 단정할 수 없었다**.  
	
	3. **모든 입찰 내역 제공 방법**  
	   - 입찰 내역을 별도 테이블로 관리할 수도 있었지만, **채팅 기능을 통해 모든 입찰 내역이 7일간 제공**되므로 별도 테이블이 필요하지 않았다.  
	   - 이로 인해 **‘최고 입찰 내역 테이블’만 유지하는 것이 최적의 선택**이었다.  

- **결론**  
	- 입찰 시점에서의 성능 차이는 크지 않지만, **최고가 조회 시 성능 이점이 있는 ‘기존 데이터 변경 방식’을 채택**했다.  
	- **오래된 입찰 데이터를 저장할 필요가 없어**, 최고 입찰 내역 테이블과 모든 입찰 내역 테이블을 분리하려 했으나, **채팅 기능으로 입찰 내역이 제공**되므로 별도 저장이 필요하지 않았다.  
	- **이 두 가지 이유로 기존 데이터 변경 방식(UPDATE 방식)을 최종 결정**했다.
  
</details>
<details><summary> <a href="https://yeim.notion.site/Stomp-Redis-1b416458a6bf802f84b5e0571a305a5f?pvs=4">💡 실시간 채팅을 위해 Stomp+Redis를 사용한 이유</a></summary>

<img width="500" src="https://github.com/user-attachments/assets/5286117a-d591-4c8e-9951-723fbc00ae96"/>

- 배경 
    - ‘멋이룸’ 경매 서비스에서 사용자 간 실시간 소통과 입찰 정보를 공유할 수 있도록 채팅 기능을 도입했다. 
    - 이를 위해 빠른 메시지 전송과 저장이 가능하면서 확장성이 좋은 기술을 선택해야 했다.
    
- 선택지
    1.	통신 방식
    - HTTP Polling / Long Polling: 구현이 간단하지만 서버 부하 증가
	- WebSocket: 양방향 통신이 가능하고 빠른 실시간 메시지 전송 지원
	- WebSocket + STOMP: 메시지 브로커 연동 가능, 서버 확장성 고려
	2.	메시지 저장소
	- RDBMS(MySQL): 영구 저장 가능하지만 속도가 느림
	- NoSQL(MongoDB): 유연한 스키마지만 새로운 기술 학습 필요
	- In-Memory(Redis): 빠른 처리 속도, TTL 설정 가능

- 의사결정/사유 
	- WebSocket + STOMP를 선택하여 실시간 양방향 통신과 확장성을 확보했다.
	- 메시지 저장소로 Redis를 선택하여 빠른 메시지 전송과 TTL 기반 자동 삭제 기능을 활용했다.
	- 메시지 브로커로 Redis Pub/Sub을 사용하여 추가적인 설정 부담 없이 효율적인 데이터 동기화가 가능하도록 설계했다.

이를 통해 사용자들은 실시간으로 입찰 정보를 공유하고 더욱 몰입할 수 있는 환경을 제공받을 수 있게 되었다.
  
</details>
<details><summary> <a href="https://yeim.notion.site/ORDER-BY-1af16458a6bf80529f0dcb8228c19694?pvs=4">💡 정렬하기 위한 컬럼에 ORDER BY를 하지 않은 이유</a></summary>

- 배경
쿼리에서 생성일을 기준으로 내림차순 정렬을 사용했지만 ORDER BY 구문을 추가하고 실행 계획을 확인한 결과 Using filesort 문제가 발생했다. 이는 MySQL이 인덱스를 활용하지 않고 데이터를 정렬하는 방식으로, 데이터 양이 많을 경우 성능이 크게 저하될 수 있다. 이 문제를 해결하기 위한 방법을 찾아보게 되었다.

- 선택지
    - 첫 번째 방법 : ORDER BY 절에 사용된 컬럼에 인덱스를 추가
    이 방법은 filesort를 방지할 수 있으며,데이터가 많고 빈번한 정렬 작업이 필요한 경우에 적합한 해결책이다.
    - 두 번째 방법 : order_by_null 옵션을 활용
    이를 통해 MySQL이 정렬 연산을 건너뛰고, 쿼리 성능을 최적화할 수 있다. 정렬이 결과에 중요한 영향을 미치지 않다고 판단한다면 이 방법이 더 나을 수 있다.

- 의사결정/사유
내가 보기에 인덱스를 추가하는 방법은 데이터가 많을 때 성능을 향상시킬 수 있지만, 현재 쿼리에서는 정렬이 필수적인 상황이 아니었다. 데이터셋에서 정렬이 결과에 큰 영향을 미치지 않으며, LIMIT이나 다른 조건이 없기 때문에 정렬을 생략해도 문제가 없다고 판단했다. 따라서 성능 최적화를 위해 order_by_null 옵션을 사용하여 불필요한 정렬을 건너뛰기로 결정했다.
  
</details>
<details><summary> <a href="https://yeim.notion.site/BooleanBuilder-1b616458a6bf803990f8c3390f195e51?pvs=4">💡 키워드 필터링 검색을 위해 BooleanBuilder를 선택한 이유</a></summary>

- 배경
쿼리 필터링을 구현하면서 처음 ExpressionBuilder를 사용했으나, 각 조건을 하나씩 명시적으로 표현하는 방식이 코드 길이를 늘리고 가독성을 떨어뜨린다는 문제에 직면했다. 이에 따라 ExpressionBuilder를 계속 사용할지, 아니면 BooleanBuilder로 전환할지 고민이 되었다.

- 선택지 (BooleanBuilder vs ExpressionBuilder)
    
    - ExpressionBuilder 사용:
기존에는 ExpressionBuilder를 사용해 각 필터 조건을 명시적으로 연결하는 방식. 여러 조건을 and()나 or()로 조합하는 형태로 쿼리를 작성할 수 있었지만 코드가 길어지고 가독성이 떨어지는 문제가 있다.

    - BooleanBuilder 사용:
    BooleanBuilder는 조건을 동적으로 추가하면서, 가독성과 유연성을 제공합니다. 조건을 순차적으로 and()나 or() 메서드를 통해 추가할 수 있어, 보다 직관적이고 유지보수가 용이한 방식.

- 결정 및 이유
선택 방법: BooleanBuilder

    ExpressionBuilder는 조건을 하나하나 표현하는 방식이 코드 길이를 증가시켜 가독성을 크게 떨어뜨리고 조건이 많아지면 수정이나 확장에 어려움과 유지보수의 불편이 있었다. 따라서 BooleanBuilder로 전환하게 되었다. 이 방법은 조건을 동적으로 추가할 수 있기에 코드의 가독성과 유지보수성을 크게 향상시켰다.
  
</details>

<details><summary> <a href="https://yeim.notion.site/RabbitMQ-3-12-1a816458a6bf806e8850e557493f1c88?pvs=4">💡 스케줄러 비동기 처리를 위해 MessageQueue를 사용한 이유</a></summary>
    
- 배경 
    - 1분마다 cron 방식으로 하나의 스케줄러가 실행될때, 데이터 처리량에 따라, 단위시간 내에 예약된 스케줄러의 실행을 확신할 수 없다.
    - <a href="https://yeim.notion.site/1af16458a6bf80cba382fa6befa59891?pvs=4">스케줄러가 단일 스레드?로 작동 안하는 문제 - 본 스케줄러의 상황</a> 
    
- 선택지
    - 스레드 풀 :
        - Thread생성과 제거에 대한 비용을 절감할 수 있다. 
            - 오버헤드가 최소로 줄어든다.
        - ThreadPool생성 시,  많은 자원을 소모하며 thread의 적정 수를 설정하지 않으면 자원 낭비가 크거나, 요청을 효과적으로 처리하지 못한다.

    - 메세지 큐
        - 스케줄러 용 서버 구성이 쉽다. 
        - 높은 신뢰성을 갖추며 서비스 결합도가 낮다. ( producer ,consumer )
        - 메세지 큐의 종류가 많고, 메세지 큐의 장점과 상반된 방법이 존재한다.

    - @Async
        - @Async과 @EnableAsync 만으로 실행이 간단하다. 
        - 기본적으로 SimpleAsyncTaskExecutor가 사용하여 작업마다 새로운 스레드를 생성한다. 
        - aop 프록시 기반으로 작동하여, 트랜잭션 사용시 주위해야한다.

    
- 의사결정/사유 
    - **선택한 방법 : 메세지 큐**
        - 데이터 안정성 측면이나 신뢰성에서 높은 점수를 주었다.
        - 또한, 스케줄러 서버 구성 시 서비스 결합도가 낮다는 장점도 좋았다. 
        - 단점인 다양한 메세지 큐의 종류는 오히려 본 서비스에 맞는 것을 찾을 수 있다고 생각하여 단점이 아닌 장점으로 보았다.
  
</details>
<details><summary> <a href="https://yeim.notion.site/feat-Jmeter-1a116458a6bf80b397f3ff7edd4d9815?pvs=4">💡 인기 검색어 캐싱을 위해 레디스를 사용한 이유</a></summary>

- 배경 
    - 인기 검색어 기능 구현 시 DB 조회 반환 방식을 고려하지 않고 바로 캐싱을 하게 된 이유와 캐싱 구현 방식에서 로컬 메모리 캐싱과 글로벌 캐싱 중 어떤 방식을 선택해야 하는가에 대해 고찰 

- 선택지
    - **로컬 메모리 캐시**
        - 장점
            - 로컬에서 작동하기 때문에 **속도가 빠름**.
            - 네트워크 지연, 단절 이슈에 자유로움.
            - 서버 어플리케이션과 라이프 사이클을 같이하므로 사용하기 간편함.
        - 단점
            - 휘발성 메모리로 어플리케이션 종료 시 메모리 데이터가 사라짐.
            - 서버마다 각기 다른 캐시를 저장하기 때문에 변경 사항이 생길 경우 동기화가 즉시 이뤄지지 않음.
            - 어플리케이션의 메모리가 부족해 부하 상황 시 서버가 다운될 수 있음.
    - **글로벌 캐시**
        - 장점
            - 데이터를 **분산해 저장**할 수 있음.
            - 외부 저장소를 이용하기 때문에 **즉각적으로 동기화**가 반영됨.
            - 별도의 캐시 서버를 이용하기 때문에 서버 간 데이터 공유가 쉬움.
            - **확장성**이 좋음.
        - 단점
            - 네트워크 트래픽으로 인해 로컬 캐시에 비해 상대적으로 느림.
            - 네트워크 환경이 불안할 경우 단절되거나 서버가 다운되는 외부 이슈가 있음.
        
        → 글로벌 캐시는 성능 저하를 감수하더라도 동기화의 이점을 취하고 싶을 때 사용함.

- 의사결정/사유 
    - **선택한 방법 : 레디스 캐싱**
    - **선택 사유:**
        - 로컬 메모리 캐싱과 레디스 캐싱의 성능을 비교 테스트 해봤을 때, 최대 응답 시간이 각각 5ms, 18ms로 차이가 있었지만 장단점을 비교했을 때 충분히 상쇄할만한 차이라고 생각했다.
        - 추후 서버가 나뉠 때 같은 캐시 데이터를 공유해 일관성을 유지할 수 있다.
        - 서버가 재시작되더라도 캐시 데이터가 유지된다.
        - 확장성이 뛰어나다.
    
</details>



<br/>

## 5. 인프라 아키텍처 & 적용 기술


### 🏰 인프라 아키텍처

<div align="center">
    <a href="https://yeim.notion.site/v1-25-02-27-1a716458a6bf806687a7f8771dc46d66"><code>🔍 인프라 구조 ver1 (25-02-27)</code></a>
    <a href="https://yeim.notion.site/v2-25-03-12-1b416458a6bf800b98e8edd3810ed1e4"><code>🔍 인프라 구조 ver2 (25-03-12)</code></a>
</div>

<br/>

<div align="center"><img src="https://github.com/user-attachments/assets/71999528-6d49-466d-8cb2-446c7a613805" width="800"/></div>

<div align="center"><img src="https://github.com/user-attachments/assets/0403c8b5-2c33-4ad5-99f2-e4116a46b23f" width="800"/></div>



### 💎 적용 기술


#### Backend

<table>
  <tr>
    <td><img src="https://img.shields.io/badge/Java_17-007396?style=flat-square&logo=java&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat-square&logo=spring&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/QueryDSL-005571?style=flat-square&logo=graphql&logoColor=white"/></td>
  </tr>
  <tr>
    <td><img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/H2-003B57?style=flat-square&logo=h2&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/JUnit-25A162?style=flat-square&logo=junit5&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=postman&logoColor=white"/></td>
  </tr>
  <tr>
    <td><img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=json-web-tokens&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Slf4j-6DB33F?style=flat-square&logo=slf4j&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Logback-EB3223?style=flat-square&logo=logback&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=apache-jmeter&logoColor=white"/></td>
  </tr>
  <tr>
    <td><img src="https://img.shields.io/badge/Web_Socket-FF6C37?style=flat-square&logo=websocket&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Stomp-000000?style=flat-square&logo=stomp&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=flat-square&logo=rabbitmq&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white"/></td>
  </tr>
</table>

#### Infra

<table>
  <tr>
    <td><img src="https://img.shields.io/badge/AWS_EC2-FF9900?style=flat-square&logo=amazon-aws&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/AWS_S3-569A31?style=flat-square&logo=amazon-s3&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/AWS_RDS-527FFF?style=flat-square&logo=amazon-rds&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=github-actions&logoColor=white"/></td>
  </tr>
</table>

#### Collaborative Tool

<table>
  <tr>
    <td><img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white"/></td>
  </tr>
  <tr>
    <td><img src="https://img.shields.io/badge/Figma-F24E1E?style=flat-square&logo=figma&logoColor=white"/></td>
    <td><img src="https://img.shields.io/badge/IntelliJ_IDEA-000000?style=flat-square&logo=intellij-idea&logoColor=white"/></td>
  </tr>
</table>

<br>


## 6. 성과 및 회고


### 👍🏻 잘된 점

- 매주 수요일을 서비스 점검 시연 요일로 지정함으로써 개발에 집중하는 시간과 개선시간으로 일정을 균형있게 관리한 것 
- 기술 선택에 있어 충분한 서치 후 여러 선택지 중 설득력 있는 근거를 찾아낸 것
- 기술을 적용함에 있어 사용자 관점을 고려해 깊이 있는 고민을 하고 문제 해결 방안을 찾아 적용한 것
- 백엔드 기술 구현 뿐만 아니라 UI, 브랜딩, 팀문화 구축 등 다양한 부분에서 실제 기업을 운영하는 것처럼 완성도 높은 프로젝트를 진행한 것 
- 평소 화목한 분위기를 유지하며 소통하고, 꾸준한 커뮤니케이션으로 갈등 없이 프로젝트를 마무리한 것
 
    
### 👀 아쉬운 점

- 대량 트래픽으로 인한 부하 테스트 진행을 하지 못한 것
- 다양한 물품에 대한 사이즈나 상세 설명에 디테일을 추가하지 못한 것
- 유지보수를 위한 테스트 코드 작성이 부족했던 것


### 📆 향후 계획

- 부하 테스트를 통해 서버 가용성 확보
- 서버 장애에 대응하기 위한 로그 및 서버 모니터링 환경 구축
- 인덱스 조회 최적화를 지금보다 더 개선할 예정 (ex. Full-Text index, elasticsearch 등)
- 테스트 코드를 도메인 별로 꼼꼼하게 작성해 안정성 및 유지보수성 향상
- 웹 소켓 서버 분리, ECS를 통해 가용성 높은 인프라 구축
- 서비스 관점에서 보증금 제도를 도입하여 신뢰도를 높이는 것
- 입찰 패턴 분석 및 사용자별 추천 시스템 구축




<br/>

## 7. 역할 분담 및 협업 방식

### 🤼‍♂️ 역할 분담

<table>
    <tr>
        <!-- 프로필 -->
        <td align="center" style="width: 150px;">
            <a href="https://github.com/yeongbinim">
                <img src="https://github.com/yeongbinim.png" width="100px;" alt="프로필 사진">
            </a>
            <p>
                <b>👑 임영빈 (팀장)</b>
            </p>
        </td>
        <td>
	        <p>
                📌 실시간 입찰 동시성 제어 <br/>
                📌 입찰 시 최고가 조회 속도 향상 <br/>
                📌 인프라 구축, CI/CD, UI(React) <br/>
                📌 결제 API 연동(Toss Payments)
            </p>
        </td>
    </tr>
   <tr>
        <!-- 프로필 -->
        <td align="center" style="width: 150px;">
            <a href="https://github.com/KyeongranMun">
                <img src="https://github.com/KyeongranMun.png" width="100px;" alt="프로필 사진">
            </a>
            <p><b>⛑️ 문경란 (부팀장)</b></p>
        </td>
        <!-- 후기 -->
        <td>
            <p>
                📌 서비스 기획 <br/>
                📌 중간 및 최종 발표 <br/>
                📌 관리자 물품 관리 <br/>
                📌 인기 TOP 10 검색어 캐싱 (Redis) <br/>
            </p>
        </td>
    </tr>
    <tr>
        <!-- 프로필 -->
        <td align="center" style="width: 150px;">
            <a href="https://github.com/answerin1">
                <img src="https://github.com/answerin1.png" width="100px;" alt="프로필 사진">
            </a>
            <p><b>🧢 김다빈</b></p>
        </td>
        <!-- 후기 -->
        <td>
            <p>
                📌 경매 물품 전체 및 필터링 검색 <br/>
                📌 쿼리 튜닝 <br/>
                📌 조회 최적화 성능 개선 <br/>
                📌 리뷰 생성, 조회 및 삭제 
            </p>
        </td>
    </tr>
    <tr>
        <!-- 프로필 -->
        <td align="center" style="width: 150px;">
            <a href="https://github.com/ant-on-grass">
                <img src="https://github.com/ant-on-grass.png" width="100px;" alt="프로필 사진">
            </a>
            <p><b>🧢 김태은</b></p>
        </td>
        <!-- 후기 -->
        <td>
            <p>
                📌 물품 관리 <br/>
                📌 경매 좋아요 <br/>
                📌 공지 스케줄링 <br/>
                📌 스케줄러 비동기 (RabbitMQ)
            </p>
        </td>
    </tr>
    <tr>
        <!-- 프로필 -->
        <td align="center" style="width: 150px;">
            <a href="https://github.com/duol9">
                <img src="https://github.com/duol9.png" width="100px;" alt="프로필 사진">
            </a>
            <p>
                <b>🧢 이하영</b>
            </p>
        </td>
        <!-- 후기 -->
        <td>
            <p>
                📌 실시간 단체 채팅 (STOMP) <br/>
                📌 실시간 접속자 목록 <br/>
                📌 경매 입찰 <br/>
                📌 경매 시작/종료 스케줄링
            </p>
        </td>
    </tr>
</table>

### 🌱 Ground Rule

<img width="700" alt="Image" src="https://github.com/user-attachments/assets/c5109d0c-4d67-4364-97b9-45b0504d1838" />

### ✨ Core Value

<img width="700" alt="Image" src="https://github.com/user-attachments/assets/86640dc2-0bb2-4d6c-9f53-4946adaa6cec" />

<br/>

## 8. 최우수 프로젝트 선정

<div align="center"><img width="700" alt="Image" src="https://github.com/user-attachments/assets/7df640e2-466c-438b-92cd-3a483685b68a" /></div>

