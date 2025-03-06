package ppt.to.json;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;


public class Extractor {

    public record Config(
        boolean excludeImages
    ) {
    }

    private Config config;

    public Extractor(Config config) {
        this.config = config;
    }
    
    public SlideShow Extract(String pptFile) throws EncryptedDocumentException, IOException {
        PrintStream out = System.out;
        SlideShow slideShow;

        FileInputStream fileInputStream = new FileInputStream(pptFile);
        try (XMLSlideShow ppt = new XMLSlideShow(fileInputStream)) {
            fileInputStream.close();

            final List<ppt.to.json.Slide> slides = new ArrayList<>();
            for ( final XSLFSlide slide : ppt.getSlides() ) {
                final List<Image> images = new ArrayList<>();
                final List<TextSection> textSections = new ArrayList<>();
                for ( XSLFShape shape : slide ) {
                    if ( shape instanceof XSLFTextShape txShape ) {
                        extractTextSection( txShape ).ifPresent( textSections::add );
                    }
                    else if ( shape instanceof XSLFPictureShape pShape ) {
                        if (!this.config.excludeImages) {
                            images.add( extractImage( pShape ) );
                        }
                    }
                    else {
                        out.println( "Ignoring type: " + shape.getClass() );
                    }
                }

                slides.add(
                    new Slide(
                        slide.getTitle(),
                        slide.getSlideName(),
                        slide.getSlideNumber(),
                        textSections,
                        images
                    )
                );
            }

            final Dimension pageSize = ppt.getPageSize();
            slideShow = new SlideShow(pageSize.height, pageSize.width, slides);
        }

        return slideShow;
    }

    private Optional<TextSection> extractTextSection(final XSLFTextShape txShape) {
        if (txShape.getText().isEmpty() || txShape.getText().isBlank()) {
            return Optional.empty();
        }

        final var anchor = txShape.getAnchor();
        return Optional.of(new TextSection(
            txShape.getText(),
            anchor.getX(),
            anchor.getY()));
    }

    private Image extractImage(final XSLFPictureShape pShape) {
        final XSLFPictureData pData = pShape.getPictureData();
        final String encodedImage = Base64.getEncoder()
            .encodeToString(pData.getData());
        final double x = pShape.getAnchor().getX();
        final double y = pShape.getAnchor().getY();

        return new Image(x, y, pData.getFileName(), encodedImage);
    }
}
