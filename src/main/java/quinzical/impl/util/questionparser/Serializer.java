package quinzical.impl.util.questionparser;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Serializer {

    public static void main(String[] args) {

        File file;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }

        Map<String, List<Question>> M = new HashMap<>();
        String activeCategory = "";

        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : lines) {
            line = line.trim();

            if (!line.matches("^.*\\(.*$")) {
                activeCategory = line;
                M.put(activeCategory, new ArrayList<>());
                continue;
            }
            String[] parts = line.split("[()]");
            String hint = parts[0].trim().replaceAll("[,.]$", "");
            String prompt = parts[1].trim();
            String solutions = parts[2].trim().replaceAll("[,.]$", "");

            Question q = new Question(hint, prompt);

            String[] slnList = solutions.split(",");
            for (String sln : slnList) {
                String[] variants = sln.split("/");
                q.addSolution(variants);
            }

            M.get(activeCategory).add(q);

        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file.getParent() + "/question.qdb");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(M);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
