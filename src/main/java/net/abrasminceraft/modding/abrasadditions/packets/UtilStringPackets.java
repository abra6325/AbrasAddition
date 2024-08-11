package net.abrasminceraft.modding.abrasadditions.packets;

import net.minecraft.network.FriendlyByteBuf;
import org.checkerframework.checker.units.qual.A;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilStringPackets {
    public static final byte[] STARTING_SEQUENCE = {(byte) 0x69, (byte) 0x42, (byte) 0x00, (byte) 0x69};
    public static final byte SEPARATOR = (byte) 0x00;
    public static String read(FriendlyByteBuf buffer,int PACKETID){
        byte[] tmp = buffer.readByteArray();
        assert(tmp.length >5);
        assert(checkStartingSequence(tmp));
        assert(tmp[4] == (byte) PACKETID);
        byte[] ret = new byte[tmp.length-5];
        int l = tmp.length-5;
        for(int i=0;i<l;i++){
            ret[i] = tmp[i+5];
        }
        return new String(ret);
    }
    public static String read(byte[] tmp,int PACKETID){

        assert(tmp.length >5);
        assert(checkStartingSequence(tmp));
        assert(tmp[4] == (byte) PACKETID);
        byte[] ret = new byte[tmp.length-5];
        int l = tmp.length-5;
        for(int i=0;i<l;i++){
            ret[i] = tmp[i+5];
        }
        return new String(ret);
    }
    public static byte[] ridStartingSequence(byte[] to,int PACKETID){
        assert(to.length >5);
        assert(checkStartingSequence(to));
        assert(to[4] == (byte) PACKETID);
        byte[] ret = new byte[to.length-5];
        int l = to.length-5;
        for(int i=0;i<l;i++){
            ret[i] = to[i+5];
        }
        return ret;
    }
    public static List<String> readList(byte[] to,int PACKETID){
        byte[] stripped = ridStartingSequence(to,PACKETID);
        List<byte[]> separated = new ArrayList<>();
        List<Byte> read = new ArrayList<>();
        for(byte i:stripped){
            if(i == SEPARATOR){
                byte[] tmp = new byte[read.size()];
                for(int j=0;j<read.size();j++){
                    tmp[j]=read.get(j);
                }
                separated.add(tmp);
                read.clear();

            }else{
                read.add(i);
            }
        }
        List<String> ret = new ArrayList<>();
        for(byte[] i:separated){
            ret.add(new String(i));
        }
        return ret;
    }
    public static byte[] encode(List<String> to,int id){
        List<Byte> tmp = new ArrayList<>();
        for(String i:to){
            byte[] tmp2 = i.getBytes();
            for(byte j:tmp2) {
                tmp.add(j);
            }
            tmp.add(SEPARATOR);
        }

        List<Byte> ret1 = new ArrayList<>();
        for(byte i:STARTING_SEQUENCE) ret1.add(i);
        ret1.add((byte) id);
        ret1.addAll(tmp);
        byte[] ret = new byte[ret1.size()];
        for(int i=0;i<ret1.size();i++) ret[i] = ret1.get(i);
        return ret;
    }
    public static boolean checkStartingSequence(byte[] x){
        boolean ret = true;
        for(int i=0;i<4;i++) ret = (x[i] == STARTING_SEQUENCE[i]);
        return ret;
    }
}
