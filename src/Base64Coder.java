public class Base64Coder {

    private static char[] map1 = new char[64];
    static {

        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) map1[i++] = c;
        for (char c = 'a'; c <= 'z'; c++) map1[i++] = c;
        for (char c = '0'; c <= '9'; c++) map1[i++] = c;
        map1[i++] = '+'; map1[i++] = '/';
    }

    private static byte[] map2 = new byte[128];
    static {

        for (int i = 0; i < map2.length; i++) map2[i] = -1;
        for (int i = 0; i < 64; i++) map2[map1[i]] = (byte)i;
    }

    public static String decodeString(String s) {
        return new String(decode(s));
    }

    public static byte[] decodeLines (String s){

        char[] buf = new char[s.length()];
        int p = 0;
        for(int ip = 0; ip < s.length(); ip++){

            char c = s.charAt(ip);
            if (c != ' ' && c != '\r' && c !='\n' && c!='\t') buf[p++] = c;

        }
        return decode(buf,0,p);
    }

    public static byte[] decode(String s){
        return decode(s.toCharArray());
    }

    public static byte[] decode(char[] in) {
        return decode(in, 0, in.length);
    }

    public static byte[] decode(char[] in, int ioff, int iLen){

        if (iLen % 4 != 0) throw new IllegalArgumentException("Length" + "of Base64 encoded input string is not a multiple of 4.");
        while (iLen > 0 && in[ioff + iLen -1] == '=') iLen--;
        int oLen = (iLen * 3) /4;
        byte[] out = new byte[oLen];
        int ip = ioff;
        int iEnd = ioff + iLen;
        int op = 0;
        while (ip < iEnd){
            int i0 = in[ip++];
            int i1 = in[ip++];
            int i2 = ip < iEnd ? in[ip++] : 'A';
            int i3 = ip < iEnd ? in[ip++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) throw new IllegalArgumentException("Illegal" + "charcter in Base64 encoded data");
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) throw new IllegalArgumentException("Illegal character in" + "Base64 encoded data.");
            int o0 = (b0 << 2) | (b1 >>> 4);
            int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3; out[op++] = (byte)o0;
            if (op < oLen) out[op++] = (byte)o1; if (op < oLen) out[op++] = (byte)o2;

        }
        return out;
    }
}
