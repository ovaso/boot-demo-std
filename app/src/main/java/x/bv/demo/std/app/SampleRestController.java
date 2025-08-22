package x.bv.demo.std.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest")
class SampleRestController {

	@GetMapping("/hello_world")
	public String greeting() {

		return "Hello World!";
	}

	@GetMapping("/hello")
	public String greetingWithParam(@RequestParam("name") String name) {

		return "Hello " + name;
	}
}
