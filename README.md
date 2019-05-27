# ChasseAuxMonstres
### Fait par : Kozlov Antoine, Michot Julien, Lantoine Gautier , GROUPE K

## Pour lancer le jeu : (Requis : Java installé)
Windows: /jar/windows.bat
Linux: 
 - cd jar/ 
 - chmod +x linux.sh
 - ./linux.sh

## Règles du jeu (obligatoire) : 
Un monstre se retrouve sur un plateau avec un nombre de cases défini par un joueur.
Le monstre se trouve sur une case avec des coordonnées (x,y).
Lorsque le monstre se déplace, la case du tour précédent devient inaccessible.
Le chasseur doit trouver le monstre en révélant une case qu'il choisit.
Le monstre voit la case révélée par le chasseur.

Le jeu se termine si : 
 - Plus aucune case ne peut être jouer par le monstre (Victoire du monstre)
 - Le chasseur trouve le monstre (Victoire du chasseur)
 - Le monstre se bloque (Victoire du chasseur)
### A chaque tour du jeu :
- Le monstre doit **obligatoirement** choisir entre 4 déplacements : Haut, Bas, Gauche, Droite
- Le chasseur doit révéler une case

## Modes de jeu :
Il existe actuellement 3 modes de jeu :

1. 2 joueurs (le premier étant le monstre, le second le chasseur)

2. 1 joueur :
 - le joueur joue le chasseur (l'IA celle du monstre)
 - le joueur joue le monstre (l'IA celle du chasseur)
 
## Règles du jeu (optionnelles) :
- ### Mode "Battle Royale" :
Tous les 10 tours, le plateau verra sa taille rétrécir d'une case de chaque côté (les cases au bord sont alors inaccessible)
- ### Mode "Monste TP" :
Lorsque le monstre n'a plus aucun moyen de se déplacer (Haut, Bas, Gauche, Droite), il est alors téléporté sur une case "libre"
- ### Mode "Monstre mange Chasseur" (seulement en mode console):
Si le chasseur tape sur une case adjacente de celle où se trouve le monstre, alors le monstre peut "manger" le chasseur et la partie se termine (Victoire du monstre)

[Source code](https://github.com/Azeyre/ChasseAuxMonstre)
