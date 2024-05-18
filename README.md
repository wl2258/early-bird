<img src="https://github.com/wl2258/early-bird/assets/77067383/6cc5787a-36fa-4122-91c6-ea037d0af7fb" width="30" height="auto" align="left" /> 
<span style="margin-left: 10px;"><h1>Early Bird</h1></span>

얼리버드(Early-Bird)는 사용자들이 다양한 종류의 상품을 판매하고 구매할 수 있는 e-commerce 플랫폼입니다. 특히 판매자는 판매자가 특정 시간에 구매 버튼이 활성화되도록 판매 시간을 지정할 수 있습니다. 이 시간 동안 구매자들은 한정 수량 상품을 구매할 수 있게 됩니다.

프로젝트 기간: 2024.04.17 - 2024.05.14

## 시스템 구조
<p align="center">
    <img width="800" alt="image" src="https://github.com/wl2258/early-bird/assets/77067383/e452f2d9-19af-4dfa-9e76-596c8e43cde0">
</p>

## 개발 환경
- Java 21
- Gradle
- Spring Boot 3.2.5
- MariaDB

## 🛠 기술 스택
**프레임워크 & 라이브러리**
<div>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Gateway-6DB33F?style=for-the-badge&logo=Spring Cloud Gateway&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Netflix Eureka-6DB33F?style=for-the-badge&logo=Spring Cloud Netflix Eureka&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Config-6DB33F?style=for-the-badge&logo=Spring Cloud Config&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Bus-6DB33F?style=for-the-badge&logo=Spring Cloud Bus&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud OpenFeign-6DB33F?style=for-the-badge&logo=OpenFeign&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Cloud Circuit Breaker-6DB33F?style=for-the-badge&logo=CircuitBreaker&logoColor=white"/>
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"/>
    <img src="https://img.shields.io/badge/QueryDSL-59666C?style=for-the-badge&logo=QueryDSL&logoColor=white"/>
</div>

**메시지 큐 시스템**
<div>
    <img src="https://img.shields.io/badge/Apache Kafka-231F20?style=for-the-badge&logo=Apache Kafka&logoColor=white"/>
    <img src="https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=RabbitMQ&logoColor=white"/>
</div>

**인프라**
<div>
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white"/>
</div>

## 📌 ERD
<p align="center">
  <img src="https://github.com/wl2258/early-bird/assets/77067383/28379645-6ea6-49ab-8c25-54b6c0166ac9" width="500" />
</p>

## 📜 API 문서
[API 문서 바로가기](https://documenter.getpostman.com/view/19468204/2sA3JRaf81)

## 🌟 주요 기능
- Redis Caching을 통한 상품 재고 관리
- Api Gateway를 통한 마이크로서비스 아키텍처 구축
- Kafka를 통한 이벤트 기반 아키텍처 구축
- Redis Replication을 통한 분산 처리
- Open Feign을 활용한 마이크로서비스 간 통신
- Circuit Breaker를 활용한 회복탄력성 증대
- 한정 판매 상품 구매 기능
- 스케줄러를 통한 주문 및 배송 상태 관리
- JWT를 사용한 로그인
- Google SMTP를 사용한 이메일 인증
- Spring Security를 사용한 인증 및 인가

## 📈 성능 최적화 및 트러블 슈팅

### 성능 최적화 사례
- MSA 도입
- API Gateway 추가
- [Redis Caching을 활용해 상품 주문 성능 개선: write-back & Lua script 사용](https://ssonzm.tistory.com/114)

### 트러블슈팅 경험
- Kafka 트랜잭션과 Spring 트랜잭션의 시점 차이로 인한 문제 발생
- [Kafka 2개의 Consumer가 하나의 토픽을 구독할 때 문제 발생](https://ssonzm.tistory.com/117)
