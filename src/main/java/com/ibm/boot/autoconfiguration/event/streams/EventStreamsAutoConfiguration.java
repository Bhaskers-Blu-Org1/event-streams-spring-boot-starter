/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * © Copyright IBM Corporation 2019.
 * LICENSE: Apache 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */
package com.ibm.boot.autoconfiguration.event.streams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "ibm.spring.event-streams.password")
@EnableConfigurationProperties({EventStreamsProperties.class, KafkaProperties.class})
public class EventStreamsAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(EventStreamsAutoConfiguration.class);

    private final KafkaProperties kafkaProperties;
    private final EventStreamsProperties eventStreamsProperties;

    public EventStreamsAutoConfiguration(EventStreamsProperties eventStreamsProperties, KafkaProperties kafkaProperties) {
        this.eventStreamsProperties = eventStreamsProperties;
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaJaasLoginModuleInitializer kafkaJaasLoginModuleInitializer() throws IOException {
        logger.info("it's working - ibm.spring.event-streams.password");
        Map<String, String> options = new HashMap<>();
        options.put("username", "token");
        options.put("password", eventStreamsProperties.getPassword());
        KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
        jaas.setLoginModule("org.apache.kafka.common.security.plain.PlainLoginModule");
        jaas.setControlFlag(KafkaJaasLoginModuleInitializer.ControlFlag.REQUIRED);
        jaas.setOptions(options);
        jaas.afterSingletonsInstantiated();
        return jaas;
    }

    @Bean
    @ConditionalOnMissingBean(ConsumerFactory.class)
    public DefaultKafkaConsumerFactory<?, ?> kafkaConsumerFactory(EventStreamsPropertiesMapper propertiesMapper) {
        Map<String, Object> configs = propertiesMapper.buildProperties(kafkaProperties.buildConsumerProperties(),
                eventStreamsProperties);
        logger.info("consumer configs = [{}]", configs);
        return new DefaultKafkaConsumerFactory<>(configs);
    }

    @Bean
    @ConditionalOnMissingBean(ProducerFactory.class)
    public DefaultKafkaProducerFactory<?, ?> kafkaProducerFactory(EventStreamsPropertiesMapper propertiesMapper) {
        Map<String, Object> configs = propertiesMapper.buildProperties(kafkaProperties.buildProducerProperties(), eventStreamsProperties);
        logger.info("producer configs =[{}]", configs);

        DefaultKafkaProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configs);
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public KafkaAdmin kafkaAdmin(EventStreamsPropertiesMapper propertiesMapper) {
        Map<String, Object> configs = propertiesMapper.buildProperties(kafkaProperties.buildAdminProperties(), eventStreamsProperties);

        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        kafkaAdmin.setFatalIfBrokerNotAvailable(kafkaProperties.getAdmin().isFailFast());
        return kafkaAdmin;
    }

    @Bean
    EventStreamsPropertiesMapper eventStreamsPropertiesMapper() {
        return new EventStreamsPropertiesMapper();
    }

}
