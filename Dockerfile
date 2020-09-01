### Build frontend
FROM node:12.18.3-stretch-slim AS frontend
MAINTAINER Jonathan Di Trapani
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend/.prettierrc.json frontend/tsconfig.json ./
COPY frontend/public public/
COPY frontend/src src/
RUN npm run pipeline

### Build backend
FROM gradle:6.5.1-jdk11 AS backend
COPY backend/build.gradle.kts backend/settings.gradle.kts ./
COPY backend/src src/
COPY --from=frontend build/ src/main/resources/webroot/
RUN gradle ktlintCheck test assembleDist installDist

### Release
FROM openjdk:11.0.8-jre-slim-buster AS release
COPY --from=backend /home/gradle/build/install/ ./
EXPOSE 44777
CMD [ "./blue/bin/blue" ]
