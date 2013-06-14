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

package com.browseengine.bobo.util;

/**
 * 
 * @author nnarkhed
 */
public class GeoMatchUtil
{
  public static final float EARTH_RADIUS_MILES = 3956.0f;
  public static final float EARTH_RADIUS_KM = 6371.0f;

  public static final float LATITUDE_DEGREES_MIN = -90.0f;
  public static final float LATITUDE_DEGREES_MAX = 90.0f;
  public static final float LONGITUDE_DEGREES_MIN = -180.0f;
  public static final float LONGITUDE_DEGREES_MAX = 180.0f;
  
  public static float degreesToRadians(float degrees)
  {
    return (float) (degrees * (Math.PI / 180));
  }
  
  public static float getMilesRadiusCosine(float radiusInMiles)
  {
    float radiusCosine = (float) (Math.cos(radiusInMiles / EARTH_RADIUS_MILES));
    return radiusCosine;
  }
  
  public static float getKMRadiusCosine(float radiusInKM)
  {
    float radiusCosine = (float) (Math.cos(radiusInKM / EARTH_RADIUS_KM));
    return radiusCosine;
  }
  
  public static float[] geoMatchCoordsFromDegrees(float latDegrees, float lonDegrees)
  {
    float[] geoMatchCoords;
    
    if (Float.isNaN(latDegrees) || Float.isNaN(lonDegrees))
    {
      geoMatchCoords = new float[] { Float.NaN, Float.NaN, Float.NaN };
    }
    else
    {
      geoMatchCoords = geoMatchCoordsFromRadians((float) (latDegrees * (Math.PI / 180)),
                                                 (float) (lonDegrees * (Math.PI / 180)));
    }
    return geoMatchCoords;
  }

  public static float[] geoMatchCoordsFromRadians(float latRadians, float lonRadians)
  {
    float[] geoMatchCoords;
    
    if (Float.isNaN(latRadians) || Float.isNaN(lonRadians))
    {
      geoMatchCoords = new float[] { Float.NaN, Float.NaN, Float.NaN };
    }
    else
    {
      geoMatchCoords = new float[]
      {
        geoMatchXCoordFromRadians(latRadians, lonRadians),
        geoMatchYCoordFromRadians(latRadians, lonRadians),
        geoMatchZCoordFromRadians(latRadians)
      };
    }
    
    return geoMatchCoords;
  }
  
  public static float geoMatchXCoordFromDegrees(float latDegrees, float lonDegrees)
  {
    if (Float.isNaN(latDegrees) || Float.isNaN(lonDegrees))
    {
      return Float.NaN;
    }
    
    return geoMatchXCoordFromRadians((float) (latDegrees * (Math.PI / 180)),
                                     (float) (lonDegrees * (Math.PI / 180)));
  }

  public static float geoMatchYCoordFromDegrees(float latDegrees, float lonDegrees)
  {
    if (Float.isNaN(latDegrees) || Float.isNaN(lonDegrees))
    {
      return Float.NaN;
    }
    
    return geoMatchYCoordFromRadians((float) (latDegrees * (Math.PI / 180)),
                                     (float) (lonDegrees * (Math.PI / 180)));
  }

  public static float geoMatchZCoordFromDegrees(float latDegrees)
  {
    if (Float.isNaN(latDegrees))
    {
      return Float.NaN;
    }
    
    return geoMatchZCoordFromRadians((float) (latDegrees * (Math.PI / 180)));
  }

  public static float geoMatchXCoordFromRadians(float latRadians, float lonRadians)
  {
    if (Float.isNaN(latRadians) || Float.isNaN(lonRadians))
    {
      return Float.NaN;
    }
    
    return (float) (Math.cos(latRadians) * Math.cos(lonRadians));
  }

  public static float geoMatchYCoordFromRadians(float latRadians, float lonRadians)
  {
    if (Float.isNaN(latRadians) || Float.isNaN(lonRadians))
    {
      return Float.NaN;
    }
    
    return (float) (Math.cos(latRadians) * Math.sin(lonRadians));
  }

  public static float geoMatchZCoordFromRadians(float latRadians)
  {
    if (Float.isNaN(latRadians))
    {
      return Float.NaN;
    }
    
    return (float) Math.sin(latRadians);
  }

  public static float getMatchLatDegreesFromXYZCoords(float x, float y, float z)
  {
    return (float) Math.toDegrees(Math.asin(z));
  }

  public static float getMatchLonDegreesFromXYZCoords(float x, float y, float z)
  {
    float lon = (float) Math.toDegrees(Math.asin(y / Math.cos(Math.asin(z))));

    if (x < 0 && y > 0)
      return 180.0f - lon;
    else if (y < 0 && x < 0)
      return -180.0f - lon;
    else
      return lon;
  }

}
