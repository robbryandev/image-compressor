package dev.robbryan.compress;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RestController
@RequestMapping("/api")
public class ApiController {
  public static ObjectMapper mapper = new ObjectMapper();

  @GetMapping(path = "/test")
  public ObjectNode test() {
    ObjectNode map = mapper.createObjectNode();
    map.put("msg", "hello world");
    return map;
  }
}