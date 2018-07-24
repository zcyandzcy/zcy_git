public class FileUtils {
    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 写入文件
     *
     * @param content
     * @param path
     */
    public static void writeIntoFile(String content, String path, boolean append) {
        FileWriter fw = null;
        BufferedWriter bufw = null;
        try {
            fw = new FileWriter(path, false);
            bufw = new BufferedWriter(fw);
            bufw.write(content);
            bufw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufw.close();
                fw.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static String bufferReadFile(String path) {
        FileReader fr = null;
        BufferedReader bufferedReader = null;
        String string = null;
        StringBuffer sb = new StringBuffer();
        try {
            fr = new FileReader(path);
            bufferedReader = new BufferedReader(fr);
            while ((string = bufferedReader.readLine()) != null) {
                sb.append(string).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                fr.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     * 导出 byteArray
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readFileToByteArray(String file) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToByteArray(new File(file));
    }
}