package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("단체 지정 서비스에 관련한 기능")
@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;

    @DisplayName("`단체 지정`을 생성한다.")
    @Test
    void createTableGroup() {
        // Given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse orderTable2 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable1.toOrderTable(), orderTable2.toOrderTable()));

        // When
        TableGroupResponse response = tableGroupService.create(request);
        OrderTable foundOrderTable1 = tableService.findById(orderTable1.getId());
        OrderTable foundOrderTable2 = tableService.findById(orderTable2.getId());

        // Then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTables()).containsAnyElementsOf(Arrays.asList(foundOrderTable1, foundOrderTable2)),
                () -> assertThat(response.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("`단체 지정`으로 등록할 `주문 테이블`이 2개 미만이면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithZeroOrOneOrderTable() {
        // Given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Collections.singletonList(orderTable1.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`은 비어있어야한다.")
    @Test
    void exceptionToCreateTableGroupWithNonemptyOrderTable() {
        // Given
        OrderTableResponse invalidOrderTable1 = tableService.create(new OrderTableRequest(3, false));
        OrderTableResponse invalidOrderTable2 = tableService.create(new OrderTableRequest(5, false));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(invalidOrderTable1.toOrderTable(), invalidOrderTable2.toOrderTable()));

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`할 `주문 테이블`이 `단체 지정`되어있으면 생성할 수 없다.")
    @Test
    void exceptionToCreateTableGroupWithRegisteredOrderTable() {
        // Given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse orderTable2 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable1.toOrderTable(), orderTable2.toOrderTable()));
        tableGroupService.create(request);

        // When & Then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`단체 지정`을 해제한다.")
    @Test
    void ungroupTableGroup() {
        // Given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse orderTable2 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable1.toOrderTable(), orderTable2.toOrderTable()));
        TableGroupResponse tableGroup = tableGroupService.create(request);

        // When
        tableGroupService.ungroup(tableGroup.getId());
        OrderTable foundOrderTable1 = tableService.findById(orderTable1.getId());
        OrderTable foundOrderTable2 = tableService.findById(orderTable2.getId());

        // Then
        assertAll(
                () -> assertThat(foundOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(foundOrderTable2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("`단체 지정`된 `주문 테이블`에서 `주문 상태`가 'COOKING' 이나 'MEAL' 이면 해제할 수 없다.")
    @Test
    void exceptionToUngroupTableGroup() {
        // Given
        OrderTableResponse orderTable1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse orderTable2 = tableService.create(new OrderTableRequest(0, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(orderTable1.toOrderTable(), orderTable2.toOrderTable()));
        TableGroupResponse tableGroup = tableGroupService.create(request);

        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable1.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // When & Then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
