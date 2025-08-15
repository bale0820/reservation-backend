# ---- build stage ----
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
# 실행용 JAR만 만들기
RUN gradle clean bootJar --no-daemon

# ---- run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
# 메모리 안전 옵션(Free 플랜 권장)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"
CMD ["java","-jar","app.jar"]