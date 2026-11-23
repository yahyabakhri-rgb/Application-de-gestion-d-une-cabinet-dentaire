# Cabinet Dentaire

Application de bureau JavaFX pour gérer un cabinet dentaire : patients, dentistes, actes, créneaux et rendez-vous. Les données sont enregistrées localement dans des fichiers CSV.

## Fonctionnalités

- Connexion administrateur et patient
- Gestion des patients, dentistes et actes
- Création et affichage des créneaux
- Réservation normale ou urgente, validation et annulation des rendez-vous
- Export d'une fiche de rendez-vous en PDF

## Prérequis

- JDK 17 ou plus récent
- JavaFX SDK compatible avec votre JDK
- Apache PDFBox (utilisé pour créer les fiches PDF)

Le projet ne contient pas de fichier Maven ou Gradle. Les bibliothèques JavaFX et PDFBox doivent être installées séparément et ajoutées au chemin de compilation et d'exécution.

## Lancement

Depuis la racine du projet, compilez les fichiers de `src` dans un dossier de sortie en ajoutant les bibliothèques JavaFX et PDFBox au classpath/module path. Puis lancez la classe principale :

```text
ma.emi.cabinet.Launcher
```

Les options exactes de `javac` et `java` dépendent de l'emplacement de votre JavaFX SDK et du fichier JAR PDFBox.

## Données de démonstration

Au premier lancement, l'application crée des données de démonstration dans `data/` lorsque les fichiers de données ne sont pas présents. Le compte administrateur initial est `admin@cabinet.ma` avec le mot de passe `admin123`. Des comptes patients de démonstration sont également créés par `DonneesInitiales`.

Ces comptes et ces mots de passe sont uniquement destinés à une démonstration locale. Remplacez-les avant tout usage réel. Le contenu local de `data/` est ignoré par Git afin de ne pas publier les données du poste.

## Organisation du projet

- `src/ma/emi/cabinet/Launcher.java` : point d'entrée
- `src/ma/emi/cabinet/ui/` : fenêtres et écrans JavaFX
- `src/ma/emi/cabinet/service/` : règles métier et génération PDF
- `src/ma/emi/cabinet/model/` : objets métier
- `src/ma/emi/cabinet/persistence/` : dépôts et stockage CSV
- `src/ma/emi/cabinet/exception/` : exceptions du projet
- `documentation/` : guides du projet

