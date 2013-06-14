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

package com.browseengine.bobo.test;

import java.util.Random;

import com.browseengine.bobo.util.BigIntArray;

public class MemoryUtil
{
  
  static int max = 5000000;

  static int[] getIndex(int count)
  {
    Random rand = new Random();
    int[] array=new int[count];
    
    for (int i=0;i<count;++i)
    {
      array[i]=rand.nextInt(max);
    }
    //Arrays.sort(array);
    return array;
  }
  
  private static class RunnerThread2 extends Thread
  {
    private int[] array;
    private BigIntArray bigarray;
    
    RunnerThread2(int[] a,BigIntArray b)
    {
      array=a;
      bigarray=b;
    }
    
    public void run()
    {
      long start=System.currentTimeMillis();
      for (int val : array)
      {
        int x = bigarray.get(val);
      }

      long end=System.currentTimeMillis();
      System.out.println("time: "+(end-start));
    }
  }

  
  static void time1(final int[][] array)
  {
    int iter = array.length;
    final BigIntArray bigArray = new BigIntArray(max);
    Thread[] threads=new Thread[iter];
    
    for (int i=0;i<iter;++i)
    {
      threads[i]=new RunnerThread2(array[i],bigArray);
    }
    
    for (Thread t : threads)
    {
      t.start();
    }
  }
  
  private static class RunnerThread extends Thread
  {
    private int[] array;
    private int[] bigarray;
    
    RunnerThread(int[] a,int[] b)
    {
      array=a;
      bigarray=b;
    }
    
    public void run()
    {
      long start=System.currentTimeMillis();
      for (int val : array)
      {
        int x = bigarray[val];
      }

      long end=System.currentTimeMillis();
      System.out.println("time: "+(end-start));
    }
  }
  
  static void time2(final int[][] array)
  {
    int iter = array.length;
    final int[] bigArray=new int[max];
    Thread[] threads=new Thread[iter];
    for (int i=0;i<iter;++i)
    {
      threads[i]=new RunnerThread(array[i],bigArray);
    }
    
    for (Thread t : threads)
    {
      t.start();
    }
  }
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    int threadCount=10;
    int numIter = 1000000;
    int[][] indexesPerThread=new int[threadCount][];
    for (int i=0;i<threadCount;++i)
    {
      indexesPerThread[i]=getIndex(numIter);
    }
    
    time1(indexesPerThread);

    //time2(indexesPerThread);
  }

}
