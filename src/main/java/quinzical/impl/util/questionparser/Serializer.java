// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

/**
 * Allows the user to locate a file within their file system containting correctly formatted questions,
 * and then reads the files, creating a map containing the questions in it
 */
public class Serializer {

    public static void main(String[] args) {

        File file;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
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

            if (!line.matches("^.*\\|.*\\|.*$")) {
                activeCategory = line;
                M.put(activeCategory, new ArrayList<>());
                continue;
            }
            String[] parts = line.split("\\|");
            String hint = parts[0].trim();
            String prompt = parts[1].trim();

            Question q = new Question(activeCategory, hint, prompt);

            String solutions = parts[2].trim();

            String[] slnList = solutions.split(",");
            for (String sln : slnList) {
                String[] variants = sln.split("/");
                q.addSolution(variants);
            }

            M.get(activeCategory).add(q);

        }

        try {
            FileOutputStream fileOut = new FileOutputStream(file.getParent() + "/question2.qdb");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(M);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
