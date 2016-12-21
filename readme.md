updated CEP Twitter demo from tsurdilo/fusion-example

Before running make sure you register with twitter and update the `src/main/resources/twitter4j.properties` accordingly.

To run pass in the name of package containing the demo:

- **demo1** - Simple demo just dumps all tweets 
- **demo2** - Shows use of drools pattern matching on events
- **demo3** - Uses CEP operators to combine two events
- **demo4** - Illustrates the use of a sliding window
- **demo5** - Rules act as a source for events into other entry-points
 
 
To run (example shown with maven)

```bash
$ mvn -q compile exec:java -Dexec.args=demo1

```