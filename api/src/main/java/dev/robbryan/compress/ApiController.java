package dev.robbryan.compress;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.ProcessBuilder;
import java.lang.Process;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

import org.springframework.util.FileCopyUtils;

@Controller
@RestController
@RequestMapping("/api")
public class ApiController {
  private ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private ResourceLoader resourceLoader;

  @PostMapping("/upload")
  public ObjectNode uploadFile(@RequestParam("file") MultipartFile file) {
    ObjectNode map = mapper.createObjectNode();
    try {
      Resource resource = resourceLoader.getResource("classpath:static/");
      File newFile = new File(resource.getFile(), file.getOriginalFilename());
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
            map.put("msg", "File uploaded successfully");
            byte[] fileBytes = Files.readAllBytes(Paths.get(replaceFilePath));
            String fileBytesString = new String(encode(fileBytes), StandardCharsets.ISO_8859_1);
            System.out.println("FileBytes: " + fileBytesString);
            map.put("file", fileBytesString);
            map.put("name", "min_" + newFileName);
          } else {
            map.put("msg", "File upload failed");
          }
          isDone = true;
        } catch (IllegalThreadStateException e) {
          // Process is still running
          Thread.sleep(1000);
        }
      }
    } catch (Exception e) {
      map.put("msg", "File upload failed");
    }
    return map;
  }
}