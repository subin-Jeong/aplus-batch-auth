# aplus-batch-template
에이쁠 배치 작업을 사용하기 위한 템플릿입니다. 자세한 내용은 README.md 에 작성되어 있습니다.

## 실행

### 실행 환경

- Java 17

### 실행 방법

jar 파일을 생성합니다.

```shell
./gradlew build
```

애플리케이션 클래스 위치와 job 이름 입력해 실행합니다.

```shell
java -DJOB_NAME=updateAccountingHistoryJob \
    -DDB_HOST=dev-aplus-db01.altools.co.kr \
    -DDB_PORT=3307 \
    -DDB_USERNAME=apluspoint \
    -DDB_PASSWORD=eoqkr1! \
    -jar ./build/libs/point-updater-0.0.1-SNAPSHOT.jar 
```

## 사용 방법

### 초기 환경 구성

배치 템플릿은 `aplus_point` 데이터베이스를 타깃으로 설정되어 있습니다.
`aplus_point` 외 데이터베이스를 사용하려면 `main/**/application.yml`, `test/**/SpringBatchInitializer.class`, `build.gradle.kts`, `test/resources/script.sql` 파일을 수정해주세요.

### JOOQ 구성

`build.gradle.kts` 에서 `jooqIncludeTables` 변수로 원하는 테이블 정보를 가져옵니다.
만약 해당 테이블에 `enum` 타입이 있다면 `ForcedType` 에 `jsonConverter` 를 만들어주세요.

### 테스트 환경 구성

`SpringBatchInitializer`에는 테스트컨테이너로 실행되는 데이터베이스 기본 정보가 포함됩니다.
`mariaDBContainer()` 에서 데이터베이스 스키마 이름을 입력하고 `test/resources/script.sql` 파일에 스키마 생성 쿼리를 작성합니다.

