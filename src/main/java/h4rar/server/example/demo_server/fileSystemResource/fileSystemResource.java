package h4rar.server.example.demo_server.fileSystemResource;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class fileSystemResource {
//    	private String PATH = "C:\\Work\\SDF\\ServerFiles\\ServerFile.mp4";
    private String PATH = "C:\\Work\\SDF\\ServerFiles\\debian-live.iso";
//    private String PATH = "C:\\Work\\SDF\\ServerFiles\\test.txt";

    @GetMapping(value = "/fsr")
    public ResponseEntity<FileSystemResource> fsr() throws IOException {
        String filePathString = PATH;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/octet-stream");
        FileSystemResource fileSystemResource = new FileSystemResource(filePathString);

        System.out.println(fileSystemResource.contentLength());

        return new ResponseEntity<>(fileSystemResource, responseHeaders, HttpStatus.OK);
    }
}
