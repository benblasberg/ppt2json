package ppt.to.json;

/**
 * Record representing a shape
 *
 * @param anchorPosition the anchor position of the shape
 * @param name           the name of the shape
 * @param xml            xml string representation of this shape
 */
public record Shape(Position anchorPosition, String name, String xml) {
}
