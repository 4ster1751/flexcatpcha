package io.github.yaforster.flexcaptcha.impl.rendering;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CaptchaRendererTest {

    @Test
    final void renderAndConvertToBytes_with_default_renderer() {
        CaptchaRenderer renderer = CaptchaRenderer.getDefaultCaptchaRenderer();
        assertDoesNotThrow(() -> renderer.renderAndConvertToBytes("abc123"));
    }

    @Test
    final void renderAndConvertToBytes_with_undefined_renderer() {
        CaptchaRenderer renderer = CaptchaRenderer.builder().build();
        assertDoesNotThrow(() -> renderer.renderAndConvertToBytes("abc123"));
    }

    @Test
    final void renderAndConvertToBytes_test_height() throws IOException {
        int targetHeight = 100;
        CaptchaRenderer renderer = CaptchaRenderer.builder().pictureHeight(targetHeight).build();
        byte[] imageAsBytes = renderer.renderAndConvertToBytes("abc123");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageAsBytes)) {
            BufferedImage image = ImageIO.read(bis);
            assertEquals(targetHeight, image.getHeight());
        }
    }

    @Test
    final void renderAndConvertToBytes_test_width() throws IOException {
        int targetWidth = 300;
        CaptchaRenderer renderer = CaptchaRenderer.builder().pictureWidth(targetWidth).build();
        byte[] imageAsBytes = renderer.renderAndConvertToBytes("abc123");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageAsBytes)) {
            BufferedImage image = ImageIO.read(bis);
            assertEquals(targetWidth, image.getWidth());
        }
    }

    @Test
    final void renderAndConvertToBytes_test_color_settings() throws IOException {
        CaptchaRenderer renderer = CaptchaRenderer.builder()
                .noiseSettings(new NoiseSettings(1, Color.GRAY))
                .availableTextColors(Collections.singletonList(Color.BLUE))
                .imageBackground(new FlatColorBackground(Color.BLUE))
                .build();
        byte[] imageAsBytes = renderer.renderAndConvertToBytes("abc123");
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageAsBytes)) {
            BufferedImage image = ImageIO.read(bis);
            int pixel = image.getRGB(0, 0);
            Color pixelColor = new Color(pixel, true);
            assertTrue(200 < pixelColor.getBlue());
        }
    }

    @Test
    final void renderAndConvertToBytes_as_png() {
        CaptchaRenderer renderer = CaptchaRenderer.builder().imgFileFormat("png").build();
        assertDoesNotThrow(() -> renderer.renderAndConvertToBytes("abc123"));
    }

    @Test
    final void renderAndConvertToBytes_catch_null_in_builder() {
        CaptchaRenderer renderer = CaptchaRenderer.builder().imgFileFormat(null).build();
        assertDoesNotThrow(() -> renderer.renderAndConvertToBytes("abc123"));
    }

    @Test
    final void renderAndConvertToBytes_with_multiple_colors() {
        List<Color> colors = List.of(Color.BLUE, Color.BLACK);
        CaptchaRenderer renderer = CaptchaRenderer.builder().availableTextColors(colors).build();
        assertDoesNotThrow(() -> renderer.renderAndConvertToBytes("abc123"));
    }

    @Test
    final void renderAndConvertToBytes_test_errorhandling() {
        try (MockedStatic<ImageIO> imageIOMock = Mockito.mockStatic(ImageIO.class)) {
            imageIOMock.when(() -> ImageIO.write(any(), any(), Mockito.any(OutputStream.class)))
                    .thenThrow(IOException.class);
            CaptchaRenderer renderer = CaptchaRenderer.builder().imgFileFormat(null).build();
            assertThrows(CaptchaRenderingException.class, () -> renderer.renderAndConvertToBytes("abc123"));
        }
    }
}