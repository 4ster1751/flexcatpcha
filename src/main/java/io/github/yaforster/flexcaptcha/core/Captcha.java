package io.github.yaforster.flexcaptcha.core;

import java.util.Base64;

/**
 * Object representing a captcha output consisting of the image data to display and transport the
 * actual captcha, and a token representing the hashed and salted solution
 *
 * @param token   String containing the hash of the original solution salted with a specified object
 * @param imgData String representation of the image containing the visual captcha
 * @author Yannick Forster
 */
public record Captcha(String token, byte[] imgData) {

    /**
     * Returns the image data byte array as base64 string.
     *
     * @return String of the base64-encoded imgData byte-array
     */
    public String getImgDataAsBase64() {
        return Base64.getEncoder().encodeToString(imgData);
    }
}
