# Dossiers
SRC = src
BIN = bin
DOC = doc
JAVADOC = $(DOC)/javadoc

# Jars à créer
JAR_CLIENT = 
JAR_SERVEUR = 
JAR_ADMIN = 

# Packages nécessaires
PACKAGES_CLIENT = modele
PACKAGES_ADMIN = 
PACKAGES_SERVEUR = modele

# Liste des packages utiles (sort trie et enlève les doublons)
PACKAGES_TOUT = $(sort $(PACKAGES_CLIENT) $(PACKAGES_ADMIN) $(PACKAGES_SERVEUR))








# Crée tous les jars
jars: $(JAR_CLIENT) $(JAR_SERVEUR) $(JAR_ADMIN)
	@echo "Pas de jarification pour l'instant : on ne sait pas quoi faire."


# Installe la base de donnée (cf. makefile du dossier db/)
install_db: 
	make -C db install






# Javadoc
doc: | $(JAVADOC)
	javadoc -public $(PACKAGES_TOUT) -sourcepath $(SRC) -d $(JAVADOC) -charset "UTF-8"





# Création des dossiers
$(JAVADOC):
	mkdir -p $(JAVADOC)


# Supprime les jars
clean:
	rm -f $(JAR_CLIENT) $(JAR_ADMIN) $(JAR_SERVEUR)

# Supprime la doc et tout ce qui peut être supprimé
maxclean: clean
	rm -fr $(JAVADOC)
	make -C db clean


.PHONY: jars install_db doc clean maxclean

