package x.bv.demo.std.ware.core.web.interceptor;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Arrays;

public class AdaptiveCachedBodyRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;
    private final boolean inMemory;

    protected static final int MAX_IN_MEMORY_SIZE = 1024 * 1024; // 1MB

    /**
     * 构造方法：根据请求体大小选择缓存策略
     */
    public AdaptiveCachedBodyRequest(HttpServletRequest request) throws IOException {
        super(request);
        int contentLength = request.getContentLength();

        if (contentLength >= 0 && contentLength <= MAX_IN_MEMORY_SIZE) {
            // 小请求体：缓存到内存
            cachedBody = request.getInputStream().readAllBytes();
            inMemory = true;
        } else {
            // 大请求体或未知大小：不缓存到内存，先为空，使用流式处理
            cachedBody = null;
            inMemory = false;
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inMemory) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                }
            };
        } else {
            // 大请求体，直接返回原始请求流，流式处理签名
            return super.getInputStream();
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    /**
     * 获取缓存请求体（小请求体才可用）
     */
    public byte[] getCachedBody() {
        if (!inMemory) throw new IllegalStateException("Request too large to cache in memory");
        return Arrays.copyOf(cachedBody, cachedBody.length);
    }

    /**
     * 流式计算 HMAC-SHA256 签名（适合大请求体）
     */
    public static byte[] computeHmacSha256(InputStream input, byte[] key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) != -1) {
            mac.update(buffer, 0, read);
        }
        return mac.doFinal();
    }
}
