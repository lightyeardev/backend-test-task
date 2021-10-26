package com.golightyear.backend


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

class BackendTestTaskApplicationIntSpec extends AbstractIntegrationSpec {

    @Autowired
    MockMvc mvc

    def "should start up application"() {
        expect: "All beans are created"
    }

}
