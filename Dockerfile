FROM arm32v7/openjdk:8

WORKDIR /home/
RUN git clone https://github.com/jhshadi/taki.git

WORKDIR /home/taki
RUN ./gradlew deploy

WORKDIR /home/taki/deploy/
ENTRYPOINT ["./bin/taki"]
CMD ["--server.port=${PORT}", "-J-Xms128M", "-J-Xmx128M"]