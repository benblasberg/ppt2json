package ppt.to.json;

/**
 * Record representing a text section
 * @param content the content of the text
 * @param x the x position of the text
 * @param y the y position of the text
 */
public record TextSection(
    String content,
    double x,
    double y) {
}
