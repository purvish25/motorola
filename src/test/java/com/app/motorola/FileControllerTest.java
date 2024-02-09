package com.app.motorola;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebMvcTest(FileController.class)
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String UPLOAD_DIR = "uploads/";

    @Test
    void testFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the file was uploaded successfully
        Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        assert Files.exists(filePath);
    }

    @Test
    void testFileDownload() throws Exception {
        // Upload a file for testing
        MockMultipartFile file = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Download the uploaded file
        mockMvc.perform(MockMvcRequestBuilders.get("/download/{fileName}", "test1.txt"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=\"test1.txt\""))
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    void testListUploadedFiles() throws Exception {
        // Upload a few files for testing
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "Content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", MediaType.TEXT_PLAIN_VALUE, "Content 2".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file2))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Retrieve the list of uploaded files
        mockMvc.perform(MockMvcRequestBuilders.get("/files"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[\"test1.txt\", \"test2.txt\"]"));
    }
}
