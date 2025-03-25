package launcher;

import service.CompressionService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static List<String> filePath(){
        scanner.nextLine();
        System.out.println("압축 파일 경로 입력");
        String dest = scanner.nextLine();
        System.out.println("해제 파일 경로 입력");
        String source = scanner.nextLine();

        return new ArrayList<>(Arrays.asList(dest, source));
    }

    public static void main(String[] args) {
        int num;

        List<String> files;
        CompressionService compressionService = new CompressionService();
        while(true){
            System.out.println("압축 프로그램입니다.");
            System.out.println("1 허프만 압축");
            System.out.println("2 허프만 압축 해제");
            System.out.println("3 LZ77 압축");
            System.out.println("4 LZ77 압축해제");
            System.out.println("5 종료");
            num = scanner.nextInt();

            switch (num) {
                case 1:
                    files = filePath();
                    compressionService.compressFile(new File(files.get(0)), new File(files.get(1)));
                    break;
                case 2:
                    files = filePath();
                    compressionService.deCompressFile(new File(files.get(0)), files.get(1));
                    break;
                case 3:
                    files = filePath();
                    compressionService.lz77Compress(new File(files.get(0)), new File(files.get(1)));
                    break;
                case 4:
                    files = filePath();
                    compressionService.deCompressLZFile(new File(files.get(0)), files.get(1));
                    break;
                case 5:
                    return;
            }
        }
    }
}
