package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.service.ProductService;
import org.fasttrackit.onlineshop.steps.ProductTestSteps;
import org.fasttrackit.onlineshop.transfer.product.ProductResponse;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
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
    @Autowired
    private ProductTestSteps productTestSteps;
    @Test
    void createProduct_whenValidRequest_thenProductIsCreated()
    {
        productTestSteps.createProduct();
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
        ProductResponse product=productTestSteps.createProduct();
        ProductResponse response = productService.getProduct(product.getId());
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
    @Test
    void updateProduct_whenValidRequest_thenReturnUpdatedProduct()
    {
        ProductResponse product=productTestSteps.createProduct();
        SaveProductRequest request=new SaveProductRequest();
        request.setName(product.getName()+ "updated");
        request.setDescription(product.getDescription()+ "updated");
        request.setPrice(product.getPrice()+ 10);
        request.setQuantity(product.getQuantity()+ 2);
        ProductResponse updateProduct = productService.updateProduct(product.getId(), request);
        assertThat(updateProduct,notNullValue());
        assertThat(updateProduct.getId(),is(product.getId()));
        assertThat(updateProduct.getName(),is(request.getName()));
        assertThat(updateProduct.getDescription(),is(request.getDescription()));
        assertThat(updateProduct.getPrice(),is(request.getPrice()));
        assertThat(updateProduct.getQuantity(),is(request.getQuantity()));
    }
    @Test
    void deleteProduct_whenExistingProduct_thenProductDoesNotExistAnymore()
    {
        ProductResponse product=productTestSteps.createProduct();
        productService.deleteProduct(product.getId());
        Assertions.assertThrows(ResourceNotFoundException.class,() -> productService.getProduct(product.getId()));

    }

}
