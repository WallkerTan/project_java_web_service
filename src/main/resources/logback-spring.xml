<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <property name="LOG_PATH" value="logs"/>
    <property name="APP_NAME" value="hospital"/>

    <property name="CONSOLE_PATTERN"
              value="%d{HH:mm:ss} %-5level %-35.35logger{35} : %msg%n"/>

    <property name="FILE_PATTERN"
              value="%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{35} : %msg%n"/>

    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/${APP_NAME}.log</file>
        <encoder>
            <pattern>${FILE_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${APP_NAME}-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <totalSizeCap>500MB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <appender name="FILE_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/${APP_NAME}-error.log</file>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
        <encoder>
            <pattern>${FILE_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${APP_NAME}-error-%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>60</maxHistory>
            <totalSizeCap>200MB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <logger name="org.hibernate"                level="WARN"/>
    <logger name="org.hibernate.SQL"            level="WARN"/>
    <logger name="org.springframework.security" level="WARN"/>
    <logger name="org.springframework"          level="INFO"/>
    <logger name="com.zaxxer.hikari"            level="WARN"/>

    <logger name="com.example.hospital" level="DEBUG" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="FILE_ERROR"/>
    </logger>

    <springProfile name="dev">
        <logger name="org.hibernate.SQL"           level="DEBUG"/>
        <logger name="org.hibernate.orm.jdbc.bind" level="TRACE"/>
    </springProfile>

    <springProfile name="prod">
        <logger name="com.example.hospital" level="INFO"/>
    </springProfile>

    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="FILE_ERROR"/>
    </root>

</configuration>