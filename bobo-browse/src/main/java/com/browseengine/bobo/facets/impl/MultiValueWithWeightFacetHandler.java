package com.browseengine.bobo.facets.impl;

import java.io.IOException;
import java.util.Properties;

import com.browseengine.bobo.api.BoboIndexReader;
import com.browseengine.bobo.api.BoboIndexReader.WorkArea;
import com.browseengine.bobo.facets.data.MultiValueWithWeightFacetDataCache;
import com.browseengine.bobo.facets.data.TermListFactory;
import com.browseengine.bobo.facets.filter.AdaptiveFacetFilter;
import com.browseengine.bobo.facets.filter.MultiValueFacetFilter;
import com.browseengine.bobo.facets.filter.RandomAccessFilter;
import com.browseengine.bobo.facets.range.MultiDataCacheBuilder;

public class MultiValueWithWeightFacetHandler extends MultiValueFacetHandler
{
  public MultiValueWithWeightFacetHandler(String name, String indexFieldName, TermListFactory termListFactory, int invertedIndexPenalty)
  {
    super(name, indexFieldName, termListFactory, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name, String indexFieldName, int invertedIndexPenalty)
  {
    super(name, indexFieldName, null, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name, int invertedIndexPenalty)
  {
    super(name, name, null, null, null, invertedIndexPenalty);
  }

  public MultiValueWithWeightFacetHandler(String name,
                                          String indexFieldName,
                                          TermListFactory termListFactory)
  {
    super(name, indexFieldName, termListFactory, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  public MultiValueWithWeightFacetHandler(String name, String indexFieldName)
  {
    super(name, indexFieldName, null, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  public MultiValueWithWeightFacetHandler(String name)
  {
    super(name, name, null, null, null, AdaptiveFacetFilter.DEFAULT_INVERTED_INDEX_PENALTY);
  }

  @Override
  public RandomAccessFilter buildRandomAccessFilter(String value, Properties prop) throws IOException
  {
    MultiValueFacetFilter f= new MultiValueFacetFilter(new MultiDataCacheBuilder(getName(), _indexFieldName), value);
    return f;
  }
  @Override
  public MultiValueWithWeightFacetDataCache load(BoboIndexReader reader, WorkArea workArea) throws IOException
  {
    MultiValueWithWeightFacetDataCache dataCache = new MultiValueWithWeightFacetDataCache();
      
    dataCache.setMaxItems(_maxItems);

    if(_sizePayloadTerm == null)
    {
      dataCache.load(_indexFieldName, reader, _termListFactory, workArea);
    }
    else
    {
      dataCache.load(_indexFieldName, reader, _termListFactory, _sizePayloadTerm);
    }
    return dataCache;
  }
}
