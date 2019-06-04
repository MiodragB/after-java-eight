# Changes in licence agreements and release cadence

Starting with Java 11, Oracle will provide JDK releases under the open source GNU General Public License v2, with the Classpath Exception (GPLv2+CPE), and under a commercial license for those using the Oracle JDK as part of an Oracle product or service, or who do not wish to use open source software. This combination of using an open source license and a commercial license replaces the historical “BCL” license, which had a combination of free and paid commercial terms.

Different builds will be provided for each license, but these builds are functionally identical and interchangeable aside from some cosmetic and packaging differences.

Ideally, we would simply refer to all Oracle JDK builds as the “Oracle JDK,” either under the GPL or the commercial license depending on your situation.  However, for historical reasons while the small remaining differences exist, we will refer to them separately as Oracle’s OpenJDK builds, and the Oracle JDK.  

We should emphasize that the OpenJDK is an official reference implementation of a Java Standard Edition since version SE 7.

## Licence differences

**Oracle OpenJDK** is released under [GPLv2+CPE](http://openjdk.java.net/legal/gplv2+ce.html) same as all other Oracle Java-related open source products.   
It clearly states that the code under the license is (L)GPL, but anything using that code can follow whatever license they'd like. No ifs, ands, or buts. If you change the core code (e.g. fixing bugs), you do still have to release those changes as part of the GPL. But using does NOT affect you. Classpath Exception is a much different than LGPL. It is a legally clean way to draw a bright line allowing non-GPL use of GPL or LGPL source code or libraries.  
Oracle OpenJDK builds are available at [jdk.java.net](https://jdk.java.net/)     

**Oracle JDK** is released under [OTN License Agreement for Java SE](https://www.oracle.com/technetwork/java/javase/terms/license/javase-license.html). This license permits personal use, development, testing, prototyping, demonstrating and some other uses at no cost. 
Oracle JDK can be downloaded from [oracle.com](https://www.oracle.com/technetwork/java/javase/downloads/index.html) among others... 

## Functional differences 

Since java 9 there was a continuous effort to make OpenJDK and Oracle JDK functionally identical. As a part of that effort Oracle open sourced “commercial features” that were not available in OpenJDK builds. These features include:  
Java Flight Recorder, Java Mission Control, Application Class-Data Sharing, and ZGC.
From Java 11 forward, therefore, Oracle JDK builds and OpenJDK. 

There do remain a small number of differences, some intentional and cosmetic, and some simply because more time to discuss with OpenJDK contributors is warranted. 

* Oracle JDK 11 emits a warning when using the -XX:+UnlockCommercialFeatures option, whereas in OpenJDK builds this option results in an error. This option was never part of OpenJDK and it would not make sense to add it now, since there are no commercial features in OpenJDK. This difference remains in order to make it easier for users of Oracle JDK 10 and earlier releases to migrate to Oracle JDK 11 and later. 
* Oracle JDK 11 can be configured to provide usage log data to the “Advanced Management Console” tool, which is a separate commercial Oracle product.  This difference remains primarily to provide a consistent experience to Oracle customers until decisions what to do with this feature are made.
* The javac --release command behaves differently for the Java 9 and Java 10 targets, since in those releases the Oracle JDK contained some additional modules that were not part of corresponding OpenJDK releases:
javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media, javafx.web, java.jnlp, jdk.jfr, jdk.management.cmm, jdk.management.jfr, jdk.management.resource, jdk.packager.services, jdk.snmp
This difference remains in order to provide a consistent experience for specific kinds of legacy use. These modules are either now available separately as part of OpenJFX, are now in both OpenJDK and the Oracle JDK because they were commercial features which Oracle contributed to OpenJDK (e.g., Flight Recorder), or were removed from Oracle JDK 11 (e.g., JNLP).
* The output of the java --version and java -fullversion commands will distinguish Oracle JDK builds from OpenJDK builds, so that support teams can diagnose any issues that may exist.  
* The Oracle JDK has always required third party cryptographic providers to be signed by a known certificate.  The cryptography framework in OpenJDK has an open cryptographic interface, meaning it does not restrict which providers can be used.  Oracle JDK 11 will continue to require a valid signature, and Oracle OpenJDK builds will continue to allow the use of either a valid signature or unsigned third party crypto provider. 
* Oracle JDK 11 will continue to include installers, branding and JRE packaging for an experience consistent with legacy desktop uses.  Oracle OpenJDK builds are currently available as zip and tar.gz files, while alternative distribution formats are being considered.

**How Oracle JDK and OpenJDK is kept in Sync?**  
All of the development and bug fixes happens in OpenJDK and then they are propagated to the Oracle JDK. Security fixes happens in private forest without public code reviews unlike general fixes, then they are pushed to Oracle JDK and then to OpenJDK.
A key point to grasp is that most JDK builds in the world are based on the open source OpenJDK project. The Oracle JDK is merely one of many builds that are based on the OpenJDK codebase. While it used to be the case that Oracle had additional extras in their JDK, as of Java 11 this is no longer the case. 

## Release cadence 
Oracle has proposed a time-driven release model for Java SE instead of the historical feature-driven model.   
Every 6 months (March and September), new release will be made available. Every 3 years LTS release will be published. 
Going from Java 9->10->11 is closer to going from 8->8u20->8u40 than from 7->8->9.  It’s scary to see at first when you’re used to major releases about every three years and have a mental model of the huge impact of those major changes.  The six-month cadence is not that.


Oracle JDK release plan: 

| Release | GA Date | Premier Support Until |Extended Support Until|
| :-----: |:-------:| :---------------------| :--------------------|
|8        |March 2014|	March 2022	        |March 2025	|
|9 (non‑LTS)|September 2017|March 2018	|Not Available	|
|10 (non‑LTS)|	March 2018|	September 2018	|Not Available	|
|11 (LTS)|	September 2018|	September 2023	|September 2026	|
|12 (non‑LTS)|	March 2019|	September 2019	|Not Available	|
|13 (non‑LTS)|	September 2019***|March 2020|Not Available|


### Versioning
Now, though, the new numbering system has semantic meaning. Basically, it is:

$FEATURE.$INTERIM.$UPDATE.$PATCH
FEATURE refers to the version of Java. So, in Java 10’s case, FEATURE is 10. (Makes sense!) It will increment every six-months, matching the new Java release cycle.

INTERIM is actually reserved for future “interim” cycles. For example, if Java wanted to start releasing faster than every six months. For the time being, it will always be 0.

UPDATE is a bit odd. It begins at 0 and one month after the last FEATURE release, it bumps up to 1. And then it increments every three months after that. So, that means that with Java 10, in April 2018, UPDATE was 1. In July 2018, it is 2, and in September, it is 3, incrementing until Java 10 is EOL.

PATCH is any releases that need to happen in between UPDATE increments, for example, critical bug fixes.

Additionally, version numbers remove trailing zeros.

So, that means that the version string when Java 10 went live was simply 10.

In April, Oracle released 10.0.1 and in July, it released 10.0.2. 

[Read more](http://openjdk.java.net/jeps/3)

   
### Support

Oracle announces that the last version, which will be released to Oracle JDK 8 before the license requirement is introduced, will be version 1.8.0_201 (also 1.8.0_202 u works without license fee), but not the release of the 16th April.

The Oracle Java 8u211/8u212 (1.8.0_211u and 1.8.0_212u) with release date 16th April will result in a need for a commercial Oracle Java license.

Anyone who wishes to continue using Oracle JDK 8 with access to updates must pay a license fee to Oracle. To continue using Oracle JDK 8 without paying such a license does not mean you violate the license or that it ceases to work. However, you will not get any updates on Java 8, which in practice means that version reach end of public updates.

While a paid long-term support release will occur every three years for Oracle JDK, every OpenJDK release will only receive updates for six months. Security patches and bug fixes will only be contributed in source form to the latest version of the OpenJDK, leaving older versions in the hands of the Java community.
In other words, Oracle is moving from a two-year release cycle to a six-month release cycle, and each OpenJDK release will only be supported until the next one is made available

For the first 6 months of Java 11’s life, Oracle will be providing GPL+CE licensed $free zero-cost downloads at jdk.java.net with security patches. To get GPL+CE licensed $free zero-cost update releases of Java 11 after the first six months, you are likely to need to obtain them from a different URL and a different build team.  

Since Oracle will no longer produce public updates for their JDK, they are also no longer actively working on OpenJDK 8. Just as with the Java 6 and 7 versions of OpenJDK, Red Hat will probably take the lead and maintain the codebase  in cooperation with other vendors like IBM, Amazon, and Azul. RedHat maintenance for OpenJDK 8 is planned for at least another 4 years, until September 2023.   
From RedHat official statement:   
“With the help of the wider OpenJDK community and my team at Red Hat, we have continued to provide updates for critical bugs and security vulnerabilities at regular intervals. I can see no reason why this process should not work in the same way for OpenJDK 8 and the next long-term support release, OpenJDK 11.”

