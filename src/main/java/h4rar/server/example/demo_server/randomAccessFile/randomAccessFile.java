package h4rar.server.example.demo_server.randomAccessFile;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;

@Controller
public class randomAccessFile {

    //    	private String PATH = "C:\\Work\\SDF\\ServerFiles\\ServerFile.mp4";
    private String PATH = "C:\\Work\\SDF\\ServerFiles\\debian-live.iso";
    //    private String PATH = "C:\\Work\\SDF\\ServerFiles\\test.txt";

    @GetMapping(value = "/srbr")
    @ResponseBody
    public ResponseEntity<InputStreamResource> srbr(
            @RequestHeader(value = "Range", required = false) String rangeHeader
    ) {
        FileChannel channel = null;
        try {
            StreamingResponseBody responseStream;
            String filePathString = PATH;
            Path filePath = Paths.get(filePathString);
            Long fileSize = Files.size(filePath);
            final HttpHeaders responseHeaders = new HttpHeaders();

            if (rangeHeader == null) {
                responseHeaders.add("Content-Type", "application/octet-stream");
                responseHeaders.add("Content-Length", fileSize.toString());
                RandomAccessFile file = new RandomAccessFile(filePathString, "r");
                channel = file.getChannel();
                InputStream is = Channels.newInputStream(channel);
                return new ResponseEntity<>(new InputStreamResource(is), responseHeaders, HttpStatus.OK);
            }
            String[] ranges = rangeHeader.split("-");
            Long rangeStart = Long.parseLong(ranges[0].substring(6));
            String contentLength = String.valueOf((fileSize - rangeStart) + 1);
            responseHeaders.add("Content-Type", "application/octet-stream");
            responseHeaders.add("Content-Length", contentLength);
            responseHeaders.add("Accept-Ranges", "bytes");
            responseHeaders.add("Content-Range", "bytes" + " " + rangeStart + "-" + fileSize + "/" + fileSize);
            RandomAccessFile file = new RandomAccessFile(filePathString, "r");
            file.seek(rangeStart);
            channel = file.getChannel();
            InputStream is = Channels.newInputStream(channel);
            return new ResponseEntity<>(new InputStreamResource(is), responseHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping(value = "/srb")
//    @ResponseBody
//    public ResponseEntity<StreamingResponseBody> stream(
//            @RequestHeader(value = "Range", required = false) String rangeHeader
//    ) {
//        try {
//            StreamingResponseBody responseStream;
//            String filePathString = PATH;
//            Path filePath = Paths.get(filePathString);
//            Long fileSize = Files.size(filePath);
//            byte[] buffer = new byte[1024];
//            final HttpHeaders responseHeaders = new HttpHeaders();
//
//            if (rangeHeader == null) {
//                responseHeaders.add("Content-Type", "application/octet-stream");
//                responseHeaders.add("Content-Length", fileSize.toString());
//                responseStream = os -> {
//                    RandomAccessFile file = new RandomAccessFile(filePathString, "r");
//                    long pos = 0;
//                    file.seek(pos);
//                    while (pos < fileSize - 1) {
//                        file.read(buffer);
//                        os.write(buffer);
//                        pos += buffer.length;
//                    }
//                    os.flush();
//                };
//                return new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.OK);
//            }
//
//            String[] ranges = rangeHeader.split("-");
//            Long rangeStart = Long.parseLong(ranges[0].substring(6));
//            Long rangeEnd;
//            if (ranges.length > 1) {
//                rangeEnd = Long.parseLong(ranges[1]);
//            } else {
//                rangeEnd = fileSize - 1;
//            }
//            if (fileSize < rangeEnd) {
//                rangeEnd = fileSize - 1;
//            }
//
//            String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
//            responseHeaders.add("Content-Type", "application/octet-stream");
//            responseHeaders.add("Content-Length", contentLength);
//            responseHeaders.add("Accept-Ranges", "bytes");
//            responseHeaders.add("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
//            final Long _rangeEnd = rangeEnd;
//            responseStream = os -> {
//                RandomAccessFile file = new RandomAccessFile(filePathString, "r");
//
//                long pos = rangeStart;
//                file.seek(pos);
//                while (pos < _rangeEnd) {
//                    file.read(buffer);
//                    os.write(buffer);
//                    pos += buffer.length;
//                }
//                os.flush();
//            };
//            return new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
//        } catch (FileNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
