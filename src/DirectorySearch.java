import java.io.File;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class DirectorySearch {
    private int directoryCount = 0;
    private int totalFileCount = 0;
    private final Queue<File> rootDirectories = new LinkedList<>();
    private final Map<File, Integer> fileCountMap = new HashMap<>();
    private final Map<File, Integer> subDirCountMap = new HashMap<>();

    public DirectorySearch() {
        // Initialize root directories if not provided
        loadRootDirectories();
    }

    // Add a directory to the root queue
    public void addDirectory(File directory) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            System.err.println("Invalid directory skipped: " + directory);
            return;
        }
        rootDirectories.add(directory);
        directoryCount++;
    }

    // Load all system root directories
    private void loadRootDirectories() {
        File[] roots = File.listRoots();
        if (roots != null) {
            for (File root : roots) {
                addDirectory(root);
            }
        }
    }

    // Traverse all subdirectories iteratively using a queue
    public void traverseAllSubdirectories() {
        if (rootDirectories.isEmpty()) {
            System.out.println("No root directories available. Loading system roots.");
            loadRootDirectories();
        }

        Queue<File> queue = new LinkedList<>(rootDirectories);

        while (!queue.isEmpty()) {
            File currentDir = queue.poll();
            if (currentDir == null || !currentDir.isDirectory()) continue;

            try {
                File[] contents = currentDir.listFiles();
                if (contents == null) continue;

                int fileCount = 0;
                int subDirCount = 0;

                for (File content : contents) {
                    if (content.isFile()) {
                        fileCount++;
                        totalFileCount++;
                    } else if (content.isDirectory()) {
                        subDirCount++;
                        queue.add(content);
                    }
                }

                fileCountMap.put(currentDir, fileCount);
                subDirCountMap.put(currentDir, subDirCount);

            } catch (SecurityException e) {
                System.err.println("Security exception accessing directory: " + currentDir.getAbsolutePath());
            }
        }
    }

    // Display the directory tree
    public void displayDirectoryTree() {
        if (fileCountMap.isEmpty()) {
            System.out.println("No directories traversed. Run traverseAllSubdirectories() first.");
            return;
        }

        System.out.println("Complete Directory Tree:");
        for (File directory : fileCountMap.keySet()) {
            int depth = getDirectoryDepth(directory);
            String indent = "│   ".repeat(Math.max(0, depth));
            System.out.printf("%s├── %s%n", indent, directory.getName());
            System.out.printf("%s    └── Files: %d%n", indent, fileCountMap.getOrDefault(directory, 0));
            System.out.printf("%s    └── Subdirectories: %d%n", indent, subDirCountMap.getOrDefault(directory, 0));
        }

        displayStatistics();
    }

    private int getDirectoryDepth(File directory) {
        int depth = 0;
        while (directory.getParentFile() != null) {
            depth++;
            directory = directory.getParentFile();
        }
        return depth;
    }

    public void displayStatistics() {
        int totalSubDirCount = 0;
        for (Integer integer : subDirCountMap.values()) {
            totalSubDirCount += integer;
        }
        System.out.println("\n--- Directory Statistics ---");
        System.out.println("Total Root Directories: " + rootDirectories.size());
        System.out.println("Total Subdirectories: " + totalSubDirCount);
        System.out.println("Total Files: " + totalFileCount);
    }

    public int getDirectoryCount() {
        return directoryCount;
    }

    public int getTotalFileCount() {
        return totalFileCount;
    }

}
