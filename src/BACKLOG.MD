1) Create view classes: MainMenu, Settings
2) ViewConfig.theme() must return the last theme, selected by user
3) Create commandExecutor to handle switching field dimension;
4) Create commandExecutor to handle navigation between windows
5) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
6) Field background image is not used
7) Delete RenderingContext, simple setter for theme will suffice