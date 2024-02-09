package com.app.motorola;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {
    private final Path root = Paths.get("uploads");

    /**
     * Handles file upload using multipart requests.
     *
     * @param file The file to be uploaded.
     * @return ResponseEntity with a success message if the file is uploaded successfully.
     * @throws RuntimeException If there is an error storing the file.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Files.createDirectories(Paths.get("uploads"));
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("File uploaded successfully");

        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    /**
     * Handles file download for a given filename.
     *
     * @param filename The name of the file to be downloaded.
     * @return ResponseEntity with the file as a resource for download.
     * @throws RuntimeException If there is an error creating the resource or setting headers.
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Path file = root.resolve(filename);
        Resource resource = null;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * Retrieves a list of uploaded files in the root directory.
     *
     * @return ResponseEntity with a list of uploaded files.
     * @throws RuntimeException If there is an error listing the files.
     */
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> files = Files.walk(this.root, 1)
                    .filter(path -> !path.equals(this.root))
                    .map(this.root::relativize)
                    .map(Path::toString)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(files);
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files. Error: " + e.getMessage());
        }
    }
}
