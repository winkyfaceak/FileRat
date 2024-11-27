//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        DirectorySearch search = new DirectorySearch();
        search.traverseAllSubdirectories();
        search.displayDirectoryTree();

        System.out.println("Total directories: " + search.getDirectoryCount());
        System.out.println("Total files: " + search.getTotalFileCount());
    }
}