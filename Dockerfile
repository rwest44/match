#Docker镜像依赖的基础镜像
FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.

#指定镜像的工作目录
WORKDIR /app

#把本地项目的代码复制到容器中
COPY pom.xml .
COPY src ./src
COPY user-center-0.0.1-SNAPSHOT.jar .

# Build a release artifact.
#执行打包
RUN mvn package -DskipTests

# Run the web service on container startup.
#运行镜像时默认执行该命令
CMD ["java","-jar","/app/target/user-center-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]