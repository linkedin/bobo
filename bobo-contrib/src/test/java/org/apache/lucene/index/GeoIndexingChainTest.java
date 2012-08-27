/**
 * This software is licensed to you under the Apache License, Version 2.0 (the
 * "Apache License").
 *
 * LinkedIn's contributions are made under the Apache License. If you contribute
 * to the Software, the contributions will be deemed to have been made under the
 * Apache License, unless you expressly indicate otherwise. Please do not make any
 * contributions that would be inconsistent with the Apache License.
 *
 * You may obtain a copy of the Apache License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, this software
 * distributed under the Apache License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Apache
 * License for the specific language governing permissions and limitations for the
 * software governed under the Apache License.
 *
 * © 2012 LinkedIn Corp. All Rights Reserved.  
 */

package org.apache.lucene.index;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.lucene.index.DocumentsWriter.IndexingChain;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;


/**
 * @author Geoff Cooney
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration( { "/TEST-servlet.xml" }) 
@IfProfileValue(name = "test-suite", values = { "unit", "all" })
public class GeoIndexingChainTest {
    private final Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);  
    }};
    
    private IndexingChain mockIndexingChain;
    private DocConsumer mockDocConsumer;
    
    private GeoIndexingChain geoIndexingChain;

    //@Resource(type = GeoSearchConfig.class)
    GeoSearchConfig config = new GeoSearchConfig();
    
    @Before
    public void setUp() {
        mockIndexingChain = context.mock(IndexingChain.class);
        mockDocConsumer = context.mock(DocConsumer.class);
        
        geoIndexingChain = new GeoIndexingChain(config, mockIndexingChain);
    }
    
    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }
    
    @Test
    public void testGetChain() {
        context.checking(new Expectations() {
            {
                one(mockIndexingChain).getChain(with(aNull(DocumentsWriter.class)));
                will(returnValue(mockDocConsumer));
            }
        });
        
        DocConsumer docConsumer = geoIndexingChain.getChain(null);
        assertTrue("Expected a GeoDocConsumer", docConsumer instanceof GeoDocConsumer);
        GeoDocConsumer geoDocConsumer = (GeoDocConsumer)docConsumer;
        assertSame("GeoDocConsumer's default consumer was not set correctly", 
                mockDocConsumer, geoDocConsumer.getDefaultDocConsumer());
    }
                                
}
