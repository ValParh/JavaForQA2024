package ru.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.shop.exception.BadProductReturnCountException;
import ru.shop.exception.EntityNotFoundException;
import ru.shop.model.Order;
import ru.shop.model.ProductReturn;
import ru.shop.repository.ProductReturnRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProductReturnServiceTest {

    private final ProductReturnRepository repository = Mockito.mock(ProductReturnRepository.class);
    private final ProductReturnService productReturnService = new ProductReturnService(repository);

    @Test
    public void shouldAddProductReturn() {
        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 10, 100);

        productReturnService.add(order, 5);

        Mockito.verify(repository).save(Mockito.any(ProductReturn.class));
    }

    @Test
    public void shouldThrowBadProductReturnCountException() {

        var order = new Order(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 10, 100);


        Assertions.assertThrows(
                BadProductReturnCountException.class,
                () -> productReturnService.add(order, 15)
        );
    }

    @Test
    public void shouldFindAllProductReturns() {
        // given
        var productReturnId = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(
                List.of(
                        new ProductReturn(productReturnId, UUID.randomUUID(), LocalDate.now(), 5),
                        new ProductReturn(UUID.randomUUID(), UUID.randomUUID(), LocalDate.now(), 3)
                )
        );


        List<ProductReturn> all = productReturnService.findAll();


        Mockito.verify(repository).findAll();
        Assertions.assertEquals(2, all.size());
    }

    @Test
    public void shouldFindProductReturnById() {

        var productReturnId = UUID.randomUUID();
        Mockito.when(repository.findById(productReturnId)).thenReturn(
                Optional.of(new ProductReturn(productReturnId, UUID.randomUUID(), LocalDate.now(), 5))
        );


        ProductReturn productReturn = productReturnService.findById(productReturnId);


        Mockito.verify(repository).findById(productReturnId);
        Assertions.assertEquals(productReturnId, productReturn.getId());
    }

    @Test
    public void shouldThrowWhenProductReturnNotFound() {

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> productReturnService.findById(UUID.randomUUID())
        );
    }
}