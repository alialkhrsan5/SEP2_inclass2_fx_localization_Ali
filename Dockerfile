FROM eclipse-temurin:21-jdk

WORKDIR /app

# GUI libraries needed by JavaFX
RUN apt-get update && apt-get install -y \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    fontconfig fonts-noto-core fonts-ipafont-gothic \
    && rm -rf /var/lib/apt/lists/*

# JavaFX SDK for Linux
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

COPY target/temp_fx_localization.jar app.jar

ENV DISPLAY=host.docker.internal:0.0
ENV DB_HOST=host.docker.internal

CMD ["java", \
     "--module-path", "/javafx-sdk/lib", \
     "--add-modules", "javafx.controls,javafx.fxml", \
     "-Dprism.order=sw", \
     "-Dfile.encoding=UTF-8", \
     "-jar", "app.jar"]

# How to run:
# mvn clean package
# docker build -t alii123x/temp-fx-localization .
# docker run --rm -e DISPLAY=host.docker.internal:0.0 alii123x/temp-fx-localization
