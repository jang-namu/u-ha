# 유하(U-ha) - 유학생과 내국인 학생의 문화교류 플랫폼

<div align="center">
  <h3>2024 LuckyThon 대상 수상작</h3>
  <p>"유학생 하이(U-ha)" - 2분 안에 EVEN하게</p>
</div>

## 📋 프로젝트 개요

유하(U-ha)는 대학 내 유학생과 내국인 학생 간의 문화교류를 촉진하기 위한 매칭 서비스 플랫폼입니다. 기존 코디네이터 프로그램의 한계(낮은 보상, 복잡한 업무, 부족한 기회)를 극복하고, 양방향 교류를 통해 서로의 필요를 충족시키는 "어울림" 플랫폼을 구현했습니다.

### 3無 원칙
- 추가 비용 없음 (대학 내 리소스 활용)
- 복잡한 기획 없음 (간단하고 직관적인 인터페이스)
- 학교별 종속성 없음 (범용적 서비스)

### 주요 기능
1. 유학생-재학생 매칭 서비스 
2. LLM 기반 실시간 통번역 및 대화 필터링
3. 편리한 관리자 대시보드

## 🚀 기술 스택

### 백엔드
- 언어: Java
- 프레임워크: Spring Boot
- ORM: JPA/Hibernate
- 데이터베이스: MySQL

### 통신
- RESTful API
- WebSocket (실시간 채팅)

### 주요 라이브러리
- Spring Data JPA
- WebSocket
- 번역 및 필터링: GPT API

## 🌟 핵심 기능 상세

### 1. 유학생 매칭 
사용자의 관심사, 언어 능력, 학과 등을 기반으로 최적의 매칭을 제공합니다. 유학생에게는 한국어 학습, 한국 생활 적응, 한국인 친구 만들기 등의 기회를, 내국인 학생에게는 외국어 연습, 해외 문화 경험, 취업 인터뷰 준비 등의 기회를 제공합니다.

### 2. 실시간 통번역 시스템
언어 장벽을 해소하기 위한 실시간 통번역 시스템을 구현했습니다. Flask로 구현된 별도 AI 서버와 연동하여 LLM 기반 필터링을 통해 번역 품질을 향상시키고 대화 맥락을 유지합니다.

### 3. 비용 최소화 구조
교내 인프라를 활용하고 대학별 동아리 주도로 유지보수하는 구조를 통해 비용을 0원으로 최소화했습니다.

## 👥 팀 정보

- 팀명: 야구좀그만봐
- 2024 LuckyThon 대상 수상
---

© 2024 유하(U-ha) | 인천대학교 LuckyThon 프로젝트
