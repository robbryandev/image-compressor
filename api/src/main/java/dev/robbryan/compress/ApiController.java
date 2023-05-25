package dev.robbryan.compress;

import java.util.HashMap;
import java.util.Map;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
      // Copy file to api\target\classes\static\{FileName}
      FileCopyUtils.copy(file.getBytes(), newFile);
      map.put("msg", "File uploaded successfully");
    } catch (Exception e) {
      map.put("msg", "File upload failed");
    }
    return map;
  }
}