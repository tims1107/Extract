package utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {



    private static void closeChannel(Channel channel){
        if (channel != null) try{channel.close();} catch (IOException ioe){}
    }

    public static String getControlNum(String filestr)  {
        SeekableByteChannel byteChannel = null;
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        Path newfile = Paths.get(filestr);

        Charset charset = Charset.forName("US-ASCII");
        long controlnum = 0;
        String num = null;

        try {
            byteChannel = Files.newByteChannel(newfile);

            if (byteChannel.read(byteBuffer) > 0) {
                byteBuffer.rewind();

              num = bb_to_str(byteBuffer,charset);
                byteBuffer.flip();
            }

            return num;

        } catch (IOException ioe) {

        } finally {
            closeChannel(byteChannel);
        }

        return null;
    }

    public static void fileChannelWrite(ByteBuffer byteBuffer, String updateFile)
             {

        Set options = new HashSet();
        options.add(StandardOpenOption.CREATE);
        options.add(StandardOpenOption.WRITE);
        //options.add(StandardOpenOption.APPEND);

        Path path = Paths.get(updateFile);

        FileChannel fileChannel = null;

        try {
            fileChannel = FileChannel.open(path, options);
            fileChannel.write(byteBuffer);

            fileChannel.close();
            fileChannel = null;
        } catch (IOException ioe){

        } finally {
            closeChannel(fileChannel);
        }



    }

    public static final Charset charset = Charset.forName("UTF-8");
    public static final CharsetEncoder encoder = charset.newEncoder();
    public static final CharsetDecoder decoder = charset.newDecoder();




    public static ByteBuffer str_to_bb(String msg, Charset charset){
        return ByteBuffer.wrap(msg.getBytes(charset));
    }

    public static ByteBuffer strbuf_to_bb(StringBuffer sb) {
        CharBuffer buffer = CharBuffer.wrap(sb);
        ByteBuffer bytes = null;
        try {
            bytes = encoder.encode(buffer);
        } catch (CharacterCodingException cce){

        }
        return bytes;
    }

    public static String bb_to_str(ByteBuffer buffer, Charset charset){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }

}
