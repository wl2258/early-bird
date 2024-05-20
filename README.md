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
- Api Gateway를 통한 마이크로서비스 아키텍처 구축
- Kafka를 통한 이벤트 기반 아키텍처 구축
  - Kafka를 통해 마이크로서비스 간 비동기 통신 구현
- Redis Replication을 통한 분산 처리
- Open Feign을 활용한 마이크로서비스 간 통신
  - 마이크로서비스 간 동기 통신 구현 
- Resilience4J의 Circuit Breaker를 활용해 회복탄력성 향상
- 한정 수량 상품 구매 기능
- 스케줄러를 통한 주문 및 배송 상태 관리
- JWT를 사용한 로그인
- Google SMTP를 사용한 이메일 인증
- Spring Security를 사용한 인증 및 인가
  
<br>

## 📈 성능 최적화 및 트러블슈팅
- Redis 캐시를 활용하여 상품 주문 처리 성능 개선: write-back & Lua script 사용
- Kafka 트랜잭션과 Spring 트랜잭션의 시점 차이로 인한 문제 발생
- Kafka Consumer 그룹 관리를 통한 메시지 처리 최적화
- Circuit Breaker를 활용한 회복탄력성 향상
