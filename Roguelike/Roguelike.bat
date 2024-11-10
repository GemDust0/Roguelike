cd %cd%\Roguelike
if exist \RogueLike.class goto EXIST

javac Roguelike.java

:EXIST
java Roguelike
pause