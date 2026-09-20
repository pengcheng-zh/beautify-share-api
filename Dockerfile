# 使用官方java运行时环境作为父镜像
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 作者信息
LABEL authors="pacal"

# 添加Mavan构建的jar包到容器中
COPY beautiful-share-front/target/beautiful-share-front-2.0.0.jar app.jar

# 指定容器启动时执行的命令
ENTRYPOINT ["java", "-jar", "app.jar"]