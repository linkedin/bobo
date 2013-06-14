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

package com.browseengine.bobo.facets.data;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class supported:
 * <ul>
 * <li> {@link Integer}</li>
 * <li> {@link Float}</li>
 * <li> {@link Character}</li>
 * <li> {@link Double}</li>
 * <li> {@link Short}</li>
 * <li> {@link Long}</li>
 * <li> {@link Date}</li>
 * </ul>
 * 
 * Autoboxing: primitive types corresponding classes above are supported.
 */
public class PredefinedTermListFactory<T> implements TermListFactory<T>
{
  private static Map<Class<?>, Class<? extends TermValueList<?>>> supportedTypes = new HashMap<Class<?>, Class<? extends TermValueList<?>>>();

  static
  {
    supportedTypes.put(int.class, TermIntList.class);
    supportedTypes.put(float.class, TermFloatList.class);
    supportedTypes.put(char.class, TermCharList.class);
    supportedTypes.put(double.class, TermDoubleList.class);
    supportedTypes.put(short.class, TermShortList.class);
    supportedTypes.put(long.class, TermLongList.class);
    supportedTypes.put(Integer.class, TermIntList.class);
    supportedTypes.put(Float.class, TermFloatList.class);
    supportedTypes.put(Character.class, TermCharList.class);
    supportedTypes.put(Double.class, TermDoubleList.class);
    supportedTypes.put(Short.class, TermShortList.class);
    supportedTypes.put(Long.class, TermLongList.class);
    supportedTypes.put(Date.class, TermDateList.class);
  }

  private final Class<T> _cls;
  private final String _format;
  private final Class<? extends TermValueList<T>> _listClass;

  public PredefinedTermListFactory(Class<?> cls, String format)
  {
    if (supportedTypes.get(cls) == null)
    {
      throw new IllegalArgumentException("Class " + cls + " not defined.");
    }
    _cls = (Class<T>) cls;
    _format = format;
    _listClass = (Class<? extends TermValueList<T>>) supportedTypes.get(_cls);
  }

  public PredefinedTermListFactory(Class<?> cls)
  {
    this(cls, null);
  }

  public TermValueList<T> createTermList(int capacity)
  {
    if (TermCharList.class.equals(_listClass)) // we treat char type separate as
                                               // it does not have a format
                                               // string
    {
      @SuppressWarnings("unchecked")
      TermValueList<T> retlist = (TermValueList<T>) (new TermCharList(capacity));;
      return retlist;
    } else
    {
      try
      {
        Constructor<? extends TermValueList<T>> constructor = _listClass
            .getConstructor(int.class, String.class); // the constructor also takes the format String as parameter
        return constructor.newInstance(capacity, _format);
      } catch (Exception e)
      {
        throw new RuntimeException(e.getMessage());
      }
    }
  }

  public TermValueList<T> createTermList()
  {
    return createTermList(-1);
  }

  public Class<T> getType()
  {
    // TODO Auto-generated method stub
    return _cls;
  }
}
