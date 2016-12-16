/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Original version from Cassandra BitUtil class
 */
package org.krugler.pinochle;

public class BitUtil {

    /** Returns the number of bits set in the long */
    public static int numBits(long x) {
        x = x - ((x >>> 1) & 0x5555555555555555L);
        x = (x & 0x3333333333333333L) + ((x >>>2 ) & 0x3333333333333333L);
        x = (x + (x >>> 4)) & 0x0F0F0F0F0F0F0F0FL;
        x = x + (x >>> 8);
        x = x + (x >>> 16);
        x = x + (x >>> 32);
        return ((int)x) & 0x7F;
    }

    /** returns true if v is a power of two or zero*/
    public static boolean isPowerOfTwo(long v) {
        return ((v & (v-1)) == 0);
    }

    private static final byte[] ntzTable = {
        8,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        6,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        7,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        6,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        5,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0,
        4,0,1,0,2,0,1,0,3,0,1,0,2,0,1,0
    };

    /** Returns number of trailing zeros in a 32 bit int value. */
    public static int ntz(int val) {
        // This implementation does a single binary search at the top level only.
        // In addition, the case of a non-zero first byte is checked for first
        // because it is the most common in dense bit arrays.

        int lowByte = val & 0xff;
        if (lowByte != 0) return ntzTable[lowByte];
        lowByte = (val>>>8) & 0xff;
        if (lowByte != 0) return ntzTable[lowByte] + 8;
        lowByte = (val>>>16) & 0xff;
        if (lowByte != 0) return ntzTable[lowByte] + 16;
        // no need to mask off low byte for the last byte.
        // no need to check for zero on the last byte either.
        return ntzTable[val>>>24] + 24;
    }

}
