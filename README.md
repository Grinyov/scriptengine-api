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
   <li> Package project: mvn package -Dmaven.test.skip=true </li>
   <li> Create docker image: mvn dockerfile:build </li>
   <li>Run: docker run -p 8080:8080 -t "imagename"</li>
   <li>Detail: https://github.com/spotify/dockerfile-maven</li>
 </ul>


