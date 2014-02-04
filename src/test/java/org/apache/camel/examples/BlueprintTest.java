/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.examples;

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://www.christianposta.com/blog">Christian Posta</a>
 */
public class BlueprintTest extends CamelBlueprintTestSupport {

    BrokerService brokerService;

    @Test
    public void testMessaging() {
        NotifyBuilder builder = new NotifyBuilder(context).whenCompleted(10).create();
        builder.matches(60, TimeUnit.SECONDS);
    }

    @Override
    public void setUp() throws Exception {
        startBroker();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        stopBroker();
    }

    private void stopBroker() throws Exception {
        this.brokerService.stop();
        this.brokerService.waitUntilStopped();
    }

    private void startBroker() throws Exception {
        this.brokerService = new BrokerService();
        this.brokerService.setDeleteAllMessagesOnStartup(true);
        this.brokerService.getPersistenceAdapter().setDirectory(new File("./target/activemq-data"));
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.addConnector("mqtt://localhost:1883");
        brokerService.addConnector("stomp://localhost:61613");
        brokerService.addConnector("amqp://localhost:5672");
        brokerService.start();
        brokerService.waitUntilStarted();
    }

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/blueprint.xml";
    }
}
