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

package com.browseengine.bobo.sort;

import java.io.IOException;
import java.text.Collator;
import java.util.Locale;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.FieldCache.StringIndex;
import org.apache.lucene.search.ScoreDoc;

public abstract class DocComparatorSource {
	
    boolean _reverse = false;
	
	public DocComparatorSource setReverse(boolean reverse){
		_reverse = reverse;
    return this;
	}
	
	public final boolean isReverse(){
		return _reverse;
	}
	
	public abstract DocComparator getComparator(IndexReader reader,int docbase)
			throws IOException;

	public static class IntDocComparatorSource extends DocComparatorSource {
		private final String field;

		public IntDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final int[] values = FieldCache.DEFAULT.getInts(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					// cannot return v1-v2 because it could overflow
					if (values[doc1.doc] < values[doc2.doc]) {
						return -1;
					} else if (values[doc1.doc] > values[doc2.doc]) {
						return 1;
					} else {
						return 0;
					}
				}

				public Integer value(ScoreDoc doc) {
					return Integer.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	public static class StringLocaleComparatorSource extends DocComparatorSource {
		private final String field;
		private final Collator _collator;

		public StringLocaleComparatorSource(String field,Locale locale) {
			this.field = field;
			_collator = Collator.getInstance(locale);
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final String[] values = FieldCache.DEFAULT.getStrings(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
				    if (values[doc1.doc] == null) {
				      if (values[doc2.doc] == null) {
				        return 0;
				      }
				      return -1;
				    } else if (values[doc2.doc] == null) {
				      return 1;
				    }				   
				    return _collator.compare(values[doc1.doc], values[doc2.doc]);
				}

				public String value(ScoreDoc doc) {
					return values[doc.doc];
				}
			};
		}
	}
	
	public static class StringValComparatorSource extends DocComparatorSource {
		private final String field;

		public StringValComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final String[] values = FieldCache.DEFAULT.getStrings(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
				    if (values[doc1.doc] == null) {
				      if (values[doc2.doc] == null) {
				        return 0;
				      }
				      return -1;
				    } else if (values[doc2.doc] == null) {
				      return 1;
				    }				   
				    return values[doc1.doc].compareTo(values[doc2.doc]);
				}

				public String value(ScoreDoc doc) {
					return values[doc.doc];
				}
			};
		}
	}
	
	public static class StringOrdComparatorSource extends DocComparatorSource {
		private final String field;

		public StringOrdComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final StringIndex values = FieldCache.DEFAULT.getStringIndex(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					return values.order[doc1.doc] -  values.order[doc2.doc];
				}

				public String value(ScoreDoc doc) {
					return String.valueOf(values.lookup[values.order[doc.doc]]);
				}
			};
		}
	}
	
	public static class ShortDocComparatorSource extends DocComparatorSource {
		private final String field;

		public ShortDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final short[] values = FieldCache.DEFAULT.getShorts(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					return values[doc1.doc] - values[doc2.doc];
				}

				public Short value(ScoreDoc doc) {
					return Short.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	public static class LongDocComparatorSource extends DocComparatorSource {
		private final String field;

		public LongDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final long[] values = FieldCache.DEFAULT.getLongs(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					// cannot return v1-v2 because it could overflow
					if (values[doc1.doc] < values[doc2.doc]) {
						return -1;
					} else if (values[doc1.doc] > values[doc2.doc]) {
						return 1;
					} else {
						return 0;
					}
				}

				public Long value(ScoreDoc doc) {
					return Long.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	public static class FloatDocComparatorSource extends DocComparatorSource {
		private final String field;

		public FloatDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final float[] values = FieldCache.DEFAULT.getFloats(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					// cannot return v1-v2 because it could overflow
					if (values[doc1.doc] < values[doc2.doc]) {
						return -1;
					} else if (values[doc1.doc] > values[doc2.doc]) {
						return 1;
					} else {
						return 0;
					}
				}

				public Float value(ScoreDoc doc) {
					return Float.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	public static class DoubleDocComparatorSource extends DocComparatorSource {
		private final String field;

		public DoubleDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final double[] values = FieldCache.DEFAULT.getDoubles(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					// cannot return v1-v2 because it could overflow
					if (values[doc1.doc] < values[doc2.doc]) {
						return -1;
					} else if (values[doc1.doc] > values[doc2.doc]) {
						return 1;
					} else {
						return 0;
					}
				}

				public Double value(ScoreDoc doc) {
					return Double.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	public static class RelevanceDocComparatorSource extends DocComparatorSource {
		public RelevanceDocComparatorSource() {
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					// cannot return v1-v2 because it could overflow
					if (doc1.score < doc2.score) {
						return -1;
					} else if (doc1.score > doc2.score) {
						return 1;
					} else {
						return 0;
					}
				}

				public Float value(ScoreDoc doc) {
					return Float.valueOf(doc.score);
				}
			};
		}
		
		
	}
	
	public static class DocIdDocComparatorSource extends DocComparatorSource {
		public DocIdDocComparatorSource() {
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final int _docbase = docbase;

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					return doc1.doc-doc2.doc;
				}

				public Integer value(ScoreDoc doc) {
					return Integer.valueOf(doc.doc+_docbase);
				}
			};
		}
	}
	
	public static class ByteDocComparatorSource extends DocComparatorSource {
		private final String field;

		public ByteDocComparatorSource(String field) {
			this.field = field;
		}

		public DocComparator getComparator(IndexReader reader,int docbase)
				throws IOException {

			final byte[] values = FieldCache.DEFAULT.getBytes(reader, field);

			return new DocComparator() {
				public int compare(ScoreDoc doc1, ScoreDoc doc2) {
					return values[doc1.doc] - values[doc2.doc];
				}

				public Byte value(ScoreDoc doc) {
					return Byte.valueOf(values[doc.doc]);
				}
			};
		}
	}
	
	
}
