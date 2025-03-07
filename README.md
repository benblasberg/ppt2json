
Parses:

  - Text
  - Images (base64 encoded bytes)
  - Shapes (xml string representation)
  - Group Shapes (container of a list of shapes)

Each of these entities contains an x,y position of where they are located on a slide.

The response is structured like this.

```json
{
  "height": <height of power point>,
  "width": <width of power point>,
  "slides": [
    {
      "name": <name of slide>,
      "number": <slide number>,
      "title": <title of slide>,
      "textSections": [
        {
          "anchorPosition": <x,y of this text>,
          "content": "string content of the text"
        }
      ],
      "images": [
        {
          "anchorPosition": <x,y of this image>,
          "name": <name of the image>,
          "encodedImage": <base64 encoding of the image bytes>
        }
      ],
      "groupShapes": [
        [{
          "anchorPosition": <x,y of this group shape>,
          "name": <name of the group shape>,
          "shapes": [{
            "anchorPosition": <x,y of this shape>,
            "name": <name of the shape>,
            "xml": <xml string representation of this shape>
          }]
        }],
        "shapes": [{
          "anchorPosition": <x,y of this shape>,
          "name": <name of the shape>,
          "xml": <xml string representation of this shape>
        }]
      ]
    }
  ]
}
```


Assumptions:

- Power point documents given are 2007 or newer format

Build and execute:

Requires java 21+

```bash
./gradlew run shadowJar
java -jar app/build/libs/app-all.jar <pptx file> <options>
```

Usage:

```text
Usage: ppt2json [-hvV] [--exclude-images] [--exclude-xml] <pptxFile>
Parses a given .pptx file to JSON and prints to STDOUT.
      <pptxFile>         The pptx file to parse.
      --exclude-images   Excludes images from the json output
      --exclude-xml      Excludes shape xml from the json output
  -h, --help             Show this help message and exit.
  -v, --verbose          Prints debug messages during execution
  -V, --version          Print version information and exit.
```

Test:

```bash
./gradlew test
```