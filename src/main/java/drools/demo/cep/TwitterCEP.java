/*
 * Copyright 2011 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package drools.demo.cep;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.io.IOException;

/**
 * TwitterCEP
 */
public class TwitterCEP {

    final static Logger logger = LoggerFactory.getLogger(TwitterCEP.class);

    public static void main(String[] args) throws TwitterException, IOException{
        if( args.length == 0 ) {
            System.out.println("Please pass the name of the demo: demo1, demo2, demo3, demo4 or demo5");
            System.exit( 0 );
        }

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kc = kieServices.getKieClasspathContainer();
        Results results = kc.verify();
        if (results.hasMessages(Message.Level.ERROR)) {
            results.getMessages(Message.Level.ERROR).forEach(m -> logger.error(m.getText()));
            System.exit(0);
        }

        KieBase kieBase = kc.getKieBase(args[0]);

        KieSession ksession = kieBase.newKieSession();

        final EntryPoint ep = ksession.getEntryPoint("twitter");

        // Connects to the twitter stream and register the listener
        new Thread(() -> {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener( new StatusAdapter(){
                @Override
                public void onStatus(Status status) {
                    ep.insert(status);
                }
            } );
            twitterStream.sample();
        }).start();

        Logger cepLogger = LoggerFactory.getLogger("cep");
        ksession.setGlobal("logger", cepLogger);
        ksession.fireUntilHalt();
    }



}
