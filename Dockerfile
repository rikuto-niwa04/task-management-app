# ==============================
# Build stage
# ==============================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle Wrapper関連を先にコピー
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Windowsの改行コード対策と実行権限付与
RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew

# 依存関係を先に取得してDockerキャッシュを利用
RUN ./gradlew dependencies --no-daemon

# アプリケーションのソースコードをコピー
COPY src src

# テストを実行して実行可能Jarを作成
RUN ./gradlew clean bootJar --no-daemon


# ==============================
# Runtime stage
# ==============================
FROM eclipse-temurin:17-jre

WORKDIR /app

# builderで作成したJarをコピー
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]