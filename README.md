Java bindings for TrailDB
====

This repository's goal is to provide Java bindings for [TrailDB](http://traildb.io), an efficient tool for storing and querying series of events, 
based on the Python bindings available [here](https://github.com/traildb/traildb-python)

## Getting started

This repository requires having TrailDB installed on the machine. See instructions on the
 [TrailDB Github readme](https://github.com/traildb/traildb) and in 
 the [getting started guide](http://traildb.io/docs/getting_started/).

If you have the Sqooba Central Repo in your pom.xml, the project is available with:

```
<dependency>
    <groupId>io.sqooba</groupId>
    <artifactId>TrailDBj</artifactId>
    <version>0.1</version>
</dependency>
```

## Minimal examples

### Java

```java
import java.io.IOException;
import java.util.Map;

public class Example {

    public static void main(String args[]) throws IOException {

        // 32-byte hex String.
        // The cookie will be used as the trail's uuid.
        String cookie = "12345678123456781234567812345678";

        // Name of the db, without .tdb.
        String path = "testdb";

        // Building a new TrailDB. Finalization taken care of by .build().
        TrailDB db = new TrailDB.TrailDBBuilder(path, new String[] { "field1", "field2" })
                .add(cookie, 120, new String[] { "a", "b" })
                .add(cookie, 121, new String[] { "c", "d" })
                .build();

        // Iterate over whole db using iterator.
        Map<String, TrailDBIterator> map = db.trails();

        for(Map.Entry<String, TrailDBIterator> entry : map.entrySet()) {
            for(TrailDBEvent event : entry.getValue()) {
                System.out.println(entry.getKey() + " " + event);
            }
        }

        // Iterate over single trail.
        TrailDBIterator trail = db.trail(0);

        for(TrailDBEvent event : trail) {
            System.out.println(event);
        }
    }
}
```

### Scala

Here is an example with the use of the TrailDBBuilder.

```scala
import io.sqooba.traildb.TrailDB
import io.sqooba.traildb.TrailDB.TrailDBBuilder
import io.sqooba.traildb.TrailDBIterator

import scala.collection.JavaConversions._

object BuilderExample {
  def main(args: Array[String]): Unit = {
    val path: String = "testdb";
    val cookie: String = "12345678123456781234567812345678";
    val fields: Array[String] = Array("action", "description");

    // Builder for new TrailDB.
    var builder: TrailDBBuilder = new TrailDB.TrailDBBuilder(path, fields);

    // Add events to builder.
    for (i <- 0 to 10) {
      builder.add(cookie, i + 120, Array("button" + i, "dummy description"));
    }

    // Build the new TrailDB
    val db:TrailDB = builder.build();
    
    // Get iterator over the trail.
    val trail:TrailDBIterator = db.trail(0);
    
    // Print events on the trail.
    for(e <- trail) println(e);
  }
}

```

Other example with directly opening an existing db.

```scala
import io.sqooba.traildb.TrailDB
import io.sqooba.traildb.TrailDBEvent
import io.sqooba.traildb.TrailDBIterator
import scala.collection.JavaConversions._

object ExistingExample {
  def main(args: Array[String]): Unit = {
    // Suppose there exists a testdb.tdb file previously created.
    val path: String = "testdb";

    // Create a TrailDB on an existing tdb file.
    val db: TrailDB = new TrailDB(path);

    // Get iterator over each trail.
    val trails: java.util.Map[String, TrailDBIterator] = db.trails();

    // Print all events of all trails.
    for (entry <- trails) {
      for (event: TrailDBEvent <- entry._2) {
        println(entry._1 + " -> " + event);
      }
    }
  }
}
```


## Bound methods

The full list of methods can be found on the [C API web page](http://traildb.io/docs/api/)

### Construct a new TrailDB

| Method            | Bound | Exposed |
|-------------------|--------|---------|
| tdb_cons_init     | Yes    | No      |
| tdb_cons_open     | Yes    | No      |
| tdb_cons_close    | Yes    | No      |
| tdb_cons_add      | Yes    | Yes     |
| tdb_cons_set_opt  | No     | No      |
| tdb_cons_get_opt  | No     | No      |
| tdb_cons_finalize | Yes    | Yes     |

### Open a TrailDB and access metadata

| Method            | Bound | Exposed |
|-------------------|--------|---------|
| tdb_init          | Yes    | No      |
| tdb_open          | Yes    | No      |
| tdb_close         | Yes    | No      |
| tdb_dontneed      | No     | No      |
| tdb_willneed      | No     | No      |
| tdb_num_trails    | Yes    | Yes     |
| tdb_num_events    | Yes    | No      |
| tdb_num_fields    | Yes    | No      |
| tdb_min_timestamp | Yes    | Yes     |
| tdb_max_timestamp | Yes    | Yes     |
| tdb_version       | Yes    | Yes     |
| tdb_error_str     | Yes    | No      |

### Setting Options

Nothing.

### Working with items, fields and values

| Method             | Bound | Exposed |
|--------------------|--------|---------|
| tdb_item_field     | No     | No      |
| tdb_item_val       | No     | No      |
| tdb_make_item      | No     | No      |
| tdb_item_is32      | No     | No      |
| tdb_lexicon_size   | Yes    | Yes     |
| tdb_get_field      | Yes    | Yes     |
| tdb_get_field_name | Yes    | Yes     |
| tdb_get_item       | Yes    | Yes     |
| tdb_get_value      | Yes    | Yes     |
| tdb_get_item_value | Yes    | Yes     |

### Working with UUIDs

| Method           | Bound | Exposed |
|------------------|--------|---------|
| tdb_get_uuid     | Yes    | Yes     |
| tdb_get_trail_id | Yes    | Yes     |
| tdb_uuid_raw     | Yes    | Yes     |
| tdb_uuid_hex     | Yes    | Yes     |

### More to come in the future
