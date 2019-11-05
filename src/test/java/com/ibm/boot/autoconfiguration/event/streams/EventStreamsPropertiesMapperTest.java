/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * © Copyright IBM Corporation 2019, 2020.
 * LICENSE: Apache 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */
package com.ibm.boot.autoconfiguration.event.streams;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EventStreamsPropertiesMapperTest {
    EventStreamsPropertiesMapper eventStreamsPropertiesMapper = new EventStreamsPropertiesMapper();

    @Test
    public void passwordIsSet_propertiesMapped_defaultPropertiesSetCorrectly() {
        EventStreamsProperties eventStreamsProperties = new EventStreamsProperties();
        eventStreamsProperties.setPassword("12345");
        Map<String, Object> buildProperties = eventStreamsPropertiesMapper.buildProperties(new HashMap<>(), eventStreamsProperties);

        assertThat(buildProperties.get(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG)).isEqualTo(eventStreamsProperties.getSecurityProtocol());
        assertThat(buildProperties.get(SaslConfigs.SASL_MECHANISM)).isEqualTo(eventStreamsProperties.getSsl().getMechanism());
        assertThat(buildProperties.get(SslConfigs.SSL_PROTOCOL_CONFIG)).isEqualTo(eventStreamsProperties.getSsl().getProtocol());
    }

    @Test
    public void sslSetupCorrectly_propertiesMapped_SSLsetup() {
        EventStreamsProperties eventStreamsProperties = new EventStreamsProperties();
        EventStreamsProperties.Ssl ssl = eventStreamsProperties.getSsl();
        ssl.setTrustStoreLocation(Mockito.mock(Resource.class));
        ssl.setTrustStorePassword("test");
        Map<String, Object> buildProperties = eventStreamsPropertiesMapper.buildProperties(new HashMap<>(), eventStreamsProperties);

        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG)).isNotNull();
        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG)).isEqualTo("test");
    }

    @Test
    public void noTrustStorePasswordOrLocationSet_propertiesMapped_SSLnotSetup() {
        Map<String, Object> buildProperties = eventStreamsPropertiesMapper.buildProperties(new HashMap<>(), new EventStreamsProperties());

        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG)).isNull();
        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG)).isNull();
    }

    @Test
    public void onlyTrustStorePasswordSet_propertiesMapped_SSLnotSetup() {
        EventStreamsProperties eventStreamsProperties = new EventStreamsProperties();
        EventStreamsProperties.Ssl ssl = eventStreamsProperties.getSsl();
        ssl.setTrustStorePassword("test");
        Map<String, Object> buildProperties = eventStreamsPropertiesMapper.buildProperties(new HashMap<>(), new EventStreamsProperties());

        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG)).isNull();
        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG)).isNull();
    }

    @Test
    public void onlyTrustStoreLocationSet_propertiesMapped_SSLnotSetup() {
        EventStreamsProperties eventStreamsProperties = new EventStreamsProperties();
        EventStreamsProperties.Ssl ssl = eventStreamsProperties.getSsl();
        ssl.setTrustStoreLocation(Mockito.mock(Resource.class));
        Map<String, Object> buildProperties = eventStreamsPropertiesMapper.buildProperties(new HashMap<>(), new EventStreamsProperties());

        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG)).isNull();
        assertThat(buildProperties.get(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG)).isNull();
    }

}