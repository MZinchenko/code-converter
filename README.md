# Code snippet converter IntelliJ IDEA ⟶ http://habr.com.
Clipboard snippet code converter for http://habr.com.

Transforms code from usual html IntelliJ IDEA format into special _habr.com_ format.  

**Usage:**
* Single run mode
    * Select code region in IntelliJ IDEA
    * Press Ctrl-C (Copy)
    * Run `java -jar code-converter.jar`
    * Press Ctrl-V (Paste)
    
* Daemon mode
    * Run `java -jar code-converter.jar daemon`
    * Select code region in IntelliJ IDEA
    * Press Ctrl-C (Copy)
    * Press Ctrl-V (Paste)
    * Select code region in IntelliJ IDEA
    * Press Ctrl-C (Copy)
    * Press Ctrl-V (Paste)
    ...    