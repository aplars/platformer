package com.sa.game.dirwatcher;

import java.util.*;
import java.io.*;

public abstract class DirWatcher extends TimerTask {
    private String path;
    private File filesArray [];
    private HashMap<File, Long> dir = new HashMap<>();
    private DirFilterWatcher dfw;

    public DirWatcher(String path) {
        this(path, "");
    }

    public DirWatcher(String path, String filter) {
        this.path = path;
        dfw = new DirFilterWatcher(filter);
        filesArray = new File(path).listFiles(dfw);

        // transfer to the hashmap be used a reference and keep the
        // lastModfied value
        for(int i = 0; i < filesArray.length; i++) {
            dir.put(filesArray[i], filesArray[i].lastModified());
        }
    }

    public final void run() {
        HashSet<File> checkedFiles = new HashSet<File>();
        filesArray = new File(path).listFiles(dfw);

        // scan the files and check for modification/addition
        for(int i = 0; i < filesArray.length; i++) {
            Long current = (Long)dir.get(filesArray[i]);
            checkedFiles.add(filesArray[i]);
            if (current == null) {
                // new file
                dir.put(filesArray[i], filesArray[i].lastModified());
                onChange(filesArray[i], "add");
            }
            else if (current.longValue() != filesArray[i].lastModified()){
                // modified file
                dir.put(filesArray[i], filesArray[i].lastModified());
                onChange(filesArray[i], "modify");
            }
        }

        // now check for deleted files
        Set<File> ref = ((HashMap<File, Long>)dir.clone()).keySet();
        ref.removeAll((Set<File>)checkedFiles);
        Iterator<File> it = ref.iterator();
        while (it.hasNext()) {
            File deletedFile = (File)it.next();
            dir.remove(deletedFile);
            onChange(deletedFile, "delete");
        }
    }

    protected abstract void onChange( File file, String action );
}
