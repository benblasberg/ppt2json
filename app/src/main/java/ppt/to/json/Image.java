package ppt.to.json;

/**
 * A record representing an image
 * @param x the x position of the image
 * @param y the y position of the image
 * @param name filename of the image
 * @param encodedImage base64 encoded bytes of the image
 */
public record Image(
    double x,
    double y,
    String name,
    String encodedImage) {
}
