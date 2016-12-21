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

package org.drools.examples.twittercbr;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.io.IOException;

/**
 * TwitterCBR
 */
public class TwitterCBR {
    public static final boolean disableLog = true;


    public static void main(String[] args) throws TwitterException, IOException{
        if( args.length == 0 ) {
            System.out.println("Please pass the name of the demo: demo1, demo2, demo3, demo4 or demo5");
            System.exit( 0 );
        }

        KieServices kieServices = KieServices.Factory.get();
        KieSession ksession = kieServices.getKieClasspathContainer().getKieBase(args[0]).newKieSession();

        final EntryPoint ep = ksession.getEntryPoint("twitter");

        // Connects to the twitter stream and register the listener 
        new Thread(() -> {
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener( new StatusAdapter(){
                @Override
                public void onStatus(Status status) {
                    super.onStatus(status);
                }
            } );
            twitterStream.sample();
        }).start();
        
        Logger cepLogger = LoggerFactory.getLogger("cep");
        ksession.setGlobal("logger", cepLogger);
        ksession.fireUntilHalt();
    }



}
