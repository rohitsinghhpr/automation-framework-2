### To pass the TestNG suite file name dynamically from the Maven command line

``<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>${suiteFile}</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>``

### set a default value for suiteFile in your <properties> section (optional): 

``<properties>
    <suiteFile>testng.xml</suiteFile>
</properties>
``

###  Pass it from Maven command line

``mvn clean test -DsuiteFile=smoke-suite.xml``
``mvn clean test -DsuiteFile=src/test/resources/testng/smoke-suite.xml``

### Pass Parameters via Maven Command Line

``mvn clean test -Dbrowser=chrome -Denv=qa -Dusername=admin -Dpassword=secret``
``mvn clean test "-DsuiteFile=regression.xml"``

### Define Custom Parameters in pom.xml
You can define properties in the `<properties>` section:

``<properties>
    <browser>chrome</browser>
    <env>qa</env>
    <username>admin</username>
    <password>secret</password>
    <suiteFile>testng.xml</suiteFile>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
</properties>``

### Override Properties from Maven Command Line

`mvn clean test -DBROWSER=firefox -DHEADLESS=false -DBASE_URL=https://example.com`

``<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <systemPropertyVariables>
            <BROWSER>${BROWSER}</BROWSER>
            <HEADLESS>${HEADLESS}</HEADLESS>
            <BASE_URL>${BASE_URL}</BASE_URL>
        </systemPropertyVariables>
    </configuration>
</plugin>``

| Source              | Priority | Used By                |
| ------------------- | -------- | ---------------------- |
| `mvn -D...` flags   | High     | `System.getProperty()` |
| `config.properties` | Default  | Fallback if no `-D`    |

adding this regaridn loombook issue in maven 


``<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <version>1.18.28</version>
  <scope>provided</scope>
</dependency>
``
