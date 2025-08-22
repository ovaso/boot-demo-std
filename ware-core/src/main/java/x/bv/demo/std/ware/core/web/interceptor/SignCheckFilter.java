package x.bv.demo.std.ware.core.web.interceptor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SignCheckFilter extends OncePerRequestFilter {

    private static final byte[] SECRET_KEY = "my-secret-key".getBytes(StandardCharsets.UTF_8);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        AdaptiveCachedBodyRequest wrappedRequest = new AdaptiveCachedBodyRequest(request);

        try {
            byte[] signature;
            if (request.getContentLength() <= AdaptiveCachedBodyRequest.MAX_IN_MEMORY_SIZE) {
                // 小请求体，使用缓存内容计算 HMAC
                signature = computeHmac(wrappedRequest.getCachedBody(), SECRET_KEY);
            } else {
                // 大请求体，流式计算 HMAC
                signature = AdaptiveCachedBodyRequest.computeHmacSha256(request.getInputStream(), SECRET_KEY);
            }

            // TODO: 校验签名
            boolean valid = verifySignature(signature, request.getHeader("X-Signature"));
            if (!valid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid signature");
                return;
            }

            // 校验通过，继续请求链
            filterChain.doFilter(wrappedRequest, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error verifying signature");
        }
    }

    private boolean verifySignature(byte[] computed, String headerSignature) {
        // TODO: 你可以将 headerSignature 转 hex / base64 后比对
        return true;
    }

    private byte[] computeHmac(byte[] body, byte[] key) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        mac.init(new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256"));
        return mac.doFinal(body);
    }
}