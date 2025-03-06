package ppt.to.json;

import java.util.List;

/**
 * Record representing a slide
 * @param title the title of the slide
 * @param name the name of the slide
 * @param number the number in the slideshow of the slide
 * @param textSections list of text sections in the slide
 * @param images list of images in the slide
 */
public record Slide(
    String title,
    String name,
    int number,
    List<TextSection> textSections,
    List<Image> images) {
}
