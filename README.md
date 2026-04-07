# Franji

Une application d’apprentissage du japonais construite en Kotlin avec Jetpack Compose.
Elle repose sur un système de cartes, couvre le kanji, le vocabulaire et la grammaire, et utilise une API FastAPI pour la gestion des données.

# ✨ Fonctionnalités

- Système de révision basé sur des cartes
  Révisez efficacement grâce à un mécanisme inspiré du spaced repetition.
- Kanji, vocabulaire et grammaire
  Chaque catégorie possède ses propres cartes, leçons et informations.
- Animation du tracé des kanji (Lottie)
  Affichage animé du tracé pour faciliter la mémorisation.
- Détection du tracé d’un kanji
  L’utilisateur dessine le kanji et l’app vérifie automatiquement la validité du tracé.
- Liste de toutes les leçons
  Navigation par catégorie : grammaire, vocabulaire et kanji.
- Synchronisation avec une API FastAPI (Python)
  - Récupération des données en JSON.
  - Stockage local des données pour un usage hors-ligne.
  - Mise en cache en mémoire au lancement de l'application pour optimiser les performances.

## 🛠️ Technologies utilisées
- Android Kotlin
- Jetpack Compose
- Lottie
- FastAPI (backend)
- JSON / stockage local

## 🚀 Installation
/!\ L'API n'est pas encore publiée /!\
- clonez le dépot
  ```git clone https://github.com/MrAntoine77/Franji.git```
- Ouvrez le projet dans Android Studio.
- Lancez l’application sur un émulateur ou un appareil physique Android.
- Assurez-vous que votre API FastAPI est opérationnelle (Non publiée pour l'instant).


