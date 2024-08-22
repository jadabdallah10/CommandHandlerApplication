# Project README

## Overview

This project is a dynamic web application developed using Eclipse and deployed on Tomcat 7.0. Below are the details of the JAR dependencies used in the project and the configuration properties.

## Dependencies

### `WEB-INF/lib` JARs

These JAR files are included in the `WEB-INF/lib` directory:

- `activation-1.1.jar`
- `asm-5.0.4.jar`
- `commons-logging-1.2.jar`
- `cxf-core-3.1.11.jar`
- `cxf-rt-bindings-soap-3.1.11.jar`
- `cxf-rt-bindings-xml-3.1.11.jar`
- `cxf-rt-databinding-jaxb-3.1.11.jar`
- `cxf-rt-frontend-jaxws-3.1.11.jar`
- `cxf-rt-frontend-simple-3.1.11.jar`
- `cxf-rt-transports-http-3.1.11.jar`
- `cxf-rt-ws-addr-3.1.11.jar`
- `cxf-rt-wsdl-3.1.11.jar`
- `cxf-rt-ws-policy-3.1.11.jar`
- `jackson-annotations-2.17.0.jar`
- `jackson-core-2.17.0.jar`
- `jackson-databind-2.17.0.jar`
- `jackson-dataformat-xml-2.17.0.jar`
- `javax.activation-api-1.2.0.jar`
- `javax.annotation-3.1.1.jar`
- `jaxb-api-2.3.1.jar`
- `jaxb-core-2.2.11.jar`
- `jaxb-impl-2.2.11.jar`
- `jaxws-api-2.2.6.jar`
- `jsr181-api-1.0-MR1.jar`
- `neethi-3.0.3.jar`
- `saaj-api-1.3.4.jar`
- `spring-aop-4.3.8.RELEASE.jar`
- `spring-beans-4.3.8.RELEASE.jar`
- `spring-context-4.3.8.RELEASE.jar`
- `spring-core-4.3.8.RELEASE.jar`
- `spring-expression-4.3.8.RELEASE.jar`
- `spring-web-4.3.8.RELEASE.jar`
- `stax2-api-3.1.4.jar`
- `woodstox-core-asl-4.4.1.jar`
- `wsdl4j-1.6.3.jar`
- `xml-resolver-1.2.jar`
- `xmlschema-core-2.2.1.jar`

### Java Resources JARs

These JAR files are included in the Java resources directory:

- `commons-collections4-4.4.jar`
- `commons-compress-1.26.2.jar`
- `commons-io-2.16.1.jar`
- `commons-logging-1.2.jar`
- `jackson-annotations-2.17.0.jar`
- `jackson-core-2.17.0.jar`
- `jackson-databind-2.17.0.jar`
- `jackson-dataformat-xml-2.17.0.jar`
- `jaxws-api-2.2.6.jar`
- `javax.servlet-3.1.jar`
- `json-20240303.jar`
- `log4j-1.2.15.jar`
- `log4j-api-2.20.0.jar`
- `log4j-core-2.20.0.jar`
- `servlet-api.jar`

### Tomcat 7.0 JARs

These JAR files are provided by Tomcat 7.0:

- `catalina-ant.jar`
- `catalina-ha.jar`
- `catalina-tribes.jar`
- `commons-collections4-4.4.jar`
- `commons-compress-1.26.2.jar`
- `commons-io-2.16.1.jar`
- `commons-logging-1.2.jar`
- `ecj-4.2.2.jar`
- `ecj-4.4.2.jar`
- `ecj-P20140317-1600.jar`
- `el-api.jar`
- `jackson-annotations-2.17.0.jar`
- `jackson-core-2.17.0.jar`
- `jackson-databind-2.17.0.jar`
- `jackson-dataformat-xml-2.17.0.jar`
- `jasper.jar`
- `jasper-el.jar`
- `javax.servlet-3.1.jar`
- `javax.servlet-api-4.0.1.jar`
- `json-20230618.jar`
- `jsp-api.jar`
- `log4j.properties`
- `log4j-1.2.15.jar`
- `orai18n-19.3.0.0.jar`
- `poi-4.1.2.jar`
- `poi-ooxml-4.1.2.jar`
- `poi-ooxml-schemas-4.1.2.jar`
- `poi-scratchpad-4.1.2.jar`
- `servlet-api.jar`
- `SparseBitSet-1.2.jar`
- `tomcat7-websocket.jar`
- `tomcat-api.jar`
- `tomcat-coyote.jar`
- `tomcat-dbcp.jar`
- `tomcat-i18n-de.jar`
- `tomcat-i18n-es.jar`
- `tomcat-i18n-fr.jar`
- `tomcat-i18n-ja.jar`
- `tomcat-i18n-ko.jar`
- `tomcat-i18n-ru.jar`
- `tomcat-i18n-zh-CN.jar`
- `tomcat-jdbc.jar`
- `tomcat-util.jar`
- `websocket-api.jar`
- `xmlbeans-3.1.0.jar`
- `annotations-api.jar`
- `catalina.jar`

## Configuration Properties

The following properties are used for configuring the application:

```properties
aes.key=KEY_64
aes.IV=IV_HEX_32
log.path=PATH_ON_LAPTOP
