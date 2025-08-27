package x.bv.demo.std.app;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rest")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
class SampleRestController {

	@GetMapping("/hello_world")
	public String greeting() {

		int n = 1 / 0;
		return "Hello World!";
	}

	@PostMapping("/hello_world")
	public Account greeting(@RequestBody @Validated Account account) {

		return account;
	}

	@GetMapping("/hello/{path_v}")
	public ResponseEntity<?> greetingWithParam(
		@RequestParam("name") @Length(min = 1, max = 2) @Pattern(regexp = "\\d*") String name,
		@PathVariable("path_v") @Max(10) Integer pathVariable,
		@RequestHeader("X-App-Key") @Length(min = 1, max = 5) String appKey
	) {

		Account account = new Account();
		account.setAccount(name);
		account.setSerialNumber(pathVariable);
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
}
