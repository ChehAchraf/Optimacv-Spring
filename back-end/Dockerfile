# ───── Stage 1: Build ─────
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copy Maven wrapper + pom first for dependency caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# ───── Stage 2: Runtime ─────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the fat jar
COPY --from=builder /app/target/*.jar app.jar

# Upload directory (writable by appuser)
RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
