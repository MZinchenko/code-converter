# Code snippet converter IntelliJ IDEA ⟶ http://habr.com.
Clipboard code snippet converter for http://habr.com.

Transforms code from usual html IntelliJ IDEA format into special _habr.com_ format.  

**Usage:**
* *Single execution mode*
    * Select code region in IntelliJ IDEA and press Ctrl-C (Copy)
    * Run `java -jar code-converter.jar`
    * Goto habr.com page editor and press Ctrl-V (Paste)
    
* *Daemon mode*
    * Run `java -jar code-converter.jar daemon`
    * Select code region in IntelliJ IDEA and press Ctrl-C (Copy)
    * Goto habr.com page editor and press Ctrl-V (Paste)
    * Select code region in IntelliJ IDEA and press Ctrl-C (Copy)
    * Goto habr.com page editor and press Ctrl-V (Paste)
    * etc  
