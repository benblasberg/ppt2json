package ppt.to.json;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

/**
 * Extractor extracts text/images/shapes from a given power point file
 */
public class Extractor {

    // Builder pattern would be nice here in real production code
    public record Config(
            boolean excludeImages,
            boolean verbose,
            boolean excludeXml
    ) {
    }

    private Config config;

    public Extractor(Config config) {
        this.config = config;
    }

    public SlideShow Extract(File pptFile) throws EncryptedDocumentException, IOException {
        SlideShow slideShow;

        final FileInputStream fileInputStream = new FileInputStream(pptFile);
        try (XMLSlideShow ppt = new XMLSlideShow(fileInputStream)) {
            fileInputStream.close();

            final List<Slide> slides = new ArrayList<>();
            for (final XSLFSlide slide : ppt.getSlides()) {
                final List<Image> images = new ArrayList<>();
                final List<TextSection> textSections = new ArrayList<>();
                final List<Shape> shapes = new ArrayList<>();
                final List<GroupShape> groupShapes = new ArrayList<>();
                for (XSLFShape shape : slide) {
                    switch (shape) {
                        case XSLFTextShape txShape -> extractTextSection(txShape).ifPresent(textSections::add);
                        case XSLFPictureShape pShape -> {
                            if (!this.config.excludeImages) {
                                extractImage(pShape).ifPresent(images::add);
                            }
                        }
                        case XSLFGroupShape gShape -> extractGroupShape(gShape).ifPresent(groupShapes::add);
                        default -> extractShape(shape).ifPresent(shapes::add);
                        // NOTE: Depending on what information we want, we can parse every type of
                        // shape available here and include as many details as possible.
                        // This covers group shapes and a generic shape for now
                    }
                }

                slides.add(
                        new Slide(
                                slide.getTitle(),
                                slide.getSlideName(),
                                slide.getSlideNumber(),
                                textSections,
                                images,
                                groupShapes,
                                shapes
                        )
                );
            }

            final Dimension pageSize = ppt.getPageSize();
            slideShow = new SlideShow(pageSize.height, pageSize.width, slides);
        }

        return slideShow;
    }

    private Optional<TextSection> extractTextSection(final XSLFTextShape txShape) {
        if (txShape.getText().isEmpty() || txShape.getText().isBlank() || txShape.isPlaceholder()) {
            return Optional.empty();
        }

        final var anchor = txShape.getAnchor();
        return Optional.of(new TextSection(
                new Position(anchor.getX(), anchor.getY()),
                txShape.getText()));
    }

    private Optional<Image> extractImage(final XSLFPictureShape pShape) {
        if (pShape.isPlaceholder()) {
            return Optional.empty();
        }
        final XSLFPictureData pData = pShape.getPictureData();
        final String encodedImage = Base64.getEncoder()
                .encodeToString(pData.getData());
        final double x = pShape.getAnchor().getX();
        final double y = pShape.getAnchor().getY();

        return Optional.of(new Image(new Position(x, y), pData.getFileName(), encodedImage));
    }

    private Optional<GroupShape> extractGroupShape(final XSLFGroupShape gShape) {
        if (gShape.isPlaceholder()) {
            return Optional.empty();
        }

        final List<Shape> shapes = new ArrayList<>();
        for (XSLFShape shape : gShape.getShapes()) {
            if (shape.isPlaceholder()) {
                continue;
            }

            shapes.add(
                    new Shape(
                            new Position(shape.getAnchor().getX(), shape.getAnchor().getY()),
                            shape.getShapeName(),
                            getXml(shape)));
        }

        return Optional.of(
                new GroupShape(
                        new Position(gShape.getAnchor().getX(), gShape.getAnchor().getY()),
                        gShape.getShapeName(),
                        shapes
                )
        );
    }

    private Optional<Shape> extractShape(final XSLFShape shape) {
        if (shape.isPlaceholder()) {
            return Optional.empty();
        }

        return Optional.of(new Shape(
                new Position(shape.getAnchor().getX(), shape.getAnchor().getY()),
                shape.getShapeName(),
                getXml(shape)));
    }

    private String getXml(final XSLFShape shape) {
        if (this.config.excludeXml()) {
            return "";
        }

        return shape.getXmlObject().xmlText();
    }

    // TODO: add more shape type extractors here if desired

    private void log(String message) {
        if (this.config.verbose()) {
            System.out.println(message);
        }
    }
}
