package com.ibm.boot.autoconfiguration.event.streams;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {EventStreamsAutoConfiguration.class},
        properties = {"ibm.spring.event-streams.password=test"})
public class EventStreamsAutoConfigurationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void ibmKafkaPropertiesAreDefined_springContextIsLoaded_propertiesAreLoadedCorrectly() {
        EventStreamsProperties eventStreamsProperties = applicationContext.getBean(EventStreamsProperties.class);
        assertThat(eventStreamsProperties.getPassword()).isEqualTo("test");
    }

    @Test
    public void eventStreamsPasswordPropertyIsDefined_springContextIsLoaded_kafkaJaasLoginModuleInitializerIsSetCorrectly() {
        KafkaJaasLoginModuleInitializer moduleInitializer = applicationContext.getBean(KafkaJaasLoginModuleInitializer.class);
        assertThat(moduleInitializer).isNotNull();
    }

    @Test
    public void eventStreamsPasswordPropertyIsDefined_springContextIsLoaded_kafkaConsumerFactoryIsSetCorrectly() {
        DefaultKafkaConsumerFactory consumerFactory = applicationContext.getBean(DefaultKafkaConsumerFactory.class);
        Map<String, Object> configurationProperties = consumerFactory.getConfigurationProperties();
        assertThat(configurationProperties.get("sasl.mechanism")).isEqualTo("PLAIN");
        assertThat(configurationProperties.get("security.protocol")).isEqualTo("SASL_SSL");
    }

    @Test
    public void eventStreamsPasswordPropertyIsDefined_springContextIsLoaded_kafkaProducerFactoryIsSetCorrectly() {
        DefaultKafkaProducerFactory producerFactory = applicationContext.getBean(DefaultKafkaProducerFactory.class);
        Map<String, Object> configurationProperties = producerFactory.getConfigurationProperties();
        assertThat(configurationProperties.get("sasl.mechanism")).isEqualTo("PLAIN");
        assertThat(configurationProperties.get("security.protocol")).isEqualTo("SASL_SSL");
    }

    @Test
    public void eventStreamsPasswordPropertyIsDefined_springContextIsLoaded_KafkaAdminIsSetCorrectly() {
        KafkaAdmin kafkaAdmin = applicationContext.getBean(KafkaAdmin.class);
        Map<String, Object> configurationProperties = kafkaAdmin.getConfig();
        assertThat(configurationProperties.get("sasl.mechanism")).isEqualTo("PLAIN");
        assertThat(configurationProperties.get("security.protocol")).isEqualTo("SASL_SSL");
    }

}