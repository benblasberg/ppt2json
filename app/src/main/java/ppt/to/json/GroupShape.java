package ppt.to.json;

import java.util.List;

/**
 * Record representing a group shape
 *
 * @param anchorPosition the anchor position of the group shape
 * @param name           the name of the group shape
 * @param shapes         the shapes within the group
 */
public record GroupShape(Position anchorPosition, String name, List<Shape> shapes) {
}
