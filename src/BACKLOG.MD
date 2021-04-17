1) Create view classes: MainMenu, Settings
2) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
3) Circular dependency between frames and buttons, which perform transition between them     
4) Think about where and how to store available languages
5) Maybe, game-specific logic should be moved to some game package
6) If game is over, with exit to menu set new GameData with same dimension and update storage
7) Duplicates switch statement at MainFrame.computeReaction 
        and GameFrame.PreferencesListener.computeReaction;