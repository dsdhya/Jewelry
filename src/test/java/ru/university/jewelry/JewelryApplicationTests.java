package ru.university.jewelry;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.Cookie;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1")
class JewelryApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void fullAuthPhotoAndCsvImportFlow() throws Exception {
        // 1. Login as admin
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(true))
                .andReturn();

        Cookie accessCookie = loginResult.getResponse().getCookie("access_token");
        Cookie refreshCookie = loginResult.getResponse().getCookie("refresh_token");

        // 2. Get /api/auth/info
        mockMvc.perform(get("/api/auth/info").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));

        // 3. Upload photo with PNG magic bytes
        byte[] pngHeader = new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00 };
        MockMultipartFile photoFile = new MockMultipartFile("file", "ring.png", "image/png", pngHeader);
        mockMvc.perform(multipart("/api/jewelry/1/photo").file(photoFile).cookie(accessCookie))
                .andExpect(status().isOk());

        // 4. Import CSV
        String csv = "article,name,category,metal,hallmark,weightGrams,price,stockQuantity\n"
                + "T-585-9991,Тест кольцо,Кольца,GOLD,585,2.50,19900,4\n";
        MockMultipartFile csvFile = new MockMultipartFile("file", "catalog.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart("/api/jewelry/import").file(csvFile).cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imported").value(1))
                .andExpect(jsonPath("$.failed").value(0));

        // 5. Refresh token
        mockMvc.perform(post("/api/auth/refresh").cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(true));

        // 6. Logout
        mockMvc.perform(post("/api/auth/logout").cookie(accessCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLogged").value(false));
    }
}
