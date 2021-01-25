# 키친포스

## 요구 사항

### 상품(product)

- `상품(product)`을 생성한다.
    - 가격은 필수이고, 0원 이상이 아니면 `상품(product)`을 생성할 수 없다.
- 모든 `상품(product)` 목록을 조회한다.

### 메뉴 그룹(menu group)

- `메뉴 그룹(menu group)`을 생성한다.
- 모든 `메뉴 그룹(menu group)` 목록을 조회한다.

### 메뉴(menu)

- `메뉴(menu)`를 생성한다.
    - 가격은 필수이고, 0원 이상이 아니면 `메뉴(menu)`를 생성할 수 없다.
    - `메뉴(menu)`가 속한 `메뉴 그룹(menu group)`이 없으면 `메뉴(menu)`를 생성할 수 없다.
    - `메뉴(menu)`는 등록된 `메뉴 상품(menu product)`이 없을 수도 있고, 포함할 수도 있다.
    - `메뉴(menu)`의 가격이 `메뉴 상품(menu product)`의 가격의 합보다 크면 `메뉴(menu)`를 생성할 수 없다.
- 모든 `메뉴(menu)` 목록을 조회한다.
    - 각 `메뉴(menu)`의 `메뉴 상품(menu product)`을 같이 조회한다.

### 주문(order)

- `주문(order)`을 생성한다.
    - `주문(order)`에 필요한 주문 메뉴를 담은 `주문 항목(order line item)`이 없으면 `주문(order)`을 생성할 수 없다.
    - 모든 `주문 항목(order line item)`의 `메뉴(menu)`는 등록되어 있어야한다.
    - `주문(order)`할 `주문 테이블(order table)`이 없으면, `주문(order)`을 생성할 수 없다.
    - `주문(order)`이 되면 `주문 상태(order status)`는 'COOKING' 이다.
- 모든 `주문(order)` 목록을 조회한다.
    - 각 `주문(order)`의 `주문 항목(order line item)`을 같이 조회한다.
- `주문(order)`의 `주문 상태(order status)`를 변경한다.
    - `주문 상태(order status)`가 'COMPLETION' 인 경우 상태를 변경할 수 없다.

### 주문 테이블(order table)

- `주문 테이블(order table)`을 생성한다.
- 모든 `주문 테이블(order table)` 목록을 조회한다.
- `주문 테이블(order table)`을 비어있는 상태로 변경한다.
    - `주문 테이블(order table)`이 존재해야한다.
    - `주문 테이블(order table)`이 `단체 지정(table group)`되어 있으면 비어있는 상태로 변경할 수 없다.
    - `주문 테이블(order table)`의 `주문 상태(order status)`가 'COOKING' 이나 'MEAL' 상태가 이면 비어있는 상태로 변경할 수 없다.
- `주문 테이블(order table)`에 `방문한 손님 수(number of guests)`를 변경한다.
    - 하나의 `주문 테이블(order table)`의 `방문한 손님 수(number of guests)`가 0명보다 적으면, `방문한 손님 수(number of guests)`를 변경할 수 없다.
    - `주문 테이블(order table)`이 비어있으면, `방문한 손님 수(number of guests)`를 변경할 수 없다.

### 단체 지정(table group)

- `단체 지정(table group)`을 생성한다.
    - `단체 지정(table group)`으로 등록할 `주문 테이블(order table)`이 2개 미만이면 생성할 수 없다.
    - `단체 지정(table group)`할 `주문 테이블(order table)`은 비어있어야한다.
    - `단체 지정(table group)`할 `주문 테이블(order table)`이 `단체 지정(table group)`되어있으면 생성할 수 없다.
- `단체 지정(table group)`을 해제한다.
    - `단체 지정(table group)`된 `주문 테이블(order table)`에서 `주문 상태(order status)`가 'COOKING' 이나 'MEAL' 이면 해제할 수 없다.

### 주문 상태(order status)

- `주문 상태(order status)`는 'COOKING'(조리중), 'MEAL'(식사중), 'COMPLETION'(계산완료) 로 구성된다.
- `주문 상태(order status)`의 상태 변경 순서는 'COOKING'(조리중) -> 'MEAL'(식사중) -> 'COMPLETION'(계산완료) 진행된다.

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

## 리팩터링 구현 목록

- [x] 실제 변경 코드의 테스트를 고려하여, Service Layer 테스트를 통합 테스트로 변경
    - [x] Product 통합 테스트 적용
        - [x] test case - `상품`을 생성한다.
        - [x] test case - 가격은 필수이고, 0원 이상이 아니면 `상품`을 생성할 수 없다.
        - [x] test case - 모든 `상품` 목록을 조회한다.
        - [x] Service Layer 테스트 케이스 리팩터링
    - [x] MenuGroup 통합 테스트 적용
        - [x] test case - `메뉴 그룹`을 생성한다.
        - [x] test case - 모든 `메뉴 그룹` 목록을 조회한다.
        - [x] Service Layer 테스트 케이스 리팩터링
    - [x] Menu 통합 테스트 적용
        - [x] Menu Service Layer 테스트의 재사용되는 코드 응집화
        - [x] test case - `메뉴`를 생성한다.
        - [x] test case - 가격은 필수이고, 0원 이상이 아니면 `메뉴`를 생성할 수 없다.
        - [x] test case - `메뉴`의 가격이 `메뉴 상품`의 가격의 합보다 크면 `메뉴`를 생성할 수 없다.
        - [x] test case - 모든 `메뉴` 목록을 조회한다.
        - [x] Service Layer 테스트 케이스 리팩터링
    - [x] OrderTable 통합 테스트 적용
        - [x] test case - `주문 테이블`을 생성한다.
        - [x] test case - `주문 테이블`을 비어있는 상태로 변경한다.
        - [x] test case - `주문 테이블`이 `단체 지정`되어 있으면 비어있는 상태로 변경할 수 없다.
        - [x] test case - `주문 테이블`의 `주문 상태`가 'COOKING' 이나 'MEAL' 상태가 이면 비어있는 상태로 변경할 수 없다.
        - [x] test case - `주문 테이블`에 `방문한 손님 수`를 변경한다.
        - [x] test case - 하나의 `주문 테이블`의 `방문한 손님 수`가 0명보다 적으면, `방문한 손님 수`를 변경할 수 없다.
        - [x] test case - `주문 테이블`이 비어있으면, `방문한 손님 수`를 변경할 수 없다.
        - [x] test case - 모든 `주문 테이블` 목록을 조회한다.
        - [x] Service Layer 테스트 케이스 리팩터링
    - [x] Order 통합 테스트 적용
        - [x] test case - `주문`을 생성한다.
        - [x] test case - `주문`에 필요한 주문 메뉴를 담은 `주문 항목`이 없으면 `주문`을 생성할 수 없다.
        - [x] test case - `주문`할 `주문 테이블`이 없으면, `주문`을 생성할 수 없다.
        - [x] test case - 모든 `주문` 목록을 조회한다.
        - [x] test case - `주문`의 `주문 상태`를 변경한다.
        - [x] test case - `주문 상태`가 'COMPLETION' 인 경우 상태를 변경할 수 없다.
    - [x] TableGroup 통합 테스트 적용
        - [x] test case - `단체 지정`을 생성한다.
        - [x] test case - `단체 지정`으로 등록할 `주문 테이블`이 2개 미만이면 생성할 수 없다.
        - [x] test case - `단체 지정`할 `주문 테이블`은 비어있어야한다.
        - [x] test case - `단체 지정`할 `주문 테이블`이 `단체 지정`되어있으면 생성할 수 없다.
        - [x] test case - `단체 지정`을 해제한다.
        - [x] test case - `단체 지정`된 `주문 테이블`에서 `주문 상태`가 'COOKING' 이나 'MEAL' 이면 해제할 수 없다.
        - [x] Service Layer 테스트 케이스 리팩터링
- [x] Domain Logic 개선에 앞서, UI 전달되는 Domain Context를 Service Layer에 격리
    - [x] Product
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
    - [x] MenuGroup
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
    - [x] Menu
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
    - [x] OrderTable
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
    - [x] Order
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
        - [x] Order와 Menu의 정보와 갯수를 전달하기 위한 Request DTO 적용
    - [x] TableGroup
        - [x] Service Layer에 Request DTO 적용
        - [x] Service Layer에 Response DTO 적용
        - [x] TableGroup에서 OrderTable들과의 정보와 갯수를 전달하기 위한 Request DTO 적용
- [x] Service Layer에 있는 Domain Logic를 Domain Layer로 이동
    - [x] OrderTable
        - [x] Service method 개선 : OrderTable 생성
        - [x] Service method 개선 : OrderTable Status 변경
        - [x] Service method 개선 : OrderTable 손님 수 변경
    - [x] Product
        - [x] Service method 개선 : Product 생성
    - [x] Order
        - [x] Service method 개선 : Order 생성
        - [x] Service method 개선 : Order Status 변경
    - [x] Menu
        - [x] Service method 개선 : Menu 생성
    - [x] TableGroup
        - [x] Service method 개선 : TableGroup 생성
        - [x] Service method 개선 : TableGroup 단체 지정 해제
- [x] JPA 도메인 리팩터링
    - [x] 모든 도메인에서 사용하는 PK와 시간 추상화 적용
    - [x] Product
        - [x] JPA Entity, Repository 적용
        - [x] price 필드 Embedded 타입 반영
    - [x] OrderTable
        - [x] JPA Entity, Repository 적용
    - [x] TableGroup
        - [x] JPA Entity, Repository 적용
        - [x] 잘못 지정된 orderTables의 @OneToMany 옵션 개선
    - [x] MenuProduct
        - [x] JPA Entity, Repository 적용
        - [x] 엔티티 필드 연관 관계 개선
    - [x] MenuGroup
        - [x] JPA Entity, Repository 적용
    - [x] Menu
        - [x] JPA Entity, Repository 적용
        - [x] 엔티티 필드 연관 관계 개선
        - [x] price 필드 Embedded 타입 반영
        - [x] 필드 제약사항 추가와 파라미터 검증
    - [x] OrderMenu
        - [x] JPA Entity, Repository 적용
    - [x] Order
        - [x] JPA Entity, Repository 적용
        - [x] 문자열 orderStatus @Enumerated OrderStatus 적용
        - [x] 누락된 필드 제약사항 수정

## 양방향을 단방향으로 구현

### 양방향을 단방향으로 개선하는 목적

클래스간 방향 개선

- 보통 양방향이면 보통 "일대다" 관계의 성능상 이슈 때문에 개선 포인트가 된다.
- 또한, 이후에 서비스에 도메인을 다루게 되면서, 양방향으로 순환 참조되는 문제가 발생할 수도 있다. 그래서 컨텍스트를 끊어주어야한다.
- 성능적 이슈 때문에 "일대다" 관계를 없애고, "다대일"의 관계를 주인관계로 사용하고, "일대다" 정보를 어떻게 가져올지 생각해야한다.
- 물론 "일대다"로 가져오는 데이터의 갯수가 비즈니스에서 적다고 하면 문제가 발생하지 않는다. 하지만 유지보수 관점에서는 결국 기술부채라고 생각한다.
- 여기에서는 "일대다" 관계를 가진 엔티티의 필드를 제거하고, 쿼리를 통해서 직접 참조하는 방식으로 한다.

패키지 간 방향 개선

- DDD의 개념을 적용하면, 애그리거트 단위로 패키징을 하면 일관성 있는 구조를 가질 수 있다.
- 일관성이 확보되면 복잡한 도메인 모델을 단순하고 확장에 용이한 구조로 가져갈 수 있게 된다.

### 개선 목록

- [ ] 클래스 간 방향 개선
    - 도메인 Menu-MenuProduct 간 도메인 일대다 제거
    - 도메인 OrderTable-Order 간 도메인 일대다 제거
- [ ] 패키지 간 방향 개선
