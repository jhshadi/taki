FROM arm32v7/openjdk:8

ENV PORT 7080
ENV WORKING_DIR /home

COPY ../taki ${WORKING_DIR}/taki

WORKDIR ${WORKING_DIR}/taki
RUN ./gradlew deploy

WORKDIR ${WORKING_DIR}/taki/deploy/
ENTRYPOINT ["./bin/taki"]
CMD ["--server.port=${PORT}", "-J-Xms128M", "-J-Xmx128M"]