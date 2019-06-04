# Java 9

Java 9 is first java release in a new "way".   
It is released in September 2017, it is non LTS release, with end of support scheduled for March 2018. 
Java 9 brought probably the biggest changes to Java ecosystem in last 10 years.

## Module system

Module system in java was planned for Java 7 but postponed for 8 and subsequently to 9. 
It is one of the biggest changes in Java ever, since it does not affect only language itself but also a compiler, VM, and Tooling.  

"The most important new software engineering technology in Java since its inception". - Paul Deitel

Module system is result of project [Jigsaw](http://openjdk.java.net/projects/jigsaw/). 
First and main reason for this feature is to modularize JDK itself. Make it maintainable again.
Second reason is to enable developers to modularize their own applications. 
Off course, modularizing applications is optional, but still, they run on modular JDK.

**Module has a name, it groups related code as well as resources and is fully self-contained.**  
Every module has a module descriptor which consists of: 

* Module’s name  
* Module’s dependencies (that is, other modules this module depends on)
* Packages it explicitly makes available to other modules (all other packages in the module are implicitly unavailable to other modules)
* services it offers
* services it consumes
* to what other modules it allows reflection
* ...

A module descriptor is the compiled version of a module declaration that’s defined in a file named module-info.java. Module descriptor is stored in a file named module-info.class in the module’s root folder.Each module declaration begins with the keyword module, followed by a unique module name and a module body enclosed in braces, as in:

```java
module modulename { 

}
```

The module declaration’s body can be empty or may contain various module directives like:   

* **requires**. A requires module directive specifies that this module depends on another module—this relationship is called a module dependency. Each module must explicitly state its dependencies. When module A requires module B, module A is said to read module B and module B is read by module A. To specify a dependency on another module, use requires, as in:

`requires modulename;`

* **requires transitive**—implied readability. To specify a dependency on another module and to ensure that other modules reading your module also read that dependency—known as implied readability—use requires transitive, as in:

`requires transitive modulename;`

* **exports and exports…to**. An exports module directive specifies one of the module’s packages whose public types (and their nested public and protected types) should be accessible to code in all other modules. An exports…to directive enables you to specify in a comma-separated list precisely which module’s or modules’ code can access the exported package—this is known as a qualified export. 

* **uses**. A uses module directive specifies a service used by this module—making the module a service consumer. A service is an object of a class that implements the interface or extends the abstract class specified in the uses directive.

* **provides…with**. A provides…with module directive specifies that a module provides a service implementation—making the module a service provider. The provides part of the directive specifies an interface or abstract class listed in a module’s uses directive and the with part of the directive specifies the name of the service provider class that implements the interface or extends the abstract class.

* **open, opens, and opens…to**. Before Java 9, reflection could be used to learn about all types in a package and all members of a type—even its private members—whether you wanted to allow this capability or not. Thus, nothing was truly encapsulated.  
A key motivation of the module system is strong encapsulation. By default, a type in a module is not accessible to other modules unless it’s a public type and you export its package. You expose only the packages you want to expose. With Java 9, this also applies to reflection.

  * **Allowing runtime-only access to a package**. An opens module directive of the form
`opens package`  
indicates that a specific package’s public types (and their nested public and protected types) are accessible to code in other modules at runtime only. Also, all the types in the specified package (and all of the types’ members) are accessible via reflection.  
  * **Allowing runtime-only access to a package by specific modules**. An opens…to module directive of the form `opens package to comma-separated-list-of-modules`  
indicates that a specific package’s public types (and their nested public and protected types) are accessible to code in the listed modules at runtime only. All of the types in the specified package (and all of the types’ members) are accessible via reflection to code in the specified modules.  
  * **Allowing runtime-only access to all packages in a module**. If all the packages in a given module should be accessible at runtime and via reflection to all other modules, you may open the entire module, as in: `open module modulename {// module directives}` 


The keywords exports, module, open, opens, provides, requires, uses, with, as well as to and transitive, are restricted keywords. They’re keywords only in module declarations and may be used as identifiers anywhere else in your code.  

Cyclic dependencies are not supported. 

**Advantages:** 
- Security (Module can expose some of it's packages, and encapsulate other)
- Reduced footprint 
- Easy deprecation (In java 9, java.corba was removed)
- Future proof (Incubator modules jdk.incubator.httpclient)
- Improved performance—The JVM uses various optimization techniques to improve application performance. JSR 376 indicates that these techniques are more effective when it’s known in advance that required types are located only in specific modules.

### Modular JDK 

JDK becoming to large is main motivation behind module system.

Now, there is more than 90 platform modules: ![jdkModules]

In this figure the bold lines represent explicit dependence relationships, as expressed in requires clauses, while the light lines represent the implicit dependencies of every module upon the base module.


### Migration 
Easy unless:   
**1\. You or some libs use JDK types that have been encapsulated.**  
They will still run, but they will not compile... This can change in next versions. If you want, you can enforce encapsulation in runtime by passing following flag to jvm: 
`--illegal-access=deny`
You can bypass encapsulation using  `-add-exports $moduleYouNeed/$packageYouNeed=$yourModule`

This command line option exports `$packageYouNeed` of `$moduleYouNeed` to `$yourModule`. Code in `$yourModule` can hence access all public types in `$packageYouNeed` but other modules can not. 

This option is available for the java and javac commands.

When setting `$yourModule` to ALL-UNNAMED, all code from the classpath can access that package. This is preferred way to go when accessing internal APIs during a migrating to Java 9. 
Example:  
`javac --add-exports java.base/sun.security.x509=ALL-UNNAMED Main.java` 

If you have trouble migrating there is handy tool shipped with JDK to help you.
`jdeps -jdkinternals target/classes/com/mbrkljac/javaaftereight/HttpClientDemo.class `
Using this tool you can check dependencies without running or compiling. It also suggests solution.
Example:  
`jdeps -jdkinternals Main.class`

**2\. You or some lib use types from non-default Java modules (java.se.ee)**
This can be mitigated using: `javac --add-modules java.xml.bind Main.java` and 
`java --add-modules java.xml.bind Main`

More info about jdeps: https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jdeps.html  

## JShell

https://docs.oracle.com/javase/9/jshell/JSHEL.pdf 

The JShell tool is a command line tool that provides interactive use of Java Programming Language elements. JShell is a REPL(Read -> type code; Eval -> execute code; Print -> see results; Loop -> interactively refine).  
With it, we can enter program elements one at a time, immediately seeing the result and adjusting accordingly. 
It is mainly intended to be used to test out ideas, explore API's as well as teaching. 

Main features: 
* **It's just plain java.**   
* You get **code completion**, 
* it has **built-in documentation**.   
* It is possible to **incrementally create code** inside of jshell. (variable can be declared latter)
* It is possible to **save and open snippets**
* Snippets can be edited using **external editor** 
* JShell API  enables **IDE integration**. (IntelliJ: Tools/JShell Console)

## Library and Language Improvements

Java 9 comes with some small extensions to existing java libraries. There are no major changes like in java 8, changes in module system are big enough for themselves. 

### Collection factory methods

Old ways: 
```
List<String> hrana  = new ArrayList<>();
hrana.add("Janjetina");
hrana.add("Svinjetina");
hrana.add("Junetina");
```
`List<String> hrana =  Arrays.asList("Janjetina")`  
`List<String> hrana = new ArrayList<>()`  
`List<String> hrana =  Collections.emptyList()`  

New way:  
`List<String> hrana = List.of("Janjetina", "Svinjetina", "Junetina")`  
`Set<String> strings = Set.of("pera", "Zdera")`  
`Map<String, Integer> map = Map.of("pera", 1, "Zdera", 2)`  
Those methods return immutable collections, and are heavily optimized. 
Iteration order for Set and Map is not guaranteed. (It was not guaranteed before also, but with old approach, order was mostly consistent. Now, that is not the case.)

### Stream API Improvements
Added methods:  
***`static Stream<T> ofNullable(T t)`***
```
List<String> nullableList = getStringsPossibleNull();
//Old way

Stream strings = nullableList == null ? Stream.empty() : nullableList.stream();

//New way
Stream strings = Stream.offNullable(nullableList);
```

***`Stream<T> takeWhile(Predicate<? super T> predicate)`***  
TakeWhile is similar to filter in the sense that it expects a predicate and returns a new stream consisting only of the elements that match the given predicate. In an ordered stream, takeWhile takes elements from the initial stream while the predicate holds true. Meaning that when an element is encountered that does not match the predicate, the rest of the stream is discarded.  
If the stream is unordered, if some of the elements in the stream match the predicate (but not all) then the operation is nondeterministic and an arbitrary subset of matching elements is returned or removed. If all elements match the predicate, all input will be returned. If none matches, empty stream will be returned.

```java
jshell> Stream.of(2, 4, 5, 6 ,8)
    .takeWhile(n -> n % 2 == 0)
    .forEach(System.out::print);
// prints out: 24
```


```java
jshell> Set<Integer> numbers = Set.of(2, 4, 5, 6 ,8);
numbers.stream()
    .takeWhile(n -> n % 2 == 0)
    .forEach(System.out::print);
//Prints out different result depending on how Set has been initialized 

numbers ==> [2, 8, 6, 5, 4]
286
```

***`Stream<T> dropWhile(Predicate<? super T> predicate)`***
dropWhile is the opposite of takeWhile. It drops the element until the first element that matches the predicate, and includes the rest in the result stream;

***`static Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)`*** 
Overloaded iterate method, added hasNext parameter which is used to determine when the Stream must  terminate.

```java
jshell> Stream.iterate(0, i -> i < 9, i -> i + 1)
  .forEach(System.out::print);
//Prints out: 012345678
```

### New Collectors:   
#### filtering
The Collectors.filtering is similar to the Stream filter(); it’s used for filtering input elements but used for different scenarios. The Stream’s filter is used in the stream chain whereas the filtering is a Collector which was designed to be used along with groupingBy.

With Stream’s filter, the values are filtered first and then it’s grouped. In this way, the values which are filtered out are gone and there is no trace of it. If we need a trace then we would need to group first and then apply filtering which actually the Collectors.filtering does.

The Collectors.filtering takes a function for filtering the input elements and a collector to collect the filtered elements:

```java
jshell> 
    List<Integer> numbers = List.of(1, 2, 3, 5, 5);
 
    Map<Integer, Long> result = numbers.stream()
      .filter(val -> val > 3)
      .collect(Collectors.groupingBy(i -> i, Collectors.counting()));
  
    result = numbers.stream()
      .collect(Collectors.groupingBy(i -> i,
        Collectors.filtering(val -> val > 3, Collectors.counting())));
```
                                 
####flatMapping

The Collectors.flatMapping is similar to Collectors.mapping but has a more fine-grained objective. Both the collectors takes a function and a collector where the elements are collected but flatMapping function accepts a Stream of elements which is then accumulated by the collector.

### Additions to optional
* **ifPresentOrElse**  
`  public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)`  
Example:   
`   Optional<String> value = Optional.of("someValue");`
`  value2.ifPresentOrElse(v -> System.out.println(v), () -> System.out.println("empty"));
`
* **or**  
Used when we want to execute some other action that also returns an Optional.  
Prior Java 9 the Optional class had only the orElse() and orElseGet() methods but both need to return unwrapped values.  
Java 9 introduces the or() method that returns another Optional lazily if our Optional is empty. If our first Optional has a defined value, the lambda passed to the or() method will not be invoked, and value will not be calculated and returned:  
`Optional<String> result = value.or(() -> defaultValue);`

* **stream**  
Added to achieve interoperability between Optional and Stream.
Calling stream() on empty optional will result in empty stream.   
Quite useful in combination with Stream.flatMap  
``` 
List<Optional<String>> listOfOptionals = Arrays.asList(Optional.empty(), Optional.of("foo"),
 Optional.empty(), Optional.of("bar"));  
  
 // Java 8:  

   List<String> filteredList = listOfOptionals.stream()
   .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
   .collect(Collectors.toList());

 //Java 9:  
`List<String> filteredList = listOfOptionals.stream()
   .flatMap(Optional::stream)
   .collect(Collectors.toList());
   ```
   
### java.time improvements 
* long dividedBy(Duration divisor)  
* Duration truncatedTo(TemporalUnit unit)
* static Clock systemUTC() -> can be accurate in nanoseconds depending on platform. 
* Stream<LocalDate> datesUntil(LocalDate endExclusive, Period step) && Stream<LocalDate> datesUntil(LocalDate endExclusive) 

### Other small improvements

* Single underscore as identifier is now illegal.

* Try-with-resources improvements
With old approach, new variable was required: 
```java
public void doWithFileInputStream(FileInputStream fis) {
	try(FileInputStream fis2 =fis) {
		fis2.read();
	}
}
```
New approach: 
```java
public void doWithFileInputStream(FileInputStream fis) {
	try(fis) {
		fis.read();
	}
}
``` 
Note: in order to use variable in try statement, it needs to be effectively final. Effectively final variable is variable that either has final keyword or is not reassigned within the method.

* Private interface methods
* Javadoc is HTML5 compliant, Search is added, modules documentation also added
https://docs.oracle.com/en/java/javase/12/docs/api/index.html 
* Localization improvements. Unicode 8.0 support.
* [JVM Compiler Interface](http://openjdk.java.net/jeps/243)
* 

## New api's. Process API and Http2 API

### Process API
java.langProcess Represents native processes created from Java
java.lang.ProcessHandle Represents **any** native process on the operating system 

ProcessHandle.of(123)  
ProcessHandle.info()  
ProcessHandle.allProcesses() -> Returns a stream of a current snapshot of processes state...

Demo:  
[ProcessHandleInfoDemo](src/main/java/com/mbrkljac/afterjavaeight/java9/ProcessHandleInfoDemo.java)
[ProcessHandleDestroyDemo](src/main/java/com/mbrkljac/afterjavaeight/java9/ProcessHandleDestroyDemo.java)  

### HttpClient API
Supports HTTP/2 and WebSocket
Caveat: incubator module (They can change, and are not by default part off jdk.)  
In order to use it pass following flag:  `--add-modules jdk.incubator.httpclient`

### Reactive Streams (Flow API)
![flowApi]

- Not meant as end-user API
- Stream data with support for backpressure 
- Vendor - neutral specification (www.reactive-streams.org)
- Flow API: interfaces added to JDK
- Interoperability for reactive projects like RxJava, Akka Streams 
- HttpClient implements Publisher/Subscriber interfaces 
- java.util.concurrent.Flow support is implemented in RxJava 2, Spring 5, Akka Streams


### StackWalker API

Old way:   
`StackTraceElement[] stackTrace = Thread.getStackTrace()`
- Low performance
- No guarantee all stack elements are returned
- No partial handling possible 

New way: 
```
StackWalker walker = StackWalker.getInstance();
walker.forEach()
```
Demo:  
[StackWalkerDemo](src/main/java/com/mbrkljac/afterjavaeight/java9/StackWalkerDemo.java)

## Performance and security improvements 

### G1 Garbage Collector

GC combinations deprecated in Java 8 were removed. 
CMS Collector is deprecated

* G1 (garbage first) garbage collector is introduced in java 6, and is now default when JVM is running in server mode (as oposed as running in client mode)

To read more about server vs client JVM: https://javapapers.com/core-java/jvm-server-vs-client-mode/ 

Important commands to remember for tuning G1:  

```
--XX:MaxGCPauseMillis=200
--XX:+G1EnableStringDeduplication
```
Concurrent Mark Sweep Heap structure:  
![cms_heap_structure]

G1 Heap structure: 
![g1_heap_structure]

G1 Heap allocation: 
![g1_allocation]
Read more:
https://www.oracle.com/technetwork/tutorials/tutorials-1876574.html 

### String performance
* Compact strings  
   * Lower memory usage without any code changes.  
   * Strings in Java are internally represented by a char[] containing the characters of the String. And, every char is made up of 2 bytes because Java internally uses UTF-16. If a String contains a word in the English language, the leading 8 bits will all be 0 for every char, as an ASCII character can be represented using a single byte.
   * As of Java 9, strings are stored as byte[] and not as char[]
   * Final field `coder` is added. This field preserves the information about encoding. 
   * If encoding is LATIN1, single byte will be used to represent every char. In case of UTF16, 2 bytes will be used.
   * This leads up to 15% less memory consumption.
```java   
       /**
        * The identifier of the encoding used to encode the bytes in
        * {@code value}. The supported values in this implementation are
        *
        * LATIN1
        * UTF16
        *
        * @implNote This field is trusted by the VM, and is a subject to
        * constant folding if String instance is constant. Overwriting this
        * field after construction will cause problems.
        */
       private final byte coder;
```

 
* Concatenation of Strings

Before java9:   

```
String valja = "Lika";
String tuITamo = "Dalmacija";
String nemaRajaBez = valja + tuITamo;
```
Would result in: 
```java
6:  new           #4      // class StringBuilder
9:  dup
10: invokespecial #5      // Method StringBuilder."<init>"
13: aload_1	          // String Lika
14: invokevirtual #6      // Method StringBuilder.append:(LString;)LStringBuilder;             
17: aload_2		  // String Dalmacija
18: invokevirtual #6      // Method StringBuilder.append:(LString;)LStringBuilder;

21: invokevirtual #7      // Method StringBuilder.toString:()LString;
....
```

Java 9: 
```java
6: aload_1		  // String Lika           
7: aload_2		  // String Dalmacija
.....
8: invokedynamic #4, 0    // InvokeDynamic #0:makeConcatWithConstants:
                          // (LString;LString;)LString;

BootstrapMethods:
   0: #19 invokeStatic StringConcatFactory.makeConcatWithConstants:
     (… , … , LMethodType; , LString; , … ) LCallSite;
     Method arguments:
        #20 \u0001\u0001
```
The reason to change the compiler now in this way is, from the project description, to “enable future optimizations of String concatenation without requiring further changes to the bytecode emitted by javac.”. Dynamic method invocation is an ideal solution for that challenge, as it delays method implementation to the runtime. The developers of the Java runtime can then improve the implementation of the factory class, without all other developers needing to recompile their projects. 

https://www.guardsquare.com/en/blog/string-concatenation-java-9-untangling-invokedynamic 

 
### Serialization
There are numerous vulnerabilities related to serialization. Java 9 tries to address this by adding new interface to filter data before deserializing. 
```java
interface ObjectInputFilter {
	Status checkInput(FilterInput filterInput);
	enum Status {
		UNDECIDED,
		ALLOWED,
		REJECTED;
	}
}
```
ObjectInoutStream::setObjectInputFilter -> per stream  
ObjectInputFilter.Config.setSerialFilter -> for all streams  

Alternatively you can filter incoming serialization data without adding or changing code. For this you can use jdk.serialFilter system property. This property is backported to Java 6/7/8

maxbytes=n;
maxarray=n;
maxdepth=n;  
com.hybrid.dto.*; (All types from this package can be serialized)  
!com.someoneelse.*; (No types from this package can be serialized)

## Further reading: 
https://docs.oracle.com/javase/9/whatsnew/toc.htm 
 
[jdkModules]: images/jdkModules.png "JDK platform modules"
[flowApi]: images/FlowAPI.png "Flow API"
[cms_heap_structure]: images/CMS.png "JDK platform modules"
[g1_heap_structure]: images/G1HeapStructure.png "JDK platform modules"
[g1_allocation]: images/G1.png "JDK platform modules"
