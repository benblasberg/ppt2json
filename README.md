Assumptions:

- Only focused on text. Images and shapes are ignored.
- Power point documents given are 2007 or greater format

Build and execute:

```bash
./gradlew run shadowJar
java -jar app/build/libs/app-all.jar <pptx file> <options>
```

Usage:

```text
Usage: ppt2json [-hvV] [--exclude-images] <pptxFile>
Parses a given .pptx file to JSON and prints to STDOUT.
      <pptxFile>         The pptx file to parse.
      --exclude-images   Excludes images from the json output
  -h, --help             Show this help message and exit.
  -v, --verbose          Prints debug messages during execution
  -V, --version          Print version information and exit.
```