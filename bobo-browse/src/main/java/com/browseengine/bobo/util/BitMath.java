package com.browseengine.bobo.util;

public class BitMath {
  private static final int[] mask = {0x2, 0xC, 0xF0, 0xFF00, 0xFFFF0000};
  private static final int[] shift = {1, 2, 4, 8, 16};

  public static int log2Ceiling(int x) {
    int result = 0;

    boolean isPowerOfTwo = (x & (x - 1)) == 0;

    for (int i = 4; i >= 0; i--) // unroll for speed...
    {
      if ((x & mask[i]) != 0)
      {
        x >>= shift[i];
        result |= shift[i];
      }
    }

    return isPowerOfTwo ? result : result + 1;
  }
}
