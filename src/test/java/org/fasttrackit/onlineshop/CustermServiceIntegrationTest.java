package org.fasttrackit.onlineshop;

import org.fasttrackit.onlineshop.domain.Customer;
import org.fasttrackit.onlineshop.service.CustomerService;
import org.fasttrackit.onlineshop.transfer.customer.SaveCustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class CustermServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Test
    void createCustomer(){
        SaveCustomerRequest request=new SaveCustomerRequest();
        request.setFirstName("First Name");
        request.setLastName("Last Name");
        Customer customer = customerService.createCustomer(request);
        assertThat(customer,notNullValue());
        assertThat(customer.getId(),greaterThan(0L));
        assertThat(customer.getFirstName(),is(request.getFirstName()));
        assertThat(customer.getLastName(),is(request.getLastName()));
    }
}
