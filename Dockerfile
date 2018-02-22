FROM hypriot/rpi-java:latest

WORKDIR /home/

RUN update-ca-certificates -f
RUN apt-get update && apt-get install -y git
RUN git clone https://github.com/jhshadi/taki.git

WORKDIR /home/taki
RUN ./gradlew deploy

WORKDIR /home/taki/deploy/
ENTRYPOINT ["./bin/taki", "--server.port=\${PORT}", "-J-Xms128M", "-J-Xmx128M"]