package com.dbapi.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RuntimeUtil {

    /**
     * 执行系统命令并返回输出结果
     *
     * @param directory  工作目录，可以为 null，表示使用当前目录
     * @param cmds       系统命令和参数（可变参数）
     * @return 命令的输出结果
     * @throws Exception 可能抛出的异常
     */
    public static List<String> executeCommand(String directory, String... cmds) throws Exception {
        List<String> output = new ArrayList<>();
        Process process = null;
        BufferedReader reader = null;

        try {
            // 使用 ProcessBuilder 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(cmds);

            // 设置工作目录
            if (directory != null) {
                processBuilder.directory(new java.io.File(directory));
            }

            process = processBuilder.start();

            // 获取输入流（命令的输出）
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("命令执行失败，退出代码: " + exitCode);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (process != null) {
                process.destroy();
            }
        }

        return output;
    }

    /**
     * 执行 Python 脚本
     *
     * @param scriptPath Python 脚本路径
     * @param args       Python 脚本的参数（可选）
     * @return 脚本执行的输出结果
     * @throws Exception 可能抛出的异常
     */
    public static List<String> executePythonScript(String scriptPath, String... args) throws Exception {
        // 构建 Python 脚本执行命令
        List<String> command = new ArrayList<>();
        command.add("python3");
        command.add(scriptPath);

        // 添加可选的参数
        for (String arg : args) {
            command.add(arg);
        }

        // 将命令转换为数组并执行
        return executeCommand(null, command.toArray(new String[0]));
    }

    public static void main(String[] args) {
        try {
            // 在 /Users/zfang 目录下执行 "ls -lah" 命令
            List<String> result = RuntimeUtil.executeCommand(null, "ping"," www.baidu.com");
            result.forEach(line -> System.out.println(line));
            // result.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
