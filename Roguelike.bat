cd %cd%\Roguelike
if exist \RogueLike.class goto EXIST

javac Roguelike.java

:EXIST
chcp 65001
java Roguelike
