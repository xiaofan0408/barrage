package file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuzefan  2019/1/7 11:30
 */
public class FileUtil {
    public static List<Path> readFileListByDir(String path) throws Exception{
        List<Path> fileList = new ArrayList<>();
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileList.add(file);
                return FileVisitResult.CONTINUE;
            }
        });
        return fileList;
    }

    public static String readByChannelTest3(int allocate, Path path) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        RandomAccessFile fis = new RandomAccessFile(new File(path.toString()), "rw");
        FileChannel channel = fis.getChannel();
        long size = channel.size();

        // 构建一个只读的MappedByteBuffer
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);

        // 如果文件内容很大,可以循环读取,计算应该读取多少次
        byte[] bytes = new byte[allocate];
        long cycle = size / allocate;
        int mode = (int)(size % allocate);
        //byte[] eachBytes = new byte[allocate];
        for (int i = 0; i < cycle; i++) {
            // 每次读取allocate个字节
            mappedByteBuffer.get(bytes);

            // 打印文件内容,关闭打印速度会很快
            // System.out.print(new String(eachBytes));
            stringBuffer.append(new String(bytes));
        }
        if(mode > 0) {
            bytes = new byte[mode];
            mappedByteBuffer.get(bytes);

            // 打印文件内容,关闭打印速度会很快
            // System.out.print(new String(eachBytes));
            stringBuffer.append(new String(bytes));
        }

        // 关闭通道和文件流
        channel.close();
        fis.close();
        return stringBuffer.toString();
    }
}
