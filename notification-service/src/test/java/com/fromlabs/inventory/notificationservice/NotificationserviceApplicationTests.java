package com.fromlabs.inventory.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"dev","liem-local"})
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = NotificationserviceApplication.class)
class NotificationserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
