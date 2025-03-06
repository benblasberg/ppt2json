package ppt.to.json;

/**
 * A record representing an image
 *
 * @param anchorPosition the anchor position of the image
 * @param name           filename of the image
 * @param encodedImage   base64 encoded bytes of the image
 */
public record Image(
        Position anchorPosition,
        String name,
        String encodedImage) {
}
