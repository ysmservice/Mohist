package red.mohist.down;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import red.mohist.util.HttpUtil;
import red.mohist.util.JarLoader;
import red.mohist.util.MD5Util;
import red.mohist.util.i18n.Message;

public class DownloadLibraries {

    public static final String FIND_LOCATE = "https://passport.lazercloud.com/api/v1/options/GetLocate";

    public static void run() throws Exception {
        String url = "";
        String path = null;
        InputStream listStream = DownloadLibraries.class.getClassLoader().getResourceAsStream("lib.red");
        if (listStream == null) return;
        Map<File, String> lib = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(listStream));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                String[] args = str.split("\\|");
                if (args.length == 2) {
                    path = args[0];
                    String md5 = args[1];

                    try {
                        File file = new File(path);
                        // Judgement files and MD5
                        if ((!file.exists() || !MD5Util.getMD5(file).equals(md5))) {
                            lib.put(file, md5);
                        }
                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        if (lib.size() > 0) {
            for (Map.Entry<File, String> entry : lib.entrySet()) {

                String[] args = entry.getKey().getPath().split("\\\\");
                int size = args.length;
                String filepath = entry.getKey().getPath().replace("\\" + args[size - 1], "");
                File newfile = new File(filepath);
                if (!newfile.exists()) {
                    newfile.mkdirs();
                }
                try {
                    String locateInfo = HttpUtil.doGet(FIND_LOCATE);

                    if (locateInfo != null && locateInfo.equals("CN")) {
                        url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
                    } else {
                        url = "https://www.mgazul.cn/"; //Github Mirror
                    }
                } catch (Exception e) {
                    if (Message.getLanguage(2).contains("CN")) {
                        url = "https://mohist-community.gitee.io/mohistdown/"; //Gitee Mirror
                    } else {
                        url = "https://www.mgazul.cn/"; //Github Mirror
                    }
                }
                new Download(url + entry.getKey().getPath().replace("\\", "/"), entry.getKey(), args[size - 1]);
                JarLoader jarLoader = new JarLoader((URLClassLoader)ClassLoader.getSystemClassLoader());

                JarLoader.loadjar(jarLoader, filepath);
            }
        }
    }
}
