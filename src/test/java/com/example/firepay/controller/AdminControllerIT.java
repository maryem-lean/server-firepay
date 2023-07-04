package com.example.firepay.controller;

import com.example.firepay.controller.dto.CustomerRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminControllerIT {

    @Autowired
    private AdminController adminController;

    @Test
    void testCreateCustomer() {
        Assertions.assertThat(adminController.fetchCustomers()).isEmpty();
        adminController.createCustomer(CustomerRequest.builder().name("potato").companyName("company").address("address").taxId("taxid").build());
        Assertions.assertThat(adminController.fetchCustomers()).hasSize(1);
    }
}
