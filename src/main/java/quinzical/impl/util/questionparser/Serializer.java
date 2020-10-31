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

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows the user to locate a file within their file system containing correctly formatted questions, and then reads
 * the files, creating a map containing the questions in it
 */
public class Serializer {

    public static void main(final String[] args) {

        //JFileChooser fileChooser = new JFileChooser();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        final File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        try {
            final Map<String, List<Question>> M = new HashMap<>();
            String activeCategory = "";

            final List<String> lines;

            lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);


            for (String line : lines) {
                line = line.trim();
                if (!line.matches("^.*\\|.*\\|.*$")) {
                    activeCategory = line;
                    M.put(activeCategory, new ArrayList<>());
                    continue;
                }
                final String[] parts = line.split("\\|");
                final String hint = parts[0].trim();
                final String prompt = parts[1].trim();

                final Question q = new Question(activeCategory, hint, prompt);

                final String solutions = parts[2].trim();

                final String[] slnList = solutions.split(",");
                for (final String sln : slnList) {
                    final String[] variants = sln.split("/");
                    q.addSolution(variants);
                }

                M.get(activeCategory).add(q);

            }

            final FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/data/question.qdb");
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(M);
            out.close();
            fileOut.close();
        } catch (final Exception ex) {
            showAlert();
        }
    }

    private static void showAlert() {
        final Alert alert = new Alert(Alert.AlertType.ERROR, "Please check the file and try again.",
            ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Invalid Question Set Loaded");
        alert.setHeaderText("This file does not appear to be a valid Quinzical Data Format.");
        alert.showAndWait();
    }
}
