[English](README.md)

# Eye of Sentinel (EOS)

### Spring Boot 기반 실시간 로그 대시보드

<p align="center">
  <b>Elasticsearch</b> 및 <b>OpenSearch</b>를 모두 지원하는 웹 로그 뷰어입니다.
  <br>복잡한 Kibana 설정 없이, 필수적인 로그 모니터링 기능을 빠르고 직관적으로 제공합니다.
</p>

---

## 대시보드 미리보기

아래는 EOS의 실제 구동 화면입니다. 깔끔한 UI와 실시간 모니터링 기능을 확인해보세요.

<p align="center">
  <img src="images/img.png" alt="EOS Dashboard Preview" width="800" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
</p>

---

## 주요 기능

EOS는 로그 분석 및 모니터링에 최적화된 핵심 기능들을 제공합니다.

* **실시간 로그 뷰어**
    * 지정된 간격(기본 10초)으로 최신 로그를 자동 새로고침하여 보여줍니다.
    * 'Live' 표시기를 통해 현재 모니터링 상태를 직관적으로 알 수 있습니다.

* **유연한 백엔드 지원**
    * **Elasticsearch (7.x - 8.x)** 및 **OpenSearch (1.x - 2.x)** 클러스터와 완벽하게 호환됩니다.
    * 단일 클라이언트로 두 엔진을 모두 지원하여 환경 제약 없이 사용할 수 있습니다.

* **동적 인덱스 및 필드 관리**
    * 클러스터에 존재하는 인덱스 목록을 자동으로 가져와 사이드바에 제공합니다.
    * 와일드카드(*) 패턴을 사용한 다중 인덱스 검색을 지원합니다.
    * 인덱스의 매핑 정보를 분석하여 테이블 헤더(컬럼)를 동적으로 구성합니다.

* **사용자 친화적인 UI/UX**
    * **Thymeleaf**와 **Tailwind CSS**로 구축된 현대적이고 반응형인 웹 인터페이스를 제공합니다.
    * 시스템 설정에 따라 자동으로 전환되는 **다크 모드**를 지원하여 눈의 피로를 줄여줍니다.

---

## 기술 스택

| 분류 | 기술 | 비고 |
| :--- | :--- | :--- |
| **Backend** | Java 17, **Spring Boot 3.2** | 견고한 백엔드 서버 구축 |
| **Search Client**| Elasticsearch Rest Client | ES/OS 호환성을 위한 Low-level 클라이언트 |
| **Frontend** | **Thymeleaf**, **Tailwind CSS** | 서버 사이드 렌더링 및 유틸리티 퍼스트 CSS 프레임워크 |
| **Build Tool** | Gradle | 의존성 관리 및 빌드 자동화 |

---

## 시작하기

### 요구 사항
* Java 17 이상
* 실행 중인 Elasticsearch 또는 OpenSearch 인스턴스

### 설치 및 실행

1.  **저장소 복제**
    ```bash
    git clone [https://github.com/dev-wonjoon/eye-of-sentinel.git](https://github.com/dev-wonjoon/eye-of-sentinel.git)
    ```

2.  **환경 설정 (`src/main/resources/application.properties`)**
    사용 중인 검색 엔진 정보에 맞춰 수정합니다.
    ```properties
    # EOS_OPENSEARCH_URI=http://your-cluster-ip:9200
    # EOS_OPENSEARCH_USERNAME=admin
    # EOS_OPENSEARCH_PASSWORD=your_password
    ```
    *(환경 변수를 통해 설정할 수도 있습니다)*

3.  **애플리케이션 실행**
    ```bash
    # Linux/macOS
    ./gradlew bootRun

    # Windows
    gradlew.bat bootRun
    ```

4.  **접속**
    브라우저에서 `http://localhost:8080`으로 접속합니다.

---

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.