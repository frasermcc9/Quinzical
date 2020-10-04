# Quinzical by Team Four

This repository contains the source code for the Quinzical application developed by Team Four.

## Running the Application

With the latest release, simply run the `launch.sh` script. You may need to edit the script and change the module path
to your local JavaFX install location.

### Native Dependencies

JavaFX and ESpeak are required to run this application.

## Overview
- **New Game**<br>This will create a new game with new questions and new categories.

- **Load Game**<br>This will attempt to load the last created new game. The application will try to save the current
game when exiting the application. In this case, load game will load that game. If the button is greyed out, Quinzical
could not find the last loaded game.

- **Practice Mode**<br>This will launch the practice module of the game. Here you can choose any category that has been
loaded, and the game will serve random questions from this category. You will have multiple attempts and be given
additional hints for questions.

- **Options**<br>Here you can adjust the settings for the Text-To-Speech system, as well as set the theme of the game.

- **Load New Questions**<br>See [the section on loading custom questions](#loading-custom-questions)

## Compiling from Source

Download the source code, then use Maven to compile. The command `mvn clean compile assembly:single` will create the
needed `.jar` file in the target directory.

## Loading Custom Questions

Use the button in the main screen to load a custom question set. This will open a file selector where you can pick
a text file in the same format as the one in the data folder to load a new question set, namely:

```text
Category Name
Some Question|Some Prompt|Some Solution
```

Note that solutions can have multiple _variants_, that is multiple correct answers (for example, what number comes
after 3: four or 4). There can also be multiple required solutions (for example, who are the lecturers of SOFTENG 206:
Catherine and Nasser). To achieve this functionality, separate required solutions by commas. Separate multiple correct
solutions with forward slashes:

```text
Some Question|Some Prompt|Solution One, Solution Two/Solution Two Alternative
```

A category should have at least 5 questions. The program might not work correctly if a category has less than five
questions.

Please not this is an experimental feature. Users should take care to ensure the files conform to the required
specifications.

## Contributors

- Fraser McCallum
- Braden Palmer
- Nasser (thanks for the initial commit)
- Haytham Galal (font designer)
- [Pixabay](https://pixabay.com/photos/search/new%20zealand/) for the theme images.

