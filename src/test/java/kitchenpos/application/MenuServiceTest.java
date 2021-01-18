package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("메뉴 서비스에 관련한 기능")
@SpringBootTest
class MenuServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    private MenuProductRequest menuProductRequest1;
    private MenuProductRequest menuProductRequest2;

    @BeforeEach
    void beforeEach() {
        ProductRequest productRequest1 = new ProductRequest("짬뽕", BigDecimal.valueOf(8_000));
        ProductResponse 짬뽕 = productService.create(productRequest1);
        ProductRequest productRequest2 = new ProductRequest("짜장면", BigDecimal.valueOf(6_000));
        ProductResponse 짜장면 = productService.create(productRequest2);
        menuProductRequest1 = new MenuProductRequest(짬뽕.getId(), 1L);
        menuProductRequest2 = new MenuProductRequest(짜장면.getId(), 1L);
    }

    @DisplayName("`메뉴`를 생성한다.")
    @Test
    void createMenu() {
        // Given
        MenuRequest menuRequest = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(14_000), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When
        Menu 짬뽕_짜장면 = menuService.create(menuRequest);

        // Then
        assertAll(
                () -> assertThat(짬뽕_짜장면.getId()).isNotNull(),
                () -> assertThat(짬뽕_짜장면.getName()).isEqualTo(menuRequest.getName()),
                () -> assertThat(짬뽕_짜장면.getPrice().intValue()).isEqualTo(menuRequest.getPrice().intValue()),
                () -> assertThat(짬뽕_짜장면.getMenuGroupId()).isEqualTo(menuRequest.getMenuGroupId()),
                () -> assertThat(짬뽕_짜장면.getMenuProducts()).extracting(MenuProduct::getProductId)
                        .containsAnyElementsOf(Collections.singletonList(menuProductRequest1.getProductId())),
                () -> assertThat(짬뽕_짜장면.getMenuProducts()).extracting(MenuProduct::getQuantity)
                        .containsAnyElementsOf(Collections.singletonList(menuProductRequest1.getQuantity()))
        );
    }

    @DisplayName("가격은 필수이고, 0원 이상이 아니면 `메뉴`를 생성할 수 없다.")
    @Test
    void exceptionToCreateMenuWithInvalidPrice() {
        // Given
        MenuRequest invalidMenuRequest1 = new MenuRequest("짬뽕_짜장면", null, 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenuRequest1));

        // Given
        MenuRequest invalidMenuRequest2 = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(-1), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(invalidMenuRequest2));
    }

    @DisplayName("`메뉴`의 가격이 `메뉴 상품`의 가격의 합보다 크면 `메뉴`를 생성할 수 없다.")
    @Test
    void exceptionToCreateMenuWithInvalidPriceOverSum() {
        // Given
        MenuRequest menuRequest = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(15_000), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuService.create(menuRequest));
    }

    @DisplayName("모든 `메뉴` 목록을 조회한다.")
    @Test
    void findAllMenus() {
        // Given
        MenuRequest menuRequest = new MenuRequest("짬뽕_짜장면", BigDecimal.valueOf(14_000), 1L,
                Arrays.asList(menuProductRequest1, menuProductRequest2));
        Menu 짬뽕_짜장면 = menuService.create(menuRequest);

        // When
        List<MenuResponse> actual = menuService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(MenuResponse::getId).containsAnyElementsOf(Collections.singletonList(짬뽕_짜장면.getId())),
                () -> assertThat(actual).extracting(MenuResponse::getName).containsAnyElementsOf(Collections.singletonList(짬뽕_짜장면.getName())),
                () -> assertThat(actual).extracting(MenuResponse::getPrice).containsAnyElementsOf(Collections.singletonList(짬뽕_짜장면.getPrice())),
                () -> assertThat(actual).extracting(MenuResponse::getMenuGroupId).containsAnyElementsOf(Collections.singletonList(짬뽕_짜장면.getMenuGroupId())),
                () -> assertThat(actual.stream().filter(mr -> mr.getId().equals(짬뽕_짜장면.getId())).findAny().get().getMenuProducts())
                        .isEqualTo(짬뽕_짜장면.getMenuProducts().stream().map(MenuProductResponse::from).collect(Collectors.toList()))
        );
    }
}
