FROM adoptopenjdk/openjdk12
COPY /war/richorbroke.war /richorbroke.war
CMD ["java", "-jar", "/richorbroke.war"]