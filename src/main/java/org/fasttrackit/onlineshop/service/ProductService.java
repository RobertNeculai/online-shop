package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistance.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.product.ProductResponse;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    // inversion of control ( IoC)
    private final ProductRepository productRepository;

    // dependancy injection  (from IoC container)
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(SaveProductRequest request) {
        LOGGER.info("Creating Product {}", request);
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        Product savedProduct = productRepository.save(product);
        return mapProductResponse(savedProduct);
    }

    public ProductResponse getProduct(long id) {
        LOGGER.info("Retrieving product {}", id);
        Product product = findProduct(id);

        return mapProductResponse(product);
        // Lambda expressions

        // optional usage explained
        //Optional<Product> productOptional = productRepository.findById(id);
        //if(productOptional.isPresent())
        //    return productOptional.get();
        //else
        //    throw new ResourceNotFoundException("Product "+id+" not found");
        //}
    }

    public Product findProduct(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found"));
    }

    private ProductResponse mapProductResponse(Product product) {
        ProductResponse productDto=new ProductResponse();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        productDto.setQuantity(product.getQuantity());
        return productDto;
    }

    public Page<ProductResponse> getProducts(GetProductsRequest request, Pageable pageable){
        LOGGER.info("Retreving products that match the filter {}",request);
        Page<Product> productsPage;
        if(request!=null)
        {
            if(request.getMinQuantity()!=null && request.getPartialName()!=null)
                productsPage= productRepository.findByNameContainingAndQuantityGreaterThanEqual(request.getPartialName(),request.getMinQuantity(),pageable);
            else if(request.getPartialName()!=null)
                productsPage= productRepository.findByNameContaining(request.getPartialName(),pageable);
            else
                productsPage=productRepository.findAll(pageable);
        }
        else {

            productsPage = productRepository.findAll(pageable);
        }
        List<ProductResponse> productDtos=new ArrayList<>();
        for(Product product:productsPage.getContent())
        {
            ProductResponse productDto = mapProductResponse(product);
            productDtos.add(productDto);
        }
        return new PageImpl<>(productDtos,pageable,productsPage.getTotalElements());
    }

    public ProductResponse updateProduct(long id,SaveProductRequest request){
        LOGGER.info("Updating product {}: {}",id,request);
        Product product=findProduct(id);
        BeanUtils.copyProperties(request,product);
        Product savedProduct = productRepository.save(product);
        return mapProductResponse(savedProduct);
    }
    public void deleteProduct(long id){
        LOGGER.info("Deleting product {}",id);
        productRepository.deleteById(id);
    }
}
