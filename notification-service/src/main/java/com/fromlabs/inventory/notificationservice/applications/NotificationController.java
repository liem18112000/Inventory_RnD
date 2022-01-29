package com.fromlabs.inventory.notificationservice.applications;

import com.fromlabs.inventory.notificationservice.config.ApiV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SupplierController is REST controller which responsible for
 * all C.R.U.D operations of ...
 * @see <a href="https://spring.io/guides/tutorials/rest/">REST Controller example : Building REST services with Spring</a>
 */
@Slf4j
@RestController
@RequestMapping(value = "${application.base-url}/" + ApiV1.URI_API, produces = ApiV1.MIME_API)
public class NotificationController {

    //<editor-fold desc="SETUP">

    public static final String SERVICE_PATH = "/notification/";

    public NotificationController() {
        this.trackControllerDependencyInjectionInformation();
    }

    private void trackControllerDependencyInjectionInformation(

    ) {
        log.info("Application controller : {}", this.getClass().getName());
    }

    //</editor-fold>
}
