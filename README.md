# Tabulator

## Description

Easy to use java library to represent objects as nicely formatted tables.

## Installation

The compiled libraries are deployed to Maven Central.

Usage with maven:

```xml
<dependency>
    <groupId>io.github.kamilszewc</groupId>
    <artifactId>tabulator</artifactId>
    <version>3.4</version>
</dependency>
```

Usage with gradle:

```groovy
implementation 'io.github.kamilszewc:tabulator:3.4'
```

## Usage

Currently, there are two types of output generators.

1. The card - can be used to represent any serializable object.
    Example:

    ```java
   String card = Card.builder()
                .object(object)
                .maxColumnWidth(30)
                .multiLine(true)
                .header("Basic class - this is very long header that needs to be multi line")
                .getCard();
   ```
    will generate
   ```
   +-----------------------------------------------+
   |  Basic class - this is very long header that  |
   |             needs to be multi line            |
   +--------------+--------------------------------+
   | first        | 1                              |
   | fourthOption |                                |
   | second       | Second entry                   |
   | thirdOption  | Third Option is quite          |
   |              | long and it would be nice      |
   |              | if we can have multi line      |
   | time         | 2022-08-22T10:12:33            |
   +--------------+--------------------------------+
   ```
2. The table - can be used to represent list of serializable objects.
   Example:
    ```java
   String table = Table.builder()
                .object(listOfObjects)
                .maxColumnWidth(25)
                .multiLine(true)
                .header("Basic class header that is too long and we need to have multiple lines more text and more and more text")
                .getTable();
   ```
    will generate
   ```
   +--------------------------------------------------------------------------------------+
   |    Basic class header that is too long and we need to have multiple lines more text  |
   |                                 and more and more text                               |
   +-------+-------------+--------------+---------------------------+---------------------+
   | first | forthOption | second       | thirdOption               | time                |
   +-------+-------------+--------------+---------------------------+---------------------+
   | 1     |             | Second entry | Third Option              | 2022-08-22T21:20:12 |
   +-------+-------------+--------------+---------------------------+---------------------+
   | 2     |             | Second entry | Third Option that is      | 2022-08-22T21:20:12 |
   |       |             |              | too long                  |                     |
   +-------+-------------+--------------+---------------------------+---------------------+
   ```

## License

Apache License 2.0
Copyright 2022-2023 Kamil Szewc
