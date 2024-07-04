package com.faker.project.file;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import jxl.write.WritableWorkbook;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件处理相关代码
 */
@Slf4j
public class FileHandle {
    // 创建扫描时 用到的正则表达式
    public static Map<String, String> configMap = new HashMap<String, String>() {
        {
            // 匹配代码中出现的数据
            put("select\\s+into", "匹配select into关键字");
            put("insert\\s+into\\+[\\w\\.]+\\s+values", "匹配insert into values");
        }
    };

    public static void main(String[] args) throws IOException, WriteException {
        // 扫描的文件路径
        String srcPath = "C:/project/faker";
        // 扫描结果保存的位置
        String savePath = "C:/Users/faker/Desktop" + "/扫描结果数据.xls";
        // 处理结果
        List<Map<String, String>> resultList = new ArrayList<>();
        // 扫描文件
        scanProjectFile(srcPath, resultList);
        // 生成扫描结果
        saveResult(resultList, savePath);
    }

    /** 扫描文件 */
    private static void scanProjectFile(String srcPath, List<Map<String, String>> resultList) throws FileNotFoundException {
        File filePath = new File(srcPath);
        if (!filePath.exists()) {
            throw new FileNotFoundException("扫描文件路径不存在");
        }
        List<File> files = Arrays.asList(Objects.requireNonNull(filePath.listFiles())); // 获取目录下所有文件
        if (CollectionUtils.isEmpty(files)) {
            throw new FileNotFoundException("扫描文件路径下不存在文件");
        }

        for (File file : files) {
            try {
                if (file.isDirectory()) { // 处理文件夹
                    scanProjectFile(file.getPath(), resultList);
                } else if (file.isFile() && file.getName().endsWith(".java")) { // 处理项目中的java文件
                    resultList.addAll(handleSingleFile(file, resultList));
                }
            } catch (Exception ex) {
                log.error("handle file error, file name: {}, file path: {}", file.getName(), file.getPath());
            }
        }

    }

    private static Collection<? extends Map<String, String>> handleSingleFile(File file, List<Map<String, String>> resultList) {
        log.info("handleSingleFile start path:{}", file.getPath());
        List<Map<String, String>> result = new ArrayList<>();
        // 初始化匹配器
        List<Pattern> patternList = new ArrayList<>();
        configMap.forEach((s, s2) -> patternList.add(Pattern.compile(s)));
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file.getPath()), StandardCharsets.UTF_8))) {
            int count = 0;
            String data;

            while ((data = bufferedReader.readLine()) != null) {
                count++;
                // 过滤注释 日志 导入语句等
                if (data.trim().startsWith("//") || data.trim().startsWith("*") || data.contains("debug") || data.contains("import")) {
                    continue;
                }
                for (Pattern pattern : patternList) {
                    Matcher matcher = pattern.matcher(data.toLowerCase(Locale.ROOT));
                    if (matcher.find()) { // 产生匹配到的数据
                        Map<String, String> addMap = new HashMap<>();
                        addMap.put("fileName", file.getAbsolutePath()); // 文件名
                        addMap.put("lineNo", String.valueOf(count)); // 行号
                        addMap.put("matchData", data); // 匹配数据
                        result.add(addMap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 生成扫描结果 */
    private static void saveResult(List<Map<String, String>> resultList, String savePath) throws WriteException, IOException {
        if (CollectionUtils.isNotEmpty(resultList)) {
            WritableWorkbook workbook = null;
            try {
                // 创建文件
                workbook = Workbook.createWorkbook(new File(savePath));
                // 创建sheet页
                WritableSheet sheet = workbook.createSheet("扫描结果", 0);
                // 创建表头
                sheet.addCell(new Label(0, 0, "序号"));
                sheet.addCell(new Label(1, 0, "扫描文件"));
                sheet.addCell(new Label(2, 0, "行号"));
                sheet.addCell(new Label(3, 0, "匹配数据"));
                // 生成表格信息
                for (int i = 0; i < resultList.size(); i++) {
                    try {
                        String no = String.valueOf(i+1);
                        String file = resultList.get(i).get("fileName");
                        String lineNo = resultList.get(i).get("lineNo");
                        String matchData = resultList.get(i).get("matchData");

                        sheet.addCell(new Label(0, i + 1, no));
                        sheet.addCell(new Label(1, i + 1, file));
                        sheet.addCell(new Label(2, i + 1, lineNo));
                        sheet.addCell(new Label(3, i + 1, matchData));
                    }  catch (Exception ex) {
                        log.error("write failed data: {}", resultList.get(i));
                    }
                    // 写入数据
                    workbook.write();
                }
            } catch (Exception ex) {
                log.error("write file failed");
            } finally {
                if (workbook != null) {
                    workbook.close();
                }
            }
        }
    }
}
