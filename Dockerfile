FROM gradle:8.8.0-jdk17 AS build

LABEL authors=["odeyalo"]

COPY . /sonata/piano

WORKDIR /sonata/piano

CMD ["gradle", "bootRun"]