++++ 1) Implement concept of storing only powers of 2 in a fieldElements.
++++ 2) Create threadsafe Model class, which must store: field, BigInteger scores and history
++++ 3) Crate ModelDao to persist Model somewhere   
4) Names of files are tied to names of FieldDimension's instances; Ask what to do about it
++++ 5) Create FieldShiftingController interface and implementation
++++ 6) You must add ability to cancel every model-modifying operation 
which can be executed after call Model.replaceState(Model another)
++++ 7) FieldShiftControllerImpl is not thread-safe
8) Create view classes
++++ 9) Create different Themes (at least the dark one)
++++ 10) Create ViewConfiguration, which loads paths from resources and then loads images.
++++ 11) Create actual images and specify them in <theme>.properties
12) ViewConfig.theme() must return the last theme, selected by user
++++ 13) Create canvas for field 
++++ 14) Create a class, which stores scaled images of field elements images
15) Create controllers to handle exit, switching field dimension, 
restarting game, moving back, shifting field and navigation between windows
16) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
17) Handle EndOfGameException in FieldMovementListener.keyPressed
18) Subscribe components to model
