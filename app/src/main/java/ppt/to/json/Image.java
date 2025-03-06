package ppt.to.json;

/**
 * A record representing an image
 * @param x the x position of the image
 * @param y the y position of the image
 * @param name
 * @param encodedImage
 */
public record Image(
    double x,
    double y,
    String name,
    String encodedImage) {
}
