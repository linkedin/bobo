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

import java.io.IOException;

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
import org.junit.runner.RunWith;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.browseengine.bobo.geosearch.index.IGeoIndexer;
import com.browseengine.bobo.geosearch.index.bo.GeoCoordinate;
import com.browseengine.bobo.geosearch.index.bo.GeoCoordinateField;

/**
 * @author Geoff Cooney
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration( { "/TEST-servlet.xml" }) 
@IfProfileValue(name = "test-suite", values = { "unit", "all" })
public class GeoDocConsumerPerThreadTest {
    private final Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);  
    }};
    
    private DocConsumerPerThread mockDocConsumerPerThread;
    
    private IGeoIndexer mockGeoIndexer;
    
    private GeoDocConsumerPerThread geoDocConsumerPerThread;

    private DocumentsWriter.DocWriter mockDocWriter;
    
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
    
    @Before
    public void setUp() throws IOException {
        mockDocConsumerPerThread = context.mock(DocConsumerPerThread.class);
        mockDocWriter = context.mock(DocumentsWriter.DocWriter.class);
        
        mockGeoIndexer = context.mock(IGeoIndexer.class);
        
        documentsWriter = buildDocumentsWriter();
        
        document = buildDocument();
        
        documentsWriterThreadState = new DocumentsWriterThreadState(documentsWriter);
        documentsWriterThreadState.docState.doc = document;
        documentsWriterThreadState.docState.docID = docID;
        
        geoDocConsumerPerThread = new GeoDocConsumerPerThread(mockDocConsumerPerThread, documentsWriterThreadState, mockGeoIndexer);
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
    public void testAddThread_NoGeoFields() throws IOException {
        context.checking(new Expectations() {
            {
                one(mockDocConsumerPerThread).processDocument();
                will(returnValue(mockDocWriter));
                
                never(mockGeoIndexer);
            }
        });
        
        assertSame("Expected defaultDocConsumerPerThread's DocWriter as return", 
                mockDocWriter, geoDocConsumerPerThread.processDocument());
    }
      
    
    @Test
    public void testAddThread_TwoGeoFields() throws IOException {
        final String geoFieldName1 = "location1";
        final GeoCoordinate geoCoordinate1 = new GeoCoordinate(45.0f, 45.0f);
        final GeoCoordinateField geoField1 = new GeoCoordinateField(geoFieldName1, geoCoordinate1); 
        document.add(geoField1);
        
        final String geoFieldName2 = "location2";
        final GeoCoordinate geoCoordinate2 = new GeoCoordinate(45.0f, 45.0f);
        final GeoCoordinateField geoField2 = new GeoCoordinateField(geoFieldName2, geoCoordinate2);
        document.add(geoField2);
        
        context.checking(new Expectations() {
            {
                one(mockDocConsumerPerThread).processDocument();
                will(returnValue(mockDocWriter));
                
                one(mockGeoIndexer).index(docID, geoField1);
                one(mockGeoIndexer).index(docID, geoField2);
            }
        });
        
        assertSame("Expected defaultDocConsumerPerThread's DocWriter as return", 
                mockDocWriter, geoDocConsumerPerThread.processDocument());
    }
    
    @Test
    public void testAddThread_TwoGeoFields_SameName() throws IOException {
        final String geoFieldName1 = "location1";
        final GeoCoordinate geoCoordinate1 = new GeoCoordinate(45.0f, 45.0f);
        final GeoCoordinateField geoField1 = new GeoCoordinateField(geoFieldName1, geoCoordinate1); 
        document.add(geoField1);
        
        final GeoCoordinate geoCoordinate2 = new GeoCoordinate(45.0f, 45.0f);
        final GeoCoordinateField geoField2 = new GeoCoordinateField(geoFieldName1, geoCoordinate2);
        document.add(geoField2);
        
        context.checking(new Expectations() {
            {
                one(mockDocConsumerPerThread).processDocument();
                will(returnValue(mockDocWriter));
                
                one(mockGeoIndexer).index(docID, geoField1);
                one(mockGeoIndexer).index(docID, geoField2);
            }
        });
        
        assertSame("Expected defaultDocConsumerPerThread's DocWriter as return", 
                mockDocWriter, geoDocConsumerPerThread.processDocument());
    }
    
    @Test
    public void testAbort() {
        context.checking(new Expectations() {
            {
                one(mockDocConsumerPerThread).abort();
                
                one(mockGeoIndexer).abort();
            }
        });
        
        geoDocConsumerPerThread.abort();
    }
}
