package com.fedco.mbc.felhr.encodings;

import android.support.v4.media.TransportMediator;

import com.fedco.mbc.felhr.droidterm.utilities.UnsignedUtil;
import java.util.ArrayList;
import java.util.List;

public class Cp437 extends SingleByteCharset {
    private static final int[] CP437_CHARMAP = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, TransportMediator.KEYCODE_MEDIA_PLAY, TransportMediator.KEYCODE_MEDIA_PAUSE, 199, 252, 233, 226, 228, 224, 229, 231, 234, 235, 232, 239, 238, 236, 196, 197, 201, 230, 198, 244, 246, 242, 251, 249, 255, 214, 220, 162, 163, 165, 8359, 402, 225, 237, 243, 250, 241, 209, 170, 186, 191, 8976, 172, 189, 188, 161, 171, 187, 9617, 9618, 9619, 9474, 9508, 9569, 9570, 9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563, 9488, 9492, 9524, 9516, 9500, 9472, 9532, 9566, 9567, 9562, 9556, 9577, 9574, 9568, 9552, 9580, 9575, 9576, 9572, 9573, 9561, 9560, 9554, 9555, 9579, 9578, 9496, 9484, 9608, 9604, 9612, 9616, 9600, 945, 223, 915, 960, 931, 963, 181, 964, 934, 920, 937, 948, 8734, 966, 949, 8745, 8801, 177, 8805, 8804, 8992, 8993, 247, 8776, 176, 8729, 183, 8730, 8319, 178, 9632, 160};

    public /* bridge */ /* synthetic */ byte[] convertToUTF8(int i) {
        return super.convertToUTF8(i);
    }

    public Cp437() {
        super(CP437_CHARMAP);
    }

    public byte[] toUTF8(byte[] encodedBytes) {
        List<Byte> utf8Bytes = new ArrayList();
        Cp437 cp437 = new Cp437();
        for (byte b : encodedBytes) {
            for (byte utf8Char : convertToUTF8(cp437.lookup(b))) {
                utf8Bytes.add(Byte.valueOf(utf8Char));
            }
        }
        byte[] wholeUTF8Stream = new byte[utf8Bytes.size()];
        for (int i = 0; i < utf8Bytes.size(); i++) {
            wholeUTF8Stream[i] = ((Byte) utf8Bytes.get(i)).byteValue();
        }
        return wholeUTF8Stream;
    }

    public int lookup(byte character) {
        int index = UnsignedUtil.byteToUshort(character);
        if (index < 0 || index > 255) {
            return 0;
        }
        return CP437_CHARMAP[index];
    }

    public int isCharPresent(int character) {
        if (character <= TransportMediator.KEYCODE_MEDIA_PAUSE) {
            return character;
        }
        for (int i = 128; i <= 255; i++) {
            if (character == CP437_CHARMAP[i]) {
                return i;
            }
        }
        return -1;
    }
}
