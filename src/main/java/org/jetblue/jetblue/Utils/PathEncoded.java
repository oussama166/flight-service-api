package org.jetblue.jetblue.Utils;

import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.*;
import static java.util.Base64.*;

@Component
public class PathEncoded {
    public static String EncodeFilePath(String path) {
        return getUrlEncoder().encodeToString(path.getBytes(UTF_8));
    }

    public static String DecodeFilePath(String encodedPath) {
        return new String(getUrlDecoder().decode(encodedPath), UTF_8);
    }
}
