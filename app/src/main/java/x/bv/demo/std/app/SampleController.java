package x.bv.demo.std.app;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import x.bv.demo.std.ware.core.validation.validator.CommonString;

import java.util.UUID;

@Slf4j
@Controller
@CrossOrigin(originPatterns = "http://localhost*")
@RequestMapping("auth")
class SampleController {

	@ResponseBody
	@PostMapping("pwd")
	public String login(
		@RequestBody @Validated UserPwdAuth req
	) {
		log.info("当前请求登录的用户: {}", req);
		return UUID.randomUUID().toString();
	}

	public record UserPwdAuth(
		@CommonString
		@Length(min = 8, max = 16)
		String username,

		@CommonString
		@Length(min = 8, max = 16)
		String password
	) {}
}
