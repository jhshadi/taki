FROM hypriot/rpi-java:latest

WORKDIR /home/

RUN apt-get update && apt-get install -y git
RUN update-ca-certificates -f

RUN git clone https://github.com/jhshadi/taki.git

WORKDIR /home/taki
RUN ./gradlew distTar

WORKDIR /home/taki/build/distributions/

ENV TAKI_DIST taki-dist
RUN mkdir ${TAKI_DIST} && tar -xvf taki*.tar -C ${TAKI_DIST} --strip-components=1

ENTRYPOINT ["./${TAKI_DIST}/taki", "--server.port=\${PORT}", "-J-Xms128M", "-J-Xmx128M"]