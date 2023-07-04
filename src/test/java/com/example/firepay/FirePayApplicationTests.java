package com.example.firepay;

import com.example.firepay.repo.CustomerEntity;
import com.example.firepay.repo.CustomerRepository;
import com.example.firepay.repo.InvoiceEntity;
import com.example.firepay.repo.InvoiceRepository;
import com.example.firepay.service.lean.FirePayService;
import com.example.firepay.service.lean.LeanClient;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Disabled // local testing purposes only
@SpringBootTest
@AutoConfigureEmbeddedDatabase
class FirePayApplicationTests {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	FirePayService firePayService;

	@Autowired
	private LeanClient leanClient;

	@Value("${app.token}")
	private String appToken;

	@Autowired
	RestTemplate restTemplate;

	UUID customerId = UUID.fromString("473a7e53-29e8-4e22-9d80-37555d5d91a9");
	String endUser = "EndUser(id=3376af1b-3544-477d-a85f-cd9bfb6a0aaf, customerId=473a7e53-29e8-4e22-9d80-37555d5d91a9, reference=Aaron-Customer-Test)";


	@Test
	void repositoriesTest() {
		customerRepository.save(CustomerEntity.builder()
				.updatedAt(Instant.now())
				.customerId(UUID.randomUUID())
				.endUserId(UUID.randomUUID())
				.name("name")
				.address("address")
				.companyName("companyName")
				.taxId("taxId")
				.build());
		var result1 = customerRepository.findAll();
		System.out.println(result1);

		invoiceRepository.save(InvoiceEntity.builder()
				.updatedAt(Instant.now())
				.customerId(UUID.randomUUID())
				.endUserId(UUID.randomUUID())
				.paymentIntentId(UUID.randomUUID())
				.paymentStatus("paymentStatus")
				.currency("currency")
				.amount(new BigDecimal(100))
				.issueDate(LocalDate.now())
				.dueDate(LocalDate.now())
				.number("number")
				.build());
		var result2 = invoiceRepository.findAll();
		System.out.println(result2);
	}

	@Test
	void testCreateCustomer() {
		var r = leanClient.createCustomer("Aaron-Test");
		System.out.println(r);
	}

	@Test
	void testCreateEndUser() {
		var r = leanClient.createEndUser(customerId, "Aaron-Customer-Test");
		System.out.println(r);
	}

	@Test
	void testCreatePaymentIntent() {
		var r = leanClient.createPaymentIntent(new BigDecimal(10), "AED", customerId);
		System.out.println(r);
	}

	@Test
	void testGetBanks() {
		var r = getBanks();
		System.out.println(r);
	}

	private Object getBanks() {
		String urlPath = "/banks/v1";
		HttpHeaders defaultHeaders = new HttpHeaders();
		defaultHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		defaultHeaders.add("lean-app-token", appToken);
		HttpEntity<Void> requestEntity = new HttpEntity<>(defaultHeaders);
		return restTemplate.exchange(urlPath, HttpMethod.GET, requestEntity, List.class).getBody();
	}


}
