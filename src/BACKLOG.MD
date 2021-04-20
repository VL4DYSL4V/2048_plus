1) Create view class: Settings
2) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
3) Circular dependency between frames and buttons, which perform transition between them
5) Maybe, game-specific logic should be moved to some game package
6) Duplicates switch statement at MainFrame.computeReaction 
        and GameFrame.PreferencesListener.computeReaction;