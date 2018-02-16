SuperNova
===============================

SuperNova is a so called vertical scroller. 

In fact this is a proof-of concept for a so called vertical scroller.
It may act as a starting point for a simple game. As a popular example
you might consider Xenon II - Megablast, Raptor or similar games.

This game is written entirely in JAVA and uses the LWJGL library as a base.

This would not have been really necessary, since this is a 2D-Game, 
but OpenGL simplifies some things in a manner I like, texture 
handling and transformation stuff for example.

This package is not meant to be a complete game at the moment nor is the
code highly sophisticated. It just demonstrates one of many ways how such 
a game could be programmed.

Usage
=================================
This is an Eclipse project, you can simply import it into your workspace.
The main class is SuperNova.java located inside the package de.pueski.supernova 
There you'll find the heart of the game. Also this is the main class to be run 
from within Eclipse.

Play
=================================
You can move around with the mouse, the keyboard or the gamepad.

Keyboard:

Use WASD or cursor keys to move, SPACE to fire, ESC moves you back to the menu, F10
exits the game.

Mouse:
Mouse to move, left mouse button to fire

Gamepad:
With an XBOX Controller use left stick to move the shop  and A-Button to fire, other pads
are not tested.


Build
=================================
Use ant to build, simply invoke 

ant dist

to build the distribution. You'll find the zipped distribution inside the dist 
directory.  

 
Run
=================================
Use the provided SuperNova(64).exe on windows or the SuperNova batch file.

For Linux and othe Unixoids simply type

java -Djava.library.path=./native -jar SuperNova.jar

13.04.2015