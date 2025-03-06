package ppt.to.json;

/**
 * Record representing a text section
 *
 * @param content        the content of the text
 * @param anchorPosition the anchor position of the text
 */
public record TextSection(
        Position anchorPosition,
        String content
) {
}
