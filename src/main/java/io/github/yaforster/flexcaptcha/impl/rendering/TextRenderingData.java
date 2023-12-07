package io.github.yaforster.flexcaptcha.impl.rendering;

import java.awt.*;

public record TextRenderingData(Font font, FontMetrics fontMetrics, int margin, float spaceBetweenCharacters,
                                int maxAdvance, int fontHeight, int charDim) {
}
