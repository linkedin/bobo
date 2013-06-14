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

package com.browseengine.bobo.analysis.section;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
import org.apache.lucene.index.Payload;

/**
 * This class augments a token stream by attaching a section id as payloads.
 *
 */
public final class SectionTokenStream extends TokenFilter
{
  private Payload _payload;
  private PayloadAttribute _payloadAtt;
  
  public SectionTokenStream(TokenStream tokenStream, int sectionId)
  {
    super(tokenStream);
    _payloadAtt = (PayloadAttribute)addAttribute(PayloadAttribute.class);
    _payload = encodeIntPayload(sectionId);
  }

  public boolean incrementToken() throws IOException
  {
    if(input.incrementToken())
    {
      _payloadAtt.setPayload(_payload);
      return true;
    }
    return false;
  }

  static public Payload encodeIntPayload(int id)
  {
    byte[] data = new byte[4];
    int off = data.length;

    do
    {
      data[--off] = (byte)(id);
      id >>>= 8;
    }
    while(id > 0);
    
    return new Payload(data, off, data.length - off);
  }
  
  static public int decodeIntPayload(Payload payload)
  {
    return decodeIntPayload(payload.getData(), payload.getOffset(), payload.length());
  }
  
  static public int decodeIntPayload(byte[] data, int off, int len)
  {
    int endOff = off + len;
    int val = 0;
    while(off < endOff)
    {
      val <<= 8;
      val += (data[off++] & 0xFF);
    }
    return val;
  }
}
