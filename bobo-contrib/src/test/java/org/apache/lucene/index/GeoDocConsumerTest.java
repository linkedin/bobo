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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.browseengine.bobo.geosearch.bo.GeoSearchConfig;
import com.browseengine.bobo.geosearch.index.IGeoIndexer;

/**
 * @author Geoff Cooney
 */
//@RunWith(SpringJUnit4ClassRunner.class) 
//@ContextConfiguration( { "/TEST-servlet.xml" }) 
//@IfProfileValue(name = "test-suite", values = { "unit", "all" })
public class GeoDocConsumerTest {
    private final Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);  
    }};
    
    private DocConsumer mockDocConsumer;
    private DocConsumerPerThread mockDocConsumerPerThread;
    
    private IGeoIndexer mockGeoIndexer;
    
    private GeoDocConsumer geoDocConsumer;

    DocumentsWriterThreadState documentsWriterThreadState;
    DocumentsWriter documentsWriter;
    
    final int docID = 10; 
    Document document;
    
    Version matchVersion = Version.LUCENE_CURRENT;
    Analyzer analyzer = new StandardAnalyzer(matchVersion);
    Directory directory;
    IndexWriter writer;
    FieldInfos fieldInfos;
    BufferedDeletesStream bufferedDeletesStream;

    //@Resource(type = GeoSearchConfig.class)
    GeoSearchConfig config = new GeoSearchConfig();
    
    @Before
    public void setUp() throws IOException {
        mockDocConsumer = context.mock(DocConsumer.class);
        mockDocConsumerPerThread = context.mock(DocConsumerPerThread.class);
        
        mockGeoIndexer = context.mock(IGeoIndexer.class);
        
        geoDocConsumer = new GeoDocConsumer(config, mockDocConsumer);
        geoDocConsumer.setGeoIndexer(mockGeoIndexer);
        
        documentsWriter = buildDocumentsWriter();
        
        document = buildDocument();
        
        documentsWriterThreadState = new DocumentsWriterThreadState(documentsWriter);
        documentsWriterThreadState.docState.doc = document;
        documentsWriterThreadState.docState.docID = docID;
    }
    
    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }
    
    private DocumentsWriter buildDocumentsWriter() throws IOException {
        analyzer = new StandardAnalyzer(matchVersion);
        IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
        Directory directory = context.mock(Directory.class);
        writer = context.mock(IndexWriter.class);
        FieldInfos fieldInfos = new FieldInfos();
        BufferedDeletesStream bufferedDeletesStream = context.mock(BufferedDeletesStream.class);
        
        DocumentsWriter documentsWriter = new DocumentsWriter(config, directory, 
                writer, fieldInfos, bufferedDeletesStream);
        return documentsWriter;
    }
    
    private Document buildDocument() {
        Document document = new Document();
        document.add(new Field("text", "my text".getBytes()));
        document.add(new Field("text", "more text".getBytes()));
        document.add(new Field("title", "A good title".getBytes()));
        
        return document;
    }
    
    @Test
    public void testAddThread() throws IOException {
        context.checking(new Expectations() {
            {
                one(mockDocConsumer).addThread(documentsWriterThreadState);
                will(returnValue(mockDocConsumerPerThread));
            }
        });
        
        DocConsumerPerThread docConsumerPerThread = geoDocConsumer.addThread(documentsWriterThreadState);
        assertTrue("Expected a GeoDocConsumerPerThread", docConsumerPerThread instanceof GeoDocConsumerPerThread);
        GeoDocConsumerPerThread geoDocConsumer = (GeoDocConsumerPerThread)docConsumerPerThread;
        assertSame("GeoDocConsumerPerThread's default consumerPerThread was not set correctly", 
                mockDocConsumerPerThread, geoDocConsumer.getDefaultDocConsumerPerThread());
    }
    
    @Test
    public void testAbort() throws IOException {
        context.checking(new Expectations() {
            {
                one(mockDocConsumer).abort();
                
                one(mockGeoIndexer).abort();
            }
        });
        
        geoDocConsumer.abort();
    }
    
    @Test
    public void testFlush() throws IOException {
        final String segmentName = "segmentA";
        BufferedDeletes bufferedDeletes = new BufferedDeletes();
        final SegmentWriteState segmentWriteState = new SegmentWriteState(null, directory, segmentName, fieldInfos, 10, 0, bufferedDeletes );
        final Collection<DocConsumerPerThread> threads = Collections.emptyList();
        context.checking(new Expectations() {
            {
                one(mockDocConsumer).flush(buildDefaultPerThreadCollection(threads), segmentWriteState);
                
                one(mockGeoIndexer).flush(directory, segmentName);
            }
        });
        
        geoDocConsumer.flush(threads, segmentWriteState);
    }
    
    private Collection<DocConsumerPerThread> buildDefaultPerThreadCollection(Collection<DocConsumerPerThread> threads) {
        Collection<DocConsumerPerThread> defaultDocConsumerThreads = 
            new HashSet<DocConsumerPerThread>(threads.size());
        
        for (DocConsumerPerThread thread: threads) {
            GeoDocConsumerPerThread geoThread = (GeoDocConsumerPerThread)thread;
            defaultDocConsumerThreads.add(geoThread.getDefaultDocConsumerPerThread());
        }
        
        return defaultDocConsumerThreads;
    }
    
    @Test
    public void testFreeRAM() throws IOException {
        context.checking(new Expectations() {
            {
                one(mockDocConsumer).freeRAM();
                will(returnValue(true));
            }
        });
        
        assertTrue("Expected success at freeing RAM in defaultDocConsumer to result in true being " +
        		"returned from geoDocConsumer as well", geoDocConsumer.freeRAM());
    }
}
