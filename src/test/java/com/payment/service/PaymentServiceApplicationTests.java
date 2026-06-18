package com.payment.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentServiceApplicationTests {

	@org.springframework.beans.factory.annotation.Autowired
	com.payment.service.repository.ClientRepository clientRepository;

	@Test
	void contextLoads() {
		System.out.println("=== CLIENTS WITH 'Chau' OR 'Tran' IN NAME ===");
		clientRepository.findAll().forEach(c -> {
			String sn = c.getShortName() != null ? c.getShortName().toLowerCase() : "";
			String fn = c.getFirstName() != null ? c.getFirstName().toLowerCase() : "";
			String ln = c.getLastName() != null ? c.getLastName().toLowerCase() : "";
			if (sn.contains("chau") || sn.contains("tran") || 
				fn.contains("chau") || fn.contains("tran") || 
				ln.contains("chau") || ln.contains("tran")) {
				System.out.println("ID: " + c.getId() + 
					" | ShortName: '" + c.getShortName() + "'" +
					" | FirstName: '" + c.getFirstName() + "'" +
					" | LastName: '" + c.getLastName() + "'" +
					" | ClientNumber: '" + c.getClientNumber() + "'" +
					" | AmndState: '" + c.getAmndState() + "'");
			}
		});
	}
}
