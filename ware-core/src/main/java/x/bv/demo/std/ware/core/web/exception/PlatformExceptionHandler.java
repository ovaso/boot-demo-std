package x.bv.demo.std.ware.core.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import x.bv.demo.std.ware.common.RestResult;
import x.bv.demo.std.ware.exception.HttpRestException;
import x.bv.demo.std.ware.exception.PlatformException;

import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class PlatformExceptionHandler extends ResponseEntityExceptionHandler {
	private static final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	private static final Logger log = LoggerFactory.getLogger(PlatformExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(
		@NonNull HandlerMethodValidationException ex,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {

		ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
		SoftReference<Map<String, String>> violations = new SoftReference<>(new HashMap<>());
		Map<String, String> violationsMap = violations.get();
		problemDetail.setProperty("violations", violationsMap);
		// 可在下方对 problemDetail 进行定制输出
		for (ParameterValidationResult valueResult : ex.getValueResults()) {
			MethodParameter methodParameter = valueResult.getMethodParameter();
			String parameterName = resolveMethodParameterName(methodParameter);
			List<MessageSourceResolvable> resolvableErrors = valueResult.getResolvableErrors();
			String message = resolvableErrors.stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
			if (Objects.nonNull(violationsMap) && Objects.nonNull(parameterName)) {
				violationsMap.put(parameterName, message);
			}
		}
		return createResponseEntity(problemDetail, headers, status, request);
	}


	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		@NonNull MethodArgumentNotValidException ex,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode status,
		@NonNull WebRequest request) {

		ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
		BindingResult bindingResult = ex.getBindingResult();
		SoftReference<Map<String, String>> violations = new SoftReference<>(new HashMap<>());
		Map<String, String> violationsMap = violations.get();
		problemDetail.setProperty("violations", violationsMap);
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String fieldName = fieldError.getField(); // 字段名
			String defaultMessage = fieldError.getDefaultMessage(); // 默认消息
			violationsMap.put(fieldName, defaultMessage);
		}
		return createResponseEntity(problemDetail, headers, status, request);
	}

	/**
	 * 拦截平台异常
	 *
	 * @param ex      异常
	 * @param headers 请求头
	 * @param request 请求
	 * @return 响应
	 */
	@ExceptionHandler(PlatformException.class)
	public ResponseEntity<Object> handlePlatformException(PlatformException ex, HttpHeaders headers, WebRequest request) {

		final ProblemDetail problemDetail;
		if (ex instanceof HttpRestException restException) {
			// 正常(但包含 server internal), 走 http 消息处理
			HttpStatus httpStatus = HttpStatus.valueOf(restException.getHttpStatusCode());
			problemDetail = createProblemDetail(
				ex,
				httpStatus,
				"",  // 此处在异常类中进行扩展并填写, 自动匹配国际化
				"",
				null,
				request
			);
			return handleExceptionInternal(ex, problemDetail, headers, httpStatus, request);
		} else {
			return handleInternalException(ex, request);
		}
	}

	/**
	 * 拦截其他未知异常
	 *
	 * @param e       异常
	 * @param request 请求
	 * @return 响应体
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleInternalException(Exception e, WebRequest request) {

		log.error(e.getMessage(), e);
		Exception exception = new Exception(e);
		ProblemDetail problemDetail = createProblemDetail(exception, INTERNAL_SERVER_ERROR, null, null, null, request);
		return createResponseEntity(problemDetail, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
	}

	/**
	 * 创建 ResponseEntity
	 *
	 * @param body       the body to use for the response
	 * @param headers    the headers to use for the response
	 * @param statusCode the status code to use for the response
	 * @param request    the current request
	 * @return
	 */
	@NonNull
	@Override
	protected ResponseEntity<Object> createResponseEntity(
		Object body,
		@NonNull HttpHeaders headers,
		@NonNull HttpStatusCode statusCode,
		@NonNull WebRequest request) {

		// 进行内容转换
		final String acceptHeader = Optional.ofNullable(request.getHeader(HttpHeaders.ACCEPT))
			.orElse(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
		List<MediaType> mediaTypes = MediaType.parseMediaTypes(acceptHeader);
		// 按质量参数排序
		MimeTypeUtils.sortBySpecificity(mediaTypes);
		// 取第一个
		if (mediaTypes.getFirst().removeQualityValue().equals(MediaType.APPLICATION_JSON)) {
			// 使用 RestResult
			if (body == null) {
				final HttpStatus resolve = Optional.ofNullable(HttpStatus.resolve(statusCode.value()))
					.orElse(HttpStatus.INTERNAL_SERVER_ERROR);
				body = new RestResult<>(resolve.getReasonPhrase(), resolve.value());
			}
			if (body instanceof ProblemDetail problemDetail) {
				body = new RestResult<>(problemDetail.getDetail(), problemDetail.getStatus());
			}
		}
		return new ResponseEntity<>(body, headers, statusCode);
	}

	/**
	 * 假如是控制器内HTTP相关参数, 则提取对HTTP参数名(Header, param, path.etc) <br>
	 * 否则返回实际参数名 <br>
	 * 如果编译时未添加 <code>-parameter</code> 选项 <br>
	 * 或者 maven 编译插件未开启 <code>parameter</code> 选项, 则返回null
	 *
	 * @param methodParameter 方法参数
	 * @return 参数名
	 */
	private String resolveMethodParameterName(MethodParameter methodParameter) {

		final Annotation[] parameterAnnotations = methodParameter.getParameterAnnotations();
		for (Annotation parameterAnnotation : parameterAnnotations) {
			if (parameterAnnotation instanceof RequestParam requestParam) {
				return requestParam.name();
			}
			if (parameterAnnotation instanceof PathVariable pathVariable) {
				return pathVariable.name();
			}
			if (parameterAnnotation instanceof CookieValue cookieValue) {
				return cookieValue.name();
			}
			if (parameterAnnotation instanceof RequestHeader requestHeader) {
				return requestHeader.name();
			}
		}
		return methodParameter.getParameterName();
	}
}
