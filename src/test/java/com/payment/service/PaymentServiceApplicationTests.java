package com.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceApplicationTests {

	@org.springframework.beans.factory.annotation.Autowired
	com.payment.service.repository.AcntContractRepository contractRepository;

	@Test
	void contextLoads() {
		System.out.println("=== CONTRACT HIERARCHY DETAILS ===");
		contractRepository.findAll().forEach(c -> {
			if (c.getClientId() != null && c.getClientId() == 94530L) {
				System.out.println("Contract ID: " + c.getId() + 
					" | Number: " + c.getContractNumber() + 
					" | ParentContractID (acntContractId): " + c.getAcntContractId() + 
					" | Oid (acntContractOid): " + c.getAcntContractOid() + 
					" | LiabContract: " + c.getLiabContract() + 
					" | BillingContract: " + c.getBillingContract() + 
					" | Level: " + c.getContractLevel() + 
					" | Product: " + c.getProduct() + 
					" | Name: " + c.getContractName());
			}
		});
	}
}
