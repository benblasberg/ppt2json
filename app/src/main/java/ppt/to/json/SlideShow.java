package ppt.to.json;

import java.util.List;

/**
 * Record representing the text and images in a slide show
 *
 * @param height the height of the slide show window
 * @param width  the width of the slide show window
 * @param slides a list of slides in the slide show
 */
public record SlideShow(
        int height,
        int width,
        List<Slide> slides) {
}
