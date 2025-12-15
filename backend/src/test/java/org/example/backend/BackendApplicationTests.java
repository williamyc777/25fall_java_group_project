package org.example.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.domain.AbstractUser;
import org.example.backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:file:./h2db/testdb;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.main.allow-bean-definition-overriding=true",
        "websocket.enabled=false"
})
class BackendApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private org.example.backend.service.AbstractUserService abstractUserService;

    private final ObjectMapper mapper = new ObjectMapper();

    @TestConfiguration
    static class WebSocketTestConfig {
        // Provide a no-op ServerEndpointExporter to satisfy context without requiring real container
        @Bean
        public ServerEndpointExporter serverEndpointExporter() {
            return new ServerEndpointExporter() {
                @Override
                public void afterPropertiesSet() {
                    // no-op for tests
                }

                @Override
                public void afterSingletonsInstantiated() {
                    // no-op to avoid real websocket registration in tests
                }
            };
        }
    }

    /** Smoke test: main page renders */
    @Test
    void mainPage_shouldReturn200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/main"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /** Admin login should return a valid token */
    @Test
    void adminLogin_shouldReturnToken() throws Exception {
        // ensure admin exists with known password
        AbstractUser existing = abstractUserService.findUserByUsername("admin");
        if (existing == null) {
            org.example.backend.domain.Admin admin = new org.example.backend.domain.Admin();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setName("admin");
            abstractUserService.saveUser(admin);
        } else {
            existing.setPassword("admin");
            abstractUserService.saveUser(existing);
        }

        String resp = mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .param("username", "admin")
                                .param("password", "admin"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = mapper.readTree(resp);
        String token = json.has("data") ? json.get("data").asText() : "";
        AbstractUser user = JwtUtil.verifyToken(token);
        assert user != null && user.getUsername().equals("admin");
    }

    /** Sign up a new user and then login with hashed password verification */
    @Test
    void signUpAndLogin_shouldWork() throws Exception {
        String uname = "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String pwd = "p" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // sign up
        String signUpResp = mockMvc.perform(
                        MockMvcRequestBuilders.post("/signUp")
                                .param("username", uname)
                                .param("password", pwd))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JsonNode signUpJson = mapper.readTree(signUpResp);
        assert signUpJson.has("data") && signUpJson.get("data").asBoolean();

        // login
        String loginResp = mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .param("username", uname)
                                .param("password", pwd))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode loginJson = mapper.readTree(loginResp);
        String token = loginJson.has("data") ? loginJson.get("data").asText() : "";
        AbstractUser user = JwtUtil.verifyToken(token);
        assert user != null && uname.equals(user.getUsername());
    }
}

