# Dossiers
SRC = src
BIN = bin
DOC = doc
JAVADOC = $(DOC)/javadoc

# Packages nécessaires
PACKAGES_CLIENT = modele communication communication.simple
PACKAGES_ADMIN = 
PACKAGES_SERVEUR = modele communication
PACKAGES_TESTS = tests

# Liste des packages utiles (sort trie et enlève les doublons)
PACKAGES_TOUT = $(sort $(PACKAGES_CLIENT) $(PACKAGES_ADMIN) $(PACKAGES_SERVEUR) $(PACKAGES_TESTS))









# Compile le java (.java -> .class)
java: | $(BIN)
	javac -sourcepath $(SRC) -d $(BIN) $(foreach package, $(PACKAGES_TOUT), $(SRC)/$(subst .,/,$(package))/*.java)


# Installe la base de donnée (cf. makefile du dossier db/)
install_db: 
	make -C db install






# Javadoc
doc: | $(JAVADOC)
	javadoc -public $(PACKAGES_TOUT) -sourcepath $(SRC) -d $(JAVADOC) -charset "UTF-8"


test_communication:
	xterm -ge 80x15+0+000 -e "java -classpath $(BIN) tests.TestComSimpleServeur 8888" &
	xterm -ge 80x15+0+230 -e "sleep 1 && java -classpath $(BIN) tests.TestComSimpleClient 127.0.0.2 8888" &
	xterm -ge 80x15+0+460 -e "sleep 3 && java -classpath $(BIN) tests.TestComSimpleClient 127.0.0.2 8888" &

kill_tests:
	ps | grep "xterm" | tr -s " " | cut -d " " -f 1 | while read pid; do kill -9 $$pid; done

# Création des dossiers
$(BIN):
	mkdir -p $(BIN)

$(JAVADOC):
	mkdir -p $(JAVADOC)


# Supprime les jars
clean:
	rm -f $(JAR_CLIENT) $(JAR_ADMIN) $(JAR_SERVEUR)

# Supprime la doc et tout ce qui peut être supprimé
maxclean: clean
	rm -fr $(JAVADOC)
	rm -fr $(BIN)
	make -C db clean


.PHONY: jars java install_db doc clean maxclean


