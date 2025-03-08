package ppt.to.json;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtractorTest {

    private final File testPPT = new File("src/test/resources/test.pptx");
    private final File testPPT2 = new File("src/test/resources/samplepptx.pptx");

    private Extractor.Config getDefaultExtractorConfig() {
        return new Extractor.Config(true, false, true);
    }

    private SlideShow getParsedSlideshow(final Extractor.Config config) throws IOException {
        final Extractor extractor = new Extractor(config);

        return extractor.Extract(testPPT);
    }

    private long countOccurrences(List<TextSection> sections, String text) {
        return sections.stream().filter(t -> t.content().contains(text)).count();
    }

    @Test
    void testSlideShowParse() throws IOException {
        final SlideShow slideShow = getParsedSlideshow(getDefaultExtractorConfig());

        assertEquals(1, slideShow.slides().size());
    }

    @Test
    void  testTextParse() throws IOException {
        final SlideShow slideShow = getParsedSlideshow(getDefaultExtractorConfig());

        final Slide slide = slideShow.slides().get(0);

        assertEquals(3, countOccurrences(slide.textSections(), "USS SPRUANCE"));
        assertEquals(1, countOccurrences(slide.textSections(), "BLUF:"));
        assertEquals(0, countOccurrences(slide.textSections(), "Michael Scott"));
    }

    @Test
    void  testTextParseMultipleSlides() throws IOException {
        final Extractor extractor = new Extractor(getDefaultExtractorConfig());

        final SlideShow slideShow = extractor.Extract(testPPT2);

        assertEquals(slideShow.slides().size(), 2);

        final Slide slide0 = slideShow.slides().get(0);
        assertEquals(1, countOccurrences(slide0.textSections(), "Sample PowerPoint File"));
        assertEquals(1, countOccurrences(slide0.textSections(), "St. Cloud Technical College"));

        final Slide slide1 = slideShow.slides().get(1);
        assertEquals(1, countOccurrences(slide1.textSections(), "This is a Sample Slide"));
        assertEquals(1, countOccurrences(slide1.textSections(), "Here is an outline of bulleted points"));
        assertEquals(1, countOccurrences(slide1.textSections(), "PRINT >"));
    }
}
