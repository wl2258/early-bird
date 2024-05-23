<img src="https://github.com/wl2258/early-bird/assets/77067383/6cc5787a-36fa-4122-91c6-ea037d0af7fb" width="30" height="auto" align="left" /> 
<span style="margin-left: 10px;"><h1>Early Bird</h1></span>

얼리버드(Early-Bird)는 사용자들이 다양한 종류의 상품을 판매하고 구매할 수 있는 e-commerce 플랫폼입니다. 특히 판매자는 판매자가 특정 시간에 구매 버튼이 활성화되도록 판매 시간을 지정할 수 있습니다. 이 시간 동안 구매자들은 한정 수량 상품을 구매할 수 있게 됩니다.

프로젝트 기간: 2024.04.17 - 2024.05.14

<br>

## 시스템 구조
<p align="center">
    <img width="800" alt="image" src="https://github.com/wl2258/early-bird/assets/77067383/a145d58e-9f60-4413-845d-770f23d3ee53">
</p>

<br>

## 개발 환경
- Java 21
- Gradle
- Spring Boot 3.2.5
- MariaDB
  
<br>

## 🛠 기술 스택
**프레임워크 & 라이브러리**
<div>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Gateway-6DB33F?style=for-the-badge&logo=Spring Cloud Gateway&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Netflix Eureka-6DB33F?style=for-the-badge&logo=Spring Cloud Netflix Eureka&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Bus-6DB33F?style=for-the-badge&logo=Spring Cloud Bus&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud OpenFeign-6DB33F?style=for-the-badge&logo=OpenFeign&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Circuit Breaker-6DB33F?style=for-the-badge&logo=CircuitBreaker&logoColor=white"/>
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"/>
    <img src="https://img.shields.io/badge/QueryDSL-59666C?style=for-the-badge&logo=QueryDSL&logoColor=white"/>
</div>

**DB**
<div>
    <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
    <img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"/>
</div>

**메시지 큐 시스템**
<div>
    <img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white"/>
</div>

**인프라**
<div>
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
</div>

<br>

## 📌 ERD
<p align="center">
  <img src="https://github.com/wl2258/early-bird/assets/77067383/28379645-6ea6-49ab-8c25-54b6c0166ac9" width="500" />
</p>

<br>

## 📜 API 문서
[API 문서 바로가기](https://documenter.getpostman.com/view/19468204/2sA3JRaf81)

<br>

## 🌟 주요 기능
- Redis Caching을 통한 상품 재고 관리
    - 데이터베이스 부하 감소 및 재고 관리 성능 향상
- Api Gateway를 통한 마이크로서비스 아키텍처 구축
    - 단일 엔드포인터 제공 및 확장성 향상, 인증 및 권한 관리
- Kafka를 통한 이벤트 기반 아키텍처 구축
  - Kafka를 통해 마이크로서비스 간 비동기 통신 구현(데이터 조작 작업)
- Redis Replication을 통한 분산 처리
    - 성능 저하 개선 및 가용성 향상
- Open Feign을 활용한 마이크로서비스 간 통신
  - 마이크로서비스 간 동기 통신 구현(단순 조회)
- 스케줄러를 통한 주문 및 배송 상태 관리
  - 주기적인 주문 및 배송 상태 변경
- 한정 수량 상품 구매 기능
- JWT를 사용한 로그인
- Google SMTP를 사용한 이메일 인증
- Spring Security를 사용한 인증 및 인가
  
<br>

## 📈 성능 최적화 및 트러블슈팅
### Redis 캐시를 활용하여 상품 주문 처리 성능 개선: write-back & Lua script 사용 ([↗️click](https://ssonzm.tistory.com/114))
<img src="https://github.com/wl2258/early-bird/assets/77067383/bb337c07-e0b3-47d4-82b8-3628dedba0ef" width="500" />

- 쓰기 전략: Write-Throw -> Write-Back
- TPS: **153.1/sec** -> **648.1/sec**
      
<br>

### Kafka 트랜잭션과 Spring 트랜잭션 시점 차이로 인한 데이터 일관성 문제 해결 ([↗️click]())

- 문제 상황: 트랜잭션 내에서 데이터 변경 후 이벤트를 발행하는 경우, 다른 서비스에서 트랜잭션 커밋 이전의 데이터를 사용할 때 예외 발생
- 해결 방법: Spring의 **@TransactionalEventListener**을 사용하여 **트랜잭션 커밋 이후**에 **이벤트를 발행**하도록 구현
<br>

### Kafka Consumer 그룹 관리를 통한 메시지 처리 최적화 ([↗️click](https://ssonzm.tistory.com/117))
- 문제 상황: Kafka에서 동일한 그룹 ID를 사용하는 2개의 Consumer가 하나의 토픽을 구독할 때, 한 컨슈머에서만 메시지를 받는 문제 발생
- 해결 방법: Consumer마다 **고유한 그룹 ID**를 지정해 독립적으로 메시지를 처리하도록 구현
<br>

### Resilience4j를 활용한 회복탄력성 향상 ([↗️click]())
- 문제 상황: OpenFeign 통신 중 장애 발생 시, 다른 서비스에 장애가 전파됨
- 해결 방법: **Resilience4J**의 **Circuit Breaker** 와 **TimeLimiter** 기능을 활용해 서비스 간 장애 전파 문제 해결
<br>

### Redis Replication을 통한 분산 처리 ([↗️click](https://ssonzm.tistory.com/118))
- 문제 상황: 단일 Redis 인스턴스에 과도한 부하가 발생하면 성능 저하 및 가용성 문제가 발생할 수 있음
- 해결 방법: Redis Replication을 통해 마스터-슬레이브 구조를 구축하여 부하를 분삼시킴
    - 마스터: 데이터 쓰기 작업 담당
    - 슬레이브: 마스터로부터 데이터를 복제받아 읽기 작업 담당
