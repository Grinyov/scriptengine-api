REST API for javax.body.ScriptEngine.

The API allows running scripts in a separate thread. And also their process and interrupt if necessary

TODO add requirements here

<h4> Technologies</h4>
 <ul>
   <li>Java 1.8</li>
   <li>Maven</li>
   <li>Spring Boot
     <ul>
        <li>core</li>
        <li>web</li>
        <li>hateoas</li>
     </ul>
   </li>
   <li>Spring Data Rest</li>
   <liHAL browser</li>
 </ul>

<h4> Docker</h4>
 <ul>
   <li>Build project with maven</li>
   <li> Create docker image</li>
   <li>Run: docker run -p 8080:8080 -t <imagename></li>