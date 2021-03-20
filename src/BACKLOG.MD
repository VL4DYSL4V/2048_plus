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
++++ 15) Create controller to handle shifting field
++++ 16) Subscribe components to model
17) Create controller to handle switching field dimension,
18) Create controller to handle restarting game 
++++ 19) Create controller to handle moving back 
20) Create controller to handle navigation between windows
++++ 21) Create controller to handle exit
22) ScoreLabel.representation() uses deprecated method Toolkit.getDefaultToolkit().getFontMetrics
++++ 23) Create enum Event 
++++ 24) Eliminate EndOfGameException by modifying Observer pattern to pass Event instances
25) Change logic of obtaining Theme object: it must be stored in some kind of RenderingContext
++++ 26) Move theme loading into some ThemeDao
27) Create class GameModel (immutable) to store only game information: field, scores and history
28) Exit from EndOfGameForm should be exit to Menu
29) Think about ModelDao threadSafety.
 There is a vulnerability!! 27-th task contains an idea how to avoid it 
