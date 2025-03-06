/*
 * This source file was generated by the Gradle 'init' task
 */
package ppt.to.json;

import java.io.IOException;

import com.google.gson.Gson;
import org.apache.poi.EncryptedDocumentException;


public class App {
    public static void main(String[] args) throws EncryptedDocumentException, IOException {
        // TODO: CLI with options here
        // Also, Dockerize
        // Add tests
        // Look into shape parsing


        Extractor extractor = new Extractor(new Extractor.Config( true ));

        final var slideShow = extractor.Extract( "/Users/ben/develop/java/ppt-to-json/Programming Exercise #4 Input.pptx" );

        final Gson gson = new Gson();

        System.out.println(gson.toJson( slideShow ));
    }
}
