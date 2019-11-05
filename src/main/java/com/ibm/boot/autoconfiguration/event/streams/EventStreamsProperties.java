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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "ibm.spring.event-streams")
public class EventStreamsProperties {
    private final String securityProtocol = "SASL_SSL";
    private String password = null;
    private Ssl ssl = new Ssl();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public Ssl getSsl() {
        return ssl;
    }

    public void setSsl(Ssl ssl) {
        this.ssl = ssl;
    }

    public static class Ssl {
        private final String protocol = "TLSv1.2";
        private final String mechanism = "PLAIN";
        private Resource trustStoreLocation;
        private String trustStorePassword;

        public Resource getTrustStoreLocation() {
            return this.trustStoreLocation;
        }

        public void setTrustStoreLocation(Resource trustStoreLocation) {
            this.trustStoreLocation = trustStoreLocation;
        }

        public String getTrustStorePassword() {
            return this.trustStorePassword;
        }

        public void setTrustStorePassword(String trustStorePassword) {
            this.trustStorePassword = trustStorePassword;
        }

        public String getProtocol() {
            return protocol;
        }

        public String getMechanism() {
            return mechanism;
        }

    }
}
