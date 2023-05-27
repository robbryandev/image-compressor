package dev.robbryan.compress;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.ProcessBuilder;
import java.lang.Process;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.util.FileCopyUtils;

@Controller
@RestController
@RequestMapping("/api")
public class ApiController {
  @Autowired
  private ResourceLoader resourceLoader;

  @PostMapping("/upload")
  public ResponseEntity<byte[]> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      File newFile = new File("/app/images/", file.getOriginalFilename());
      FileCopyUtils.copy(file.getBytes(), newFile);
      String newFilePath = newFile.getAbsolutePath();
      String newFileName = newFile.getName();
      String replaceFilePath = newFile.getAbsolutePath().replace(newFileName, "min_" + newFileName);
      String command = "ffmpeg -y -i " + newFilePath + " -compression_level 100 " + replaceFilePath;
      System.out.println(command);
      ProcessBuilder builder = new ProcessBuilder(command.split(" "));
      builder.redirectErrorStream(true);
      Process process = builder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      boolean isDone = false;
      while (!isDone) {
        try {
          int exitCode = process.exitValue();
          if (exitCode == 0) {
            byte[] fileBytes = Files.readAllBytes(Paths.get(replaceFilePath));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "filename.ext");
            isDone = true;
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
          }
        } catch (IllegalThreadStateException e) {
          // Process is still running
          Thread.sleep(1000);
        }
      }
    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}