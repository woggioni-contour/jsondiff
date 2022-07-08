## Overview

This is a sample CLI project using [json-patch](https://github.com/java-json-tools/json-patch)

## Build

Compile the project with

```bash
./gradlew build
```

this generates an executable jar in the `build/distributions` folder

## Run
It is possible to either run directly from Gradle using

```bash
./gradlew envelopeRun --args='--help'
```

or running the executable jar

```bash
java -jar build/distributions/jsondiff-envelope-0.1-SNAPSHOT.jar --help 
```

## Examples
Print the diff between 2 json documents `file1.json` and `file2.json` to `stdout`:

```bash
java -jar jsondiff-envelope.jar diff -i1 file1.json -i2 file2.json 
```
 Apply the diff in `patch.json` to `file1.json` and write the result on `file2.json`

```bash
java -jar jsondiff-envelope.jar patch -i file1.json -p patch.json -o file2.json 
```
