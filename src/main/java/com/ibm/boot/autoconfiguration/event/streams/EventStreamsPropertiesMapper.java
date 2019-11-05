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

import java.util.HashMap;
import java.util.Map;

public class EventStreamsPropertiesMapper {
    public Map<String, Object> buildProperties(Map<String, Object> properties, EventStreamsProperties eventStreamsProperties) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(properties);

        map.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, eventStreamsProperties.getSecurityProtocol());

        EventStreamsProperties.Ssl ssl = eventStreamsProperties.getSsl();
        map.put(SaslConfigs.SASL_MECHANISM, ssl.getMechanism());
        map.put(SslConfigs.SSL_PROTOCOL_CONFIG, ssl.getProtocol());

        if (ssl.getTrustStoreLocation() != null && ssl.getTrustStorePassword() != null) {
            map.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, ssl.getTrustStoreLocation());
            map.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, ssl.getTrustStorePassword());
        }

        return map;
    }
}
