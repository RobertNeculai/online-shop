package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.transfer.SaveProductRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
public class ProductServiceIntegrationTest {
    // field-injection ( injecting dependencies from IoC annotating the field itself )
    // field = instance variables
    @Autowired
    private ProductService productService;
    @Test
    void createProduct_whenValidRequest_thenProductIsCreated()
    {
        createProduct();
    }
    @Test
    void createProduct_whenMissingName_thenExceptionIsThrown(){
        SaveProductRequest request= new SaveProductRequest();
        request.setQuantity(1);
        request.setPrice(100.0);

        try {
            productService.createProduct(request);
        } catch (Exception e) {
            assertThat(e,notNullValue());
            assertThat("Unexpected excetion type",e instanceof TransactionSystemException);
        }

    }
    @Test
    void getProduct_whenExistingProduct_thenProductIsReturned()
    {
        Product product=createProduct();
        Product response = productService.getProduct(product.getId());
        assertThat(response,notNullValue());
        assertThat(product.getName(),is(response.getName()));
        assertThat(product.getPrice(),is(response.getPrice()));
        assertThat(product.getQuantity(),is(response.getQuantity()));
        assertThat(product.getImageUrl(),is(response.getImageUrl()));
        assertThat(product.getDescription(),is(response.getDescription()));

    }
    @Test
    void getProduct_whenNonExistingProduct_thenTrowResourceNotFoundException(){
        Assertions.assertThrows(ResourceNotFoundException.class,() -> productService.getProduct(999999999));

    }
    private Product createProduct() {
        SaveProductRequest request= new SaveProductRequest();
        request.setName("Phone");
        request.setQuantity(100);
        request.setPrice(300.5);
        Product product = productService.createProduct(request);
        assertThat(product,notNullValue());
        assertThat(product.getId(),greaterThan(0L));
        assertThat(product.getName(),is(request.getName()));
        assertThat(product.getPrice(),is(request.getPrice()));
        assertThat(product.getQuantity(),is(request.getQuantity()));
        return product;
    }
}
