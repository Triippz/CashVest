# CashVest

CashVest is a Java Spring/JavaFX desktop application for managing and building investment portfolios. It was created for IST 261 at Penn State.

## Build

Use maven to build a fat jar.

```bash
mvn package
```

## Running

```bash
./CashVest.jar
```

## Notes
The database we are utilizing is a file-based H2 DB.
Right now, the database is configured to utilize the target directory in a unix-based env.
If you are having liquibase db creation issues and are on Windows. Change the 
file separators to Windows-based ones.

## Scripts
The management script to package, clean, or test the application is located in ``/bin``

``./bin/manage.sh``

## Dependencies

<table>

<tbody>

<tr>

<th>Dependency</th>

<th>Version</th>

<th>License</th>

</tr>

<tr>

<td>Spring Framework</td>

<td>2.2.3.Release</td>

<td>Apache-2.0</td>

</tr>

<tr>

<td>Liquibase</td>

<td>3.8.6</td>

<td>Apache-2.0</td>

</tr>

<tr>

<td>OpenJFX</td>

<td>11</td>

<td>GPL 2.0</td>

</tr>

<tr>

<td>Apache Commons IO</td>

<td>2.6</td>

<td>Apache-2.0</td>

</tr>

<tr>

<td>H2 Database Engine</td>

<td>1.4.200</td>

<td>EPL 1.0, MPL 2.0</td>

</tr>

<tr>

<td>Apache Commons Lang</td>

<td>3.9</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>Project Lombok</td>

<td>1.18.10</td>

<td>MIT</td>

</tr>

<tr>

<td>JUnit Jupiter Engine</td>

<td>5.6.0</td>

<td>EPL 2.0</td>

</tr>

<tr>

<td>AssetJ Fluent Assertions</td>

<td>3.14.0</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>ArchUnit</td>

<td>0.13.0</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>Reactor Test Support</td>

<td>3.3.2.RELEASE</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>JFoeniX</td>

<td>9.0.9</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>ControlsFX</td>

<td>11.0.1</td>

<td>BSD 3-clause</td>

</tr>

<tr>

<td>Apache Commons Validator</td>

<td>1.6</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>IEXTrading4J All</td>

<td>3.3.2</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>Libphonenumber</td>

<td>8.11.2</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>Kotlin Standard Library JDK 8</td>

<td>1.3.61</td>

<td>Apache 2.0</td>

</tr>

<tr>

<td>Kotlin Test</td>

<td>1.3.61</td>

<td>Apache 2.0</td>

</tr>

</tbody>

</table>
        
        
## License
[MIT](https://choosealicense.com/licenses/mit/)
