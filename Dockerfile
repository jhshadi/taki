FROM eclipse-temurin:17-jre

ENV PORT 9000
ENV WORKING_DIR /opt/app

RUN mkdir ${WORKING_DIR}
COPY ./build/install/* ${WORKING_DIR}

WORKDIR ${WORKING_DIR}

ENTRYPOINT ["./bin/taki"]
CMD ["--server.port=${PORT}", "-J-Xms128M", "-J-Xmx128M"]