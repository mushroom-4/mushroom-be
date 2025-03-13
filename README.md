# Wanna Be Cool? MUTT-IROOM

<img src="https://github.com/user-attachments/assets/b2d42506-101e-48f3-aa8c-d3e3c8515326" width="1200"/>

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
    - [🤼‍♂️ 역할 분담](#역할-분담)
    - [🌱 Ground Rule](#Ground-Rule)
    - [✨ Core Value](#Core-Value)

<br/>

## 1. 프로젝트 소개

<div align="center"><img src="https://github.com/user-attachments/assets/c3f6ba7b-3a39-41f3-a947-0e8319eaa1bc" width="1200"/></div>

**[패션 경매 e-commerce 플랫폼]**

> 멋이룸은 단순히 물건을 사고파는 곳이 아닙니다.
> 
> 트렌드를 선도하는 사람들, 희귀한 아이템을 찾는 컬렉터들, 그리고 나만의 감각을 더하고 싶은 이들이 모이는 공간입니다.
>
> <br/>
> 
> 한정판 스니커즈, 독창적인 디자이너 브랜드, 빈티지 명품까지 멋이룸에서는 누구나 원하는 아이템을 경매로 만나고,
>
> 직접 그 가치를 결정할 수 있습니다.
>
> <br/>
> 
> 우리는 **구매의 순간을 하나의 경험**으로 만듭니다.
> 
> 치열한 경매의 긴장감, 예상치 못한 기회, 그리고 내가 원하던 아이템을 손에 넣었을 때의 짜릿함까지!
> 
> 당신이 원하는 멋을 이루는 곳, **멋이룸**과 함께 하세요!

<br/>

## 2. 주요 기능

- 유저가 판매와 구매 기능 모두 사용할 수 있습니다.
- 판매하기 서비스
    - 유저가 판매를 원할 때는 판매하기 서비스에서 판매하고자 하는 아이템을 등록할 수 있습니다.
    - 흐름
        1. 판매하고자 하는 물품의 정보를 입력하고, 멋이룸 플랫폼으로 상품을 전달합니다.
        2. 멋이룸 플랫폼에서 검수를 진행한 후 경매 물품을 등록합니다. 
- 구매하기 서비스
    - 유저는 경매에 자유롭게 참여할 수 있습니다.
    - 흐름
        1. 구매하고자 하는 물품에 입찰가를 정해 입찰 대기를 할 수 있습니다.
        2. 입찰가는 계속 수정이 가능하며, 가장 높은 입찰가로 입찰한 유저가 낙찰이 됩니다. 
- 입찰자 실시간 채팅 기능
    - 입찰에 참여한 유저들끼리 실시간으로 채팅을 할 수 있습니다.
    - 입찰에 참여하지 않은 유저는 채팅에 참여할 수 없지만 실시간 채팅 내용을 확인할 수 있습니다.
    - 최대 입찰 금액이 변경되는 사항을 실시간으로 확인할 수 있습니다.
- 관심 갖기 기능
    - 유저는 관심 있는 경매 물품에 대해 관심 갖기를 등록하고, 관심 갖기를 한 물품의 목록을 확인할 수 있습니다.
- 경매 공지 기능
    - 관심 갖기한 경매 물품의 경매가 시작될 때 공지 알림을 받을 수 있습니다.
- 결제 기능
    - 유저는 낙찰받은 경매 물품을 다양한 결제 수단을 통해 안정적으로 결제할 수 있습니다.
- 리뷰 기능
    - 유저는 경매 물품의 결제를 완료한 후, 해당 물품을 판매한 유저에 대한 리뷰를 남길 수 있습니다.

<br/>

## 3. 트러블 슈팅 & 성능 개선

<details>
    <summary><a href="https://yeim.notion.site/AWS-EC2-19b16458a6bf808391f0c77b5f21fd22">[트러블슈팅] AWS EC2 서버 시간 불일치 이슈</a></summary>

- 문제 상황

- 문제 원인
  
- 해결

</details>
<details><summary><a href="https://yeim.notion.site/Unique-1ac16458a6bf8016a621f70aa977ea9b?pvs=25">[트러블슈팅] Unique 제약 조건을 활용한 동시성 제어</a></summary>


- 문제 상황

- 문제 원인
  
- 해결
     
</details>
<details><summary><a href="https://yeim.notion.site/1b316458a6bf80fa8834d56610052e0e">[트러블슈팅] 메시지 유실 이슈</a></summary>
    
- 문제 상황

- 문제 원인
  
- 해결
    
</details>
<details><summary><a href="https://yeim.notion.site/19f16458a6bf808fbe9cfb9dc5a1593f">[트러블슈팅] 결제 API와 트랜잭션 충돌 제어</a></summary>
    
- 문제 상황

- 문제 원인
  
- 해결

</details>
<details><summary><a href="https://yeim.notion.site/1a416458a6bf80b4a71cebf6e247a5f1">[트러블슈팅] 채팅 내역 중복 로드 이슈</a></summary>

- 문제 상황

- 문제 원인
  
- 해결

</details>
<details><summary><a href="https://yeim.notion.site/03-12-1ad16458a6bf80beae0bc2e546df1421">[트러블슈팅] 스케줄러 데이터 처리 누락 이슈</a></summary>

- 문제 상황

- 문제 원인
  
- 해결

</details>
<details><summary><a href="https://yeim.notion.site/1af16458a6bf80748e49f37da8e1a621?pvs=25">[성능개선] 복합 인덱스 적용</a></summary>

- 개선 이슈

- 개선 과정
  
- 결과

</details>
<details><summary><a href="https://yeim.notion.site/1ad16458a6bf8049885aea64850c1c91">[성능개선] 쿼리 튜닝으로 검색 속도 최적화하기</a></summary>

- 개선 이슈

- 개선 과정
  
- 결과 

</details>

<br/>


## 4. 기술적 의사 결정 과정

<details>
  <summary>
    <a href="https://www.notion.so/yeim/INSERT-UPDATE-1ad16458a6bf80dbaefefe3bfadff7b2?pvs>https://www.notion.so/yeim/INSERT-UPDATE-1ad16458a6bf80dbaefefe3bfadff7b2?pvs=4">입찰을 INSERT가 아닌, UPDATE로 한 이유</a>
  </summary>

- 배경 
    
- 선택지
    
- 의사결정/사유 
  
</details>
<details><summary> <a href="https://www.notion.so/yeim/Stomp-Redis-1b416458a6bf802f84b5e0571a305a5f?pvs=4">실시간 채팅을 위해 Stomp+Redis를 사용한 이유</a></summary>

- 배경 
    
- 선택지
    
- 의사결정/사유 
  
</details>
<details><summary> <a href="https://www.notion.so/yeim/VS-1b216458a6bf8047ad01fa4c28a4bef8?pvs=4">조회 최적화 단일 VS 복합</a></summary>

- 배경 
    
- 선택지
    
- 의사결정/사유 
  
</details>
<details><summary> <a href="https://www.notion.so/yeim/ORDER-BY-1af16458a6bf80529f0dcb8228c19694?pvs=4">정렬하기 위한 컬럼에 ORDER BY를 하지 않은 이유</a></summary>

- 배경 
    
- 선택지
    
- 의사결정/사유 
  
</details>
<details><summary> <a href="https://www.notion.so/yeim/RabbitMQ-3-12-1a816458a6bf806e8850e557493f1c88?pvs=4">스케줄러 비동기 처리를 위해 RabbitMQ를 사용한 이유</a></summary>
    
- 배경 
    
- 선택지
    
- 의사결정/사유 
  
</details>
<details><summary> <a href="https://www.notion.so/yeim/feat-Jmeter-1a116458a6bf80b397f3ff7edd4d9815?pvs=4">인기 검색어 캐싱을 위해 레디스를 사용한 이유</a></summary>

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


    
### 👀 아쉬운 점



### 📆 향후 계획

- 기술적인 내용들


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

