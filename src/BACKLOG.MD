1) Create view classes: MainMenu, Settings
2) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
3) Most of classes from view.component are sensitive only to theme change, they don't need ViewContext field;
    Think of refactoring StyleVaryingComponent.updateStyle() method to consume new theme and 
    re-style depending on it;
4) Refactor ScoreLabel to take MessageSource as constructor-arg and not to use constant SCORES
5) Think about where and how to store available languages
6) Move dark.properties from 'theme' directory to classpath and rename it to 'theme_dark.properties'